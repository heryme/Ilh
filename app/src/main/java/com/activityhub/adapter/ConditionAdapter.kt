package com.activityhub.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView

import com.activityhub.R
import com.activityhub.model.Profile_Condition

import java.util.ArrayList

class ConditionAdapter(private val mContext: Context, private val mLayoutResourceId: Int, departments: ArrayList<Profile_Condition.Data>) : ArrayAdapter<Profile_Condition.Data>(mContext, mLayoutResourceId, departments) {

    private val TAG = ConditionAdapter::class.java.simpleName
    private val mDepartments: MutableList<Profile_Condition.Data>
    private val mDepartmentsAll: List<Profile_Condition.Data>

    init {
        this.mDepartments = ArrayList(departments)
        this.mDepartmentsAll = ArrayList<Profile_Condition.Data>(departments)
    }

    override fun getCount(): Int {
        return mDepartments.size
    }

    override fun getItem(position: Int): Profile_Condition.Data? {
        return mDepartments[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        try {
            if (convertView == null) {
                val inflater = (mContext as Activity).layoutInflater
                convertView = inflater.inflate(mLayoutResourceId, parent, false)
            }
            val department = getItem(position)
            val name = convertView!!.findViewById<View>(R.id.tvRowItemConditionName) as TextView
            name.text = department!!.conditionName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView!!
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun convertResultToString(resultValue: Any): String {
                return (resultValue as Profile_Condition.Data).conditionName
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                Log.e(TAG, "mDepartmentsAll Sizee--->" + mDepartmentsAll.size)
                Log.e(TAG, "mDepartments Sizee--->" + mDepartments.size)

                val filterResults = FilterResults()
                val departmentsSuggestion = ArrayList<Profile_Condition.Data>()
                if (constraint != null) {
                    for (department in mDepartmentsAll) {
                        if (department.conditionName.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            departmentsSuggestion.add(department)
                        }
                    }
                    filterResults.values = departmentsSuggestion
                    filterResults.count = departmentsSuggestion.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mDepartments.clear()
                if (results != null && results.count > 0) {
                    for (`object` in results.values as List<*>) {
                        if (`object` is Profile_Condition.Data) {
                            mDepartments.add(`object`)
                        }
                    }
                    notifyDataSetChanged()
                } else if (constraint == null) {
                    mDepartments.addAll(mDepartmentsAll)
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}

