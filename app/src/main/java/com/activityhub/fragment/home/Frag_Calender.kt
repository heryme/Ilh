package com.activityhub.fragment.home

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.adapter.UserEventAdapter
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.fragment.event.Frag_Event_Details
import com.activityhub.model.Event
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.extension.isNetWork
import com.activityhub.utils.extension.setMargins
import com.activityhub.utils.other.*
import com.activityhub.utils.other.Common.AUTH_TOKEN
import com.activityhub.utils.other.Common.GO_FROM_USER_EVENT_LIST
import com.prolificinteractive.materialcalendarview.*
import com.southernbox.nestedcalendar.behavior.CalendarBehavior
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.WeekFields
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


open class Frag_Calender : Frag_Base(), OnDateSelectedListener, ApiResponseInterface, UserEventAdapter.DeleteEvent {


    private val TAG = Frag_Calender::class.java.name

    private lateinit var userEventAdapter: UserEventAdapter
    private lateinit var userEventList: ArrayList<Event.Data>
    private lateinit var duplicateUserEventList: ArrayList<Event.Data>

    private var calendarBehavior: CalendarBehavior? = null
    private var dayOfWeek: Int = 0
    private var dayOfMonth: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    lateinit var model: Event
    var rootView: View? = null
    private lateinit var calendar_view: MaterialCalendarView
    private lateinit var recycler_calendar_event: RecyclerView
    private lateinit var item_not_found: LinearLayout
    private lateinit var text_no_data: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_calender, container, false)
        }

        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()
        return rootView

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun getDeletedEvent() {
        calendar_view.removeDecorators()
        calendar_view.invalidateDecorators()
        getUserEventAPI()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile_item -> {
             /*   (activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
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

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {

    }

    override fun initComponent() {
    }

    override fun initToolbar() {
        (activity as Act_Home).setToolbarTitle(getString(R.string.calendar), false, true)
        setHasOptionsMenu(true)
    }

    override fun initListeners() {
    }

    override fun initData() {
        initCalendarView()
        initRecyclerView()
        getUserEventAPI()
    }

    override fun initIDs(rootView: View) {
        recycler_calendar_event = rootView.findViewById(R.id.recycler_calendar_event)
        calendar_view = rootView.findViewById(R.id.calendar_view)
        item_not_found = rootView.findViewById(R.id.item_not_found)
        text_no_data = rootView.findViewById(R.id.text_no_data)
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(error_message)
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            GET_USER_EVENT -> {
                model = apiResponseManager.response as Event/*User_Events*/
                Log.e(TAG, "Get User Event  Response:-${model.data}")
                if (model.statusCode == STATUS_CODE) {
                    userEventList.clear()
                    userEventList.addAll(model.data)
                    duplicateUserEventList.clear()
                    duplicateUserEventList.addAll(model.data)
                    userEventAdapter.notifyDataSetChanged()

                    if (userEventList.size > 0) {
                        recycler_calendar_event.visibility = View.VISIBLE
                        item_not_found.visibility = View.GONE
                    } else {
                        recycler_calendar_event.visibility = View.GONE
                        showItemNotFoundView()
                    }
                    ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor())
                }
            }
        }
    }

    private fun showItemNotFoundView() {
        item_not_found.visibility = View.VISIBLE
        if (calendarBehavior?.calendarMode == CalendarMode.WEEKS) {
            text_no_data.gravity = Gravity.CENTER
        } else {
            text_no_data.gravity = Gravity.CENTER or Gravity.BOTTOM
            setMargins(text_no_data, 0, 0, 0, 160)
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initCalendarView() {
        auth_token = sessionManager[AUTH_TOKEN, ""]

        calendar_view.topbarVisible = false
        val behavior = (calendar_view.layoutParams as CoordinatorLayout.LayoutParams).behavior
        if (behavior is CalendarBehavior) {
            calendarBehavior = behavior
        }

        val calendar = Calendar.getInstance()

        calendar_view.setSelectedDate(LocalDate.now())
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get((Calendar.MONTH)) + 1

        year = calendar.get(Calendar.YEAR)
        //setTitle((calendar.get(Calendar.MONTH) + 1).toString() + "月")
        // tvMonthTitle.text = SimpleDateFormat("MMMM").format(calendar.time) + "-" + year

        /* if (calendarBehavior?.calendarMode == CalendarMode.MONTHS) {
             item_not_found.visibility = View.VISIBLE
             text_no_data.gravity = Gravity.CENTER or Gravity.BOTTOM
             setMargins(text_no_data, 0, 0, 0, 160)
         }*/

        calendar_view.setOnDateChangedListener(this)
        calendar_view.showOtherDates = MaterialCalendarView.SHOW_DEFAULTS

        val instance = LocalDate.now()
        calendar_view.setSelectedDate(LocalDate.now())

        val min = LocalDate.of(instance.year, Month.JANUARY, 1)
        val max = LocalDate.of(instance.year, Month.DECEMBER, 31)

        calendar_view.state().edit().setMinimumDate(min).setMaximumDate(max).commit()

        calendar_view.setOnDateChangedListener { widget, calendarDay, selected ->
            val localDate = calendarDay.date
            val weekFields = WeekFields.of(Locale.getDefault())
            calendarBehavior?.setWeekOfMonth(localDate.get(weekFields.weekOfMonth()))

            if (selected) {
                val tempDateList = ArrayList<Event.Data>()
                dayOfWeek = localDate.dayOfWeek.value
                dayOfMonth = localDate.dayOfMonth
                Log.e(TAG, "localDateDate ----->" + localDate)

                /* for (i in duplicateUserEventList.indices) {
                     if (duplicateUserEventList[i].eventStartDate == localDate.toString()) {
                         tempDateList.add(duplicateUserEventList[i])
                     }
               }*/

                for (i in duplicateUserEventList.indices) {
                    if (checkBetweenDate(localDate.toString(), duplicateUserEventList[i].eventStartDate, duplicateUserEventList[i].eventEndDate)) {
                        tempDateList.add(duplicateUserEventList[i])
                        Log.e(TAG, "Between ----->" + localDate)
                    }
                }

                userEventList.clear()
                userEventList.addAll(tempDateList)
                userEventAdapter.notifyDataSetChanged()

                if (userEventList.size > 0) {
                    recycler_calendar_event.visibility = View.VISIBLE
                    item_not_found.visibility = View.GONE
                } else {
                    recycler_calendar_event.visibility = View.GONE
                    showItemNotFoundView()
                }
            }
        }

        calendar_view.setOnMonthChangedListener(OnMonthChangedListener { widget, calendarDay ->
            if (calendarBehavior?.calendarMode == null) {
                return@OnMonthChangedListener
            }
            val localDate = calendarDay.date
            val newDate: LocalDate
            if (calendarBehavior?.calendarMode == CalendarMode.WEEKS) {
                newDate = localDate.plusDays((dayOfWeek - 1).toLong())
                dayOfMonth = newDate.dayOfMonth
                text_no_data.gravity = Gravity.CENTER
            } else {
                val monthDays = localDate.month.length(localDate.isLeapYear)
                if (dayOfMonth > monthDays) {
                    dayOfMonth = monthDays
                }
                newDate = localDate.plusDays((dayOfMonth - 1).toLong())
                dayOfWeek = newDate.dayOfWeek.value
                text_no_data.gravity = Gravity.CENTER or Gravity.BOTTOM
                setMargins(text_no_data, 0, 0, 0, 160)

            }
            widget.setSelectedDate(newDate)
            val weekFields = WeekFields.of(Locale.getDefault())
            calendarBehavior?.setWeekOfMonth(newDate.get(weekFields.weekOfMonth()))
            month = newDate.month.value
            year = newDate.year
            getUserEventAPI()
        })


        /* calendar_view.setOnMonthChangedListener { widget, calendarDay ->
             val localDate = calendarDay.date
             var newDate: LocalDate

             newDate = localDate.plusDays(dayOfWeek.toLong() - 1)
             dayOfMonth = newDate.dayOfMonth
             val monthDays = localDate.month.length(localDate.isLeapYear)
             if (dayOfMonth > monthDays) {
                 dayOfMonth = monthDays
             }
             newDate = localDate.plusDays(dayOfMonth.toLong() - 1)
             dayOfWeek = newDate.dayOfWeek.value


             widget.setSelectedDate(newDate)


             val weekFields = WeekFields.of(Locale.getDefault())
             //tvMonthTitle.text = newDate.month.name + "-" + newDate.year
             month = newDate.month.value
             year = newDate.year
             getUserEventAPI()

         }*/

    }

    private fun initRecyclerView() {
        userEventList = ArrayList()
        duplicateUserEventList = ArrayList()
        userEventAdapter = UserEventAdapter(activity!!, this, userEventList, auth_token, this)

        recycler_calendar_event.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recycler_calendar_event.adapter = userEventAdapter
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

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        } else {
                            val day = CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
                            dates.add(day)
                        }

                    } catch (e: Exception) {

                    }

                }

            }

            return dates
        }

        override fun onPostExecute(calendarDays: List<CalendarDay>) {
            super.onPostExecute(calendarDays)
            if (calendar_view != null) {
                calendar_view?.addDecorator(EventDecorator(ContextCompat.getColor(activity!!, R.color.btn_color), calendarDays))
            }
        }
    }

    private fun getUserEventAPI() {

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

    fun openEventDetail(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable(Frag_Event_Directory.eventData, model.data[position])
        val eventFrg = Frag_Event_Details()
        eventFrg.arguments = bundle
       /* (activity as Act_Home).loadFragmentwithAnim(eventFrg, false,
                resources.getString(R.string.event_directory), true, true)*/

        (activity as Act_Home).changeFragment(eventFrg, false,
                resources.getString(R.string.event_directory),
                true,
                true,
                true)

        sessionManager.put(GO_FROM_USER_EVENT_LIST, false)
    }

}
