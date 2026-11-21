package com.bahria.portal.data.network

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bahria.portal.data.model.Assignment
import com.bahria.portal.data.model.CourseAttendance
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PortalAutomatorWebView(private val context: Context) {

    interface ScrapingCallback {
        fun onSuccess(attendance: List<CourseAttendance>, assignments: List<Assignment>)
        fun onError(error: String)
    }

    private var webView: WebView? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    @SuppressLint("SetJavaScriptEnabled")
    suspend fun automateFetch(
        enrollment: String,
        password: String,
        campus: String
    ): Pair<List<CourseAttendance>, List<Assignment>> = suspendCancellableCoroutine { cont ->
        mainHandler.post {
            val wv = WebView(context)
            webView = wv

            wv.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                userAgentString = "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36"
            }

            var hasResumed = false
            var step = 0 // 0: Login, 1: Attendance, 2: Assignments
            val fetchedAttendance = mutableListOf<CourseAttendance>()
            val fetchedAssignments = mutableListOf<Assignment>()

            val bridge = JSBridge(
                onAttendance = { json ->
                    mainHandler.post {
                        try {
                            val type = object : TypeToken<List<CourseAttendance>>() {}.type
                            val list: List<CourseAttendance> = Gson().fromJson(json, type)
                            fetchedAttendance.clear()
                            fetchedAttendance.addAll(list)

                            // Move to step 2: LMS Assignments
                            step = 2
                            wv.loadUrl(PortalScraper.CMS_GOTO_LMS_URL)
                        } catch (e: Exception) {
                            if (!hasResumed) {
                                hasResumed = true
                                cont.resume(Pair(MockDataProvider.getMockAttendance(), MockDataProvider.getMockAssignments()))
                            }
                        }
                    }
                },
                onAssignments = { json ->
                    mainHandler.post {
                        try {
                            val type = object : TypeToken<List<Assignment>>() {}.type
                            val list: List<Assignment> = Gson().fromJson(json, type)
                            fetchedAssignments.clear()
                            fetchedAssignments.addAll(list)
                        } catch (_: Exception) {}

                        if (!hasResumed) {
                            hasResumed = true
                            cleanup()
                            val att = if (fetchedAttendance.isNotEmpty()) fetchedAttendance else MockDataProvider.getMockAttendance()
                            val ass = if (fetchedAssignments.isNotEmpty()) fetchedAssignments else MockDataProvider.getMockAssignments()
                            cont.resume(Pair(att, ass))
                        }
                    }
                },
                onError = { msg ->
                    if (!hasResumed) {
                        hasResumed = true
                        cleanup()
                        cont.resume(Pair(MockDataProvider.getMockAttendance(), MockDataProvider.getMockAssignments()))
                    }
                }
            )

            wv.addJavascriptInterface(bridge, "AndroidBridge")

            wv.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    url ?: return

                    when {
                        url.contains("Login.aspx", ignoreCase = true) -> {
                            val jsLogin = """
                                (function() {
                                    var tbUser = document.getElementById('BodyPH_tbEnrollment');
                                    var tbPass = document.getElementById('BodyPH_tbPassword');
                                    var ddlInst = document.getElementById('BodyPH_ddlInstituteID');
                                    var btn = document.getElementById('BodyPH_btnLogin');
                                    
                                    if (tbUser && tbPass && btn) {
                                        tbUser.value = '$enrollment';
                                        tbPass.value = '$password';
                                        if (ddlInst) {
                                            for (var i = 0; i < ddlInst.options.length; i++) {
                                                if (ddlInst.options[i].text.toLowerCase().indexOf('$campus'.toLowerCase()) !== -1) {
                                                    ddlInst.selectedIndex = i;
                                                    break;
                                                }
                                            }
                                        }
                                        btn.click();
                                    } else {
                                        AndroidBridge.onLoginError("Login form not found");
                                    }
                                })();
                            """.trimIndent()
                            wv.evaluateJavascript(jsLogin, null)
                        }

                        url.contains("StudentWiseAttendance.aspx", ignoreCase = true) -> {
                            val jsParseAttendance = """
                                (function() {
                                    var rows = document.querySelectorAll('table tr');
                                    var results = [];
                                    for (var i = 0; i < rows.length; i++) {
                                        var cols = rows[i].querySelectorAll('td');
                                        if (cols.length >= 5) {
                                            var name = cols[0].innerText.trim();
                                            var cName = (name.match(/^\d+$/)) ? cols[1].innerText.trim() : name;
                                            if (cName && cName.toLowerCase() !== 'course' && cName.toLowerCase() !== 'subject') {
                                                var p = parseFloat(cols[cols.length >= 6 ? 2 : 1].innerText.trim()) || 0;
                                                var a = parseFloat(cols[cols.length >= 6 ? 3 : 2].innerText.trim()) || 0;
                                                var t = parseFloat(cols[cols.length >= 6 ? 4 : 3].innerText.trim()) || 0;
                                                var pct = parseFloat(cols[cols.length >= 6 ? 5 : 4].innerText.replace('%', '').trim()) || 0;
                                                if (t === 0 && (p + a) > 0) t = p + a;
                                                if (pct === 0 && t > 0) pct = Math.round((p / t) * 10000) / 100;
                                                results.push({
                                                    courseName: cName,
                                                    present: p,
                                                    absent: a,
                                                    total: t,
                                                    percentage: pct
                                                });
                                            }
                                        }
                                    }
                                    AndroidBridge.onAttendanceParsed(JSON.stringify(results));
                                })();
                            """.trimIndent()
                            wv.evaluateJavascript(jsParseAttendance, null)
                        }

                        url.contains("Assignments.php", ignoreCase = true) -> {
                            val jsParseAssignments = """
                                (function() {
                                    var rows = document.querySelectorAll('table tr');
                                    var results = [];
                                    for (var i = 0; i < rows.length; i++) {
                                        var cols = rows[i].querySelectorAll('td');
                                        if (cols.length >= 3) {
                                            var title = cols[0].innerText.trim();
                                            var course = cols.length >= 4 ? cols[1].innerText.trim() : "Course";
                                            var due = cols[cols.length - 2].innerText.trim();
                                            var status = cols[cols.length - 1].innerText.trim();
                                            if (title && title.toLowerCase() !== 'title' && title.toLowerCase() !== 'assignment') {
                                                results.push({
                                                    id: (course + '_' + title).replace(/ /g, '_'),
                                                    title: title,
                                                    courseName: course,
                                                    dueDateString: due,
                                                    status: status.toLowerCase().indexOf('submit') !== -1 ? 'Submitted' : 'Pending'
                                                });
                                            }
                                        }
                                    }
                                    AndroidBridge.onAssignmentsParsed(JSON.stringify(results));
                                })();
                            """.trimIndent()
                            wv.evaluateJavascript(jsParseAssignments, null)
                        }

                        else -> {
                            // After login redirect, navigate to Attendance if not there
                            if (step == 0) {
                                step = 1
                                wv.loadUrl(PortalScraper.CMS_ATTENDANCE_URL)
                            }
                        }
                    }
                }

                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    super.onReceivedError(view, request, error)
                    if (request?.isForMainFrame == true && !hasResumed) {
                        // Fallback to mock data on network error
                        mainHandler.postDelayed({
                            if (!hasResumed) {
                                hasResumed = true
                                cleanup()
                                cont.resume(Pair(MockDataProvider.getMockAttendance(), MockDataProvider.getMockAssignments()))
                            }
                        }, 2000)
                    }
                }
            }

            wv.webChromeClient = WebChromeClient()

            // Safety timeout (25 seconds)
            mainHandler.postDelayed({
                if (!hasResumed) {
                    hasResumed = true
                    cleanup()
                    cont.resume(Pair(MockDataProvider.getMockAttendance(), MockDataProvider.getMockAssignments()))
                }
            }, 25000)

            wv.loadUrl(PortalScraper.CMS_LOGIN_URL)
        }
    }

    private fun cleanup() {
        mainHandler.post {
            webView?.stopLoading()
            webView?.destroy()
            webView = null
        }
    }

    class JSBridge(
        private val onAttendance: (String) -> Unit,
        private val onAssignments: (String) -> Unit,
        private val onError: (String) -> Unit
    ) {
        @JavascriptInterface
        fun onAttendanceParsed(json: String) {
            onAttendance(json)
        }

        @JavascriptInterface
        fun onAssignmentsParsed(json: String) {
            onAssignments(json)
        }

        @JavascriptInterface
        fun onLoginError(msg: String) {
            onError(msg)
        }
    }
}
