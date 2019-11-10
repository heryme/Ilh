package com.activityhub.firebase


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.activityhub.R
import com.activityhub.activity.common.Act_Splash
import com.activityhub.activity.common.BaseActivity
import com.activityhub.utils.other.Common
import com.activityhub.utils.other.Common.NOTIFICATION_CHANNEL_ID
import com.activityhub.utils.other.SessionManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class FirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var intent: Intent
    private lateinit var notificationManager: NotificationManager


    companion object {
        private val TAG = "MyFirebaseMsgService"
        val NOTIFICATION_ID = 1
        @SuppressLint("StaticFieldLeak")
        private lateinit var sessionManager: SessionManager
    }

    private val notificationIcon: Int
        get() {
            val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return if (useWhiteIcon) R.drawable.ic_launcher else R.drawable.ic_launcher
        }

    override fun onNewToken(device_token: String?) {
        super.onNewToken(device_token)
        sessionManager = SessionManager(BaseActivity.globalActivity)
        sessionManager.put(Common.FCM_TOKEN, device_token!!)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.e(TAG, "From: " + remoteMessage!!.from!!)
        sendNotification(remoteMessage.notification!!.body)
    }

    private fun sendNotification(messageBody: String?) {
        val random = Random()
        val Low = 10
        val High = 100
        val notificationId = random.nextInt(High - Low) + Low

        val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round)

        val intent = Intent(this, Act_Splash::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(applicationContext, (Math.random() * 100).toInt(), intent, PendingIntent.FLAG_ONE_SHOT)

//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(resources.getString(R.string.app_name))
                .setColor(NotificationCompat.COLOR_DEFAULT)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(notificationIcon)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setLargeIcon(icon)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(longArrayOf(500, 500))
                .setContentIntent(pendingIntent)


        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            @SuppressLint("WrongConstant") val notificationChannel = NotificationChannel("2", "Activityhub NOTIFICATION", importance)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(500, 500)
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }


}