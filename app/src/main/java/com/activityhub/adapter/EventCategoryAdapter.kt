package com.activityhub.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.activityhub.R
import com.activityhub.model.Event_Filter

class EventCategoryAdapter(private val mContext: Context, @param:LayoutRes private val mResource: Int, private val categoryList: List<Event_Filter.AllCategory>) : ArrayAdapter<Event_Filter.AllCategory>(mContext, mResource, 0, categoryList) {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, parent)
    }

    private fun createItemView(position: Int, parent: ViewGroup): View {
        val view = mInflater.inflate(mResource, parent, false)

        val tvRowItemDieses = view.findViewById<TextView>(R.id.tvRowItemDieses)
        tvRowItemDieses.text = categoryList[position].categoryName
        return view
    }
}
