package com.activityhub.activity.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import com.activityhub.R
import com.activityhub.activity.common.BaseActivity
import com.activityhub.utils.extension.Toast
import com.activityhub.utils.extension.isNetWork
import com.activityhub.utils.extension.makeTextViewClickeble
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.other.Common.FCM_TOKEN
import com.activityhub.utils.other.SIGN_UP
import com.activityhub.utils.other.STATUS_CODE
import com.activityhub.utils.other.Validator
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.act_register.*
import okhttp3.ResponseBody
import org.json.JSONObject

class Act_Register : BaseActivity(), View.OnClickListener, ApiResponseInterface {

    private val TAG = Act_Register::class.java.name
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_register)

        initComponent()
        initToolbar()
        initListeners()
        initData()

    }

    override fun initComponent() {
    }

    override fun initToolbar() {
    }

    override fun initData() {
        device_token = when {
            sessionManager[FCM_TOKEN, ""] != "" -> sessionManager[FCM_TOKEN, ""]
            else -> generateFCMToken()
        }
    }

    override fun initListeners() {

        btnSignUpActivityCreateAccount.setOnClickListener(this)
        btnSignUpActivityLogin.setOnClickListener(this)
        image_password.setOnClickListener(this)

        makeTextViewClickeble(this@Act_Register,
                tvSignUpActivityTermsAndCondition, "https://www.google.com/", 38, 58)

    }

    override fun onClick(view: View?) {
        when (view) {
            btnSignUpActivityCreateAccount -> {
                if (isNetWork(this)) {
                    if (isValidInput()) {
                        ApiRequest(this@Act_Register,
                                ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).signUp(etSignUpActivityName.text.toString(),
                                        etSignUpActivityEmail.text.toString(),
                                        etSignUpActivityPassword.text.toString(),
                                        device_token), SIGN_UP, true, this)
                    }
                } else {
                    showSnackBar(resources.getString(R.string.internet_not_available))
                }

            }
            btnSignUpActivityLogin -> {
                startActivity(Intent(this@Act_Register, Act_Login::class.java))
                finish()
            }

            image_password -> {
                if (isPasswordVisible) {
                    isPasswordVisible = false
                    image_password.setImageResource(R.drawable.ic_eye_hide)
                    etSignUpActivityPassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
                    etSignUpActivityPassword.setSelection(etSignUpActivityPassword.length())
                } else {
                    image_password.setImageResource(R.drawable.ic_eye_show)
                    etSignUpActivityPassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    etSignUpActivityPassword.setSelection(etSignUpActivityPassword.length())
                    isPasswordVisible = true
                }
            }
        }
    }

    private fun generateFCMToken(): String {
        var token: String = ""
        if (isNetWork(this)) {
            FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult> {
                        override fun onComplete(task: Task<InstanceIdResult>) {
                            if (!task.isSuccessful) {
                                Log.e(TAG, "Fcm Not Successful ----->")
                                return
                            }
                            token = task.result?.token.toString()
                            Log.e(TAG, "Fcm Token = $token")
                        }
                    })
            return token
        } else {
            return token
        }
    }

    private fun isValidInput(): Boolean {
        if (TextUtils.isEmpty(etSignUpActivityName!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etSignUpActivityName, getString(R.string.toast_enter_name))
            return false
        } else if (TextUtils.isEmpty(etSignUpActivityEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etSignUpActivityEmail, getString(R.string.toast_email))
            return false
        } else if (!Validator.validateEmail(etSignUpActivityEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etSignUpActivityEmail, getString(R.string.error_valid_email))
            return false
        } else if (TextUtils.isEmpty(etSignUpActivityConfirmEmail!!.text.toString())) {
            showSnackBar(etSignUpActivityConfirmEmail, getString(R.string.toast_enter_confirm))
            return false
        } else if (!Validator.validateEmail(etSignUpActivityConfirmEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etSignUpActivityConfirmEmail, getString(R.string.error_valid_con_email))
            return false
        } else if (TextUtils.isEmpty(etSignUpActivityPassword!!.text.toString())) {
            showSnackBar(etSignUpActivityPassword, getString(R.string.toast_password))
            return false
        } else if (!Validator.isBothPasswordEqual(this@Act_Register, etSignUpActivityEmail, etSignUpActivityConfirmEmail)) {
            showSnackBar(etSignUpActivityPassword, getString(R.string.err_email_add_dosnt_match))
            return false
        }
        return true
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {

        when (apiResponseManager.type) {
            SIGN_UP -> {
                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                Log.e(TAG, "Sign Up Response:-$responseValue")
                val response = JSONObject(responseValue)
                val statusCode = response.optInt("status_code")
                val message = response.optString("message")

                if (statusCode == STATUS_CODE) {
                    Toast(message, true, this@Act_Register)
                    startActivity(Intent(this@Act_Register, Act_Login::class.java))
                }

            }
        }

    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(layout_main, error_message)
        }
    }

}
