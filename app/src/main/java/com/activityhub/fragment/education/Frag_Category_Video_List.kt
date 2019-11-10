package com.activityhub.fragment.education


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.adapter.Adp_Category_Video_List
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.model.Category_Video_List
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.extension.isNetWork
import com.activityhub.utils.other.CATEGORY_VIDEO_LIST
import com.activityhub.utils.other.Common
import com.activityhub.utils.other.Common.ID
import com.activityhub.utils.other.Common.SUB_ID
import java.util.*


class Frag_Category_Video_List : Frag_Base(), ApiResponseInterface {

    private lateinit var recycler_category_video: RecyclerView
    var map = HashMap<String, String>()
    var category_id = ""
    var sub_category_id = ""
    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_category_video_list, container, false)
        }
        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()
        callCategoryVideo()

        return rootView
    }

    private fun callCategoryVideo() {

        if (isNetWork(activity!!)) {
            ApiRequest(activity!!,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).getCategoryVideoList(
                            auth_token,
                            category_id,
                            sub_category_id), CATEGORY_VIDEO_LIST, true, this)
        } else {
            showSnackBar(activity!!.resources.getString(R.string.internet_not_available))
        }

    }

    override fun initComponent() {
    }

    override fun initToolbar() {
        setHasOptionsMenu(true)
        //(activity as Act_Home).setToolbarTitle(getString(R.string.education), true, true)
    }

    override fun initListeners() {
        recycler_category_video.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    override fun initData() {
        val bundle = arguments
        map = bundle?.getSerializable(Common.DATA) as HashMap<String, String>
        category_id = map[ID]!!
        sub_category_id = map[SUB_ID]!!

    }

    override fun initIDs(rootView: View) {
        recycler_category_video = rootView.findViewById(R.id.recycler_category_video)
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            CATEGORY_VIDEO_LIST -> {
                val commonResponse = apiResponseManager.response as Category_Video_List

                if (commonResponse.statusCode == 200) {
                    recycler_category_video.adapter =
                            Adp_Category_Video_List(commonResponse.data.videoArray, activity!!, activity!!)
                }
            }
        }
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(error_message)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
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

