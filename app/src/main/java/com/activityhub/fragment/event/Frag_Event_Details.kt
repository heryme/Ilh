package com.activityhub.fragment.event

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.*
import android.widget.*
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.fragment.home.Frag_Event_Directory
import com.activityhub.model.CommonResponse
import com.activityhub.model.Event
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.extension.formattedDateFromString
import com.activityhub.utils.extension.isNetWork
import com.activityhub.utils.extension.openGmail
import com.activityhub.utils.other.ADD_EVENT
import com.activityhub.utils.other.MyCustomButton
import com.activityhub.utils.other.getCalendarUriBase
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.willy.ratingbar.ScaleRatingBar
import java.text.SimpleDateFormat
import java.util.*

class Frag_Event_Details : Frag_Base(), View.OnClickListener, ApiResponseInterface {

    private val TAG = Frag_Event_Details::class.java.name
    private lateinit var eventData: Event.Data
    var rootView: View? = null
    private lateinit var image_event: ImageView
    private lateinit var text_event_name: TextView
    private lateinit var text_call: TextView
    private lateinit var text_date: TextView
    private lateinit var text_full_date: TextView
    private lateinit var text_event_details: TextView
    private lateinit var text_location: TextView
    private lateinit var text_email: TextView
    private lateinit var text_overview: TextView
    private lateinit var text_rating: TextView
    private lateinit var text_website: TextView
    private lateinit var text_price: TextView
    private lateinit var relative_location: RelativeLayout
    private lateinit var relative_call: RelativeLayout
    private lateinit var relative_date: RelativeLayout
    private lateinit var relative_email: RelativeLayout
    private lateinit var relative_website: RelativeLayout
    private lateinit var layout_add_to_calendar: LinearLayout
    private lateinit var ratingbar_event: ScaleRatingBar
    private lateinit var button_add_to_calendar: MyCustomButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_event_details, container, false)
        }

        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()
        initMap()
        return rootView
    }

    override fun onClick(view: View?) {

        when (view) {

            relative_location -> {

                changeBackground(1)
                val bundle = Bundle()
                bundle.putSerializable(Frag_Event_Directory.eventData, eventData)
                val locationFrg = Frag_Event_Location()
                locationFrg.arguments = bundle
              /*  (activity as Act_Home).loadFragmentwithAnim(locationFrg,
                        false, resources.getString(R.string.location), false, true)*/

                (activity as Act_Home).changeFragment(locationFrg,
                        false,
                        resources.getString(R.string.location),
                        false,
                        true,
                        true)

            }

            relative_call -> {
                try {
                    changeBackground(3)
                    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + eventData.phoneNumber)))
                } catch (e: ActivityNotFoundException) {
                    android.widget.Toast.makeText(activity, activity?.resources?.getString(R.string.no_app_found), android.widget.Toast.LENGTH_SHORT).show()
                }
            }

            relative_website -> {
                try {
                    changeBackground(4)
                    val i = Intent(Intent.ACTION_VIEW)

                    var url = eventData.websiteLink

                    if (!url.startsWith("https://") && !url.startsWith("http://")) {
                        url = "http://$url"
                    }
                    i.data = Uri.parse(url)
                    startActivity(i)
                } catch (e: ActivityNotFoundException) {
                    android.widget.Toast.makeText(activity, activity?.resources?.getString(R.string.no_app_found), android.widget.Toast.LENGTH_SHORT).show()
                }

            }

            relative_date -> {
                changeBackground(2)
            }

            relative_email -> {
                changeBackground(5)
                openGmail(activity!!, eventData.email)
            }

            button_add_to_calendar -> {
                callAddEventApi()
            }

        }
    }

    private fun callAddEventApi() {

        if (isNetWork(activity!!)) {
            ApiRequest(activity!!,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API).addEventIntoCalendar(auth_token, eventData.eventId.toString()),
                    ADD_EVENT, true, this)
        } else {
            Toast.makeText(activity, activity!!.resources.getString(R.string.internet_not_available), Toast.LENGTH_LONG).show()
        }

    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            ADD_EVENT -> {
                val commonResponse = apiResponseManager.response as CommonResponse
                if (commonResponse.statusCode == 200) {
                    addCalendarReminder()
                    appcontroller.getEvent_data().is_add_calender = 1
                    layout_add_to_calendar.visibility = View.GONE
                }

            }
        }
    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            showSnackBar(error_message)
        }
    }

    private fun addCalendarReminder() {

        val cal_end_date = Calendar.getInstance()
        val cal_start_date = Calendar.getInstance()

        val cr = activity?.contentResolver
        val EVENTS_URI = Uri.parse(getCalendarUriBase(true) + "events")

        val timeZone = TimeZone.getDefault()

        val sdf = SimpleDateFormat("yyyy-MM-dd h:m a", Locale.getDefault())

        try {
            cal_start_date.time = sdf.parse(eventData.eventStartDate + " " + eventData.eventStartTime)
            cal_end_date.time = sdf.parse(eventData.eventEndDate + " " + eventData.eventEndTime)

            var values = ContentValues()
            values.put(CalendarContract.Events.CALENDAR_ID, 1)
            values.put(CalendarContract.Events.TITLE, eventData.title + ": " + activity!!.resources.getString(R.string.app_name))
            values.put(CalendarContract.Events.DESCRIPTION, eventData.shortDescription)
            values.put(CalendarContract.Events.DTSTART, cal_start_date.timeInMillis)
            values.put(CalendarContract.Events.DTEND, cal_end_date.timeInMillis)
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.id)
            values.put(CalendarContract.Events.EVENT_LOCATION, eventData.location)
            values.put(CalendarContract.Events.HAS_ALARM, 1)
            val event = cr!!.insert(EVENTS_URI, values)

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

    }

    override fun initComponent() {
    }

    override fun initToolbar() {

        setHasOptionsMenu(true)
        (activity as Act_Home).setToolbarTitle(getString(R.string.event_directory), true, true)
        (activity as Act_Home).toolbar?.setNavigationOnClickListener {
            (activity as Act_Home).onBackFragmentHandling()
        }
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

    override fun initListeners() {
        relative_location.setOnClickListener(this)
        relative_date.setOnClickListener(this)
        relative_call.setOnClickListener(this)
        relative_website.setOnClickListener(this)
        relative_email.setOnClickListener(this)
        button_add_to_calendar.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {

        val bundle = arguments
        eventData = bundle?.getSerializable(Frag_Event_Directory.eventData) as Event.Data

        appcontroller.setEvent_data(bundle.getSerializable(Frag_Event_Directory.eventData) as Event.Data)
        Log.e(TAG, "eventData = $eventData")

        if (eventData.is_add_calender == 1) {
            layout_add_to_calendar.visibility = View.GONE
        } else {
            layout_add_to_calendar.visibility = View.VISIBLE
        }

        try {
            ratingbar_event.rating = eventData.rating_data.toFloat()
            text_rating.text = "Current Rating " + eventData.rating_data + " of out 5 stars"
        } catch (e: java.lang.NumberFormatException) {
            ratingbar_event.rating = 0.0F
            text_rating.text = "Current Rating 0 of out 5 stars"
        }


        ratingbar_event.isClickable = false
        text_event_name.text = eventData.title
        text_event_details.text = eventData.shortDescription
        text_location.text = eventData.location
        text_call.text = eventData.phoneNumber
        text_website.text = eventData.websiteLink
        text_overview.text = eventData.description

        Log.e("is_add_calender ", "is_add_calender == " + eventData.is_add_calender + "  " + eventData.title)

        Glide.with(appcontroller.getAppContext()).load(eventData.event_detail_image)
                .into(image_event)

        val dateTimeBuilder: StringBuilder = StringBuilder()
        dateTimeBuilder.append(formattedDateFromString("yyyy-MM-dd", "EEE d MMM", eventData.eventStartDate))
        dateTimeBuilder.append(" - ")
        dateTimeBuilder.append(formattedDateFromString("yyyy-MM-dd", "EEE d MMM", eventData.eventEndDate))
        dateTimeBuilder.append(" | ")
        dateTimeBuilder.append(eventData.eventStartTime)
        dateTimeBuilder.append(" " + eventData.eventEndTime)

        text_date.text = dateTimeBuilder.toString()
        text_full_date.text = dateTimeBuilder.toString()
        if (eventData.price == "0.00") {
            text_price.text = "FREE"
        } else {
            try {
                text_price.text = "£" + eventData.price
            } catch (e: NumberFormatException) {
                text_price.text = "£ 0.00"
            }

        }

        if (eventData.email != "") {
            relative_email.visibility = View.VISIBLE
            text_email.text = eventData.email
        } else {
            relative_email.visibility = View.GONE
        }
    }

    override fun initIDs(rootView: View) {
        image_event = rootView.findViewById(R.id.image_event)
        text_event_name = rootView.findViewById(R.id.text_event_name)
        text_call = rootView.findViewById(R.id.text_call)
        text_date = rootView.findViewById(R.id.text_date)
        text_full_date = rootView.findViewById(R.id.text_full_date)
        text_event_details = rootView.findViewById(R.id.text_event_details)
        text_location = rootView.findViewById(R.id.text_location)
        text_email = rootView.findViewById(R.id.text_email)
        text_overview = rootView.findViewById(R.id.text_overview)
        relative_location = rootView.findViewById(R.id.relative_location)
        relative_date = rootView.findViewById(R.id.relative_date)
        relative_call = rootView.findViewById(R.id.relative_call)
        relative_email = rootView.findViewById(R.id.relative_email)
        relative_website = rootView.findViewById(R.id.relative_website)
        layout_add_to_calendar = rootView.findViewById(R.id.layout_add_to_calendar)
        ratingbar_event = rootView.findViewById(R.id.ratingbar_event)
        text_rating = rootView.findViewById(R.id.text_rating)
        text_website = rootView.findViewById(R.id.text_website)
        text_price = rootView.findViewById(R.id.text_price)
        button_add_to_calendar = rootView.findViewById(R.id.button_add_to_calendar)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.profile, menu)
    }


    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear()

            val googlePlex = CameraPosition.builder()
                    .target(LatLng(eventData.latitude.toDouble(), eventData.longitude.toDouble()))
                    .zoom(14F)
                    .bearing(0f)
                    .tilt(45f)
                    .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 5000, null)

            mMap.addMarker(MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin_black))
                    .position(LatLng(eventData.latitude.toDouble(), eventData.longitude.toDouble()))
                    .title(eventData.location))
        }

    }

    fun changeBackground(no: Int) {

        when (no) {
            1 -> {
                relative_location.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_select_event)
                relative_date.background = ContextCompat.getDrawable(activity!!, R.drawable.two_side_border_event_detail)
                relative_call.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                relative_website.background = ContextCompat.getDrawable(activity!!, R.drawable.three_side_border_event_detail)
                relative_email.background = ContextCompat.getDrawable(activity!!, R.drawable.three_side_border_event_detail)
            }
            2 -> {
                relative_date.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_select_event)
                relative_call.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                relative_website.background = ContextCompat.getDrawable(activity!!, R.drawable.three_side_border_event_detail)
                relative_location.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                relative_email.background = ContextCompat.getDrawable(activity!!, R.drawable.three_side_border_event_detail)
            }
            3 -> {
                relative_call.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_select_event)
                relative_date.background = ContextCompat.getDrawable(activity!!, R.drawable.two_side_border_event_detail)
                relative_website.background = ContextCompat.getDrawable(activity!!, R.drawable.two_side_border_event_detail)
                relative_location.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                relative_email.background = ContextCompat.getDrawable(activity!!, R.drawable.three_side_border_event_detail)
            }
            4 -> {
                relative_website.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_select_event)
                relative_date.background = ContextCompat.getDrawable(activity!!, R.drawable.two_side_border_event_detail)
                relative_call.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                relative_location.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                relative_email.background = ContextCompat.getDrawable(activity!!, R.drawable.three_side_border_event_detail)
            }
            5 -> {
                relative_email.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_select_event)
                relative_location.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                relative_date.background = ContextCompat.getDrawable(activity!!, R.drawable.two_side_border_event_detail)
                relative_call.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                relative_website.background = ContextCompat.getDrawable(activity!!, R.drawable.three_side_border_event_detail)

            }
        }

    }

//    override fun onResume() {
//        super.onResume()
//        initMap()
//    }


}

