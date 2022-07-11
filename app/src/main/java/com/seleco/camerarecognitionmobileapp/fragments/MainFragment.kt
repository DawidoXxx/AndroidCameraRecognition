package com.seleco.camerarecognitionmobileapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.seleco.camerarecognitionmobileapp.*
import org.eclipse.paho.client.mqttv3.*


class MainFragment : Fragment() {

    private lateinit var mqttClient: MQTTClient
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

        //#1 Get arguments from prev fragment
        mqttServerUrl = this.arguments?.getString(MQTT_Server_URL,"").toString()
        mqttUserTopic = this.arguments?.getString(MQTT_Topic,"").toString()
        mqttUserLogin = this.arguments?.getString(MQTT_User_Login,"").toString()
        mqttUserPassword = this.arguments?.getString(MQTT_User_Password,"").toString()

        serTV.text = mqttServerUrl
        userLog.text = mqttUserLogin
        userPass.text = mqttUserPassword

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //#2 Get arguments from previous fragment
//        activity?.supportFragmentManager
//            ?.setFragmentResultListener(MQTT_Credentials, this) { key, bundle ->
//                mqttServerUrl = bundle.getString(MQTT_Server_URL).toString()
//                mqttUserLogin = bundle.getString(MQTT_User_Login).toString()
//                mqttUserPassword = bundle.getString(MQTT_User_Password).toString()
//            }

        if (mqttServerUrl != "" &&
            mqttUserPassword != "" &&
            mqttUserLogin != "") {

            mqttClient = MQTTClient(context, mqttServerUrl, "blablabla")

            mqttClient.connect(mqttUserLogin, mqttUserPassword, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(this.javaClass.name,"We connect to broker")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(this.javaClass.name,"We couldn't connect")
                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame_layout,MqttConnectorFragment())?.commit()
                }

            },object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    Log.d(this.javaClass.name,"Connection has been lost")
                    activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame_layout,MqttConnectorFragment())?.commit()
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    Log.d(this.javaClass.name,"Message received: ${message.toString()}")
                    Toast.makeText(context,message.toString(),Toast.LENGTH_SHORT).show()
                    //TODO(Get new activity and go to web view or maybe another fragment)
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.d(this.javaClass.name,"Delivery completed")
                }

            })
        } else {
            //Go back to connection fragment with toast message that you sent wrong data
            Toast.makeText(context,"Wrong credentials",Toast.LENGTH_SHORT).show()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame_layout,MqttConnectorFragment())?.commit()
        }
    }
}