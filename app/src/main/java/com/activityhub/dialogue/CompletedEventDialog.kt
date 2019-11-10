package com.activityhub.dialogue

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.activityhub.R
import com.activityhub.model.CommonResponse
import com.activityhub.model.Completed_Event
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.extension.Toast
import com.activityhub.utils.extension.isNetWork
import com.activityhub.utils.other.*
import com.activityhub.utils.other.Common.AUTH_TOKEN
import com.activityhub.utils.other.Common.COMPLETED_EVENT_DIALOG
import kotlinx.android.synthetic.main.dialog_attended_event.*


class CompletedEventDialog(val activity: Activity, val mContext: Context) :
        Dialog(activity, R.style.Theme_Dialog), View.OnClickListener, ApiResponseInterface {

    private val TAG = CompletedEventDialog::class.java.name
    private lateinit var view: View
    private lateinit var sessionManager: SessionManager
    private var initDialog: Int = 0
    lateinit var auth_token: String
    var mRating: Float = 0f
    private var eventCompletedList: ArrayList<Completed_Event.Data>? = null

    init {
        init(activity)
    }

    @SuppressLint("InflateParams")
    private fun init(context: Activity) {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.dialog_attended_event, null)
        setContentView(view)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        initDialogueAttributes()
        initComponants()
        initData()
        initListeners()
        callCompletedEventAPI()

    }

    private fun initDialogueAttributes() {
        val window = window
        when {
            window != null -> {
                window.setGravity(Gravity.CENTER)
                window.setWindowAnimations(R.style.animation_exit_down)
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(window.attributes)
                lp.width = WindowManager.LayoutParams.MATCH_PARENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                window.attributes = lp
            }
        }
    }

    private fun initComponants() {
        sessionManager = SessionManager(activity)
    }

    private fun initData() {
        auth_token = sessionManager[AUTH_TOKEN, ""]
    }

    private fun initListeners() {
        text_submit.setOnClickListener(this)
        text_cancel.setOnClickListener(this)
        text_did_not_attend.setOnClickListener(this)
    }

    private fun callCompletedEventAPI() {
        if (isNetWork(activity)) {
            Log.e(TAG, "Token--->" + sessionManager[AUTH_TOKEN, ""])
            ApiRequest(activity,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API)
                            .completedEvent(sessionManager[AUTH_TOKEN, ""]),
                    COMPLETED_EVENT,
                    false,
                    this)
        } else {
            Toast(activity.resources.getString(R.string.internet_not_available), true, context)
        }
    }

    private fun callEventRatingAPI(eventId: Int, ratting: Float) {
        if (isNetWork(activity)) {
            Log.e(TAG, "Token--->" + sessionManager[AUTH_TOKEN, ""])
            Log.e(TAG, "Event Id--->$eventId")
            Log.e(TAG, "Ratiing Final--->$ratting")
            ApiRequest(activity,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).ratingEvent(
                            sessionManager[AUTH_TOKEN, ""],
                            eventId,
                            ratting), EVENT_RATTING, true, this)
        } else {
            Toast(activity.resources.getString(R.string.internet_not_available), true, context)
        }
    }

    private fun calUserEventAttend(eventId: Int) {
        if (isNetWork(activity)) {
            Log.e(TAG, "Token--->" + sessionManager[AUTH_TOKEN, ""])
            Log.e(TAG, "Event Id--->$eventId")
            ApiRequest(activity,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).userEventAttend(
                            sessionManager[AUTH_TOKEN, ""],
                            eventId), USER_EVENT_ATTEND, true, this)
        } else {
            Toast(activity.resources.getString(R.string.internet_not_available), true, context)
        }
    }


    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            COMPLETED_EVENT -> {
                val completedEventModel = apiResponseManager.response as Completed_Event
                Log.e(TAG, "Get CompletedEventDialog Event  Response:-${completedEventModel.data}")
                if (completedEventModel.statusCode == STATUS_CODE) {
                    // callback.onSuccess(completedEventModel)
                    eventCompletedList = ArrayList()
                    eventCompletedList?.addAll(completedEventModel.data)

                    openDialog(false)
                } else {
                    //callback.onFailure(true, activity.resources.getString(R.string.try_after_some_time))
                    Toast(activity.resources.getString(R.string.try_after_some_time), true, context)
                }
            }

            EVENT_RATTING -> {
                val commonResponse = apiResponseManager.response as CommonResponse
                if (commonResponse.statusCode == STATUS_CODE) {
                    Toast(commonResponse.message, true, context)
                    openDialog(true)
                } else {
                    Toast(activity.resources.getString(R.string.try_after_some_time), true, context)
                }
            }

            USER_EVENT_ATTEND -> {
                val commonResponse = apiResponseManager.response as CommonResponse
                if (commonResponse.statusCode == STATUS_CODE) {
                    Toast(commonResponse.message, true, context)
                    openDialog(false)
                } else {
                    Toast(activity.resources.getString(R.string.try_after_some_time), true, context)
                }
            }

        }
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            //Toast(error_message, true, context)
            //callback.onFailure(true, error_message)
            when (apiResponseManager.type) {
                EVENT_RATTING -> {
                    Toast(error_message, true, context)
                }
            }
        }
    }

    override fun onClick(view: View?) {

        when (view) {

            text_submit -> {
                Log.e("mRating", "mRating = $mRating")
                callEventRatingAPI(eventCompletedList?.get(initDialog - 1)?.eventId!!, mRating)
            }
            text_cancel -> {
                openDialog(false)
            }
            text_did_not_attend -> {
                Toast("Click", true, context)
                calUserEventAttend(eventCompletedList?.get(initDialog - 1)?.eventId!!)
            }
        }

    }

    override fun onBackPressed() {
        dismiss()
    }

    @SuppressLint("SetTextI18n")
    fun openDialog(isGiveRating: Boolean) {
        if (eventCompletedList?.size!! > 0) {
            if (initDialog < eventCompletedList?.size!!) {
                ratingbar_event.rating = 0F
                sessionManager.put("", "")
                text_event_title.text = "Did you enjoy the " + eventCompletedList?.get(initDialog)?.title + "?"
                ratingbar_event.setOnRatingChangeListener { ratingBar, rating, fromUser ->
                    mRating = rating
                    //Toast("Rating-->" + rating, true, context)
                    Log.e(TAG, "Rating-->--->" + mRating)
                }

                if (isGiveRating) {
                    Log.e(TAG, "Rating-->" + mRating)
                }
                show()
                initDialog++
                Log.e(TAG, "initDialog-->$initDialog")
                if (eventCompletedList?.size!! == initDialog) {
                    sessionManager.put(COMPLETED_EVENT_DIALOG, true)
                }
            } else {
                dismiss()
                initDialog = 0
            }
        } else {
            sessionManager.put(COMPLETED_EVENT_DIALOG, true)
        }
    }

}


