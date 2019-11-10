package com.activityhub.adapter


import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.activityhub.R
import com.activityhub.fragment.home.Frag_Calender
import com.activityhub.model.CommonResponse
import com.activityhub.model.Event
import com.activityhub.restapi.ApiInitialize
import com.activityhub.restapi.ApiRequest
import com.activityhub.restapi.ApiResponseInterface
import com.activityhub.restapi.ApiResponseManager
import com.activityhub.utils.extension.Toast
import com.activityhub.utils.extension.formattedDateFromString
import com.activityhub.utils.extension.isNetWork
import com.activityhub.utils.other.DELETE_EVENT
import kotlinx.android.synthetic.main.row_item_user_event.view.*

class UserEventAdapter(var context: Context, var activity: Frag_Calender,
                       var userEventList: ArrayList<Event.Data>,
                       var auth_token: String,
                       var callBackDeletedEvent: DeleteEvent) : RecyclerView.Adapter<UserEventAdapter.SubViewHolder>(), ApiResponseInterface {

    private val TAG = UserEventAdapter::class.java.name
    private var mPosition: Int = 0

    public interface DeleteEvent {
        fun getDeletedEvent()
    }

    private var mCallback: DeleteEvent? = null

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        this.mCallback = callBackDeletedEvent
        holder.bind(position, userEventList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder =
            SubViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item_user_event,
                    parent, false))

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = userEventList.size

    inner class SubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int, userEventList: List<Event.Data>) {

            itemView.tvEventTitle.text = userEventList[position].title

            //Date Split And Set -DD
            val date = userEventList[position].eventStartDate
            val separated = date.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (separated.size == 3) {
                itemView.tvDate.text = separated[2]
            }


            val dateTimeBuilder: StringBuilder? = StringBuilder()
            dateTimeBuilder?.append(formattedDateFromString("yyyy-MM-dd", "EEE d MMM", userEventList[position].eventStartDate))
            dateTimeBuilder?.append(" - ")
            dateTimeBuilder?.append(formattedDateFromString("yyyy-MM-dd", "EEE d MMM", userEventList[position].eventEndDate))

            dateTimeBuilder?.append(" | ")

            dateTimeBuilder?.append(userEventList[position].eventStartTime)
            if (userEventList[position].eventStartTime != "" && userEventList[position].eventEndTime != "") {
                dateTimeBuilder?.append(" - ")
            }
            dateTimeBuilder?.append(userEventList[position].eventEndTime)

            itemView.tvTime.text = dateTimeBuilder.toString()


            /* val time = StringBuilder()
            time.append(userEventList[position].eventStartTime + " - " + userEventList[position].eventEndTime)
            itemView.tvTime.text = time.toString()*/

            itemView.tvDay.text = formattedDateFromString("yyyy-MM-dd", "EEE", userEventList[position].eventStartDate)

            itemView.layout_delete.setOnClickListener {
                mPosition = position
                callDeleteEvent(userEventList[position].eventId.toString())
            }

            itemView.layout_event.setOnClickListener {
                activity.openEventDetail(position)
            }

        }
    }

    private fun callDeleteEvent(eventId: String) {
        if (isNetWork(context)) {
            Log.e(TAG, "User Session--->$auth_token")
            Log.e(TAG, "Event Id --->$eventId")
            ApiRequest(context as Activity,
                    ApiInitialize.initialize(ApiInitialize.MAIN_URL_API)
                            .deleteEvent(auth_token, eventId), DELETE_EVENT, true, this)
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {
            DELETE_EVENT -> {
                val commonResponse = apiResponseManager.response as CommonResponse
                Log.e(TAG, "Delete User Event Response--->$commonResponse")
                if (commonResponse.statusCode == 200) {
                    Toast(commonResponse.message, true, context)
                    notifyItemRemoved(mPosition)
                    userEventList.removeAt(mPosition)
                    mCallback?.getDeletedEvent()
                    Log.e(TAG, "userEventList Size--->" + userEventList.size)
                }

            }
        }
    }

//    private fun DeleteCalendarEntry(entryID: Int): Int {
//        var iNumRowsDeleted = 0
//
//        val eventsUri = Uri.parse(getCalendarUriBase(true) + "events")
//        val eventUri = withAppendedId(eventsUri, entryID)
//        iNumRowsDeleted = context.contentResolver.delete(eventUri, null, null)
//
//        Log.e("DELETED", "Deleted $iNumRowsDeleted calendar entry.")
//
//        return iNumRowsDeleted
//    }

//    private fun deleteEvent(resolver: ContentResolver, eventsUri: Uri, calendarId: Int) {
//        val cursor: Cursor = resolver.query(eventsUri, arrayOf("_id"), "calendar_id=$calendarId", null, null)
//
//        while (cursor.moveToNext()) {
//            val eventId = cursor.getLong(cursor.getColumnIndex("_id"))
//            resolver.delete(withAppendedId(eventsUri, eventId), null, null)
//        }
//        cursor.close()
//    }

    override fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean) {
        if (!error) {
            android.widget.Toast.makeText(context, error_message, android.widget.Toast.LENGTH_LONG).show()
        }
    }
}





