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


abstract class Frag_Base : Fragment() {

    protected lateinit var sessionManager: SessionManager
    protected lateinit var appcontroller: AppController
    lateinit var snackbar: Snackbar
    protected lateinit var auth_token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(activity!!)
        appcontroller = activity!!.application as AppController
        auth_token = sessionManager[Common.AUTH_TOKEN, ""]
    }


    // where we initialize componants of fragment
    abstract fun initComponent()

    // where we initialize toolbar of fragment
    abstract fun initToolbar()

    // where we initialize listeners of fragment
    abstract fun initListeners()

    // where we set data of fragment
    abstract fun initData()

    // where we initialize id of fragment
    abstract fun initIDs(rootView: View)

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
