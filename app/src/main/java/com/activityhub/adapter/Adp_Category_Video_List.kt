package com.activityhub.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.app.AppController
import com.activityhub.fragment.education.Frag_Category_Play_Video
import com.activityhub.model.Category_Video_List
import com.activityhub.utils.other.loadImage
import kotlinx.android.synthetic.main.item_category_video_list.view.*


class Adp_Category_Video_List(val items: List<Category_Video_List.Data.VideoArray>?, val context: Context,
                              val activity: Activity) : RecyclerView.Adapter<ViewHolder5>() {

    protected lateinit var appcontroller: AppController

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder5 {
        appcontroller = activity.application as AppController
        return ViewHolder5(LayoutInflater.from(context).inflate(R.layout.item_category_video_list, p0, false))
    }

    override fun onBindViewHolder(holder: ViewHolder5, position: Int) {

        holder.text_video_question!!.text = items!![position].videoName
        holder.text_question_desc.text = items[position].videoDescription
        holder.text_video_length.text = items[position].time

        if (items[position].video_thumb != "") {
            loadImage(items[position].video_thumb, appcontroller.getAppContext()!!, holder.image_video, R.drawable.ic_video, R.drawable.ic_video)
        }

        holder.layout_video.setOnClickListener {
            appcontroller.setVideo_data(items, position)
            (context as Act_Home).changeFragment(Frag_Category_Play_Video(),
                    false,
                    "",
                    false,
                    true,
                    true)
            /* (context as Act_Home).loadFragmentwithAnim(Frag_Category_Play_Video(),
                     false, "", true, true)*/
        }
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

}

class ViewHolder5(view: View) : RecyclerView.ViewHolder(view) {
    val text_video_question = view.text_video_question
    val text_question_desc = view.text_question_desc
    val image_video = view.image_video
    val text_video_length = view.text_video_length
    val layout_video = view.layout_video
}