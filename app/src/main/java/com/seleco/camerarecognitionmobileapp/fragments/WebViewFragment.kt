package com.seleco.camerarecognitionmobileapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import com.seleco.camerarecognitionmobileapp.R

class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    lateinit var urlToPicture: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = activity?.getSharedPreferences("DataToWebView",Context.MODE_PRIVATE)

        urlToPicture = sharedPreferences?.getString("urlToWebView","https://www.google.com").toString()

        val webView = view.findViewById<WebView>(R.id.web_view_fragment)
        val goBackBtn = view.findViewById<Button>(R.id.go_back_to_main_btn)

        webView.apply {

            loadUrl(urlToPicture)
            settings.javaScriptEnabled = true
            //settings.safeBrowsingEnabled = true
        }

        goBackBtn.setOnClickListener {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.frame_layout,MainFragment())
                ?.commit()
        }
    }
}