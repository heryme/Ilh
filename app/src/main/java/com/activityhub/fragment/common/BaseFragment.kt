package com.activityhub.fragment.common


import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import com.activityhub.R
import com.activityhub.app.AppController
import com.activityhub.utils.other.Common
import com.activityhub.utils.other.SessionManager


abstract class BaseFragment : Fragment() {

    protected lateinit var sessionManager: SessionManager
    protected lateinit var appcontroller: AppController
    lateinit var snackbar: Snackbar
    protected lateinit var auth_token: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sessionManager = SessionManager(activity!!)
        appcontroller = activity!!.application as AppController
        auth_token = sessionManager[Common.AUTH_TOKEN, ""]
    }

//
//    abstract fun initComponent()
//
//    abstract fun initToolbar()
//
//    abstract fun initListeners()
//
//    abstract fun initData()
//


    fun showSnackBar(message: String) {
        val parentLayout = activity!!.findViewById<View>(android.R.id.content)
        snackbar = Snackbar
                .make(parentLayout, message, Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.dismiss)) {
                    snackbar.dismiss()
                }
        snackbar.setActionTextColor(Color.RED)
        snackbar.duration = 4000
        snackbar.show()
    }


}
