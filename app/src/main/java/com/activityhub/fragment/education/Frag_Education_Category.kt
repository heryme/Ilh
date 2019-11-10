package com.activityhub.fragment.education


import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.adapter.Adp_Education_Category
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.fragment.home.Frag_Event_Directory
import com.activityhub.model.Education_Category
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.extension.Toast
import com.activityhub.utils.extension.isNetWork
import com.activityhub.utils.other.EDUCATION_CATEGORY


class Frag_Education_Category : Frag_Base(), ApiResponseInterface {

    private val TAG = Frag_Education_Category::class.java.name

    private lateinit var recycler_education_category: RecyclerView
    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_education_category, container, false)
        }

        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()
        callEducationCategory()

        return rootView
    }




    private fun callEducationCategory() {

        if (isNetWork(activity!!)) {
            ApiRequest(activity!!,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).getEducationCategory(
                            auth_token),
                    EDUCATION_CATEGORY, true, this)
        } else {
            showSnackBar(activity!!.resources.getString(R.string.internet_not_available))
        }

    }

    override fun initComponent() {
    }

    override fun initToolbar() {
        (activity as Act_Home).setToolbarTitle(getString(R.string.education), false, true)
        if (activity != null) {
            (activity as Act_Home).toolbar?.setNavigationOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    (activity as Act_Home).onBackFragmentHandling()
                }
            })
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.profile_item -> {
              /*  (activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
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



    override fun initListeners() {
        recycler_education_category.layoutManager = GridLayoutManager(activity, 2) as RecyclerView.LayoutManager?
    }

    override fun initData() {
    }

    override fun initIDs(rootView: View) {
        recycler_education_category = rootView.findViewById(R.id.recycler_education_category)
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            EDUCATION_CATEGORY -> {
                val commonResponse = apiResponseManager.response as Education_Category
                if (commonResponse.statusCode == 200) {
                    recycler_education_category.adapter = Adp_Education_Category(commonResponse.data, activity!!, this)
                }
            }
        }
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(error_message)
        }
    }

}

