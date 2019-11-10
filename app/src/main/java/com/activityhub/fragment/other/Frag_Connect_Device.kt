package com.activityhub.fragment.other

import android.os.Bundle
import android.util.Log
import android.view.*
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base

class Frag_Connect_Device : Frag_Base() {


    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_connect_device, container, false)
        }

        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()
        return rootView
    }

    override fun initComponent() {
    }

    override fun initToolbar() {
        setHasOptionsMenu(true)
        clickToolBarBackButton()
    }

    override fun initListeners() {

    }

    override fun initData() {
    }

    override fun initIDs(rootView: View) {
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_item -> {
                /* (activity as Act_Home).loadFragment(Frag_Account(),
                         false, "",
                         false,false)*/
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


}



