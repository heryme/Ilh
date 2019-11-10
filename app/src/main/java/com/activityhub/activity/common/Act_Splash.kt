package com.activityhub.activity.common

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.activityhub.R
import com.activityhub.activity.auth.Act_Login
import com.activityhub.activity.home.Act_Home
import com.activityhub.utils.extension.isNetWork
import com.activityhub.utils.other.Common
import com.activityhub.utils.other.Common.FCM_TOKEN
import com.activityhub.utils.other.setScreenWithNoLimits
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult


class Act_Splash : BaseActivity() {

    private val TAG = Act_Splash::class.java.name
    private val SPLASH_TIME_OUT = 3000.0 // TODO
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash)

        setScreenWithNoLimits(window)
        initComponent()
        initToolbar()
        initListeners()
        initData()
    }

    override fun initToolbar() {
    }

    override fun initListeners() {
    }

    override fun initComponent() {
    }

    override fun initData() {
        sessionManager.put(Common.COMPLETED_EVENT_DIALOG, false)
        Log.e("AUTH_TOKEN", "AUTH TOKEN = $auth_token")
        generateFCMToken()
        checkLogin()
    }

    private fun checkLogin() {
        handler = Handler()
        runnable = Runnable {

            if (sessionManager[Act_Welcome.isFirstTime, false]) {
                if (sessionManager[getString(R.string.isLogin), false]) {
                    val intent = Intent(this@Act_Splash, Act_Home::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@Act_Splash, Act_Login::class.java)
                    startActivity(intent)
                }
                finish()
            } else {
                val intent = Intent(this@Act_Splash, Act_Welcome::class.java)
                startActivity(intent)
                finish()
            }
        }
        handler.postDelayed(runnable, SPLASH_TIME_OUT.toLong())

    }

    private fun generateFCMToken(): String {
        var device_token = ""
        if (isNetWork(this)) {
            FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult> {
                        override fun onComplete(task: Task<InstanceIdResult>) {
                            if (!task.isSuccessful) {
                                Log.e(TAG, "FCM TOKEN NOT GENERATED.")
                                return
                            }
                            device_token = task.result?.token.toString()
                            Log.e(TAG, "FCM TOKEN =  $device_token")
                            sessionManager.put(FCM_TOKEN, device_token)
                        }
                    })
            return device_token
        } else {
            return device_token
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy()
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onResume() {
//        handler?.postDelayed(runnable, SPLASH_TIME_OUT.toLong())
        checkLogin()
        super.onResume()
    }
}