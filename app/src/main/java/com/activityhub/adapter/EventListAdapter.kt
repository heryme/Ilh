package com.activityhub.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.activityhub.R
import com.activityhub.app.AppController
import com.activityhub.utils.extension.formattedDateFromString
import com.activityhub.fragment.home.Frag_Event_Directory
import com.activityhub.model.Event
import com.activityhub.utils.other.loadImage
import kotlinx.android.synthetic.main.row_item_event_list.view.*


class EventListAdapter(var context: Activity, var activity: Frag_Event_Directory, var eventList: List<Event.Data>)
    : RecyclerView.Adapter<EventListAdapter.SubViewHolder>() {

    private val TAG = EventListAdapter::class.java.name

    protected lateinit var appcontroller: AppController

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        holder.bind(position, eventList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            SubViewHolder = SubViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item_event_list, parent, false))

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = eventList.size

    inner class SubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int, event_list: List<Event.Data>) {

            appcontroller = context.application as AppController


            Log.e("SubViewHolder", "SubViewHolder = " + event_list[position].price.toString())

            val dateTimeBuilder: StringBuilder? = StringBuilder()
            dateTimeBuilder?.append(formattedDateFromString("yyyy-MM-dd", "EEE d MMM", eventList[position].eventStartDate))
            dateTimeBuilder?.append(" - ")
            dateTimeBuilder?.append(formattedDateFromString("yyyy-MM-dd", "EEE d MMM", eventList[position].eventEndDate))

            dateTimeBuilder?.append(" | ")

            dateTimeBuilder?.append(eventList[position].eventStartTime)
            if (eventList[position].eventStartTime != "" && eventList[position].eventEndTime != "") {
                dateTimeBuilder?.append(" - ")
            }
            dateTimeBuilder?.append(eventList[position].eventEndTime)
/*
            Glide.with(context).load(eventList[position].eventImage).placeholder(R.drawable.ic_shoes_green)
                    .into(itemView.ivRowItemEventListEvent);*/
            loadImage(eventList[position].eventImage, appcontroller.getAppContext()!!,itemView.ivRowItemEventListEvent,R.drawable.ic_shoes_green,R.drawable.ic_shoes_green)

            itemView.tvRowItemEventListCategoryName?.text = event_list[position].title
            itemView.tvRowItemEventListLocation?.text = event_list[position].location
            itemView.tvRowItemEventListDate?.text = dateTimeBuilder.toString()
            if (event_list[position].price == "0.00") {
                itemView.tvRowItemEventListPrice?.text = "FREE"
            } else {
                try {
                    itemView.tvRowItemEventListPrice?.text = "£" + event_list[position].price
                } catch (e: NumberFormatException) {
                    itemView.tvRowItemEventListPrice?.text = "£ 0.00"
                }

            }

            Log.e("is_add_calender", " is_add_calender = " + event_list[position].is_add_calender)

            itemView.layout_event_directory.setOnClickListener {
                activity.openEventDetail(position)
            }
        }
    }

}





