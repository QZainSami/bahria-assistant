package com.bahria.portal.data.network

import android.util.Log
import com.bahria.portal.data.model.Assignment
import com.bahria.portal.data.model.CourseAttendance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class PortalScraper {

    companion object {
        private const val TAG = "PortalScraper"
        const val CMS_LOGIN_URL = "https://cms.bahria.edu.pk/Logins/Student/Login.aspx"
        const val CMS_ATTENDANCE_URL = "https://cms.bahria.edu.pk/Sys/Student/ClassAttendance/StudentWiseAttendance.aspx"
        const val CMS_GOTO_LMS_URL = "https://cms.bahria.edu.pk/Sys/Common/GoToLMS.aspx"
        const val LMS_ASSIGNMENTS_URL = "https://lms.bahria.edu.pk/Student/Assignments.php"

        private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
    }

    private val cookieStore = HashMap<String, MutableList<Cookie>>()

    private val client: OkHttpClient by lazy {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL").apply {
            init(null, trustAllCerts, SecureRandom())
        }

        OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    val list = cookieStore.getOrPut(url.host) { mutableListOf() }
                    for (c in cookies) {
                        list.removeAll { it.name == c.name }
                        list.add(c)
                    }
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val matching = mutableListOf<Cookie>()
                    cookieStore.forEach { (host, cookies) ->
                        if (url.host.contains(host) || host.contains(url.host)) {
                            matching.addAll(cookies)
                        }
                    }
                    return matching
                }
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    suspend fun login(enrollment: String, pass: String, campus: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            // Step 1: GET Login Page to extract ViewState & hidden fields
            val getRequest = Request.Builder()
                .url(CMS_LOGIN_URL)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .build()

            val getResponse = client.newCall(getRequest).execute()
            val getHtml = getResponse.body?.string() ?: ""
            val doc = Jsoup.parse(getHtml)

            val viewState = doc.select("input[name=__VIEWSTATE]").`val`()
            val eventValidation = doc.select("input[name=__EVENTVALIDATION]").`val`()
            val viewStateGen = doc.select("input[name=__VIEWSTATEGENERATOR]").`val`()

            val instituteCode = resolveInstituteCode(campus, doc)

            // Step 2: POST Login Credentials
            val formBuilder = FormBody.Builder()
                .add("__VIEWSTATE", viewState)
                .add("__VIEWSTATEGENERATOR", viewStateGen)
                .add("__EVENTVALIDATION", eventValidation)
                .add("BodyPH_tbEnrollment", enrollment.trim())
                .add("BodyPH_tbPassword", pass.trim())
                .add("BodyPH_ddlInstituteID", instituteCode)
                .add("BodyPH_btnLogin", "Sign In")

            val postRequest = Request.Builder()
                .url(CMS_LOGIN_URL)
                .header("User-Agent", USER_AGENT)
                .header("Referer", CMS_LOGIN_URL)
                .post(formBuilder.build())
                .build()

            val postResponse = client.newCall(postRequest).execute()
            val postHtml = postResponse.body?.string() ?: ""

            // Check if redirected or if error banner was returned
            val isSuccess = !postHtml.contains("Invalid Enrollment", ignoreCase = true) &&
                    !postHtml.contains("Incorrect Password", ignoreCase = true) &&
                    (postResponse.request.url.toString() != CMS_LOGIN_URL || postHtml.contains("Logout", ignoreCase = true) || postHtml.contains("Attendance", ignoreCase = true))

            if (isSuccess) {
                Result.success(true)
            } else {
                val errorMsg = extractErrorMessage(postHtml) ?: "Invalid credentials or campus selected."
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Login failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun fetchAttendance(): Result<List<CourseAttendance>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(CMS_ATTENDANCE_URL)
                .header("User-Agent", USER_AGENT)
                .header("Referer", "https://cms.bahria.edu.pk/Sys/Student/Default.aspx")
                .build()

            val response = client.newCall(request).execute()
            val html = response.body?.string() ?: ""
            val doc = Jsoup.parse(html)

            val list = mutableListOf<CourseAttendance>()
            val tables = doc.select("table")

            for (table in tables) {
                val rows = table.select("tr")
                for (row in rows) {
                    val cols = row.select("td")
                    if (cols.size >= 5) {
                        val text0 = cols[0].text().trim()
                        val text1 = cols[1].text().trim()

                        // Detect table headers vs data rows
                        val courseName = if (text0.matches(Regex("^[0-9]+$"))) text1 else text0
                        if (courseName.equals("Course", ignoreCase = true) || courseName.equals("Subject", ignoreCase = true) || courseName.isBlank()) {
                            continue
                        }

                        var present = 0.0
                        var absent = 0.0
                        var total = 0.0
                        var percentage = 0.0

                        for (i in 1 until cols.size) {
                            val txt = cols[i].text().trim().replace("%", "")
                            val num = txt.toDoubleOrNull() ?: continue

                            if (cols.size >= 6) {
                                // Typical layout: [SrNo, Course, Present, Absent, Total, %]
                                when (i) {
                                    2 -> present = num
                                    3 -> absent = num
                                    4 -> total = num
                                    5 -> percentage = num
                                }
                            } else {
                                when (i) {
                                    1 -> present = num
                                    2 -> absent = num
                                    3 -> total = num
                                    4 -> percentage = num
                                }
                            }
                        }

                        if (total > 0 || percentage > 0) {
                            if (total == 0.0 && present + absent > 0) {
                                total = present + absent
                            }
                            if (percentage == 0.0 && total > 0) {
                                percentage = (present / total) * 100.0
                            }

                            list.add(
                                CourseAttendance(
                                    courseName = courseName,
                                    present = present,
                                    absent = absent,
                                    total = total,
                                    percentage = Math.round(percentage * 100.0) / 100.0
                                )
                            )
                        }
                    }
                }
            }

            if (list.isNotEmpty()) {
                Result.success(list)
            } else {
                Result.failure(Exception("Could not parse attendance table from portal."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Fetch attendance failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun fetchAssignments(): Result<List<Assignment>> = withContext(Dispatchers.IO) {
        try {
            // Step 1: Bridge to LMS
            val lmsBridgeRequest = Request.Builder()
                .url(CMS_GOTO_LMS_URL)
                .header("User-Agent", USER_AGENT)
                .build()
            client.newCall(lmsBridgeRequest).execute()

            // Step 2: Open Assignments page
            val request = Request.Builder()
                .url(LMS_ASSIGNMENTS_URL)
                .header("User-Agent", USER_AGENT)
                .build()

            val response = client.newCall(request).execute()
            val html = response.body?.string() ?: ""
            val doc = Jsoup.parse(html)

            val list = mutableListOf<Assignment>()
            val rows = doc.select("table tr")

            for (row in rows) {
                val cols = row.select("td")
                if (cols.size >= 3) {
                    val title = cols[0].text().trim()
                    val course = if (cols.size >= 4) cols[1].text().trim() else "Course"
                    val dueDate = cols[cols.size - 2].text().trim()
                    val statusText = cols[cols.size - 1].text().trim()

                    if (title.equals("Assignment", ignoreCase = true) || title.equals("Title", ignoreCase = true) || title.isBlank()) {
                        continue
                    }

                    val isPending = !statusText.contains("Submitted", ignoreCase = true) && !statusText.contains("Graded", ignoreCase = true)
                    val status = if (isPending) "Pending" else "Submitted"

                    val dueTimestamp = Assignment.parseDate(dueDate)
                    val anchor = row.select("a[href]").firstOrNull { a ->
                        val text = a.text().lowercase()
                        val href = a.attr("href").lowercase()
                        text.endsWith(".docx") || text.endsWith(".doc") || text.endsWith(".pdf") ||
                                href.contains("download") || href.contains("assignment") || href.contains(".docx") || href.contains(".pdf")
                    } ?: row.select("a[href]").firstOrNull()

                    val link = anchor?.attr("abs:href") ?: ""
                    val anchorText = anchor?.text()?.trim() ?: ""

                    var fileName = ""
                    if (anchorText.matches(Regex(".*\\.(docx|doc|pdf|zip|pptx|txt)$", RegexOption.IGNORE_CASE))) {
                        fileName = anchorText
                    } else if (link.isNotBlank()) {
                        val uri = android.net.Uri.parse(link)
                        val paramFile = uri.getQueryParameter("file") ?: uri.getQueryParameter("filename") ?: uri.getQueryParameter("f")
                        if (!paramFile.isNullOrBlank()) {
                            fileName = paramFile
                        } else {
                            val lastSegment = link.substringAfterLast("/").substringBefore("?")
                            if (lastSegment.contains(".")) {
                                fileName = lastSegment
                            }
                        }
                    }
                    if (fileName.isBlank()) {
                        fileName = "${title.replace("[^a-zA-Z0-9.-]".toRegex(), "_")}.docx"
                    }

                    val item = Assignment(
                        id = "${course}_$title".replace(" ", "_"),
                        title = title,
                        courseName = course,
                        dueDateString = dueDate,
                        dueTimestamp = dueTimestamp,
                        status = status,
                        downloadUrl = link,
                        fileName = fileName
                    )
                    list.add(item)
                }
            }

            Result.success(list)
        } catch (e: Exception) {
            Log.e(TAG, "Fetch assignments failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun downloadAssignmentFile(
        downloadUrl: String,
        targetDir: java.io.File,
        suggestedFileName: String
    ): Result<java.io.File> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(downloadUrl)
                .header("User-Agent", USER_AGENT)
                .header("Referer", LMS_ASSIGNMENTS_URL)
                .build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("HTTP ${response.code}: Download failed"))
            }
            val body = response.body ?: return@withContext Result.failure(Exception("Empty file response"))

            targetDir.mkdirs()

            // 1. Try to extract filename from Content-Disposition header
            val contentDisposition = response.header("Content-Disposition")
            var resolvedName = extractFileNameFromDisposition(contentDisposition)

            // 2. If not in header, use suggestedFileName or URL
            if (resolvedName.isNullOrBlank()) {
                resolvedName = suggestedFileName.ifBlank {
                    downloadUrl.substringAfterLast("/").substringBefore("?").ifBlank { "assignment" }
                }
            }

            // Clean filename of unsafe characters
            resolvedName = resolvedName.replace("[\\\\/:*?\"<>|]".toRegex(), "_")

            // Write to a temporary file first
            val tempFile = java.io.File(targetDir, "download_${System.currentTimeMillis()}.tmp")
            tempFile.outputStream().use { out ->
                body.byteStream().use { input ->
                    input.copyTo(out)
                }
            }

            // 3. Inspect magic bytes & Content-Type to guarantee the extension matches the true file format!
            val contentType = response.header("Content-Type")?.lowercase() ?: ""
            val magicBytes = ByteArray(4)
            val readBytes = tempFile.inputStream().use { it.read(magicBytes) }

            val isZipOrDocx = readBytes >= 4 &&
                    magicBytes[0] == 0x50.toByte() && magicBytes[1] == 0x4B.toByte() &&
                    magicBytes[2] == 0x03.toByte() && magicBytes[3] == 0x04.toByte() // "PK\u0003\u0004"
            val isPdf = readBytes >= 4 &&
                    magicBytes[0] == 0x25.toByte() && magicBytes[1] == 0x50.toByte() &&
                    magicBytes[2] == 0x44.toByte() && magicBytes[3] == 0x46.toByte() // "%PDF"

            var finalExtension = if (resolvedName.contains(".")) resolvedName.substringAfterLast(".") else ""
            if (isZipOrDocx) {
                // If it's an Office Open XML (DOCX/PPTX/XLSX) or ZIP container
                if (finalExtension.equals("pdf", ignoreCase = true) || finalExtension.isBlank()) {
                    finalExtension = if (contentType.contains("presentation")) "pptx" else "docx"
                }
            } else if (isPdf) {
                finalExtension = "pdf"
            } else if (contentType.contains("officedocument.wordprocessingml") || contentType.contains("msword")) {
                if (!finalExtension.equals("docx", ignoreCase = true) && !finalExtension.equals("doc", ignoreCase = true)) {
                    finalExtension = "docx"
                }
            }

            val baseName = if (resolvedName.contains(".")) resolvedName.substringBeforeLast(".") else resolvedName
            val finalFileName = if (finalExtension.isNotBlank()) "$baseName.$finalExtension" else resolvedName
            val finalFile = java.io.File(targetDir, finalFileName)

            if (finalFile.exists()) {
                finalFile.delete()
            }
            tempFile.renameTo(finalFile)

            Result.success(finalFile)
        } catch (e: Exception) {
            Log.e(TAG, "File download failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun extractFileNameFromDisposition(header: String?): String? {
        if (header.isNullOrBlank()) return null
        val regex = Regex("filename\\*?=['\"]?(?:UTF-8'')?([^;'\"]+)", RegexOption.IGNORE_CASE)
        val match = regex.find(header)
        var name = match?.groupValues?.getOrNull(1)?.trim()
        if (!name.isNullOrBlank()) {
            return try {
                java.net.URLDecoder.decode(name, "UTF-8").replace("\"", "").replace("'", "")
            } catch (_: Exception) {
                name.replace("\"", "").replace("'", "")
            }
        }
        return null
    }

    private fun resolveInstituteCode(campus: String, doc: org.jsoup.nodes.Document): String {
        val options = doc.select("select[name=BodyPH_ddlInstituteID] option")
        for (opt in options) {
            if (opt.text().contains(campus, ignoreCase = true)) {
                return opt.`val`()
            }
        }
        // Fallback default ID if not parsed
        return "1"
    }

    private fun extractErrorMessage(html: String): String? {
        val doc = Jsoup.parse(html)
        val alert = doc.select(".alert-danger, #lblError, #BodyPH_lblError").first()
        return alert?.text()?.trim()
    }
}
