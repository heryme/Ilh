package com.activityhub.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.activityhub.R
import kotlinx.android.synthetic.main.item_row_notification_details.view.*

class NotificationDetailsAdapter(var context: Context, var detailsList: List<String>) :
        RecyclerView.Adapter<NotificationDetailsAdapter.SubViewHolder>() {

    var selectedPosition: Int = -1

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        holder.bind(position, detailsList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder =
            SubViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row_notification_details, parent, false))

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = detailsList.size

    inner class SubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int, detailsList: List<String>) {

            itemView.tvNotificationTitle?.text = detailsList[position]

            itemView.llMainNotificationDetailsAdapter.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
            }

            if (selectedPosition == position) {
                itemView.ivCheck.visibility = View.VISIBLE
                itemView.ivCheck.setImageResource(R.drawable.ic_svg_check_mark_black)
            } else {
                itemView.ivCheck.visibility = View.INVISIBLE
            }

        }
    }

}





