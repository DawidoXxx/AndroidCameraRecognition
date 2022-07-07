package com.seleco.camerarecognitionmobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.edit

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val userNameBtn = findViewById<EditText>(R.id.userNameEditText)
        val userPasswordBtn = findViewById<EditText>(R.id.userPasswordEditText)

        //Checking if written login and password are correct
        //if its go to main activity
        //if not tell user that he get it wrong
        loginBtn.setOnClickListener{
            val name = userNameBtn.text
            val pass = userPasswordBtn.text
            if (name.equals("seleco")&&pass.equals("camera")){
                val sharedPreferences =getSharedPreferences("loginSettings", MODE_PRIVATE)
                sharedPreferences.edit {
                    putBoolean("autoLogin",true)
                }

                val mainIntent = Intent(this,MainActivity::class.java)
                startActivity(mainIntent)
            }
            else
                Toast.makeText(this,"Wrong user name or password",Toast.LENGTH_SHORT).show()
        }
    }
}