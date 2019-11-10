package com.activityhub.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.fragment.other.Frag_Connect_Device
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_item_manage_devices.view.*


class ManageDeviceAdapter(var context: Context, var iconList: List<Int>,
                          var deviceList: List<String>) : RecyclerView.Adapter<ManageDeviceAdapter.SubViewHolder>() {
    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        holder.bind(position, deviceList, iconList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder =
            SubViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_item_manage_devices,
                    parent, false))

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = iconList.size

    inner class SubViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int, deviceList: List<String>, iconList: List<Int>) {

            Glide.with(context).load(iconList[position])
                    .into(itemView.ivRowItemManageDevice)
            itemView.tvRowItemManageDeviceName?.text = deviceList[position]
            itemView.tvRowItemManageDeviceConnection?.text = deviceList[position]

            itemView.layout_services.setOnClickListener {
                /* (context as Act_Home).loadFragmentwithAnim(Frag_Connect_Device(),
                         false, deviceList[position], true, false)*/

                (context as Act_Home).changeFragment(Frag_Connect_Device(),
                        false,
                        deviceList[position],
                        true,
                        false,
                        true)
            }

        }

    }


}





