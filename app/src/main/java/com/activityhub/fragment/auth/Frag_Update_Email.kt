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
import com.activityhub.utils.other.*
import com.activityhub.utils.other.Common.AUTH_TOKEN
import okhttp3.ResponseBody
import org.json.JSONObject


class Frag_Update_Email : Frag_Base(), View.OnClickListener, ApiResponseInterface {

    private val TAG = Frag_Update_Email::class.java.name
    private var rootView: View? = null

    private lateinit var btnUpdateEmailFragmentSaveChanges: Button
    private lateinit var etUpdateEmailFragmentOldEmail: EditText
    private lateinit var etUpdateEmailFragmentNewEmail: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_update_email, container, false)
        }

        initToolbar()
        initIDs(rootView!!)
        initComponent()
        initListeners()
        initData()
        return rootView
    }

    private fun clickToolBarBackButton() {
        if (activity != null) {
            (activity as Act_Home).toolbar?.setNavigationOnClickListener { (activity as Act_Home).onBackFragmentHandling() }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            btnUpdateEmailFragmentSaveChanges -> {
                updateEmailAPI()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun initComponent() {

    }

    override fun initToolbar() {
        setHasOptionsMenu(true)
        clickToolBarBackButton()

    }

    override fun initListeners() {
        btnUpdateEmailFragmentSaveChanges.setOnClickListener(this)
    }

    override fun initData() {
            Log.e(TAG,"backStackEntryCount --->" + activity!!.supportFragmentManager.backStackEntryCount)
    }

    override fun initIDs(rootView: View) {
        btnUpdateEmailFragmentSaveChanges = rootView.findViewById(R.id.btnUpdateEmailFragmentSaveChanges)
        etUpdateEmailFragmentNewEmail = rootView.findViewById(R.id.etUpdateEmailFragmentNewEmail)
        etUpdateEmailFragmentOldEmail = rootView.findViewById(R.id.etUpdateEmailFragmentOldEmail)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_item -> {
                /*  (activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
                          false,
                          "",
                          false, false)*/

                //(activity as Act_Home).onBackFragmentHandling()

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

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            UPDATE_EMAIL -> {
                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                Log.e(TAG, "Update Email Response:-$responseValue")
                val response = JSONObject(responseValue)
                val statusCode = response.optInt(statusCode)
                val message = response.optString(message)
                if (statusCode == STATUS_CODE) {
                    Toast(message, true, activity!!)
                    startActivity(Intent(activity, Act_Login::class.java))
                } else if (statusCode == 201) {
                    Toast(message, true, activity!!)
                }


            }
        }
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(error_message)
        }
    }

    private fun updateEmailAPI() {
        if (isNetWork(activity!!)) {
            if (isValidInput()) {
                ApiRequest(activity!!,
                        ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).updateEmail(sessionManager[AUTH_TOKEN, ""],
                                etUpdateEmailFragmentOldEmail.text.toString(),
                                etUpdateEmailFragmentNewEmail.text.toString()),
                        UPDATE_EMAIL,
                        true,
                        this)
            }
        } else {
            showSnackBar(activity!!.resources.getString(R.string.internet_not_available))
        }

    }

    private fun isValidInput(): Boolean {
        if (TextUtils.isEmpty(etUpdateEmailFragmentOldEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(getString(R.string.pls_enter_old_email))
            return false
        } else if (!Validator.validateEmail(etUpdateEmailFragmentOldEmail!!.text.toString().trim { it <= ' ' })) {
            Toast(getString(R.string.error_valid_email), true, activity!!)
            showSnackBar(getString(R.string.error_valid_email))
            return false
        } else if (TextUtils.isEmpty(etUpdateEmailFragmentNewEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(getString(R.string.pls_enter_new_email))
            return false
        } else if (!Validator.validateEmail(etUpdateEmailFragmentNewEmail!!.text.toString().trim { it <= ' ' })) {
            showSnackBar(getString(R.string.error_valid_email))
            return false
        }
        return true
    }


}

