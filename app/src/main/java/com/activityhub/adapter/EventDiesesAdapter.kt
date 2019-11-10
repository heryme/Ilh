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

class EventDiesesAdapter(private val mContext: Context, @param:LayoutRes private val mResource: Int,
                         private val diesesList: List<Event_Filter.AllDiese>) :
        ArrayAdapter<Event_Filter.AllDiese>(mContext, mResource, 0, diesesList) {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?,
                                 parent: ViewGroup): View {

        return createItemView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = mInflater.inflate(mResource, parent, false)

        val tvRowItemDieses = view.findViewById<View>(R.id.tvRowItemDieses) as TextView
        tvRowItemDieses.text = diesesList[position].dieasesName
        return view
    }
}
