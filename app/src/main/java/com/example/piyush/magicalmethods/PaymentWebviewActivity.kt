package com.example.piyush.magicalmethods

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_payment_webview.*
import java.net.URLEncoder
import android.content.Intent



class PaymentWebviewActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_webview)

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

        val url = "http://mm.s-ct.asia/payment_request.php"
        val postData = "courseKey=" + URLEncoder.encode("asaouetsfnvpsq") + "&email=" + URLEncoder.encode("ashish96082@gmail.com") + "&userid=" + URLEncoder.encode("randomkey")

        paymentWebview.postUrl(url, postData.toByteArray())
    }
}
