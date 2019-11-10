package com.activityhub.fragment.home


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.adapter.EventListAdapter
import com.activityhub.app.AppController
import com.activityhub.utils.extension.isNetWork
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.fragment.event.Frag_Event_Details
import com.activityhub.fragment.event.Frag_Event_Filter
import com.activityhub.model.Event
import com.activityhub.model.Selected_Filter
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.other.*
import com.activityhub.utils.other.Common.AUTH_TOKEN
import com.activityhub.utils.other.Common.GO_BACK_EVENT_DETAILS
import com.activityhub.utils.other.Common.GO_FROM_USER_EVENT_LIST
import com.activityhub.utils.other.Common.IS_FILTER
import kotlinx.android.synthetic.main.toolbar.*


class Frag_Event_Directory : Frag_Base(), View.OnClickListener, ApiResponseInterface, Frag_Event_Filter.SelectedFilterDataListener {


    private val TAG = "Frag_Event_Directory"

    private var eventList = ArrayList<Event.Data>()
    lateinit var model: Event
    private var selectedFilter: Selected_Filter? = null

    private lateinit var eventListAdapter: EventListAdapter

    private var DISEASE_GROUP = "disease group"
    private var EVENT_CATEGORY = "event category"
    private var DATE = "date"
    private var INTENSITY = "intensity"
    private var FREE = "free"
    private var MAXVALUE = "maxvalue"
    private var MINVALUE = "minvalue"

    var diseaseGroupId = ""
    var categoryId = ""
    var date = ""
    var intensity = ""
    var maxPrice = ""
    var minPrice = ""
    var rootView: View? = null
    private lateinit var button_go: MyCustomButton
    private lateinit var button_filter: MyCustomButton
    private lateinit var recycler_events: RecyclerView
    private lateinit var text_result_title: MyCustomTextView
    private lateinit var text_data_not_found: MyCustomTextView
    private lateinit var edit_location: MyCustomEdittext
    private lateinit var edit_search: MyCustomEdittext

    companion object Companion {
        var eventData: String = "eventData"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_event_directory, container, false)
        }

        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()
        callEventListAPI()
        return rootView
    }

    override fun initComponent() {
    }

    override fun initToolbar() {
        (activity as Act_Home).setToolbarTitle(getString(R.string.event_directory), false, true)
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.text = getString(R.string.clear_all)
        setHasOptionsMenu(true)
    }

    override fun initListeners() {
        button_go.setOnClickListener(this)
        button_filter.setOnClickListener(this)
    }

    override fun initData() {
        eventListAdapter = EventListAdapter(activity!!, this, eventList)
        recycler_events.adapter = eventListAdapter
        recycler_events.layoutManager = LinearLayoutManager(activity)
    }

    @SuppressLint("SetTextI18n")
    private fun initFilterData() {

        if (sessionManager[IS_FILTER, 0] == 1) {

            Log.e("Frag_Event_Directory", " 1 ")
            button_filter.text = getString(R.string.filter_result)
            text_result_title.text = "Matched results"

            selectedFilter = Selected_Filter(sessionManager[DISEASE_GROUP, 0],
                    sessionManager[EVENT_CATEGORY, 0],
                    sessionManager[DATE, "Date"],
                    "",
                    sessionManager[FREE, ""],
                    sessionManager[MINVALUE, ""],
                    sessionManager[MAXVALUE, ""],
                    false)

            sessionManager.put(GO_BACK_EVENT_DETAILS, false)

            if (selectedFilter != null) {

                if (selectedFilter?.diseaseGroupId != 0) {
                    diseaseGroupId = selectedFilter?.diseaseGroupId.toString()
                }

                if (selectedFilter?.eventCategoryId != 0) {
                    categoryId = selectedFilter?.eventCategoryId.toString()
                }

                if (selectedFilter?.date != "Date") {
                    date = selectedFilter?.date!!
                }


                maxPrice = when {
                    selectedFilter?.priceRangeFree.equals("Free") -> 0.0.toString()
                    selectedFilter?.isClear!! -> ""
                    else -> selectedFilter?.priceMax.toString()
                }

                minPrice = when {
                    selectedFilter?.priceRangeFree.equals("Free") -> 0.0.toString()
                    selectedFilter?.isClear!! -> ""
                    else -> selectedFilter?.priceMin.toString()
                }
//                intensity = selectedFilter?.intensity!!
            }

        } else {
            diseaseGroupId = ""
            categoryId = ""
            date = ""
            intensity = ""
            maxPrice = ""
            minPrice = ""
            button_filter.text = getString(R.string.filter)
            text_result_title.text = activity!!.resources.getString(R.string.viewing_all_events)
        }

    }

    override fun initIDs(rootView: View) {
        button_go = rootView.findViewById(R.id.button_go)
        button_filter = rootView.findViewById(R.id.button_filter)
        recycler_events = rootView.findViewById(R.id.recycler_events)
        text_result_title = rootView.findViewById(R.id.text_result_title)
        text_data_not_found = rootView.findViewById(R.id.text_data_not_found)
        edit_location = rootView.findViewById(R.id.edit_location)
        edit_search = rootView.findViewById(R.id.edit_location)
    }

    override fun onClick(view: View?) {
        when (view) {

            button_go -> {
                if (edit_location.text!!.trim().toString() != "" ||
                        edit_search.text!!.trim().toString() != "") {
                    callEventListAPI()
                } else {
                    showSnackBar(resources.getString(R.string.search_directory_message))
                }
            }

            button_filter -> {
                /*  (activity as Act_Home).loadFragmentwithAnim(Frag_Event_Filter(),
                          false, getString(R.string.event_directory), true, true)*/

                (activity as Act_Home).changeFragment(Frag_Event_Filter(),
                        false,
                        getString(R.string.event_directory),
                        true,
                        true,
                        true)
            }
        }
    }

    override fun getFilterData(selectedFilter: Selected_Filter) {
        Log.e(TAG, "Event Data--->" + selectedFilter.date)
        this.selectedFilter = selectedFilter
        callEventListAPI()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Frag_Event_Filter.setSelectedFilterDataListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_item -> {
                /*(activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
                        false, "", false, true)*/
                (activity as Act_Home).changeFragment(Frag_Account(),
                        false,
                        "",
                        false,
                        false,
                        true)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            GET_EVENT_LIST -> {
                model = apiResponseManager.response as Event
                Log.e(TAG, "Event List Response:- ${model.data}")
                if (model.statusCode == STATUS_CODE) {
                    edit_location.setText("")
                    edit_search.setText("")
                    eventList.clear()
                    eventList.addAll(model.data)
                    eventListAdapter.notifyDataSetChanged()
                    if (eventList.size == 0) {
                        text_data_not_found.visibility = View.VISIBLE
                        recycler_events.visibility = View.GONE
                    } else {
                        recycler_events.visibility = View.VISIBLE
                        text_data_not_found.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(error_message)
        }
    }

    fun openEventDetail(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable(eventData, model.data[position])
        val eventFrg = Frag_Event_Details()
        eventFrg.arguments = bundle
        sessionManager.put(GO_FROM_USER_EVENT_LIST, true)
        /*(activity as Act_Home).loadFragmentwithAnim(eventFrg, false,
                resources.getString(R.string.event_directory), true, true)*/

        (activity as Act_Home).changeFragment(eventFrg,
                false,
                resources.getString(R.string.event_directory),
                true,
                true,
                true)

    }

    @SuppressLint("SetTextI18n")
    private fun callEventListAPI() {

        initFilterData()

        Log.e(TAG, "Search data diseaseGroupId = $diseaseGroupId categoryId = $categoryId date = $date minPrice = " +
                "$minPrice maxPrice = $maxPrice")

        if (isNetWork(activity!!)) {

            ApiRequest(activity!!, ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).getEventList(
                    sessionManager[AUTH_TOKEN, ""],
                    diseaseGroupId,
                    categoryId,
                    date,
                    intensity,
                    minPrice,
                    maxPrice,
                    edit_search.text.toString(),
                    edit_location.text.toString()),
                    GET_EVENT_LIST, true, this)

        } else {
            showSnackBar(activity!!.resources.getString(R.string.internet_not_available))
        }

    }


}

