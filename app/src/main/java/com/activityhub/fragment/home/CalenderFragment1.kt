package com.activityhub.fragment.home


import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.adapter.UserEventAdapter
import com.activityhub.utils.extension.isNetWork
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.BaseFragment
import com.activityhub.fragment.event.Frag_Event_Details
import com.activityhub.model.Event
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.other.*
import com.activityhub.utils.other.Common.AUTH_TOKEN
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.southernbox.nestedcalendar.behavior.CalendarBehavior
import kotlinx.android.synthetic.main.fragment_calender1.*
import kotlinx.android.synthetic.main.item_not_found.*
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

open class CalenderFragment1 : BaseFragment(), OnDateSelectedListener, ApiResponseInterface, View.OnClickListener {

    private val TAG = CalenderFragment1::class.java.name

    private lateinit var userEventAdapter: UserEventAdapter
    private lateinit var userEventList: ArrayList<Event.Data>

    private var calendarBehavior: CalendarBehavior? = null
    private var dayOfWeek: Int = 0
    private var dayOfMonth: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    private var isWeekMode: Boolean = false
    private var mCalendar: CalendarDay? = null
    private var calendarState = MODE.MONTH
    lateinit var calendar_now: Calendar
    lateinit var model: Event

    private enum class MODE {
        WEEK,
        MONTH
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        calendarState = MODE.MONTH
        Log.e(TAG, "calendarState---->" + calendarState)
        return inflater.inflate(R.layout.fragment_calender1, container, false)
    }

    private fun initListeners() {
        image_collapse_calender.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListeners()
        initCalendarView()
        initRecyclerView()
        getUserEventAPI()

    }

    override fun onClick(view: View?) {
        when (view) {
            image_collapse_calender -> {
                when (calendarState) {

                    MODE.MONTH -> {

                        val cal = Calendar.getInstance()
                        val day = CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))

                        Log.e(TAG, "mCalendar In Month---->" + mCalendar)

                        calendarView.selectedDate = mCalendar

                        calendarView.showOtherDates = MaterialCalendarView.SHOW_DEFAULTS
                        calendarView.state().edit()
                                .setCalendarDisplayMode(CalendarMode.WEEKS)
                                .isCacheCalendarPositionEnabled(false)
                                /* .setMinimumDate(day)*/
                                .commit()

                        if (mCalendar != null) {
                            calendarView.currentDate = mCalendar
                        }
                        //recycler_view.visibility = View.VISIBLE
                        calendarState = MODE.WEEK
                        image_collapse_calender.setImageResource(R.drawable.ic_up_arrow_grey)

                        isWeekMode = true


                        /*if (userEventList.size > 0) {
                            recycler_view.visibility = View.VISIBLE
                            itemNotFound.visibility = View.GONE
                        } else {
                            itemNotFound.visibility = View.VISIBLE
                            recycler_view.visibility = View.GONE
                        }*/
                    }

                    MODE.WEEK -> {
                        Log.e(TAG, "mCalendar In Weeek ---->" + mCalendar)
                        if (mCalendar != null) {
                            calendarView.currentDate = mCalendar
                        }
                        calendarView.showOtherDates = MaterialCalendarView.SHOW_DEFAULTS
                        calendarView.state().edit()
                                .setCalendarDisplayMode(CalendarMode.MONTHS)
                                .isCacheCalendarPositionEnabled(false)
                                .commit()
                        //recycler_view.visibility = View.GONE
                        calendarState = MODE.MONTH
                        image_collapse_calender.setImageResource(R.drawable.ic_down_arrow_grey)
                        isWeekMode = false

                    }

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_item -> {
                (activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
                        false,
                        "",
                        false,
                        false)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {

    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(error_message)
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            GET_USER_EVENT -> {
                /* val getUserEvent*/ model = apiResponseManager.response as Event/*User_Events*/
                Log.e(TAG, "Get User Event  Response:-${model.data}")
                if (model.statusCode == STATUS_CODE) {
                    userEventList.clear()
                    userEventList.addAll(model.data)
                    userEventAdapter.notifyDataSetChanged()

                    Log.e(TAG, "isWeekMode---->" + isWeekMode)

                    if (isWeekMode) {
                        if (userEventList.size > 0) {
                            recycler_view.visibility = View.VISIBLE
                            itemNotFound.visibility = View.GONE
                        } else {
                            itemNotFound.visibility = View.VISIBLE
                            recycler_view.visibility = View.GONE
                        }
                    } else {
                        recycler_view.visibility = View.GONE
                        itemNotFound.visibility = View.GONE
                    }

                    ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor())
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initCalendarView() {
        calendar_now = Calendar.getInstance()
        auth_token = sessionManager[AUTH_TOKEN, ""]
        calendarView.topbarVisible = false
        Log.e("initCalendarView", "calendarView.tileHeight" + calendarView.tileHeight)
        //calendarView.setTileHeightDp(24)
        /*  val behavior = (calendarView.layoutParams as CoordinatorLayout.LayoutParams).behavior
          if (behavior is CalendarBehavior) {
              calendarBehavior = behavior
          }*/
        val calendar = Calendar.getInstance()

        calendarView.setSelectedDate(LocalDate.now())
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get((Calendar.MONTH)) + 1

        year = calendar.get(Calendar.YEAR)
        //setTitle((calendar.get(Calendar.MONTH) + 1).toString() + "月")
        tvMonthTitle.text = SimpleDateFormat("MMMM").format(calendar.time) + "-" + year

        val month_date = SimpleDateFormat("MMMM")
        var month_name = month_date.format(calendar.time)

        calendarView.setOnDateChangedListener(this)
        calendarView.showOtherDates = MaterialCalendarView.SHOW_DEFAULTS

        val instance = LocalDate.now()
        calendarView.setSelectedDate(LocalDate.now())

        val min = LocalDate.of(instance.year, Month.JANUARY, 1)
        val max = LocalDate.of(instance.year, Month.DECEMBER, 31)

        calendarView.state().edit().setMinimumDate(min).setMaximumDate(max).commit()

        calendarView.setOnDateChangedListener { widget, calendarDay, selected ->
            val localDate = calendarDay.date
            val weekFields = WeekFields.of(Locale.getDefault())
            calendarBehavior?.setWeekOfMonth(localDate.get(weekFields.weekOfMonth()))
            if (selected) {
                dayOfWeek = localDate.dayOfWeek.value
                dayOfMonth = localDate.dayOfMonth
            }
        }

        calendarView.setOnMonthChangedListener { widget, calendarDay ->

            calendar_now.get(Calendar.MONTH)

            mCalendar = calendarDay
            val localDate = calendarDay.date
            val newDate: LocalDate
            if (calendarState == MODE.WEEK) {
                newDate = localDate.plusDays(dayOfWeek.toLong() - 1)
                dayOfMonth = newDate.dayOfMonth
            } else {
                val monthDays = localDate.month.length(localDate.isLeapYear)
                if (dayOfMonth > monthDays) {
                    dayOfMonth = monthDays
                }
                newDate = localDate.plusDays(dayOfMonth.toLong() - 1)
                dayOfWeek = newDate.dayOfWeek.value
            }



            widget.setSelectedDate(newDate)
            val weekFields = WeekFields.of(Locale.getDefault())
//            calendarBehavior?.setWeekOfMonth(newDate.get(weekFields.weekOfMonth()))
            //setTitle(newDate.month.value.toString() + "月")
            tvMonthTitle.text = newDate.month.name + "-" + newDate.year
            month = newDate.month.value
            year = newDate.year
            //isScroll = true
            getUserEventAPI()

            /*   if (userEventList.size > 0) {
                   recycler_view.visibility = View.VISIBLE
                   itemNotFound.visibility = View.GONE
               } else {
                   itemNotFound.visibility = View.VISIBLE
                   recycler_view.visibility = View.GONE
               }*/

        }

    }

    private fun initRecyclerView() {
        userEventList = ArrayList()
//        userEventAdapter = UserEventAdapter(activity!!, this, userEventList,
//                auth_token/*,
//                itemNotFound*/)

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = userEventAdapter

        recycler_view.addOnItemTouchListener(RecyclerTouchListener(activity,
                recycler_view,
                object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        isWeekMode = false
                        val bundle = Bundle()
                        bundle.putSerializable(Frag_Event_Directory.eventData, model.data[position])
                        val eventFrg = Frag_Event_Details()
                        eventFrg.arguments = bundle
                        (activity as Act_Home).loadFragment(eventFrg, false,
                                resources.getString(R.string.event_directory), true, true)
                        sessionManager.put(Common.GO_FROM_USER_EVENT_LIST, false)
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                }))
    }

    @SuppressLint("StaticFieldLeak")
    private inner class ApiSimulator : AsyncTask<Void, Void, List<CalendarDay>>() {

        @SuppressLint("SimpleDateFormat")
        override fun doInBackground(vararg voids: Void): List<CalendarDay> {

            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            val dates = java.util.ArrayList<CalendarDay>()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val format = SimpleDateFormat("yyyy-MM-dd")
            val cal = Calendar.getInstance()
            var event_start_date: Date

            if (userEventList.size > 0) {

                for (i in userEventList.indices) {

                    try {

                        event_start_date = format.parse(userEventList[i].eventStartDate)
                        cal.time = event_start_date

                        if (getDaysBetweenDates(userEventList[i].eventStartDate, userEventList[i].eventEndDate) != 0L) {

                            try {
                                var temp = LocalDate.parse(userEventList[i].eventStartDate, formatter)
                                val loop_s = getDaysBetweenDates(userEventList[i].eventStartDate, userEventList[i].eventEndDate)

                                for (j in loop_s.toInt() + 1 downTo 1) {
                                    val day = CalendarDay.from(temp)
                                    dates.add(day)
                                    temp = temp.plusDays(1)
                                }

                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }

                        } else {

                            val day = CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
                            dates.add(day)
                        }

                    } catch (e: ParseException) {

                    }


                }

            }

            return dates
        }

        override fun onPostExecute(calendarDays: List<CalendarDay>) {
            super.onPostExecute(calendarDays)

            /*if (isFinishing()) {
                return
            }*/
            if (calendarView != null) {
                calendarView.addDecorator(EventDecorator(ContextCompat.getColor(activity!!, R.color.btn_color), calendarDays))
            }
        }
    }

    protected fun initEventAndData() {

    }

    private fun getUserEventAPI() {
        Log.e(TAG, "Token--->" + sessionManager[AUTH_TOKEN, ""])
        Log.e(TAG, "Months--->" + month)
        Log.e(TAG, "Years--->" + year)
        Log.e(TAG, "Token--->" + sessionManager[AUTH_TOKEN, ""])
        Log.e(TAG, "Months--->" + month)
        Log.e(TAG, "Years--->" + year)

        if (isNetWork(activity!!)) {
            ApiRequest(activity!!,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).getUserEvent(sessionManager[AUTH_TOKEN, ""],
                            month, year),
                    GET_USER_EVENT,
                    true,
                    this)
        } else {
            showSnackBar(activity!!.resources.getString(R.string.internet_not_available))
        }
    }

}


