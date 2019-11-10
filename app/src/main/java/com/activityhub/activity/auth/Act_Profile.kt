package com.activityhub.activity.auth

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import biz.kasual.materialnumberpicker.MaterialNumberPicker
import com.activityhub.R
import com.activityhub.activity.common.BaseActivity
import com.activityhub.activity.home.Act_Home
import com.activityhub.adapter.ConditionAdapter
import com.activityhub.adapter.IntrestsAdapter
import com.activityhub.utils.extension.Toast
import com.activityhub.utils.extension.isNetWork
import com.activityhub.model.Profile_Condition
import com.activityhub.model.Get_Intrest
import com.activityhub.model.GetProfileModel
import com.activityhub.model.WeightArray
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.other.*
import com.activityhub.utils.other.Common.AUTH_TOKEN
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.libaml.android.view.chip.ChipLayout
import kotlinx.android.synthetic.main.act_profile.*
import okhttp3.ResponseBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class Act_Profile : BaseActivity(), View.OnClickListener, ApiResponseInterface {

    private val TAG = Act_Profile::class.java.name

    private var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    lateinit var simpleDateFormat: SimpleDateFormat

    private var intrestsAdapter: IntrestsAdapter? = null
    private var conditionAdapter: ConditionAdapter? = null

    private var interestList = ArrayList<Get_Intrest.Data>()
    private var conditionList = ArrayList<Profile_Condition.Data>()
    private var selectedInterestedList = ArrayList<Int>()
    private var selectedConditionIdHashSet = HashSet<Int>()

    var selectedResultSurvey = ""
    var selectedActivityLevel = ""
    var selectedGender = ""
    var selectedBloodType = ""
    var pickerHeight = ""
    var pickerInch = ""
    private var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_profile)

        initComponent()
        initToolbar()
        initListeners()
        initData()

    }

    override fun initComponent() {
        simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    }

    override fun initToolbar() {
    }

    override fun initListeners() {
        llYourProfileDateofBirth.setOnClickListener(this)
        llYourProfileCondition.setOnClickListener(this)
        llYourProfileGender.setOnClickListener(this)
        llYourProfileHight.setOnClickListener(this)
        llYourProfileActivityLevel.setOnClickListener(this)
        llYourProfileInterests.setOnClickListener(this)
        llYourProfileResultsSurvey.setOnClickListener(this)
        llYourProfileWight.setOnClickListener(this)
        tvYourProfileDateOfBirth.setOnClickListener(this)
        btnYourProfileActivityUpdate.setOnClickListener(this)
        btnYourProfileActivitySkip.setOnClickListener(this)
        btnYourProfileActivitySubmit.setOnClickListener(this)
        etYourProfileActivityHeight.setOnClickListener(this)
        etYourProfileActivityCurrentWeight.setOnClickListener(this)
        etYourProfileActivityUpdateWeight.setOnClickListener(this)

        radio_group_gender.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.radio_button_male -> {
                    gender = resources.getString(R.string.male)
                    selectedGender = gender
                }
                R.id.radio_button_female -> {
                    gender = resources.getString(R.string.female)
                    selectedGender = gender
                }
                R.id.radio_button_other -> {
                    gender = resources.getString(R.string.other)
                    selectedGender = gender
                }

            }
        }

        rgYourProfileResultSurvey.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<View>(checkedId) as RadioButton
            selectedResultSurvey = radioButton.text.toString()
        }

        rgYourProfileActivityLevel.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<View>(checkedId) as RadioButton
            selectedActivityLevel = radioButton.text.toString()
        }
    }

    override fun initData() {
        getDataFromIntent()
        getProfileAPI()
        getConditionAPI()
        getInterestAPI()
    }

    private fun setUpGraph(weightList: List<WeightArray>) {

        var xAxis = 0.0

        val weightArrayList: ArrayList<DataPoint> = ArrayList()

        graph.setBackgroundColor(ContextCompat.getColor(this@Act_Profile, R.color.white))
        graph.gridLabelRenderer.gridStyle = GridLabelRenderer.GridStyle.NONE
        graph.gridLabelRenderer.isVerticalLabelsVisible = false
        graph.gridLabelRenderer.isHorizontalLabelsVisible = false
        graph.viewport.isScrollable = true

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.5)
        graph.viewport.setMaxX(3.5)

        for (i in weightList.indices) {
            xAxis += 1
            Log.e(TAG, "xAxis-->$xAxis")
            try {
                weightArrayList.add(DataPoint(xAxis, weightList.get(i).weight.toDouble()))
            } catch (e: NumberFormatException) {
                weightArrayList.add(DataPoint(xAxis, 0.0))
            }
        }

        Log.e(TAG, "aarr List Size-->" + weightArrayList.size)

        val series = LineGraphSeries<DataPoint>(weightArrayList.toTypedArray())
        graph.addSeries(series)
    }

    private fun getDataFromIntent() {
        val value = intent.getBooleanExtra("isComeFromSignIn", false)
        Log.e(TAG, "Get Data Intent-->$value")
        if (value) {
            viewLineResultSurvey.visibility = View.VISIBLE
            llYourProfileActivitySkipUpdate.visibility = View.VISIBLE
            btnYourProfileActivitySubmit.visibility = View.GONE
            etYourProfileActivityUpdateWeight.visibility = View.GONE
            tvYourProfileActivityUpdateWeightLable.visibility = View.GONE
            tvYourProfileActivityYourProgress.visibility = View.GONE
            llYourProfileActivityGraph.visibility = View.GONE
            etYourProfileActivityCurrentWeight.isEnabled = true
        } else {
            etYourProfileActivityUpdateWeight.visibility = View.VISIBLE
            etYourProfileActivityCurrentWeight.isEnabled = false
            tvYourProfileActivityUpdateWeightLable.visibility = View.VISIBLE
            btnYourProfileActivitySubmit.visibility = View.VISIBLE
            viewLineResultSurvey.visibility = View.GONE
            llYourProfileActivitySkipUpdate.visibility = View.GONE
            tvYourProfileActivityYourProgress.visibility = View.VISIBLE
            llYourProfileActivityGraph.visibility = View.VISIBLE

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(view: View?) {
        when (view) {
            btnYourProfileActivityUpdate -> {
                updateProfile()
            }

            btnYourProfileActivitySkip -> {
                startActivity(Intent(this@Act_Profile, Act_Home::class.java))
            }

            llYourProfileDateofBirth -> {
                if (tvYourProfileDateOfBirth.visibility == View.GONE) {
                    tvYourProfileDateOfBirth.visibility = View.VISIBLE
                    imYourProfileRightArrow.setImageResource(R.drawable.ic_down_arrow)
                } else {
                    tvYourProfileDateOfBirth.visibility = View.GONE
                    imYourProfileRightArrow.setImageResource(R.drawable.ic_right_arrow)
                }
            }

            llYourProfileCondition -> {
                if (acYourProfileActivityCondition.visibility == View.GONE) {
                    acYourProfileActivityCondition.visibility = View.VISIBLE
                    acYourProfileActivityCondition.performClick()
                    hideSoftKeyboard(mActivity)
                    imYourProfileRightArrowCondition.setImageResource(R.drawable.ic_down_arrow)
                } else {
                    hideSoftKeyboard(mActivity)
                    acYourProfileActivityCondition.visibility = View.GONE
                    imYourProfileRightArrowCondition.setImageResource(R.drawable.ic_right_arrow)
                }
            }

            llYourProfileGender -> {
                if (llYourProfileSpinnerView.visibility == View.GONE) {
                    llYourProfileSpinnerView.visibility = View.VISIBLE
                    imYourProfileRightArrowSex.setImageResource(R.drawable.ic_down_arrow)

                } else {
                    imYourProfileRightArrowSex.setImageResource(R.drawable.ic_right_arrow)
                    llYourProfileSpinnerView.visibility = View.GONE
                }
            }

            llYourProfileHight -> {
                if (etYourProfileActivityHeight.visibility == View.GONE) {
                    etYourProfileActivityHeight.visibility = View.VISIBLE
                    imYourProfileRightArrowHight.setImageResource(R.drawable.ic_down_arrow)
                } else {
                    etYourProfileActivityHeight.visibility = View.GONE
                    imYourProfileRightArrowHight.setImageResource(R.drawable.ic_right_arrow)
                }
            }

            llYourProfileActivityLevel -> {
//                if (rgYourProfileActivityLevel.visibility == View.GONE) {
//                    rgYourProfileActivityLevel.visibility = View.VISIBLE
//                    imYourProfileRightArrowActivityLevel.setImageResource(R.drawable.ic_down_arrow)
//                } else {
//                    rgYourProfileActivityLevel.visibility = View.GONE
//                    imYourProfileRightArrowActivityLevel.setImageResource(R.drawable.ic_right_arrow)
//                }
            }

            llYourProfileResultsSurvey -> {
                if (rgYourProfileResultSurvey.visibility == View.GONE) {
                    rgYourProfileResultSurvey.visibility = View.VISIBLE
                    imYourProfileRightArrowResultSurvey.setImageResource(R.drawable.ic_down_arrow)
                } else {
                    rgYourProfileResultSurvey.visibility = View.GONE
                    imYourProfileRightArrowResultSurvey.setImageResource(R.drawable.ic_right_arrow)
                }
            }

            llYourProfileInterests -> {
                if (rvYourProfileinterests.visibility == View.GONE) {
                    rvYourProfileinterests.visibility = View.VISIBLE
                    initInterestData()
                    imYourProfileinterests.setImageResource(R.drawable.ic_down_arrow)
                } else {
                    rvYourProfileinterests.visibility = View.GONE
                    imYourProfileinterests.setImageResource(R.drawable.ic_right_arrow)
                }
            }

            llYourProfileWight -> {
                if (llYourProfileWeight.visibility == View.GONE) {
                    llYourProfileWeight.visibility = View.VISIBLE
                    imYourProfileweight.setImageResource(R.drawable.ic_down_arrow)

                } else {
                    imYourProfileweight.setImageResource(R.drawable.ic_right_arrow)
                    llYourProfileWeight.visibility = View.GONE
                }
            }

            tvYourProfileDateOfBirth -> {
                setDatePicker()
            }

            btnYourProfileActivitySubmit -> {
                updateProfile()
            }

            etYourProfileActivityHeight -> {
                showHeightPickerDialog()
            }

            etYourProfileActivityCurrentWeight -> {
                showWeightPickerDialog(false)
            }

            etYourProfileActivityUpdateWeight -> {
                showWeightPickerDialog(true)
            }

        }
    }

    fun initInterestData() {
        rvYourProfileinterests.layoutManager = LinearLayoutManager(this@Act_Profile, LinearLayoutManager.VERTICAL, false)
        rvYourProfileinterests.setHasFixedSize(true)

        intrestsAdapter = IntrestsAdapter(this, interestList,
                object : IntrestsAdapter.OnItemCheckListener {
                    override fun onItemCheck(item: Get_Intrest.Data) {
                        selectedInterestedList.add(item.intrestId)
                        Log.e(TAG, "Check Value-->" + item.intrestId)
                        Log.e(TAG, "selectedInterestedList Size-->" + selectedInterestedList.size)
                    }

                    override fun onItemUncheck(item: Get_Intrest.Data) {
                        selectedInterestedList.remove(item.intrestId)
                        Log.e(TAG, "Un Check Value-->" + item.intrestId)
                        Log.e(TAG, "selectedInterestedList Size-->" + selectedInterestedList.size)
                    }
                })

        rvYourProfileinterests.adapter = intrestsAdapter
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            GET_CONDITION -> {
                val model = apiResponseManager.response as Profile_Condition

                Log.e(TAG, "Get Condition Response:-${model.data}")
                if (model.statusCode == STATUS_CODE) {
                    if (model.data.isNotEmpty()) {
                        conditionList.addAll(model.data)
                        setConditionAdapter()
                    }
                }
            }
            GET_INTEREST -> {
                val model = apiResponseManager.response as Get_Intrest
                Log.e(TAG, "Get Interest Response:-${model.data}")
                if (model.statusCode == STATUS_CODE) {
                    interestList.addAll(model.data)
                    Log.e(TAG, "interestList Size:-${interestList.size}}")
                }
            }

            UPDATE_PROFILE -> {
                val model = apiResponseManager.response as ResponseBody
                val responseValue = model.string()
                Log.e(TAG, "Update Profile Response:-$responseValue")
                val response = JSONObject(responseValue)
                val statusCode = response.optInt(statusCode)
                val message = response.optString(message)
                if (statusCode == STATUS_CODE) {
                    Toast(message, true, this@Act_Profile)
                    selectedInterestedList.clear()
                    selectedConditionIdHashSet.clear()
                    startActivity(Intent(this@Act_Profile, Act_Home::class.java))
                }
            }
            GET_PROFILE -> {
                val getProfileModel = apiResponseManager.response as GetProfileModel

                Log.e(TAG, "Get Profile Response:-${getProfileModel.data}")
                if (getProfileModel.statusCode == STATUS_CODE) {
                    setProfileData(getProfileModel)
                }
            }


        }
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(layout_main, error_message)
        }
    }

    private fun getConditionAPI() {

        if (isNetWork(this)) {
            ApiRequest(this@Act_Profile,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).getCondition(sessionManager[AUTH_TOKEN, ""],
                            ""),
                    GET_CONDITION,
                    true,
                    this)
        } else {
            showSnackBar(resources.getString(R.string.internet_not_available))
        }
    }

    private fun getInterestAPI() {
        if (isNetWork(this)) {
            ApiRequest(this@Act_Profile,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).getInterest(sessionManager[AUTH_TOKEN, ""],
                            ""),
                    GET_INTEREST,
                    true,
                    this)
        } else {
            showSnackBar(resources.getString(R.string.internet_not_available))
        }
    }

    private fun getProfileAPI() {

        if (isNetWork(this)) {
            ApiRequest(this@Act_Profile,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).getProfile(sessionManager[AUTH_TOKEN, ""],
                            ""), GET_PROFILE, true, this)
        } else {
            showSnackBar(resources.getString(R.string.internet_not_available))
        }
    }

    private fun updateProfile() {
        val tempInterested: String
        val tempCondition: String
        val finalInterested: String
        val finalCondition: String

        //Final Interested
        tempInterested = selectedInterestedList.toString()
        finalInterested = tempInterested.replace("[", "").replace("]", "")

        //Final Condition
        tempCondition = selectedConditionIdHashSet.toString()
        finalCondition = tempCondition.replace("[", "").replace("]", "")

        Log.e(TAG, "finalInterested-->" + finalInterested)
        Log.e(TAG, "selectedInterestedList Data-->" + selectedInterestedList)
        Log.e(TAG, "final selectedConditionId-->" + finalCondition)

        if (isNetWork(this)) {
            ApiRequest(this@Act_Profile,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).updateProfile(
                            sessionManager[AUTH_TOKEN, ""],
                            finalCondition,
                            tvYourProfileDateOfBirth.text.toString(),
                            selectedGender,
                            selectedBloodType,
                            etYourProfileActivityHeight.text.toString(),
                            selectedActivityLevel,
                            etYourProfileActivityCurrentWeight.text.toString(),
                            etYourProfileActivityUpdateWeight.text.toString(),
                            finalInterested,
                            selectedResultSurvey), UPDATE_PROFILE, true, this)
        } else {
            showSnackBar(resources.getString(R.string.internet_not_available))
        }
    }

    private fun setConditionAdapter() {

        conditionAdapter = ConditionAdapter(this@Act_Profile, R.layout.row_item_condition, conditionList)
        acYourProfileActivityCondition.adapter = conditionAdapter

        acYourProfileActivityCondition.setOnItemClickListener { arg0, arg1, arg2, arg3 ->
            val selected = arg0.adapter.getItem(arg2) as Profile_Condition.Data
            val selectedConditionList = ArrayList<Int>()
            Log.e(TAG, "Condition Selected Item ID --->" + selected.conditionId)
            selectedConditionList.add(selected.conditionId)
            selectedConditionIdHashSet.addAll(selectedConditionList)
            Log.e(TAG, "selectedConditionIdHashSet ID --->" + selectedConditionIdHashSet.toString())
        }


        acYourProfileActivityCondition.onChipItemChangeListener = object : ChipLayout.ChipItemChangeListener {
            override fun onChipAdded(pos: Int, txt: String) {
                Log.d(txt, pos.toString())
            }

            override fun onChipRemoved(pos: Int, txt: String) {
                Log.e("TAG", "Remove From the MainActivity--->$txt -->$pos")
            }
        }
    }

    private fun setDatePicker() {
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // etPatientRegisterBirthDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    val calendar = GregorianCalendar(year, monthOfYear, dayOfMonth)
                    tvYourProfileDateOfBirth.text = simpleDateFormat.format(calendar.time)
                    //age = getAge(simpleDateFormat!!.format(calendar.getTime()))
                    Log.e("Register", "select  Date==>" + simpleDateFormat.format(calendar.time))
                }, mYear, mMonth, mDay)
        datePickerDialog.show()
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
    }

    private fun setProfileData(getProfileModel: GetProfileModel) {
        //Set Date Of Birth
        tvYourProfileDateOfBirth.text = getProfileModel.data.dateOfBirth

        val user_gender = getProfileModel.data.gender
        selectedGender = user_gender
        when (user_gender) {
            resources.getString(R.string.male) -> {
                radio_button_male.isChecked = true
            }
            resources.getString(R.string.female) -> {
                radio_button_female.isChecked = true
            }
            resources.getString(R.string.other) -> {
                radio_button_other.isChecked = true
            }
        }

        //Set Height
        val height = getProfileModel.data.height

        if (height != "") {
            val parts = height.split(" ")
            if (parts.isNotEmpty() && !parts.equals("")) {

                try {
                    pickerHeight = parts[0]
                    pickerInch = parts[1]

                    Log.e("height", "height  = $pickerHeight inch = $pickerInch")
                } catch (e: Exception) {

                }

            }
        }

        etYourProfileActivityHeight.text = getProfileModel.data.height

        //Weight
        if (!getProfileModel.data.weight.equals("")) {
            etYourProfileActivityCurrentWeight.text = getProfileModel.data.weight
            etYourProfileActivityUpdateWeight.visibility = View.VISIBLE
            tvYourProfileActivityUpdateWeightLable.visibility = View.VISIBLE
            llYourProfileActivityGraph.visibility = View.VISIBLE
            tvYourProfileActivityYourProgress.visibility = View.VISIBLE
            etYourProfileActivityCurrentWeight.isEnabled = false
        } else {
            tvYourProfileActivityUpdateWeightLable.visibility = View.GONE
            etYourProfileActivityUpdateWeight.visibility = View.GONE
            llYourProfileActivityGraph.visibility = View.GONE
            tvYourProfileActivityYourProgress.visibility = View.GONE
            etYourProfileActivityCurrentWeight.isEnabled = true
        }

        //Activity Level
        when {
            getProfileModel.data.activityLevel == getString(R.string.light) -> rgYourProfileActivityLevel.check(R.id.rbYourProfileActivityLevelLight)
            getProfileModel.data.activityLevel == getString(R.string.moderate) -> rgYourProfileActivityLevel.check(R.id.rbYourProfileActivityLevelModerate)
            getProfileModel.data.activityLevel == getString(R.string.high) -> rgYourProfileActivityLevel.check(R.id.rbYourProfileActivityLevelHigh)
        }

        //Result Survey
        when {
            getProfileModel.data.resultSurvey == getString(R.string.daily) -> rgYourProfileResultSurvey.check(R.id.rbYourProfileResultSurveyDaily)
            getProfileModel.data.resultSurvey == getString(R.string.weekly) -> rgYourProfileResultSurvey.check(R.id.rbYourProfileResultSurveyWeekly)
            getProfileModel.data.resultSurvey == getString(R.string.monthly) -> rgYourProfileResultSurvey.check(R.id.rbYourProfileResultSurveyMonthly)
        }

        //Set Condition
        val tempConditionList: ArrayList<String> = ArrayList()
        for (i in 0 until getProfileModel.data.condition.size) {
            tempConditionList.add(getProfileModel.data.condition[i].conditionName)
        }
        acYourProfileActivityCondition.text = tempConditionList


        setUpGraph(getProfileModel.data.weightArray)
    }

    fun showHeightPickerDialog() {
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        val heightDialogg = Dialog(this, R.style.DialogCustomTheme)
        heightDialogg.setContentView(R.layout.dialog_hight_selection)
        heightDialogg.setCancelable(true)
        heightDialogg.setCanceledOnTouchOutside(true)
        heightDialogg.window!!.setLayout(width, height)

        val pickerDialogHeight = heightDialogg.findViewById(R.id.pickerDialogHeight) as MaterialNumberPicker
        val pickerDialogInch = heightDialogg.findViewById(R.id.pickerDialogInch) as MaterialNumberPicker

        val tvDialogHeightCancel = heightDialogg.findViewById(R.id.tvDialogHeightCancel) as TextView
        val tvDialogHeightOk = heightDialogg.findViewById(R.id.tvDialogHeightOk) as TextView

        //Set Value From The API
        if (pickerHeight != "") {
            try {
                pickerDialogHeight.value = pickerHeight.toInt()
                pickerDialogInch.value = pickerInch.toInt()
            } catch (e: NumberFormatException) {

            }

        }

        tvDialogHeightCancel.setOnClickListener { heightDialogg.dismiss() }

        tvDialogHeightOk.setOnClickListener {
            heightDialogg.dismiss()
            Log.e(TAG, "pickerDialogHeight--->" + pickerDialogHeight.value)
            Log.e(TAG, "pickerDialogInch--->" + pickerDialogInch.value)
            val stringBuilder = StringBuilder()
            stringBuilder.append(pickerDialogHeight.value)
            stringBuilder.append(" ft ")
            stringBuilder.append(pickerDialogInch.value)
            stringBuilder.append(" in")

            etYourProfileActivityHeight.text = stringBuilder.toString()
        }
        heightDialogg.show()
    }

    private fun showWeightPickerDialog(isUpdatedWeight: Boolean) {
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        val weightDialog = Dialog(this, R.style.DialogCustomTheme)
        weightDialog.setContentView(R.layout.dialog_weight_selection)
        weightDialog.setCancelable(true)
        weightDialog.setCanceledOnTouchOutside(true)
        weightDialog.window!!.setLayout(width, height)

        val pickerDialogWeightSt = weightDialog.findViewById(R.id.pickerDialogWeightSt) as MaterialNumberPicker
        val pickerDialogWeightLb = weightDialog.findViewById(R.id.pickerDialogWeightLb) as MaterialNumberPicker

        val tvDialogWeightCancel = weightDialog.findViewById(R.id.tvDialogWeightCancel) as TextView
        val tvDialogWeightOk = weightDialog.findViewById(R.id.tvDialogWeightOk) as TextView

        /*//Set Value From The API
        if (pickerHeight != "") {
            pickerDialogWeightSt.value = pickerHeight.toInt()
            pickerDialogWeightLb.value = pickerInch.toInt()
        }*/

        tvDialogWeightCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                weightDialog.dismiss()
            }
        })

        tvDialogWeightOk.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                weightDialog.dismiss()
                Log.e(TAG, "pickerDialog Weight St--->" + pickerDialogWeightSt.value)
                Log.e(TAG, "pickerDialog  Weight Lb--->" + pickerDialogWeightLb.value)
                val stringBuilder = StringBuilder()
                stringBuilder.append(pickerDialogWeightSt.value)
                stringBuilder.append("st ")
                stringBuilder.append(pickerDialogWeightLb.value)
                stringBuilder.append("lb")
                if (isUpdatedWeight) {
                    etYourProfileActivityUpdateWeight.text = stringBuilder.toString()
                } else {
                    etYourProfileActivityCurrentWeight.text = stringBuilder.toString()
                    etYourProfileActivityUpdateWeight.text = stringBuilder.toString()
                }
            }
        })
        weightDialog.show()
    }


}