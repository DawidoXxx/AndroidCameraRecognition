package com.seleco.camerarecognitionmobileapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MQTTClient(context:Context?,serverURI:String,clientID:String = "") : LifecycleObserver{


    private var mqttClient = MqttAndroidClient(context,serverURI,clientID)

    //Default listeners
    private val defaultCbConnect = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d(this.javaClass.name,"(Default) Successful connected")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d(this.javaClass.name,"Can't connect to broker: ${exception?.message}")
        }
    }

    private val defaultCbClient = object : MqttCallback {
        override fun connectionLost(cause: Throwable?) {
            Log.d(this.javaClass.name,"Connection is lost due to: ${cause?.message}")
        }

        override fun messageArrived(topic: String?, message: MqttMessage?) {
            Log.d(this.javaClass.name,"Message arrived: ${message.toString()} from topic: $topic")

        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.d(this.javaClass.name,"Delivery completed")
        }
    }

    private val defaultCbSubscribe = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d(this.javaClass.name,"Subscribed to topic ")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d(this.javaClass.name,"Failed to subscribe")
        }
    }

    private val defaultCbUnsubscribe = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d(this.javaClass.name,"Unsubscribed to topic")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d(this.javaClass.name,"Failed to unsubscribe")
        }
    }

    private val defaultCbPublish = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d(this.javaClass.name,"Message published to topic")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d(this.javaClass.name,"Failed to publish message")
        }
    }

    private val defaultCbDisconnect = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d(this.javaClass.name,"Disconnect completed")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d(this.javaClass.name,"Failed to disconnect")
        }
    }

    //Connect to broker method
    fun connect(username:String, password:String,
                cbConnect:IMqttActionListener = defaultCbConnect,
                cbClient:MqttCallback = defaultCbClient){
        mqttClient.setCallback(cbClient)
        val options = MqttConnectOptions()
        options.userName = username
        options.password = password.toCharArray()
        options.connectionTimeout = 200
        options.keepAliveInterval = 200


        try {
            mqttClient.connect(options,null,cbConnect)
        } catch (e:MqttException){
            e.printStackTrace()
        }
    }

    //Check if app is connected to MQTT Broker
    fun isConnected():Boolean{
        return mqttClient.isConnected
    }

    //Subscribe to specific topic
    fun subscribe(topic:String,qos:Int = 1,cbSubscribe:IMqttActionListener = defaultCbSubscribe){
        try {
            mqttClient.subscribe(topic,qos,null,cbSubscribe)
        } catch (e:MqttException){
            e.printStackTrace()
        }
    }

    fun unsubscribe(topic:String,cbUnsubscribe:IMqttActionListener = defaultCbUnsubscribe){
        try {
            mqttClient.unsubscribe(topic,null,cbUnsubscribe)
        } catch (e:MqttException){
            e.printStackTrace()
        }
    }

    //Publish message to broker
    fun publish(topic: String,msg:String,
                qos: Int = 1,
                retained:Boolean = false,
                cbPublish:IMqttActionListener = defaultCbPublish){
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic,message,null,cbPublish)
        } catch (e:MqttException){
            e.printStackTrace()
        }
    }

    //Part of Lifecycle observer mechanism to get awarness of this activity about it state
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun initializeSomething(){
        Log.d(this.javaClass.name,"Im alive at resume event")
    }

    public fun releaseResources(){
        mqttClient.unregisterResources()
        mqttClient.close()
    }

}