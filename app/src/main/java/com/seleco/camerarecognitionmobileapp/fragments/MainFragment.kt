package com.seleco.camerarecognitionmobileapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.seleco.camerarecognitionmobileapp.*
import com.seleco.camerarecognitionmobileapp.MyMqttService
import java.lang.StringBuilder


class MainFragment : Fragment() {

//    private lateinit var mqttClient: MQTTClient
    private lateinit var mqttServerUrl:String
    private lateinit var mqttUserTopic:String
    private lateinit var mqttUserLogin:String
    private lateinit var mqttUserPassword:String


    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view =  inflater.inflate(R.layout.fragment_main,container,false)

        val serTV = view.findViewById<TextView>(R.id.server_url_get_data)
        val userLog = view.findViewById<TextView>(R.id.user_login_get_data)
        val userPass = view.findViewById<TextView>(R.id.user_pass_get_data)
        val stopServiceBtn = view.findViewById<Button>(R.id.stop_service_btn)

        //#1 Get arguments from prev fragment
        //Shit has to be modified
        //Probably i need ViewModel to make MqttClient there couse i don't wanna
        //initiate it everytime i come back here
        //If its service than i shouldnt need to init everytime i go back
        //I need mechanism when i close app and come back there should be reconnect mech..
        mqttServerUrl = this.arguments?.getString(MQTT_Server_URL,"").toString()
        mqttUserTopic = this.arguments?.getString(MQTT_Topic,"").toString()
        mqttUserLogin = this.arguments?.getString(MQTT_User_Login,"").toString()
        mqttUserPassword = this.arguments?.getString(MQTT_User_Password,"").toString()


//        serTV.text = mqttServerUrl
//        userLog.text = mqttUserLogin
//        userPass.text = mqttUserPassword
        serTV.text = "url"
        userLog.text = "login"
        userPass.text = "password"
        stopServiceBtn.setOnClickListener {
            (activity as MainActivity).stopService(Intent(context,MyMqttService::class.java))
            stopServiceBtn.text = "Stopped"
        }

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val webActivity = WebViewActivity()

        //create service intent and putExtras
        //with mqtt broker login info and topic to subscribe
        val serviceIntent = Intent(context,MyMqttService::class.java)
        serviceIntent.putExtra(MQTT_Server_URL,mqttServerUrl)
        serviceIntent.putExtra(MQTT_User_Login,mqttUserLogin)
        serviceIntent.putExtra(MQTT_User_Password,mqttUserPassword)
        serviceIntent.putExtra(MQTT_Topic,mqttUserTopic)

        (activity as MainActivity).startService(serviceIntent)



        //#2 Get arguments from previous fragment
//        activity?.supportFragmentManager
//            ?.setFragmentResultListener(MQTT_Credentials, this) { key, bundle ->
//                mqttServerUrl = bundle.getString(MQTT_Server_URL).toString()
//                mqttUserLogin = bundle.getString(MQTT_User_Login).toString()
//                mqttUserPassword = bundle.getString(MQTT_User_Password).toString()
//            }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

//        outState.putString("","")
//        super.onSaveInstanceState(outState)
    }
}