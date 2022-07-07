package com.seleco.camerarecognitionmobileapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("loginSettings", MODE_PRIVATE)
        var edit = sharedPreferences.edit()

        //If you are not log in go to login Activity else stay in main
        if (!sharedPreferences.getBoolean("autoLogin",false)) {
            val loginIntent = Intent(this,LoginActivity::class.java)
            startActivity(loginIntent)
        }

        

    }
}