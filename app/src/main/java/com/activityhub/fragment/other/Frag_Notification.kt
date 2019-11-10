package com.activityhub.fragment.other

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.utils.extension.Toast


class Frag_Notification : Frag_Base(), View.OnClickListener {

    var rootView: View? = null

    private lateinit var layout_repeat: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_notification, container, false)
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
        layout_repeat.setOnClickListener(this)

    }

    override fun initData() {
    }

    override fun initIDs(rootView: View) {
        layout_repeat = rootView.findViewById(R.id.layout_repeat)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
        inflater?.inflate(R.menu.profile, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_item -> {
                /*(activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
                        false, "", false, false)*/
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

    override fun onClick(view: View?) {
        when (view) {
            layout_repeat -> {
                /* (activity as Act_Home).loadFragment(Frag_Notification_Details(),
                         false, getResources().getString(R.string.repeat), true, false)*/

                (activity as Act_Home).changeFragment(Frag_Notification_Details(),
                        false,
                        getResources().getString(R.string.repeat),
                        true,
                        false,
                        false)
            }
        }
    }

    private fun clickToolBarBackButton() {
        if (activity != null) {
            (activity as Act_Home).toolbar?.setNavigationOnClickListener { (activity as Act_Home).onBackFragmentHandling() }
        }
    }


}

