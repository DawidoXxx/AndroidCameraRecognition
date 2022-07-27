package com.seleco.camerarecognitionmobileapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.lang.StringBuilder

class MyMqttService : Service() {

    lateinit var mqttClient:MQTTClient

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Get data from incoming intent
        val mqttServerUrl = intent?.getStringExtra(MQTT_Server_URL).toString()
        val mqttLogin = intent?.getStringExtra(MQTT_User_Login)
        val mqttPassword = intent?.getStringExtra(MQTT_User_Password)
        val mytopic = intent?.getStringExtra(MQTT_Topic)
        val mqttClientId = MqttClient.generateClientId()
        startForegroundService()

        mqttClient =  MQTTClient(this, mqttServerUrl,mqttClientId)

        if (mqttLogin!=null && mqttPassword!=null && mytopic!=null){

            mqttClient.connect(mqttLogin,mqttPassword,
                object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(this@MyMqttService.javaClass.toString(),"We succesed in connecting to server")
                    //If connection is successful than subscribe
                    mqttClient.subscribe(mytopic.toString(), 1, object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            Log.d(this.javaClass.name, "Success in subscribing to the topic")
//                            Toast.makeText(context, "You did it", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                            Log.d(this.javaClass.name, "Failure in subscribing to the topic")
//                            Toast.makeText(context, "${exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(this@MyMqttService.javaClass.toString(),"We failed in connecting to server")
                }
            },
                object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    Log.d(this.javaClass.name,"Connection has been lost")
                    //If we dont mind lossing state from mainFragment than commitAllowingStaleLoss()
                    //If not commit()
                    //The Exception hit when transaction is called after onSavedInstanceState()
                    //activityivity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame_layout,MqttConnectorFragment())?.commitAllowingStateLoss()
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    Log.d(this.javaClass.name,"Message received: ${message.toString()}")
                    val messageToPast = message.toString()
                    val sharedPreferences = getSharedPreferences("DataToWebView",Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("urlToWebView",getUrlFromMessage(messageToPast)).apply()
                    Toast.makeText(this@MyMqttService,messageToPast,Toast.LENGTH_SHORT).show()
                    //More complex messages to jsonObject
//                    JSONObject jsonmsg = new JSONObject(new String(m.getPayload());
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.d(this.javaClass.name,"Delivery completed")
                }

            }
                )
        }

        return START_STICKY
    }

    private fun getMainActivityPendingINtent() = PendingIntent.getActivity(
        this,
    0,
    Intent(this,MainActivity::class.java).also { it.action = ACTION_SHOW_MAIN_FRAGMENT },
    FLAG_UPDATE_CURRENT)

    private fun startForegroundService(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Mqtt communication")
            .setContentIntent(getMainActivityPendingINtent())

        startForeground(NOTIFICATION_ID,notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_NAME,
            IMPORTANCE_LOW)

        notificationManager.createNotificationChannel(channel)
    }


    override fun onDestroy() {
        //here we have to stop foregroundService
        mqttClient.releaseResources()
        super.onDestroy()
    }

    fun getUrlFromMessage(message:String):String{
        val urlToReturn = StringBuilder()
        urlToReturn.append(message.removePrefix("{").removeSuffix("}").split("\":\"")[1].replace("\"",""))
        return urlToReturn.toString()
    }
}