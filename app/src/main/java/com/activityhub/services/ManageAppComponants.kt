package com.activityhub.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.activityhub.utils.other.Common.IS_FILTER
import com.activityhub.utils.other.SessionManager

@SuppressLint("Registered")
class ManageAppComponants : Service() {

    lateinit var sessionManager: SessionManager

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        sessionManager = SessionManager(this)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sessionManager.put(IS_FILTER, 0)
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        Log.e("ManageAppComponants", "END")
        stopSelf()
    }

}
