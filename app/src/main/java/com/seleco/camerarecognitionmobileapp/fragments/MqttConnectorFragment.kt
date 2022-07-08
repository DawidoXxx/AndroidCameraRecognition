package com.seleco.camerarecognitionmobileapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.seleco.camerarecognitionmobileapp.R


class MqttConnectorFragment : Fragment(R.layout.fragment_mqtt_connector) {



//    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?): View? {
//
//         }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<Button>(R.id.mqtt_connector_button)
        val fragment = Fragment(R.layout.fragment_main)

        button.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame_layout,fragment)?.commit()

        }
    }
}