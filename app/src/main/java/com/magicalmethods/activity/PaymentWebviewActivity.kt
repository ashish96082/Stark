package com.magicalmethods.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.firebase.auth.FirebaseAuth
import com.magicalmethods.R
import kotlinx.android.synthetic.main.activity_payment_webview.*
import java.net.URLEncoder


class PaymentWebviewActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_webview)

        val courseKey = intent.getStringExtra("courseKey")
        if (courseKey == null)
            onBackPressed()

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user == null) {
            openLoginActivity()
        }
        user?.displayName

        val paymentWebview = payment_webview as WebView
        paymentWebview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url!!.startsWith("paymentresponse://")) {
                    val intent = Intent(applicationContext, PaymentResponseActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                return false
            }
        }
        val paymentWebviewSettings = paymentWebview.settings
        paymentWebviewSettings.javaScriptEnabled = true
        paymentWebviewSettings.displayZoomControls = false

        val url = "http://appb.magicalmethods.com/payment_request.php"
        val postData = "courseKey=" + URLEncoder.encode(courseKey) + "&email=" + URLEncoder.encode(user?.email) + "&userid=" + URLEncoder.encode(user?.uid)

        paymentWebview.postUrl(url, postData.toByteArray())
    }

    private fun openLoginActivity() {
        val intent =  Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
