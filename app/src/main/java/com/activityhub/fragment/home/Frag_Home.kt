package com.activityhub.fragment.home


import android.app.Activity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.LinearLayout
import com.activityhub.R
import com.activityhub.activity.home.Act_Home
import com.activityhub.dialogue.CompletedEventDialog
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.fragment.education.Frag_Education_Category
import com.activityhub.utils.other.Common
import com.activityhub.utils.other.MyCustomButton
import kotlinx.android.synthetic.main.act_home.*
import kotlinx.android.synthetic.main.fragment_home.*


class Frag_Home : Frag_Base(), View.OnClickListener {

    var rootView: View? = null
    private lateinit var layout_directory: LinearLayout
    private lateinit var layout_calendar: LinearLayout
    private lateinit var layout_education: LinearLayout
    private lateinit var layout_result: LinearLayout
    private lateinit var button_view_directory: MyCustomButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false)
        }

        initComponent()
        initToolbar()
        initIDs(rootView!!)
        initListeners()
        initData()

        return rootView
    }

    override fun initComponent() {
    }

    override fun initToolbar() {
        (activity as Act_Home).setToolbarTitle("", false, false)
        setHasOptionsMenu(true)
    }

    override fun initListeners() {
        layout_directory.setOnClickListener(this)
        layout_calendar.setOnClickListener(this)
        layout_education.setOnClickListener(this)
        layout_result.setOnClickListener(this)
        button_view_directory.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun initIDs(rootView: View) {
        layout_directory = rootView.findViewById(R.id.layout_directory)
        layout_calendar = rootView.findViewById(R.id.layout_calendar)
        layout_education = rootView.findViewById(R.id.layout_education)
        layout_result = rootView.findViewById(R.id.layout_result)
        button_view_directory = rootView.findViewById(R.id.button_view_directory)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (!sessionManager[Common.COMPLETED_EVENT_DIALOG, true]) {
            openCompletedEventDialog()
        }

    }

    override fun onClick(view: View?) {
        when (view) {

            layout_directory -> {
                //(activity as Act_Home).navHomeActivity.getMenu().findItem(R.id.nav_directory).setChecked(true);

                (activity as Act_Home).navHomeActivity.menu.getItem(1).isChecked = true

                changeBackGroundColor(1)
                /*(activity as Act_Home).loadFragment(Frag_Event_Directory(),
                        true, resources.getString(R.string.event_directory), false, true)*/
                (activity as Act_Home).changeFragment(Frag_Event_Directory(),
                        true,
                        resources.getString(R.string.event_directory),
                        false,
                        true,
                        false)
            }

            layout_calendar -> {
                changeBackGroundColor(2)
                (activity as Act_Home).navHomeActivity.menu.getItem(2).isChecked = true

               /* (activity as Act_Home).loadFragment(Frag_Calender(),
                        true, resources.getString(R.string.calendar), false, true)*/

                (activity as Act_Home).changeFragment(Frag_Calender(),
                        true,
                        resources.getString(R.string.calendar),
                        false,
                        true,
                        false)
            }

            layout_education -> {
                changeBackGroundColor(3)
                (activity as Act_Home).changeFragment(Frag_Education_Category(),
                        false,
                        resources.getString(R.string.education),
                        false,
                        true,
                        true)
                /*  (activity as Act_Home).loadFragmentwithAnim(Frag_Education_Category(),
                          true, resources.getString(R.string.education), false, true)*/
            }

            layout_result -> {
                changeBackGroundColor(4)
               /* (activity as Act_Home).loadFragment(Frag_Result(),
                        true, resources.getString(R.string.result), false, true)*/
                (activity as Act_Home).changeFragment(Frag_Result(),
                        true,
                        resources.getString(R.string.result),
                        false,
                        true,
                        false)

            }

            button_view_directory -> {
                (activity as Act_Home).navHomeActivity.menu.getItem(1).isChecked = true
              /*  (activity as Act_Home).loadFragment(Frag_Event_Directory(),
                        true, resources.getString(R.string.event_directory), false, true)*/

                (activity as Act_Home).changeFragment(Frag_Event_Directory(),
                        true,
                        resources.getString(R.string.event_directory),
                        false,
                        true,
                        false)
            }


        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile_item -> {
                /*(activity as Act_Home).loadFragmentwithAnim(Frag_Account(),
                        false,
                        "",
                        false,
                        false)*/


                (activity as Act_Home).changeFragment(Frag_Account(),
                        false,
                        "",
                        false,
                        false,
                        false)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun changeBackGroundColor(position: Int) {
        when (position) {
            1 -> {
                layout_directory.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_blue_square)
                layout_calendar.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                layout_education.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                layout_result.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)

                ivHomeFragmentDirectory.setColorFilter(ContextCompat.getColor(activity!!, R.color.white))
                tvHomeFragmentDirectory.setTextColor(ContextCompat.getColor(activity!!, R.color.white))

                ivHomeFragmentCalender.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentCalender.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

                ivHomeFragmentEducation.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentEducation.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

                ivHomeFragmentResult.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentResults.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

            }
            2 -> {
                layout_directory.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                layout_calendar.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_blue_square)
                layout_education.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                layout_result.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)

                ivHomeFragmentDirectory.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentDirectory.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

                ivHomeFragmentCalender.setColorFilter(ContextCompat.getColor(activity!!, R.color.white))
                tvHomeFragmentCalender.setTextColor(ContextCompat.getColor(activity!!, R.color.white))

                ivHomeFragmentEducation.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentEducation.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

                ivHomeFragmentResult.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentResults.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

            }
            3 -> {
                layout_directory.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                layout_calendar.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                layout_education.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_blue_square)
                layout_result.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)

                ivHomeFragmentDirectory.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentDirectory.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

                ivHomeFragmentCalender.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentCalender.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

                ivHomeFragmentEducation.setColorFilter(ContextCompat.getColor(activity!!, R.color.white))
                tvHomeFragmentEducation.setTextColor(ContextCompat.getColor(activity!!, R.color.white))

                ivHomeFragmentResult.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentResults.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

            }
            4 -> {
                layout_directory.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                layout_calendar.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                layout_education.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_white_square_border)
                layout_result.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_blue_square)

                ivHomeFragmentDirectory.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentDirectory.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

                ivHomeFragmentCalender.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentCalender.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

                ivHomeFragmentEducation.setColorFilter(ContextCompat.getColor(activity!!, R.color.homeFrgClickBackground))
                tvHomeFragmentEducation.setTextColor(ContextCompat.getColor(activity!!, R.color.black))

                ivHomeFragmentResult.setColorFilter(ContextCompat.getColor(activity!!, R.color.white))
                tvHomeFragmentResults.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
            }
        }

    }

    private fun openCompletedEventDialog() {
        CompletedEventDialog(context as Activity, context as Activity)
    }


}

