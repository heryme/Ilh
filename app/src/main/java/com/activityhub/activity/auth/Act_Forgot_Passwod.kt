package com.activityhub.activity.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.activityhub.utils.other.FORGOT_PASSWORD
import com.activityhub.utils.other.STATUS_CODE
import com.activityhub.utils.other.Validator
import kotlinx.android.synthetic.main.act_forgot_passwod.*
import okhttp3.ResponseBody
import org.json.JSONObject

class Act_Forgot_Passwod : BaseActivity(), View.OnClickListener, ApiResponseInterface {

    private val TAG = Act_Forgot_Passwod::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_forgot_passwod)

        initComponent()
        initToolbar()
        initListeners()
        initData()
    }

    override fun initComponent() {
    }

    override fun initToolbar() {
    }

    override fun initListeners() {
        btnForgotPasswordActivitySendEmail.setOnClickListener(this)

        makeTextViewClickeble(this@Act_Forgot_Passwod,
                tvForgotPasswordActivityTermsAndCondition,
                "https://www.google.com/",38,58)
    }

    override fun initData() {
    }

    override fun onClick(view: View?) {
        when (view) {
            btnForgotPasswordActivitySendEmail -> {
                if (isNetWork(this)) {
                    if(isValidInput()) {
                        ApiRequest(this@Act_Forgot_Passwod,
                            ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).forgotPassword(etForgotPasswordActivityEmailAddress.text.toString()),
                                FORGOT_PASSWORD,
                            true,
                            this)
                     }
                }else {
                    showSnackBar(resources.getString(R.string.internet_not_available))
                }
            }
        }

    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            FORGOT_PASSWORD -> {
                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                val response = JSONObject(responseValue)
                val statusCode = response.optInt("status_code")
                val message = response.optString("message")
                if (statusCode == STATUS_CODE) {
                    Toast(message, true, this@Act_Forgot_Passwod)
                    startActivity(Intent(this@Act_Forgot_Passwod, Act_Login::class.java))
                }
            }
        }
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
    }

    private fun isValidInput(): Boolean {
        if (TextUtils.isEmpty(etForgotPasswordActivityEmailAddress!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etForgotPasswordActivityEmailAddress, getString(R.string.toast_email))
            return false
        } else if (!Validator.validateEmail(etForgotPasswordActivityEmailAddress!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(etForgotPasswordActivityEmailAddress, getString(R.string.error_valid_email))
            return false
        }
        return true
    }

}
