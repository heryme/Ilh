package com.activityhub.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.activityhub.R
import com.activityhub.model.Get_Intrest

import kotlinx.android.synthetic.main.item_row_intrests_view.view.*


/**
 * Created by Admin on 6-11-2017.
 */
class IntrestsAdapter(var context: Context,
                      var interestList: List<Get_Intrest.Data>,
                      var onItemCheckListener: OnItemCheckListener) : RecyclerView.Adapter<IntrestsAdapter.SubViewHolder>() {


    interface OnItemCheckListener {
        fun onItemCheck(item: Get_Intrest.Data)
        fun onItemUncheck(item: Get_Intrest.Data)
    }

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        holder?.bind(position, interestList, onItemCheckListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            SubViewHolder = SubViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.item_row_intrests_view, parent, false))

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = interestList.size
    inner class SubViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int, interestList: List<Get_Intrest.Data>,
                 onItemCheckListener: OnItemCheckListener) {

            val interest = interestList[position]

            itemView?.tvItemRowName?.text = interestList[position].intrestName

            //Set State If Already Selected Or Not
            if(interestList[position].status.equals("true")) {
                itemView?.cbItemRow?.isChecked = true
                onItemCheckListener.onItemCheck(interest)
            }else {
                itemView?.cbItemRow?.isChecked = false
                onItemCheckListener.onItemUncheck(interest)
            }

            itemView?.cbItemRow!!.setChecked(itemView?.cbItemRow.isChecked())
            itemView?.cbItemRow.setOnClickListener(View.OnClickListener {
                if (itemView?.cbItemRow.isChecked()) {
                    //Toast("Check", true, context)
                    onItemCheckListener.onItemCheck(interest)
                } else {
                    //Toast("Un Check", true, context)
                    onItemCheckListener.onItemUncheck(interest)
                }
            })


        }


    }


}





