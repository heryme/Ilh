package com.activityhub.fragment.home


import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.fragment.results.Frag_Assessment


class Frag_Result : Frag_Base(), View.OnClickListener {

    private lateinit var llMyAssessmentPlan: LinearLayout
    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_result, container, false)
        }
        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()
        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onClick(view: View?) {
        when (view) {
            llMyAssessmentPlan -> {
                /* (activity as Act_Home).loadFragmentwithAnim(Frag_Assessment(),
                         false,
                         getString(R.string.result),
                         false, true)*/

                (activity as Act_Home).changeFragment(Frag_Assessment(),
                        false,
                        getString(R.string.result),
                        false,
                        true,
                        true)
            }
        }
    }

    override fun initComponent() {

    }

    override fun initToolbar() {
    }

    override fun initListeners() {
        llMyAssessmentPlan.setOnClickListener(this)
    }

    override fun initData() {
    }

    override fun initIDs(rootView: View) {
        llMyAssessmentPlan = rootView.findViewById(R.id.llMyAssessmentPlan)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_item -> {
                /*(activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
                        false,
                        "",
                        false,
                        false)*/
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


}

