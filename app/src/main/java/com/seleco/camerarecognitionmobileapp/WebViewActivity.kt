package com.seleco.camerarecognitionmobileapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

//Activity for opening web page with received url
class WebViewActivity() : AppCompatActivity() {

    lateinit var url: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val webView: WebView = findViewById(R.id.webview)
        webView.apply {
            loadUrl("https://www.google.com/")
            settings.javaScriptEnabled = true
            //settings.safeBrowsingEnabled = true
        }


    }
}