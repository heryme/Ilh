package com.activityhub.fragment.auth


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import com.activityhub.R
import com.activityhub.activity.auth.Act_Login
import com.activityhub.activity.home.Act_Home
import com.activityhub.utils.extension.Toast
import com.activityhub.utils.extension.isNetWork
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.other.Common.AUTH_TOKEN
import com.activityhub.utils.other.STATUS_CODE
import com.activityhub.utils.other.UPDATE_PASSWORD
import com.activityhub.utils.other.message
import com.activityhub.utils.other.statusCode
import okhttp3.ResponseBody
import org.json.JSONObject


class Frag_Update_Password : Frag_Base(), View.OnClickListener, ApiResponseInterface {

    private val TAG = Frag_Update_Password::class.java.name

    private var rootView: View? = null

    private lateinit var etUpdatePasswordFragmentOldPassword:EditText

    private lateinit var etUpdatePasswordFragmentNewPassword:EditText

    private lateinit var btnUpdatePasswordFragmentSaveChanges:Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_update_password, container, false)
        }

        initToolbar()
        initIDs(rootView!!)
        initListeners()

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
        inflater?.inflate(R.menu.profile, menu);
    }


    override fun initComponent() {

    }

    override fun initToolbar() {
        setHasOptionsMenu(true)
        clickToolBarBackButton()
    }

    override fun initListeners() {
        btnUpdatePasswordFragmentSaveChanges.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun initIDs(rootView: View) {
        etUpdatePasswordFragmentOldPassword = rootView.findViewById(R.id.etUpdatePasswordFragmentOldPassword)
        etUpdatePasswordFragmentNewPassword = rootView.findViewById(R.id.etUpdatePasswordFragmentNewPassword)
        btnUpdatePasswordFragmentSaveChanges = rootView.findViewById(R.id.btnUpdatePasswordFragmentSaveChanges)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.profile_item -> {
                /*(activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
                        false,
                        "",
                        false, false)*/

                (activity as Act_Home).changeFragment(Frag_Account(),
                        false,
                        "",
                        false,
                        false,
                        false)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun clickToolBarBackButton() {
        (activity as Act_Home).toolbar?.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                (activity as Act_Home).onBackFragmentHandling()
            }
        })
    }

    override fun onClick(view: View?) {
        when (view) {
            btnUpdatePasswordFragmentSaveChanges -> {
                updatePasswordAPI()
            }
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            UPDATE_PASSWORD -> {
                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                Log.e(TAG, "Update Password Response:-$responseValue")
                val response = JSONObject(responseValue)
                val statusCode = response.optInt(statusCode)
                val message = response.optString(message)
                if (statusCode == STATUS_CODE) {
                    Toast(message, true, activity!!)
                    startActivity(Intent(activity, Act_Login::class.java))
                } else if (statusCode == 201) {
                    showSnackBar(message)
                }


            }
        }
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(error_message)
        }
    }



    private fun updatePasswordAPI() {
        Log.e(TAG, "Token--->" + sessionManager[AUTH_TOKEN, ""])

        if (isNetWork(activity!!)) {
            if (isValidInput())
                ApiRequest(activity!!,
                        ApiInitialize.initialize(ApiInitialize.MAIN_URL_API)
                                .updatePassword(sessionManager[AUTH_TOKEN, ""],
                                        etUpdatePasswordFragmentOldPassword.text.toString(),
                                        etUpdatePasswordFragmentNewPassword.text.toString()), UPDATE_PASSWORD, true, this)
        } else {
            showSnackBar(activity!!.resources.getString(R.string.internet_not_available))
        }

    }

    private fun isValidInput(): Boolean {
        if (TextUtils.isEmpty(etUpdatePasswordFragmentOldPassword!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(getString(R.string.pls_enter_old_pwd))
            return false
        } else if (TextUtils.isEmpty(etUpdatePasswordFragmentNewPassword!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(getString(R.string.pls_enter_new_pwd))
            return false
        }
        return true
    }

}

