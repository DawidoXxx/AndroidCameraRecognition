package com.seleco.camerarecognitionmobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import com.seleco.camerarecognitionmobileapp.fragments.MqttConnectorFragment
import com.seleco.camerarecognitionmobileapp.fragments.WebViewFragment


class MainActivity : AppCompatActivity(), LifecycleObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //TODO get along with navigation
        navigateToWebViewFragmentIfNeeded(intent)

        //Part of Lifecycle observer mechanism to get awarness of this activity about it state
        //ProcessLifecycleOwner.get().lifecycle.addObserver("class with implemented LifecycleObserver")

        val fragmentMqttConnector = MqttConnectorFragment()
        val sharedPreferences = getSharedPreferences("loginSettings", MODE_PRIVATE)
        var edit = sharedPreferences.edit()

        //If you are not log in go to login Activity else stay in main
        if (!sharedPreferences.getBoolean("autoLogin",false)) {
            val loginIntent = Intent(this,LoginActivity::class.java)
            startActivity(loginIntent)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout,fragmentMqttConnector).commit()

        navigateToWebViewFragmentIfNeeded(intent)

    }

    fun startService(){
        Intent(this, MyMqttService::class.java).also {
            startService(it)
        }
    }

    fun stopService(){
        Intent(this, MyMqttService::class.java).also {
            stopService(it)
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToWebViewFragmentIfNeeded(intent)
    }

    private fun navigateToWebViewFragmentIfNeeded(intent: Intent?){
        if (intent?.action == ACTION_SHOW_MAIN_FRAGMENT){
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout,WebViewFragment()).commit()
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        //if pressed back system thinks that you wont come back so destroy everything
        //override the onBackPressed() method to implement custom behavior,
        //such as displaying a dialog that asks the user to confirm that they want to exit your app.
    }

    //Its gonna save to bundle stuff when activity is going to be destroyed
    //its the same bundle as in onCreate()
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {

        //save here

        //Propably should solve problem with "IllegalStateException: Can not perform this action after onSaveInstanceState"
        super.onSaveInstanceState(outState, outPersistentState)

        //to do the same as above but different way check MainFragment

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //here code to run when activity get reactivated

    }

    override fun onResume() {
        Log.d(this.javaClass.name,"Main Activity has been resumed")
        super.onResume()
    }



    override fun onDestroy() {
        Log.d(this::class.java.name,"Main Activity has been destroyed")
        super.onDestroy()
    }

    //Test !!!
    //Another app, such as the device's phone app, interrupts your app's activity.
    //The system destroys and recreates your activity.
    //The user places your activity in a new windowing environment, such as picture-in-picture (PIP) or multi-window.

}