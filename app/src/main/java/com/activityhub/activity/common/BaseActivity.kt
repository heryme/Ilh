package com.activityhub.activity.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.activityhub.R
import com.activityhub.app.AppController
import com.activityhub.utils.extension.isTablet
import com.activityhub.utils.other.Common.AUTH_TOKEN
import com.activityhub.utils.other.Common.FCM_TOKEN
import com.activityhub.utils.other.SessionManager


@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity() {

    lateinit var sessionManager: SessionManager
    lateinit var snackbar: Snackbar

    protected lateinit var mActivity: Activity
    protected lateinit var appcontroller: AppController
    protected lateinit var mContext: Context
    protected lateinit var device_token :String
    protected lateinit var auth_token :String

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var globalActivity: Activity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBaseComponants()
        initBaseData()
    }

    private fun initBaseData() {
        device_token =  sessionManager[FCM_TOKEN, ""]
        auth_token = sessionManager[AUTH_TOKEN, ""]
    }

    private fun initBaseComponants() {
        sessionManager = SessionManager(baseContext)
        appcontroller = application as AppController
        mActivity = this@BaseActivity
        mContext = this@BaseActivity
        globalActivity = this@BaseActivity

        requestedOrientation = when {
            isTablet(baseContext) -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    // where we initialize componants of activity
    abstract fun initComponent()

    // where we initialize toolbar of activity
    abstract fun initToolbar()

    // where we initialize listeners of activity
    abstract fun initListeners()

    // where we set data of activity
    abstract fun initData()


    fun showSnackBar(view: View, message: String) {
        snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.dismiss)) {
                    snackbar.dismiss()
                }
        snackbar.setActionTextColor(Color.RED)
        snackbar.duration = 4000
        snackbar.show()
    }

     fun showSnackBar(message: String) {
        val parentLayout = findViewById<View>(android.R.id.content)
        snackbar = Snackbar
                .make(parentLayout, message, Snackbar.LENGTH_SHORT)
         snackbar.show()
    }


    /*fun openExitDialogue() {

        dialogue = openDialogActionTwo(resources.getString(R.string.tv_title_text), resources.getString(R.string.close_yes),
                resources.getString(R.string.close_no), mActivity)

        val layout_negative = dialogue.findViewById(R.id.layout_negative) as LinearLayout
        val layout_positive = dialogue.findViewById(R.id.layout_positive) as LinearLayout

        layout_negative.setOnClickListener {
            dialogue.dismiss()
        }

        layout_positive.setOnClickListener {
            dialogue.dismiss()
        }

    }*/

}

