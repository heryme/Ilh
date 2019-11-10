package com.activityhub.dialogue

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.activityhub.R
import com.activityhub.app.AppController
import com.activityhub.utils.extension.isNetWork
import com.activityhub.interfaces.CommonCallBack
import com.activityhub.model.CommonResponse
import com.activityhub.model.Event
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.other.*
import com.activityhub.utils.other.Common.AUTH_TOKEN
import kotlinx.android.synthetic.main.dialog_add_calendar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddCalendarEvent(val activity: Activity, val mContext: Context, val callback: CommonCallBack) :
        Dialog(activity, R.style.Theme_Dialog), View.OnClickListener, ApiResponseInterface {

    private lateinit var view: View
    lateinit var appcontroller: AppController
    private lateinit var sessionManager: SessionManager
    lateinit var event_data: Event.Data
    lateinit var auth_token: String

    init {
        init(activity)
    }

    @SuppressLint("InflateParams")
    private fun init(context: Activity) {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.dialog_add_calendar, null)

        setContentView(view)
        initDialogueAttributes()
        initComponants()
        initData()
        initListeners()
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
        appcontroller = activity.application as AppController
        sessionManager = SessionManager(activity)

    }

    private fun initData() {
        event_data = appcontroller.getEvent_data()
        auth_token = sessionManager[AUTH_TOKEN, ""]
    }

    private fun initListeners() {
        text_add_calendar.setOnClickListener(this)
        text_discard.setOnClickListener(this)
    }

    private fun callAddEventApi() {

        if (isNetWork(activity)) {
            ApiRequest(activity,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API)
                            .addEventIntoCalendar(auth_token,
                                    event_data.eventId.toString()), ADD_EVENT,
                    true,
                    this)
        } else {
            Toast.makeText(activity, activity.resources.getString(R.string.internet_not_available), Toast.LENGTH_LONG).show()
        }

    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            ADD_EVENT -> {
                val commonResponse = apiResponseManager.response as CommonResponse
                if (commonResponse.statusCode == 200) {
                    val map = HashMap<String, String>()
                    map[MESSAGE] = commonResponse.message
                    callback.onSuccess(map)
                    addCalendarReminder()
                } else {
                    callback.onFailure(true, activity.resources.getString(R.string.try_after_some_time))
                }
                onBackPressed()
            }
        }
    }

    private fun addCalendarReminder() {

        val cal_end_date = Calendar.getInstance()
        val cal_start_date = Calendar.getInstance()

        val cr = activity.contentResolver
        val EVENTS_URI = Uri.parse(getCalendarUriBase(true) + "events")

//        val begin: Long = cal_start_date.timeInMillis
//        val end: Long = cal_end_date.timeInMillis
//
//        val proj = arrayOf<String>(CalendarContract.Instances._ID,
//                CalendarContract.Instances.BEGIN,
//                CalendarContract.Instances.END,
//                CalendarContract.Instances.EVENT_ID)

//        lateinit var cursor: Cursor
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!SystemPermissions.checkPermissionForReadCalendar(activity)) {
//                SystemPermissions.checkPermissionForReadCalendar(activity)
//                SystemPermissions.requestPermissionForReadCalendar(activity)
//            } else {
//                cursor = CalendarContract.Instances.query(cr, proj,
//                        begin, end, event_data.title + ": " + activity.resources.getString(R.string.app_name))
//
//            }
//        } else {
//            cursor = CalendarContract.Instances.query(cr, proj,
//                    begin, end, event_data.title + ": " + activity.resources.getString(R.string.app_name))
//        }

//        when {
//            cursor.count > 0 -> {
//                Log.e("addCalendarReminder", "already added")
//            }
//            else -> {
        val timeZone = TimeZone.getDefault()

        val sdf = SimpleDateFormat("yyyy-MM-dd h:m a", Locale.getDefault())

        try {
            cal_start_date.time = sdf.parse(event_data.eventStartDate + " " + event_data.eventStartTime)
            cal_end_date.time = sdf.parse(event_data.eventEndDate + " " + event_data.eventEndTime)

            var values = ContentValues()
            values.put(CalendarContract.Events.CALENDAR_ID, 1)
            values.put(CalendarContract.Events.TITLE, event_data.title + ": " + activity.resources.getString(R.string.app_name))
            values.put(CalendarContract.Events.DESCRIPTION, event_data.shortDescription)
            values.put(CalendarContract.Events.DTSTART, cal_start_date.timeInMillis)
            values.put(CalendarContract.Events.DTEND, cal_end_date.timeInMillis)
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.id)
            values.put(CalendarContract.Events.EVENT_LOCATION, event_data.location)
            values.put(CalendarContract.Events.HAS_ALARM, 1)
            val event = cr.insert(EVENTS_URI, values)

            val REMINDERS_URI = Uri.parse(getCalendarUriBase(true) + "reminders")
            values = ContentValues()
            values.put(CalendarContract.Reminders.EVENT_ID, event?.lastPathSegment!!.toLong())
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
            values.put(CalendarContract.Reminders.MINUTES, 60)
            cr.insert(REMINDERS_URI, values)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ParseException", e.message)
        }

//            }
//        }


    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            callback.onFailure(true, error_message)
            addCalendarReminder()
            onBackPressed()
        }
    }


    override fun onClick(view: View?) {

        when (view) {

            text_add_calendar -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!SystemPermissions.checkPermissionForWriteCalendar(activity) &&
                            !SystemPermissions.checkPermissionForReadCalendar(activity)) {

                        SystemPermissions.checkPermissionForReadCalendar(activity)
                        SystemPermissions.checkPermissionForWriteCalendar(activity)
                        SystemPermissions.requestPermissionForCalendar(activity)
                        SystemPermissions.requestPermissionForReadCalendar(activity)
                    } else {
                        callAddEventApi()
                    }
                } else {
                    callAddEventApi()
                }
            }

            text_discard -> {
                callback.onFailure(false, activity.resources.getString(R.string.try_after_some_time))
                onBackPressed()
            }
        }

    }

    override fun onBackPressed() {
        dismiss()
    }

}


