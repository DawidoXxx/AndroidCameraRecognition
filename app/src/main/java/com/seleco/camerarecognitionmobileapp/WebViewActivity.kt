package com.seleco.camerarecognitionmobileapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

//Activity for opening web page with received url
class WebViewActivity : AppCompatActivity() {

    lateinit var urlToPicture: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        urlToPicture = intent.getStringExtra("url").toString()
        val webView: WebView = findViewById(R.id.webview)
        webView.apply {

            urlToPicture?.let { loadUrl(it)}
            settings.javaScriptEnabled = true
            //settings.safeBrowsingEnabled = true
        }


    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}