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
                    val link = row.select("a[href]").firstOrNull()?.attr("abs:href") ?: ""
                    val fileName = if (link.isNotBlank() && !link.endsWith("/")) link.substringAfterLast("/") else "${title.replace(" ", "_")}.pdf"

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
        destinationFile: java.io.File
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
            destinationFile.parentFile?.mkdirs()
            destinationFile.outputStream().use { out ->
                body.byteStream().use { input ->
                    input.copyTo(out)
                }
            }
            Result.success(destinationFile)
        } catch (e: Exception) {
            Log.e(TAG, "File download failed: ${e.message}", e)
            Result.failure(e)
        }
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
