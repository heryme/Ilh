package com.activityhub.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.activityhub.R
import com.activityhub.model.Assesment
import kotlinx.android.synthetic.main.row_item_assessment_radio_subview.view.*

class RadioTypeAdapter(var context: Context, var radioList: List<Assesment.Data.MainForm.Question.answerData>) :
        RecyclerView.Adapter<RadioTypeAdapter.SubViewHolder>() {


    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        holder.bind(position, radioList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder =
            SubViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item_assessment_radio_subview, parent, false))

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = radioList.size

    inner class SubViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int, radioList: List<Assesment.Data.MainForm.Question.answerData>) {
            itemView.radioRowSubView?.text = radioList[position].formQuestionValue
        }
    }

}





