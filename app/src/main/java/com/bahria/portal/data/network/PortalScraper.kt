package com.bahria.portal.data.network

import android.util.Log
import com.bahria.portal.data.model.Assignment
import com.bahria.portal.data.model.CourseAttendance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
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

    private val cookieStore = java.util.concurrent.ConcurrentHashMap<String, MutableList<Cookie>>()

    fun syncWithSystemCookieManager(urls: List<String> = listOf(CMS_LOGIN_URL, LMS_ASSIGNMENTS_URL)) {
        try {
            val cm = android.webkit.CookieManager.getInstance()
            for (urlStr in urls) {
                val httpUrl = urlStr.toHttpUrlOrNull() ?: continue
                val cookieString = cm.getCookie(urlStr) ?: continue
                val cookiePairs = cookieString.split(";")
                synchronized(cookieStore) {
                    val list = cookieStore.getOrPut(httpUrl.host) { mutableListOf() }
                    for (pair in cookiePairs) {
                        val parts = pair.trim().split("=", limit = 2)
                        if (parts.size == 2) {
                            val name = parts[0].trim()
                            val value = parts[1].trim()
                            val cookie = Cookie.Builder()
                                .domain(httpUrl.host)
                                .path("/")
                                .name(name)
                                .value(value)
                                .build()
                            list.removeAll { it.name == name }
                            list.add(cookie)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Cookie sync from system CookieManager: ${e.message}")
        }
    }

    private val client: OkHttpClient by lazy {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("SSL").apply {
            init(null, trustAllCerts, SecureRandom())
        }

        val dispatcher = okhttp3.Dispatcher().apply {
            maxRequests = 32
            maxRequestsPerHost = 16
        }

        OkHttpClient.Builder()
            .dispatcher(dispatcher)
            .connectionPool(okhttp3.ConnectionPool(10, 5, TimeUnit.MINUTES))
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    synchronized(cookieStore) {
                        val list = cookieStore.getOrPut(url.host) { mutableListOf() }
                        val cm = try { android.webkit.CookieManager.getInstance() } catch (_: Exception) { null }
                        for (c in cookies) {
                            list.removeAll { it.name == c.name }
                            list.add(c)
                            try {
                                cm?.setCookie(url.toString(), "${c.name}=${c.value}; Path=${c.path}; Domain=${c.domain}")
                            } catch (_: Exception) {}
                        }
                    }
                }

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val matching = mutableListOf<Cookie>()
                    synchronized(cookieStore) {
                        cookieStore.forEach { (host, cookies) ->
                            if (url.host.contains(host) || host.contains(url.host)) {
                                matching.addAll(cookies)
                            }
                        }
                    }
                    return matching
                }
            })
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    suspend fun ensureLmsSession(enrollment: String, pass: String, campus: String): Boolean = withContext(Dispatchers.IO) {
        if (enrollment.isBlank() || pass.isBlank()) return@withContext false
        try {
            syncWithSystemCookieManager()
            val loginRes = login(enrollment, pass, campus)
            if (!loginRes.isSuccess) {
                Log.w(TAG, "ensureLmsSession: CMS login failed")
                return@withContext false
            }

            // Bridge from CMS into LMS to get LMS session cookies (PHPSESSID)
            val lmsBridgeRequest = Request.Builder()
                .url(CMS_GOTO_LMS_URL)
                .header("User-Agent", USER_AGENT)
                .header("Referer", CMS_LOGIN_URL)
                .build()
            val resp = client.newCall(lmsBridgeRequest).execute()
            resp.close()
            Log.d(TAG, "ensureLmsSession: Successfully bridged to LMS")
            true
        } catch (e: Exception) {
            Log.e(TAG, "ensureLmsSession error: ${e.message}", e)
            false
        }
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
                .header("Referer", CMS_ATTENDANCE_URL)
                .build()
            client.newCall(lmsBridgeRequest).execute().close()

            // Step 2: Open Assignments page
            val initialRequest = Request.Builder()
                .url(LMS_ASSIGNMENTS_URL)
                .header("User-Agent", USER_AGENT)
                .build()

            val initialResponse = client.newCall(initialRequest).execute()
            val initialHtml = initialResponse.body?.string() ?: ""
            val initialDoc = Jsoup.parse(initialHtml)

            // Extract current semester token
            val semesterSelect = initialDoc.select("select#semesterId option[selected]").firstOrNull()
                ?: initialDoc.select("select#semesterId option").firstOrNull()
            val semesterKey = semesterSelect?.`val`() ?: ""

            // Extract all registered courses from dropdown
            val courseOptions = initialDoc.select("select#courseId option").filter {
                it.`val`().isNotBlank() && !it.text().contains("Select Course", ignoreCase = true)
            }

            val allAssignments = mutableListOf<Assignment>()

            if (courseOptions.isEmpty()) {
                val defaultCourse = initialDoc.select("select#courseId option[selected]").firstOrNull()?.text()?.trim() ?: "Course"
                allAssignments.addAll(parseAssignmentsFromTable(initialDoc, defaultCourse))
            } else {
                // Fetch assignments across ALL courses concurrently in parallel
                coroutineScope {
                    val deferredList = courseOptions.map { opt ->
                        async(Dispatchers.IO) {
                            val courseKey = opt.`val`()
                            val courseTitle = opt.text().trim()
                            if (opt.hasAttr("selected")) {
                                parseAssignmentsFromTable(initialDoc, courseTitle)
                            } else {
                                try {
                                    val courseUrl = "https://lms.bahria.edu.pk/Student/Assignments.php?s=$semesterKey&oc=$courseKey"
                                    val req = Request.Builder()
                                        .url(courseUrl)
                                        .header("User-Agent", USER_AGENT)
                                        .header("Referer", LMS_ASSIGNMENTS_URL)
                                        .build()
                                    val resp = client.newCall(req).execute()
                                    val html = resp.body?.string() ?: ""
                                    parseAssignmentsFromTable(Jsoup.parse(html), courseTitle)
                                } catch (e: Exception) {
                                    Log.w(TAG, "Failed to fetch assignments for $courseTitle: ${e.message}")
                                    emptyList()
                                }
                            }
                        }
                    }
                    deferredList.awaitAll().forEach { list ->
                        allAssignments.addAll(list)
                    }
                }
            }

            Result.success(allAssignments)
        } catch (e: Exception) {
            Log.e(TAG, "Fetch assignments failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun parseAssignmentsFromTable(doc: org.jsoup.nodes.Document, courseTitle: String): List<Assignment> {
        val list = mutableListOf<Assignment>()
        val rows = doc.select("table.table tr")

        for (row in rows) {
            val cols = row.select("td")
            if (cols.size >= 8) {
                val assignNo = cols[0].text().trim()
                val rawTitle = cols[1].text().trim()
                if (rawTitle.isBlank() || rawTitle.equals("Title", ignoreCase = true) || rawTitle.equals("Assignment", ignoreCase = true)) {
                    continue
                }

                // Download anchor in column 2: e.g. <a href="Download.php?k=..." class="label label-info">... Assignment </a>
                val downloadAnchor = cols[2].select("a[href*='Download.php']").firstOrNull {
                    it.text().contains("Assignment", ignoreCase = true)
                } ?: cols[2].select("a[href*='Download.php']").firstOrNull()
                  ?: cols[2].select("a[href]").firstOrNull()

                var downloadUrl = downloadAnchor?.attr("href") ?: ""
                if (downloadUrl.isNotBlank() && !downloadUrl.startsWith("http")) {
                    downloadUrl = "https://lms.bahria.edu.pk/Student/" + downloadUrl.trimStart('/')
                }

                // Column 3: Added Submission (e.g. contains Download link for submission vs "No Submission")
                val submissionCol = cols[3]
                val hasSubmission = submissionCol.select("a[href*='Download.php']").isNotEmpty() ||
                        (submissionCol.text().contains("Submission", ignoreCase = true) && !submissionCol.text().contains("No Submission", ignoreCase = true))

                // Column 6: Action ("Deadline Exceeded", "Submit")
                val actionCol = cols[6].text().trim()
                val isDeadlineExceeded = actionCol.contains("Deadline Exceeded", ignoreCase = true)

                // Column 7: Deadline ("25 November 2026-03:00 pm")
                val deadlineStr = cols[7].text().trim()
                val dueTimestamp = Assignment.parseDate(deadlineStr)

                val status = when {
                    hasSubmission -> "Submitted"
                    isDeadlineExceeded -> "Overdue"
                    dueTimestamp in 1 until System.currentTimeMillis() -> "Overdue"
                    else -> "Pending"
                }

                val safeCourse = courseTitle.replace("[^a-zA-Z0-9.-]".toRegex(), "_")
                val safeTitle = rawTitle.replace("[^a-zA-Z0-9.-]".toRegex(), "_")
                val suggestedFileName = "${safeCourse}_${safeTitle}.docx"

                list.add(
                    Assignment(
                        id = "${safeCourse}_${safeTitle}_$assignNo",
                        title = rawTitle,
                        courseName = courseTitle,
                        dueDateString = deadlineStr,
                        dueTimestamp = dueTimestamp,
                        status = status,
                        downloadUrl = downloadUrl,
                        fileName = suggestedFileName
                    )
                )
            } else if (cols.size in 3..7) {
                val rawTitle = cols[0].text().trim()
                if (rawTitle.isBlank() || rawTitle.equals("Title", ignoreCase = true)) continue
                val course = if (cols.size >= 4) cols[1].text().trim() else courseTitle
                val deadlineStr = cols[cols.size - 1].text().trim()
                val dueTimestamp = Assignment.parseDate(deadlineStr)
                val anchor = row.select("a[href*='Download.php']").firstOrNull() ?: row.select("a[href]").firstOrNull()
                var downloadUrl = anchor?.attr("href") ?: ""
                if (downloadUrl.isNotBlank() && !downloadUrl.startsWith("http")) {
                    downloadUrl = "https://lms.bahria.edu.pk/Student/" + downloadUrl.trimStart('/')
                }

                list.add(
                    Assignment(
                        id = "${course}_$rawTitle".replace(" ", "_"),
                        title = rawTitle,
                        courseName = course,
                        dueDateString = deadlineStr,
                        dueTimestamp = dueTimestamp,
                        status = if (dueTimestamp in 1 until System.currentTimeMillis()) "Overdue" else "Pending",
                        downloadUrl = downloadUrl,
                        fileName = "${rawTitle.replace(" ", "_")}.docx"
                    )
                )
            }
        }
        return list
    }

    suspend fun downloadAssignmentFile(
        downloadUrl: String,
        targetDir: java.io.File,
        suggestedFileName: String,
        onSessionNeeded: (suspend () -> Boolean)? = null
    ): Result<java.io.File> = withContext(Dispatchers.IO) {
        try {
            syncWithSystemCookieManager()
            var result = executeDownload(downloadUrl, targetDir, suggestedFileName)

            // If it failed because of an HTML login redirect (session expired), re-authenticate and retry!
            if (result.isFailure && onSessionNeeded != null) {
                val err = result.exceptionOrNull()?.message ?: ""
                if (err.contains("SESSION_EXPIRED", ignoreCase = true)) {
                    Log.i(TAG, "LMS session expired on download. Refreshing portal session...")
                    val refreshed = onSessionNeeded()
                    if (refreshed) {
                        result = executeDownload(downloadUrl, targetDir, suggestedFileName)
                    }
                }
            }

            result
        } catch (e: Exception) {
            Log.e(TAG, "File download failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun executeDownload(
        downloadUrl: String,
        targetDir: java.io.File,
        suggestedFileName: String
    ): Result<java.io.File> {
        val request = Request.Builder()
            .url(downloadUrl)
            .header("User-Agent", USER_AGENT)
            .header("Referer", LMS_ASSIGNMENTS_URL)
            .header("Accept", "*/*")
            .build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            return Result.failure(Exception("HTTP ${response.code}: Download failed"))
        }
        val body = response.body ?: return Result.failure(Exception("Empty file response"))

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

        val fileLength = tempFile.length()
        if (fileLength == 0L) {
            tempFile.delete()
            return Result.failure(Exception("Downloaded file was empty"))
        }

        // 3. Inspect header bytes to verify it's a real document and NOT an HTML login redirect!
        val headerBytes = ByteArray(1024.coerceAtMost(fileLength.toInt()))
        val readBytes = tempFile.inputStream().use { it.read(headerBytes) }
        val headerText = String(headerBytes, 0, readBytes, Charsets.UTF_8).lowercase()
        val contentType = response.header("Content-Type")?.lowercase() ?: ""

        val isHtml = contentType.contains("text/html") ||
                headerText.contains("<!doctype html") ||
                headerText.contains("<html") ||
                headerText.contains("<head") ||
                headerText.contains("login.aspx") ||
                headerText.contains("aspirewebcontrols") ||
                headerText.contains("cms - bahria university") ||
                headerText.contains("window.location")

        if (isHtml) {
            tempFile.delete()
            return Result.failure(Exception("SESSION_EXPIRED: Portal returned HTML login page instead of assignment binary"))
        }

        // 4. Inspect magic bytes & Content-Type to guarantee the extension matches the true file format!
        val isZipOrDocx = readBytes >= 4 &&
                headerBytes[0] == 0x50.toByte() && headerBytes[1] == 0x4B.toByte() &&
                headerBytes[2] == 0x03.toByte() && headerBytes[3] == 0x04.toByte() // "PK\u0003\u0004"
        val isPdf = readBytes >= 4 &&
                headerBytes[0] == 0x25.toByte() && headerBytes[1] == 0x50.toByte() &&
                headerBytes[2] == 0x44.toByte() && headerBytes[3] == 0x46.toByte() // "%PDF"
        val isOleDoc = readBytes >= 4 &&
                headerBytes[0] == 0xD0.toByte() && headerBytes[1] == 0xCF.toByte() &&
                headerBytes[2] == 0x11.toByte() && headerBytes[3] == 0xE0.toByte() // Word 97-2003 .doc (OLE2)
        val isRar = readBytes >= 4 &&
                headerBytes[0] == 0x52.toByte() && headerBytes[1] == 0x61.toByte() &&
                headerBytes[2] == 0x72.toByte() && headerBytes[3] == 0x21.toByte() // "Rar!"

        var finalExtension = if (resolvedName.contains(".")) resolvedName.substringAfterLast(".").lowercase() else ""
        when {
            isZipOrDocx -> {
                // If it's an Office Open XML (DOCX/PPTX/XLSX) or ZIP container
                if (finalExtension.isBlank() || finalExtension == "pdf" || finalExtension == "doc") {
                    finalExtension = when {
                        contentType.contains("presentation") || headerText.contains("ppt/") -> "pptx"
                        contentType.contains("spreadsheet") || headerText.contains("xl/") -> "xlsx"
                        contentType.contains("zip") -> "zip"
                        else -> "docx"
                    }
                }
            }
            isPdf -> finalExtension = "pdf"
            isOleDoc -> finalExtension = "doc"
            isRar -> finalExtension = "rar"
            contentType.contains("officedocument.wordprocessingml") -> finalExtension = "docx"
            contentType.contains("msword") -> finalExtension = "doc"
            finalExtension.isBlank() -> finalExtension = "docx"
        }

        val baseName = if (resolvedName.contains(".")) resolvedName.substringBeforeLast(".") else resolvedName
        val finalFileName = "$baseName.$finalExtension"
        val finalFile = java.io.File(targetDir, finalFileName)

        if (finalFile.exists()) {
            finalFile.delete()
        }
        tempFile.renameTo(finalFile)

        return Result.success(finalFile)
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
