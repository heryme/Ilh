package com.activityhub.fragment.event

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.media.Image
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.*
import android.widget.*
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.adapter.EventCategoryAdapter
import com.activityhub.adapter.EventDiesesAdapter
import com.activityhub.utils.extension.isNetWork
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.fragment.home.Frag_Event_Directory
import com.activityhub.model.Event_Filter
import com.activityhub.model.Selected_Filter
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.other.Common
import com.activityhub.utils.other.Common.IS_FILTER
import com.activityhub.utils.other.EVENT_FILTER
import com.activityhub.utils.other.MyCustomButton
import com.activityhub.utils.other.STATUS_CODE
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import kotlinx.android.synthetic.main.frag_event_filter.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Frag_Event_Filter : Frag_Base(), View.OnClickListener, ApiResponseInterface, AdapterView.OnItemSelectedListener {

    private val TAG = Frag_Event_Filter::class.java.name

    private var DISEASE_GROUP = "disease group"
    private var EVENT_CATEGORY = "event category"
    private var DATE = "date"
    private var INTENSITY = "intensity"
    private var FREE = "free"
    private var MAXVALUE = "maxvalue"
    private var MINVALUE = "minvalue"

    private var eventDiesesAdapter: EventDiesesAdapter? = null
    private var eventCategoryAdapter: EventCategoryAdapter? = null

    private var diesesList = ArrayList<Event_Filter.AllDiese>()
    private var categoryList = ArrayList<Event_Filter.AllCategory>()

    private var selectedDiseaseGroupId: Int = 0
    private var selectedEventCategorId: Int = 0
    private var selectedIntensity: String = ""
    private var selectedPriceRangeFree: String = ""

    private var maxValue: String = ""
    private var minValue: String = ""

    private var isClear: Boolean = false
    private var seekBarResetMinValue: Float = 0.0F
    private var seekBarResetMaxValue: Float = 0.0F

    private var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    var simpleDateFormat: SimpleDateFormat? = null

    private lateinit var text_date: TextView
    private lateinit var spinner_diseas_group: Spinner
    private lateinit var spinner_event_category: Spinner
    private lateinit var image_diseas_group: ImageView
    private lateinit var image_event_category: ImageView
    private lateinit var relative_intensity: RadioGroup
    private lateinit var radio_group_free: RadioGroup
    private lateinit var radio_button_light: RadioButton
    private lateinit var radio_button_moderate: RadioButton
    private lateinit var radio_button_high: RadioButton
    private lateinit var radio_button_free: RadioButton
    private lateinit var text_price_range: TextView
    private lateinit var seekbar_price: CrystalRangeSeekbar
    private lateinit var button_apply: MyCustomButton

    companion object {
        var callback: SelectedFilterDataListener? = null
        fun setSelectedFilterDataListener(callback: SelectedFilterDataListener) {
            Companion.callback = callback
        }
    }

    interface SelectedFilterDataListener {
        fun getFilterData(selectedFilter: Selected_Filter)
    }

    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_event_filter, container, false)
        }

        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()
        callFilterAPI()
        return rootView
    }

    override fun onStop() {
        super.onStop()
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.visibility = View.GONE
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.text = ""
    }

    override fun onResume() {
        super.onResume()
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.visibility = View.VISIBLE
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.text = getString(R.string.clear_all)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.visibility = View.GONE
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.text = ""
    }

    override fun onClick(view: View?) {
        when (view) {

            text_date -> {
                setDatePicker()
            }
            button_apply -> {
                Log.e(TAG, "Is Clear--->$isClear")
                sendDataToInterface()
            }

            (activity as Act_Home).tvbarHomeActivityToolbarLocation -> {
                clearAllData()
            }

        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            EVENT_FILTER -> {
                val eventFilterModel = apiResponseManager.response as Event_Filter
                Log.e(TAG, "Event Filter Response:-${eventFilterModel.data}")
                if (eventFilterModel.statusCode == STATUS_CODE) {
                    val ee: Event_Filter.AllDiese?
                    ee = Event_Filter.AllDiese()
                    ee.diesesId = 0
                    ee.dieasesName = "Disease Group"
                    diesesList.clear()
                    diesesList.add(0, ee)
                    diesesList.addAll(eventFilterModel.data.allDieses)
                    eventDiesesAdapter?.notifyDataSetChanged()

                    var eeCat: Event_Filter.AllCategory? = null
                    eeCat = Event_Filter.AllCategory()
                    eeCat.categoryId = 0
                    eeCat.categoryName = "Event Category"
                    categoryList.clear()
                    categoryList.add(0, eeCat)
                    categoryList.addAll(eventFilterModel.data.allCategory)
                    eventCategoryAdapter?.notifyDataSetChanged()

                    seekBarResetMinValue = eventFilterModel.data.minPrice.toFloat()
                    seekBarResetMaxValue = eventFilterModel.data.maxPrice.toFloat()


                    if (sessionManager[IS_FILTER, 0] == 1) {
                        setPreSelectedData(eventFilterModel.data.minPrice, eventFilterModel.data.maxPrice)
                    } else {
                        clearAllData()
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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0) {
            spinner_diseas_group -> {
                val selectedItemText = p0.getItemAtPosition(p2) as Event_Filter.AllDiese
                if (p2 > 0) {
                    Log.e(TAG, "Selected Dieases:${selectedItemText.dieasesName}")
                    Log.e(TAG, "Selected DieasesId:${selectedItemText.diesesId}")
                    selectedDiseaseGroupId = selectedItemText.diesesId
                    image_diseas_group.setImageResource(R.drawable.ic_grey_right_arrow)
                }
            }
            spinner_event_category -> {
                val selectedItemText = p0.getItemAtPosition(p2) as Event_Filter.AllCategory
                if (p2 > 0) {
                    Log.e(TAG, "Selected Category:${selectedItemText.categoryName}")
                    Log.e(TAG, "Selected categoryId:${selectedItemText.categoryId}")
                    selectedEventCategorId = selectedItemText.categoryId
                    image_event_category.setImageResource(R.drawable.ic_grey_right_arrow)
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpSeekBar(minValue: Double, maxValue: Double) {

        if (sessionManager[MINVALUE, ""] != "" && sessionManager[MAXVALUE, ""] != "") {

            text_price_range.text = "£${DecimalFormat("##").format(sessionManager[MINVALUE, ""].toFloat())} - £${DecimalFormat("##").format(sessionManager[MAXVALUE, ""].toFloat())}"

            seekbar_price.setMinStartValue(sessionManager[MINVALUE, ""].toFloat())
            seekbar_price.setMaxStartValue(sessionManager[MAXVALUE, ""].toFloat())

            seekbar_price.setMinValue(minValue.toFloat())
            seekbar_price.setMaxValue(maxValue.toFloat())

            seekbar_price.apply()

        } else {
            text_price_range.text = "£${DecimalFormat("##").format(minValue)} - £${DecimalFormat("##").format(maxValue)}"
            seekbar_price.setMinValue(minValue.toFloat())
            seekbar_price.setMaxValue(maxValue.toFloat())
        }


        seekbar_price.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            /* this.maxValue = ""
             this.minValue = ""*/
            /*if (!sessionManager[MINVALUE, ""].equals("") && !sessionManager[MAXVALUE, ""].equals("")) {
                this.maxValue = sessionManager[MAXVALUE, ""]
                this.minValue = sessionManager[MINVALUE, ""]
            }*/
            isClear = false
        }

        seekbar_price.setOnRangeSeekbarFinalValueListener { minValue, maxValue ->
            Log.d(TAG, "minValue -- >$minValue :maxValue--> $maxValue")
            text_price_range.text = "£${DecimalFormat("##").format(minValue)} - £${DecimalFormat("##").format(maxValue)}"
            this.maxValue = maxValue.toString()
            this.minValue = minValue.toString()
        }

    }

    private fun setDatePicker() {
        simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(activity!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
                    text_date.text = simpleDateFormat!!.format(calendar.time)
                    Log.e("Register", "select  Date==>" + simpleDateFormat!!.format(calendar.time))
                }, mYear, mMonth, mDay)
        datePickerDialog.show()
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    override fun initToolbar() {
        setHasOptionsMenu(true)
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.text = getString(R.string.clear_all)
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.setTextColor(ContextCompat.getColor(activity!!, R.color.black))
        (activity as Act_Home).setToolbarTitle(getString(R.string.filters), true, true)

        if (activity != null) {
            (activity as Act_Home).toolbar?.setNavigationOnClickListener { (activity as Act_Home).onBackFragmentHandling() }
        }
    }

    override fun initComponent() {
    }

    override fun initListeners() {
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.setOnClickListener(this)
        text_date.setOnClickListener(this)
        button_apply.setOnClickListener(this)
        (activity as Act_Home).tvbarHomeActivityToolbarLocation.setOnClickListener(this)

        relative_intensity.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = activity?.findViewById<View>(checkedId) as RadioButton
            selectedIntensity = radioButton.text.toString()
            Log.e(TAG, "selectedIntensity--->" + selectedIntensity)
        }

        radio_group_free.setOnCheckedChangeListener { group, checkedId ->

            val radioButton = activity?.findViewById<View>(checkedId) as RadioButton
            selectedPriceRangeFree = radioButton.text.toString()
            Log.e(TAG, "selectedPriceRangeFree--->" + selectedPriceRangeFree)

        }
    }

    override fun initData() {
        spinner_diseas_group.onItemSelectedListener = this
        eventDiesesAdapter = EventDiesesAdapter(activity!!, R.layout.row_item_dieses, diesesList)
        spinner_diseas_group.adapter = eventDiesesAdapter

        val spinnerDiseaseTouch = View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                image_diseas_group.setImageResource(R.drawable.ic_grey_down_arrow)
            }
            false
        }
        spinner_diseas_group.setOnTouchListener(spinnerDiseaseTouch)

        spinner_event_category.onItemSelectedListener = this
        eventCategoryAdapter = EventCategoryAdapter(activity!!, R.layout.row_item_dieses, categoryList)
        spinner_event_category.adapter = eventCategoryAdapter


        val spinnerCategoryTouch = View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                image_event_category.setImageResource(R.drawable.ic_grey_down_arrow)
            }
            false
        }

        spinner_event_category.setOnTouchListener(spinnerCategoryTouch)
    }

    override fun initIDs(rootView: View) {

        text_date = rootView.findViewById(R.id.text_date)
        spinner_diseas_group = rootView.findViewById(R.id.spinner_diseas_group)
        image_diseas_group = rootView.findViewById(R.id.image_diseas_group)
        image_event_category = rootView.findViewById(R.id.image_event_category)
        spinner_event_category = rootView.findViewById(R.id.spinner_event_category)
        relative_intensity = rootView.findViewById(R.id.relative_intensity)
        radio_button_light = rootView.findViewById(R.id.radio_button_light)
        radio_button_moderate = rootView.findViewById(R.id.radio_button_moderate)
        radio_button_high = rootView.findViewById(R.id.radio_button_high)
        radio_group_free = rootView.findViewById(R.id.radio_group_free)
        radio_button_free = rootView.findViewById(R.id.radio_button_free)
        text_price_range = rootView.findViewById(R.id.text_price_range)
        seekbar_price = rootView.findViewById(R.id.seekbar_price)
        button_apply = rootView.findViewById(R.id.button_apply)
    }

    private fun sendDataToInterface() {

        sessionManager.put(DISEASE_GROUP, selectedDiseaseGroupId)
        sessionManager.put(EVENT_CATEGORY, selectedEventCategorId)
        sessionManager.put(DATE, text_date.text.toString())
        sessionManager.put(INTENSITY, selectedIntensity)
        sessionManager.put(FREE, selectedPriceRangeFree)
        sessionManager.put(MINVALUE, minValue)
        sessionManager.put(MAXVALUE, maxValue)

        val selectedFilterModel = Selected_Filter(selectedDiseaseGroupId,
                selectedEventCategorId,
                text_date.text.toString(),
                selectedIntensity,
                selectedPriceRangeFree,
                minValue,
                maxValue,
                isClear)

        sessionManager.put(IS_FILTER, 1)

        callback?.getFilterData(selectedFilterModel)

        /*(activity as Act_Home).loadFragment(Frag_Event_Directory(),
                false, resources.getString(R.string.event_directory), false, true)*/

        (activity as Act_Home).onBackFragmentHandling()

        /*   (activity as Act_Home).changeFragment(Frag_Event_Directory(),
                   false,
                   resources.getString(R.string.event_directory),
                   false,
                   true,
                   false)*/
    }

    private fun callFilterAPI() {

        if (isNetWork(activity!!)) {
            ApiRequest(activity!!,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).eventFilter(sessionManager[Common.AUTH_TOKEN, ""],
                            ""),
                    EVENT_FILTER,
                    true,
                    this)
        } else {
            showSnackBar(activity!!.resources.getString(R.string.internet_not_available))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun clearAllData() {

        sessionManager.put(IS_FILTER, 0)

        val diesesResetData = eventDiesesAdapter?.getPosition(diesesList.get(0))
        spinner_diseas_group.setSelection(diesesResetData!!)

        val categoryResetData = eventCategoryAdapter?.getPosition(categoryList.get(0))
        spinner_event_category.setSelection(categoryResetData!!)

        text_date.text = "Date"

        radio_button_free.isChecked = false

        selectedDiseaseGroupId = 0
        selectedEventCategorId = 0
        text_date.text = "Date"
        selectedIntensity = ""
        selectedPriceRangeFree = ""
        isClear = true

        seekbar_price.setMinValue(seekBarResetMinValue)
        seekbar_price.setMaxValue(seekBarResetMaxValue)
        seekbar_price.setMinStartValue(seekBarResetMinValue)
        seekbar_price.setMaxStartValue(seekBarResetMaxValue)
        seekbar_price.apply()
        text_price_range.text = "£${DecimalFormat("##.##").format(seekBarResetMinValue)} - £${DecimalFormat("##.##").format(seekBarResetMaxValue)}"

    }

    private fun setPreSelectedData(minValue: Double, maxValue: Double) {

        for (i in diesesList.indices) {
            if (diesesList[i].diesesId == sessionManager[DISEASE_GROUP, 0]) {
                val diesesData = eventDiesesAdapter?.getPosition(diesesList.get(i))
                spinner_diseas_group.setSelection(diesesData!!)
                break
            }
        }

        for (i in diesesList.indices) {
            if (categoryList[i].categoryId == sessionManager[EVENT_CATEGORY, 0]) {
                val categoryData = eventCategoryAdapter?.getPosition(categoryList.get(i))
                spinner_event_category.setSelection(categoryData!!)
                break
            }
        }

        text_date.text = sessionManager[DATE, "Date"]

        if (sessionManager[FREE, ""] == "Free") {
            radio_button_free.isChecked = true
        }

        setUpSeekBar(minValue, maxValue)

    }


}

