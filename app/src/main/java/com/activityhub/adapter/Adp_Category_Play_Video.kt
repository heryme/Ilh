package com.activityhub.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.activityhub.R
import com.activityhub.app.AppController
import com.activityhub.fragment.education.Frag_Category_Play_Video
import com.activityhub.model.Category_Video_List
import com.activityhub.utils.other.loadImage
import kotlinx.android.synthetic.main.item_category_video_list.view.*


class Adp_Category_Play_Video(val items: List<Category_Video_List.Data.VideoArray>?, var video_position: Int,
                              val context: Activity, val activity: Frag_Category_Play_Video) : RecyclerView.Adapter<ViewHolder6>() {

    protected lateinit var appcontroller: AppController

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder6 {
        appcontroller = context.application as AppController
        return ViewHolder6(LayoutInflater.from(appcontroller.getAppContext()).inflate(R.layout.item_category_video_list, p0, false))
    }

    override fun onBindViewHolder(holder: ViewHolder6, position: Int) {

        holder.text_video_question!!.text = items!![position].videoName
        holder.text_question_desc.text = items[position].videoDescription
        holder.text_video_length.text = items[position].time

        if (items[position].video_thumb != "") {
            loadImage(items[position].video_thumb, appcontroller.getAppContext()!!, holder.image_video, R.drawable.ic_video, R.drawable.ic_video)
        }

        if (video_position == position) {
            holder.layout_video.setBackgroundResource(R.drawable.bg_grey_square_border)
            holder.image_video_playing.visibility = View.VISIBLE
        } else {
            holder.layout_video.setBackgroundResource(R.drawable.bg_white_square_border)
            holder.image_video_playing.visibility = View.GONE
        }

        holder.layout_video.setOnClickListener {
            video_position = position
            activity.initVideo(position)
        }


    }

    override fun getItemCount(): Int {
        return items!!.size
    }

}

class ViewHolder6(view: View) : RecyclerView.ViewHolder(view) {
    val text_video_question = view.text_video_question
    val text_question_desc = view.text_question_desc
    val image_video = view.image_video
    val text_video_length = view.text_video_length
    val layout_video = view.layout_video
    val image_video_playing = view.image_video_playing
}