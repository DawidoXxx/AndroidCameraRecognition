package com.seleco.camerarecognitionmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

//Activity for opening web page with received url
class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

//        Get url from mqtt received message
        val url:String = "";

        val webView: WebView = findViewById(R.id.webview)
        webView.loadUrl(url)
    }
}