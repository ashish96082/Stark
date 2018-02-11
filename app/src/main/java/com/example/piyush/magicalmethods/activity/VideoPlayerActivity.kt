package com.example.piyush.magicalmethods.activity

/**
 * Created by Android Studio.
 * User: ashish kumar
 * Date: 01-02-2018
 * Time: 04:41 PM
 */


import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.piyush.magicalmethods.R
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayerActivity : Activity() {
    lateinit var vimeoId: String

    private var customHtml = "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "</head>" +
            "<body style=\"padding: 0; margin: 0;\">" +
            "<div style=\"position: fixed; left: 0; top: 0; width: 100%; height: 100%;\">" +
            "<iframe src=\"https://player.vimeo.com/video/{{vimeo-id}}\" width=\"100%\" height=\"100%\" frameborder=\"0\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>" +
            "</div>" +
            "</body>" +
            "</html>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        vimeoId = intent.getStringExtra("vimeoVideoId") ?: "252220553"
        customHtml = customHtml.replace("{{vimeo-id}}", vimeoId)
        println(customHtml)

        val videoPlayer = video_player_webview as WebView
        videoPlayer.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return true
            }
        }
        val mVideoPlayerSettings = videoPlayer.settings
        mVideoPlayerSettings.javaScriptEnabled = true
        mVideoPlayerSettings.displayZoomControls = false


        videoPlayer.loadData(customHtml,"text/html", "UTF-8")
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
