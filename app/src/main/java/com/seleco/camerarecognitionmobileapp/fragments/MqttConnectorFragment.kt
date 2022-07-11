package com.seleco.camerarecognitionmobileapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import com.seleco.camerarecognitionmobileapp.*


class MqttConnectorFragment : Fragment(R.layout.fragment_mqtt_connector) {



//    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?): View? {
//
//         }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connectUrlTextView = view.findViewById<TextView>(R.id.server_url_text_view)
        val topicTextView = view.findViewById<TextView>(R.id.topic_text_view)
        val loginTextView = view.findViewById<TextView>(R.id.user_login_text_view)
        val passwordTextView = view.findViewById<TextView>(R.id.password_text_view)

        val prefillBtn = view.findViewById<Button>(R.id.prefil_btn)
        val connectBtn = view.findViewById<Button>(R.id.connect_btn)
        val cleanBtn = view.findViewById<Button>(R.id.clear_btn)
        val fragment = MainFragment()

        //makes bundle from user data and send it to another fragment and switch view to new one
        connectBtn.setOnClickListener{
            val mqttCredentialBundle = bundleOf(
                MQTT_Server_URL to connectUrlTextView.text.toString(),
                MQTT_Topic to topicTextView.text.toString(),
                MQTT_User_Login to loginTextView.text.toString(),
                MQTT_User_Password to passwordTextView.text.toString()
            )

            //#1 way
            fragment.arguments = mqttCredentialBundle
            //#2 way of transferring data between fragments
            //activity?.supportFragmentManager?.setFragmentResult(MQTT_Credentials,mqttCredentialBundle)

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame_layout,fragment)?.commit()

        }

        cleanBtn.setOnClickListener {
            connectUrlTextView.text = ""
            topicTextView.text = ""
            loginTextView.text = ""
            passwordTextView.text = ""
        }

        //Prefill editText's with default values
        prefillBtn.setOnClickListener {
            connectUrlTextView.text = getString(R.string.defaultUrl)
            topicTextView.text = getString(R.string.defaultTopic)
            loginTextView.text = getString(R.string.defaultLogin)
            passwordTextView.text = getString(R.string.defaultPassword)
        }
    }

    //"@style/Theme.CameraRecognitionMobileApp"
}