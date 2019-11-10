package com.activityhub.fragment.other


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.adapter.NotificationDetailsAdapter
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base

class Frag_Notification_Details : Frag_Base() {

    private lateinit var notificationDetailsAdapter: NotificationDetailsAdapter
    var rootView: View? = null
    private lateinit var recycler_notification: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_notification_details, container, false)
        }

        initToolbar()
        initComponent()
        initIDs(rootView!!)
        initListeners()
        initData()
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.profile, menu)
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
        val detailsList: ArrayList<String> = ArrayList()
        detailsList.add("Never")
        detailsList.add("Every Day")
        detailsList.add("Every Week")
        detailsList.add("Every 2 Weeks")
        detailsList.add("Every Month")
        detailsList.add("Every Year")

        notificationDetailsAdapter = NotificationDetailsAdapter(activity!!, detailsList)
        recycler_notification.adapter = notificationDetailsAdapter
        recycler_notification.layoutManager = LinearLayoutManager(activity)
    }

    override fun initIDs(rootView: View) {
        recycler_notification = rootView.findViewById(R.id.recycler_notification)
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

