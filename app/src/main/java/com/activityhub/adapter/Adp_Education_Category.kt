package com.activityhub.adapter

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.app.AppController
import com.activityhub.fragment.education.Frag_Education_Category
import com.activityhub.fragment.education.Frag_Education_SubCategory
import com.activityhub.model.Education_Category
import com.activityhub.utils.other.Common.DATA
import com.activityhub.utils.other.Common.ID
import com.activityhub.utils.other.Common.NAME
import com.activityhub.utils.other.loadImage
import kotlinx.android.synthetic.main.item_education_category.view.*
import java.util.HashMap


class Adp_Education_Category(val items: List<Education_Category.Data>?, val context: Activity,
                             val frag_context: Frag_Education_Category) : RecyclerView.Adapter<ViewHolder3>() {

    private lateinit var appController: AppController
    var selected_position = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder3 {
        appController = context.application as AppController
        return ViewHolder3(LayoutInflater.from(context).inflate(R.layout.item_education_category, p0, false))
    }

    override fun onBindViewHolder(holder: ViewHolder3, position: Int) {

        holder.text_category!!.text = items!![position].categoryName

        if (items[position].categoryImage != "") {
            loadImage(items[position].categoryImage, appController.getAppContext()!!, holder.image_category, R.drawable.ic_svg_white_calendar, R.drawable.ic_svg_white_calendar)
        }

//        if (position == selected_position) {
//            holder.layout_education.setBackgroundResource(R.drawable.bg_blue_square)
//        } else {
//            holder.layout_education.setBackgroundResource(R.drawable.bg_white_square_border)
//        }

        holder.layout_education.setOnClickListener {
            //            selected_position = position
//            notifyDataSetChanged()
            val bundle = Bundle()
            val map = HashMap<String, String>()
            map[ID] = items[position].id.toString()
            map[NAME] = items[position].categoryName
            bundle.putSerializable(DATA, map)
            val frag_education_subcategory = Frag_Education_SubCategory()
            frag_education_subcategory.arguments = bundle
            (context as Act_Home).changeFragment(frag_education_subcategory,
                    false,
                    items[position].categoryName,
                    false,
                    true,
                    true)
            /*(context as Act_Home).loadFragmentwithAnim(frag_education_subcategory,
                    false, items[position].categoryName, true, true)*/
        }


    }

    override fun getItemCount(): Int {
        return items!!.size
    }

}

class ViewHolder3(view: View) : RecyclerView.ViewHolder(view) {
    val text_category = view.text_category
    val image_category = view.image_category
    val layout_education = view.layout_education
}