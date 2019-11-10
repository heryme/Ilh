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
import com.activityhub.fragment.education.Frag_Category_Video_List
import com.activityhub.fragment.education.Frag_Education_SubCategory
import com.activityhub.model.Education_Subcategory
import com.activityhub.utils.other.Common.DATA
import com.activityhub.utils.other.Common.ID
import com.activityhub.utils.other.Common.NAME
import com.activityhub.utils.other.Common.SUB_ID
import com.activityhub.utils.other.loadImage
import kotlinx.android.synthetic.main.item_education_subcategory.view.*


class Adp_Education_Subcategory(val items: List<Education_Subcategory.Data>?, val category_id: String, val context: Activity,
                                val frag_context: Frag_Education_SubCategory) : RecyclerView.Adapter<ViewHolder4>() {

    protected lateinit var appcontroller: AppController

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder4 {
        appcontroller = context.application as AppController
        return ViewHolder4(LayoutInflater.from(context).inflate(R.layout.item_education_subcategory, p0, false))
    }

    override fun onBindViewHolder(holder: ViewHolder4, position: Int) {
        appcontroller = context.application as AppController

        holder.text_category!!.text = items!![position].subCategoryName

        if (items[position].subCategoryImage != "") {
            loadImage(items[position].subCategoryImage, appcontroller.getAppContext()!!, holder.image_category, R.drawable.ic_svg_white_calendar, R.drawable.ic_svg_white_calendar)
        }

        holder.text_category_description.text = items[position].subCategoryDescription

        holder.layout_education.setOnClickListener {

            val bundle = Bundle()
            val map = HashMap<String, String>()
            map[ID] = category_id
            map[SUB_ID] = items[position].id.toString()
            map[NAME] = items[position].subCategoryName
            bundle.putSerializable(DATA, map)
            val frag_education_subcategory = Frag_Category_Video_List()
            frag_education_subcategory.arguments = bundle

            (context as Act_Home).changeFragment(frag_education_subcategory,
                    false,
                    items[position].subCategoryName,
                    false,
                    true,
                    true)
            /*(context as Act_Home).loadFragmentwithAnim(frag_education_subcategory,
                    false, items[position].subCategoryName, true, true)*/
        }

    }

    override fun getItemCount(): Int {
        return items!!.size
    }

}

class ViewHolder4(view: View) : RecyclerView.ViewHolder(view) {
    val text_category = view.text_category
    val text_category_description = view.text_category_description
    val image_category = view.image_category
    val layout_education = view.layout_education
}