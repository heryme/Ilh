package com.activityhub.activity.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.activityhub.R
import com.activityhub.activity.common.BaseActivity
import com.activityhub.activity.home.Act_Home
import com.activityhub.utils.extension.isNetWork
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.other.Common.AUTH_TOKEN
import com.activityhub.utils.other.Common.FCM_TOKEN
import com.activityhub.utils.other.SIGN_IN
import com.activityhub.utils.other.STATUS_CODE
import com.activityhub.utils.other.data
import com.activityhub.utils.other.statusCode
import kotlinx.android.synthetic.main.act_login.*
import okhttp3.ResponseBody
import org.json.JSONObject


class Act_Login : BaseActivity(), View.OnClickListener, ApiResponseInterface {

    private val TAG = javaClass.simpleName
    private val deviceToken = "device_token"

    companion object {
        val isComeFromSignIn = "isComeFromSignIn"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_login)

        initComponent()
        initToolbar()
        initListeners()
        initData()

//        TODO Remove when give build
//        etSignInActivityEmail.setText("osm@yopmail.com")
//        etSignInActivityPassword.setText("123456")

    }

    override fun onClick(view: View?) {
        when (view) {

            tvSignInActivityNewUser -> {
                startActivity(Intent(this@Act_Login, Act_Register::class.java))
            }

            tvSignInActivityForgotPassword -> {
                startActivity(Intent(this@Act_Login, Act_Forgot_Passwod::class.java))
            }

            btnSignInActivitySignIn -> {

                if (isNetWork(this)) {
                    if (isValidInput()) {
                        Log.e(TAG, "Device token --- ${sessionManager[FCM_TOKEN, ""]}")
                        ApiRequest(this@Act_Login,
                                ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).signIn(
                                        etSignInActivityEmail.text.toString(),
                                        etSignInActivityPassword.text.toString(),
                                        sessionManager[FCM_TOKEN, ""]), SIGN_IN, true, this)
                    }
                } else {
                    showSnackBar(resources.getString(R.string.internet_not_available))
                }

            }
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            SIGN_IN -> {
                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                val response = JSONObject(responseValue)
                val statusCode = response.optInt(statusCode)
                val data = response.getJSONObject(data)

                Log.e("SIGN_IN", "SIGN_IN = " + data.optString(getString(R.string.token)))
                if (statusCode == STATUS_CODE) {
                    sessionManager.put(getString(R.string.id), data.optString(getString(R.string.id)))
                    sessionManager.put(getString(R.string.name), data.optString(getString(R.string.name)))
                    sessionManager.put(getString(R.string.device_token), data.optString(getString(R.string.device_token)))
                    sessionManager.put(AUTH_TOKEN, getString(R.string.bearer) + " " + data.optString(getString(R.string.token)))
                    sessionManager.put(getString(R.string.emailId), data.optString(getString(R.string.emailId)))
                    sessionManager.put(getString(R.string.profileImage), data.optString(getString(R.string.profileImage)))
                    sessionManager.put(getString(R.string.isLogin), true)

                    if (chkSignInActivityRememberMe.isChecked) {
                        sessionManager.put(getString(R.string.isRememberMe), true)
                        sessionManager.put(getString(R.string.rememberMeEmail), etSignInActivityEmail.text.toString())
                        sessionManager.put(getString(R.string.rememberMePass), etSignInActivityPassword.text.toString())
                    } else {
                        sessionManager.put(getString(R.string.isRememberMe), false)
                    }


                    Log.e(TAG, "DEVICE_TOKEN= " + sessionManager[deviceToken, ""])
                    Log.e(TAG, "AUTH_TOKEN= " + sessionManager[AUTH_TOKEN, ""])

                    if (!sessionManager[getString(R.string.isFirstTimeYourProfile), false]) {
                        startActivity(Intent(this@Act_Login,
                                Act_Profile::class.java).putExtra(isComeFromSignIn, true))
                        sessionManager.put(getString(R.string.isFirstTimeYourProfile), true)
                        finish()
                    } else {
                        startActivity(Intent(this@Act_Login, Act_Home::class.java))
                        finish()
                    }
                }

            }
        }
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(layout_main, error_message)
        }
    }

    override fun initComponent() {
    }

    override fun initToolbar() {
    }

    override fun initListeners() {
        tvSignInActivityNewUser.setOnClickListener(this)
        tvSignInActivityForgotPassword.setOnClickListener(this)
        btnSignInActivitySignIn.setOnClickListener(this)
    }

    override fun initData() {
        if (sessionManager[getString(R.string.isRememberMe), false]) {
            chkSignInActivityRememberMe.isChecked = true
            etSignInActivityEmail.setText(sessionManager[getString(R.string.rememberMeEmail), ""])
            etSignInActivityPassword.setText(sessionManager[getString(R.string.rememberMePass), ""])
        }
    }

    private fun isValidInput(): Boolean {
        if (TextUtils.isEmpty(etSignInActivityEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etSignInActivityEmail, getString(R.string.toast_email))
            return false
        } else if (TextUtils.isEmpty(etSignInActivityPassword!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etSignInActivityPassword, getString(R.string.toast_password))
            return false
        }
        return true
    }

}
