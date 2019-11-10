package com.activityhub.fragment.other

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.adapter.ManageDeviceAdapter
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base
import kotlinx.android.synthetic.main.frag_manage_devices.*

class Frag_Manage_Devices : Frag_Base() {

    private var manegeDeviceAdapter: ManageDeviceAdapter? = null
    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_manage_devices, container, false)
        }

        initToolbar()
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
        val iconList: ArrayList<Int> = ArrayList()
        val deviceList: ArrayList<String> = ArrayList()

        iconList.add(R.drawable.ic_fitbit)
        iconList.add(R.drawable.ic_heart)
        iconList.add(R.drawable.ic_under_armour)

        deviceList.add("Fitbit")
        deviceList.add("Health")
        deviceList.add("Under Armour")

        manegeDeviceAdapter = ManageDeviceAdapter(activity!!, iconList, deviceList)
        rvManageDevicesFragmentList.adapter = manegeDeviceAdapter
        rvManageDevicesFragmentList.layoutManager = LinearLayoutManager(activity)

//        rvManageDevicesFragmentList.addOnItemTouchListener(RecyclerTouchListener(activity,
//                rvManageDevicesFragmentList,
//                object : RecyclerTouchListener.ClickListener {
//                    override fun onClick(view: View?, position: Int) {
//                        (activity as Act_Home).loadFragment(Frag_Connect_Device(),
//                                false, deviceList[position], true, false)
//
//                    }
//
//                    override fun onLongClick(view: View?, position: Int) {
//
//                    }
//                }))
    }

    override fun initIDs(rootView: View) {
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_item -> {
//                (activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
//                        false, "", false, false)
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
        if (activity != null) {
            (activity as Act_Home).toolbar?.setNavigationOnClickListener { (activity as Act_Home).onBackFragmentHandling() }
        }
    }

}



