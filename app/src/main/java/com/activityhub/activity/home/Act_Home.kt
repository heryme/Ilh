package com.activityhub.activity.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import com.activityhub.R
import com.activityhub.activity.common.BaseActivity
import com.activityhub.dialogue.AddCalendarEvent
import com.activityhub.fragment.auth.Frag_Account
import com.activityhub.fragment.auth.Frag_Update_Email
import com.activityhub.fragment.auth.Frag_Update_Password
import com.activityhub.fragment.education.Frag_Category_Play_Video
import com.activityhub.fragment.education.Frag_Category_Video_List
import com.activityhub.fragment.education.Frag_Education_Category
import com.activityhub.fragment.education.Frag_Education_SubCategory
import com.activityhub.fragment.event.Frag_Event_Details
import com.activityhub.fragment.event.Frag_Event_Filter
import com.activityhub.fragment.event.Frag_Event_Location
import com.activityhub.fragment.home.Frag_Calender
import com.activityhub.fragment.home.Frag_Event_Directory
import com.activityhub.fragment.home.Frag_Home
import com.activityhub.fragment.home.Frag_Result
import com.activityhub.fragment.other.Frag_Notification
import com.activityhub.fragment.other.Frag_Notification_Details
import com.activityhub.interfaces.CommonCallBack
import com.activityhub.services.ManageAppComponants
import com.activityhub.utils.extension.disableShiftMode
import com.activityhub.utils.other.Common.GO_FROM_USER_EVENT_LIST
import com.activityhub.utils.other.MESSAGE
import com.activityhub.utils.other.setFontTypeFace
import kotlinx.android.synthetic.main.act_home.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class Act_Home : BaseActivity() {

    private val TAG = "HOME"
    lateinit var dialogue: Dialog
    var toolbar: Toolbar? = null
    lateinit var fragment: Fragment
    var doubleBackToExitPressedOnce = false
    var current_title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_home)

        initIDs()
        initToolbar()
        initListeners()
        initData()
        initComponent()
    }

    private fun initIDs() {
        toolbar = findViewById(R.id.toolbar)
    }


    override fun initComponent() {
        /*loadFragment(Frag_Home(),
                false, "",
                false, false)*/

        changeFragment(Frag_Home(),
                true,
                "",
                false,
                false,
                false)

        startService(Intent(this@Act_Home, ManageAppComponants::class.java))
    }

    override fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun initListeners() {
        navHomeActivity.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        disableShiftMode(navHomeActivity)
    }

    override fun initData() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val fragment: Fragment
            when (item.itemId) {
                R.id.nav_home -> {
                    //tvbarHomeActivityToolbarTitle!!.setText(resources.getString(R.string.ilhp))
                    fragment = Frag_Home()
                    //loadFragment(fragment, true, "", false, false)
                    changeFragment(fragment, true, "", false, false, false)
                    return true
                }
                R.id.nav_directory -> {
                    // tvbarHomeActivityToolbarTitle!!.setText(resources.getString(R.string.directory))
                    fragment = Frag_Event_Directory()/*DirectioryFragment()*/
                    //loadFragment(fragment, true, resources.getString(R.string.event_directory), false, true)
                    changeFragment(fragment, true, "", false, false, false)
                    return true
                }
                R.id.nav_calender -> {
                    //tvbarHomeActivityToolbarTitle!!.setText(resources.getString(R.string.calender))
                    fragment = Frag_Calender()
                    changeFragment(fragment, true, "", false, false, false)
                    //loadFragment(fragment, true, resources.getString(R.string.calender), false, true)
                    return true
                }
                R.id.nav_goals -> {
                    // tvbarHomeActivityToolbarTitle!!.setText(resources.getString(R.string.goals))
                    fragment = Frag_Result()
                    changeFragment(fragment, true, "", false, false, false)
                    //loadFragment(fragment, true, resources.getString(R.string.result), false, true)
                    return true
                }
            }
            return false
        }
    }

    fun loadFragment(fragment: Fragment, isClearBackStack: Boolean, title: String, isShowBackButton: Boolean, isBlackTitle: Boolean) {

        this.fragment = fragment

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        //fragmentTransaction.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right)
        fragmentTransaction.replace(R.id.frameContainerHomeActivity, fragment, fragment.javaClass.simpleName)

        if (isClearBackStack) {
            val fm = supportFragmentManager
            for (i in 0 until fm.backStackEntryCount) {
                Log.e(TAG, "Back Stack Entry--->" + supportFragmentManager.backStackEntryCount)
                //Clear Fragment Back Stack
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }

        val backStateName = fragment.javaClass.simpleName
        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
            fragmentTransaction.commit()
            Log.e(TAG, "BACKSTACK COUNT =" + supportFragmentManager.backStackEntryCount)
        }
        tvbarHomeActivityToolbarTitle?.text = title

        when {
            isShowBackButton -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                toolbar?.setNavigationIcon(R.drawable.ic_left_arrow)
                tvbarHomeActivityToolbarTitle!!.setTextColor(ContextCompat.getColor(mActivity, R.color.black))
                tvbarHomeActivityToolbarTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_size_14))
                setFontTypeFace(getString(R.string.font_regular), tvbarHomeActivityToolbarTitle, assets)
                tvbarHomeActivityToolbarTitle.background = null

            }
            isBlackTitle -> {
                tvbarHomeActivityToolbarTitle!!.setTextColor(ContextCompat.getColor(mActivity, R.color.black))
                tvbarHomeActivityToolbarTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_size_14))
                setFontTypeFace(getString(R.string.font_regular), tvbarHomeActivityToolbarTitle, assets)
                tvbarHomeActivityToolbarTitle.background = null
            }
            else -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                tvbarHomeActivityToolbarTitle.background = ContextCompat.getDrawable(mActivity, R.drawable.ic_activityhub_green)
            }
        }
    }

    fun loadFragmentwithAnim(fragment: Fragment, isClearBackStack: Boolean, title: String, isShowBackButton: Boolean, isBlackTitle: Boolean) {

        this.fragment = fragment

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right)

        fragmentTransaction.replace(R.id.frameContainerHomeActivity, fragment, fragment.javaClass.simpleName)

        if (isClearBackStack) {
            val fm = supportFragmentManager
            for (i in 0 until fm.backStackEntryCount) {
                Log.e(TAG, "Back Stack Entry--->" + fragmentManager.backStackEntryCount)
                //Clear Fragment Back Stack
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }

        val backStateName = fragment.javaClass.simpleName
        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) {
            fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
            fragmentTransaction.commit()
            //tvbarHomeActivityToolbarTitle?.setText(title)
            Log.e(TAG, "BACKSTACK COUNT =" + fragmentManager.backStackEntryCount)
        }
        tvbarHomeActivityToolbarTitle?.text = title

        when {
            isShowBackButton -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                toolbar?.setNavigationIcon(R.drawable.ic_left_arrow)
                tvbarHomeActivityToolbarTitle!!.setTextColor(ContextCompat.getColor(mActivity, R.color.black))
                tvbarHomeActivityToolbarTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_size_14))
                setFontTypeFace(getString(R.string.font_regular), tvbarHomeActivityToolbarTitle, assets)
                tvbarHomeActivityToolbarTitle.background = null
            }
            isBlackTitle -> {
                tvbarHomeActivityToolbarTitle!!.setTextColor(ContextCompat.getColor(mActivity, R.color.black))
                tvbarHomeActivityToolbarTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_size_14))
                setFontTypeFace(getString(R.string.font_regular), tvbarHomeActivityToolbarTitle, assets)
                tvbarHomeActivityToolbarTitle.background = null
            }
            else -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                tvbarHomeActivityToolbarTitle.background = ContextCompat.getDrawable(mActivity, R.drawable.ic_activityhub_green)
            }
        }
    }

    fun onBackFragmentHandling() {
        Log.e(TAG, "On Back Fragment Count " + supportFragmentManager.backStackEntryCount)

        if (supportFragmentManager.backStackEntryCount == 1) {

            if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true

                android.widget.Toast.makeText(this, this.getString(R.string.msg_app_exit), android.widget.Toast.LENGTH_SHORT).show()

                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)

            } else {
                finishAffinity()
            }

        } else if (supportFragmentManager.backStackEntryCount > 1) {

            Log.e(TAG, "Before popBackStack--->" + supportFragmentManager.backStackEntryCount)
            supportFragmentManager.popBackStackImmediate()
            Log.e(TAG, "After popBackStack--->" + supportFragmentManager.backStackEntryCount)

            Log.e(TAG, "Fragment List--->" + supportFragmentManager.fragments)
            Log.e(TAG, "Fragment Name--->" + supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1))

            val frag = supportFragmentManager.findFragmentByTag(supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1).tag)

            if (frag is Frag_Event_Directory) {
                changeToolbarTitle(resources.getString(R.string.event_directory), 1)
            } else if (frag is Frag_Event_Details) {
                changeToolbarTitle(resources.getString(R.string.event_directory), 2)
            } else if (frag is Frag_Home || frag is Frag_Account) {
                changeToolbarTitle("", 3)
            } else if (frag is Frag_Notification) {
                changeToolbarTitle(resources.getString(R.string.notification), 2)
            } else if (frag is Frag_Calender) {
                changeToolbarTitle(resources.getString(R.string.calendar), 1)
            }else if(frag is Frag_Education_Category) {
                changeToolbarTitle(resources.getString(R.string.education), 1)
            }

        }/* else {
            Log.e(TAG, "Back Stack else")
            val fragment = supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName)
            if (fragment is Frag_Event_Details && sessionManager[GO_FROM_USER_EVENT_LIST, true] &&
                    fragment.isAdded && fragment.isVisible) {
                if (appcontroller.getEvent_data().is_add_calender == 1) {
                    loadFragment(Frag_Event_Directory(), true, resources.getString(R.string.event_directory), false, true)
                } else {
                    addEventCalendarDialogue()
                }

            } else {
                Log.e(TAG, "LOL HERE")
                super.onBackPressed()
            }
        }*/
    }

    fun changeFragment(mFragment: Fragment?,
                       isClearBackStack:
                       Boolean,
                       title: String,
                       isShowBackButton:
                       Boolean,
                       isBlackTitle: Boolean,
                       isAnimation: Boolean) {
        try {
            var strFragmentName = ""

            when (mFragment) {
                is Frag_Home -> strFragmentName = Frag_Home::class.java.name
                is Frag_Calender -> strFragmentName = Frag_Calender::class.java.name
                is Frag_Event_Directory -> strFragmentName = Frag_Event_Directory::class.java.name
                is Frag_Result -> strFragmentName = Frag_Result::class.java.name
                is Frag_Account -> strFragmentName = Frag_Account::class.java.name
                is Frag_Update_Email -> strFragmentName = Frag_Update_Email::class.java.name
                is Frag_Education_Category -> strFragmentName = Frag_Education_Category::class.java.name
                is Frag_Education_SubCategory -> strFragmentName = Frag_Education_SubCategory::class.java.name
                is Frag_Category_Video_List -> strFragmentName = Frag_Category_Video_List::class.java.name
                is Frag_Category_Play_Video -> strFragmentName = Frag_Category_Play_Video::class.java.name
                is Frag_Update_Password -> strFragmentName = Frag_Update_Password::class.java.name
                is Frag_Event_Location -> strFragmentName = Frag_Event_Location::class.java.name
                is Frag_Event_Details -> strFragmentName = Frag_Event_Details::class.java.name
                is Frag_Event_Filter -> strFragmentName = Frag_Event_Filter::class.java.name
                is Frag_Notification_Details -> strFragmentName = Frag_Notification_Details::class.java.name
                is Frag_Notification -> strFragmentName = Frag_Notification::class.java.name
            }

            if (mFragment != null) {
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val mTempFragment = fragmentManager.findFragmentByTag(strFragmentName)

                /*Log.e("HOME", "TITLE = $strFragmentName")
                if (mTempFragment != null) {
                    fragmentTransaction.replace(R.id.frameContainerHomeActivity, mTempFragment)

                    fragmentTransaction.commit()
                } else {*/

                fragmentTransaction.add(R.id.frameContainerHomeActivity, mFragment, strFragmentName)
                if (isClearBackStack) {
                    val fm = supportFragmentManager
                    for (i in 0 until fm.backStackEntryCount) {
                        Log.d(TAG, "Back Stack Entry--->" + supportFragmentManager.backStackEntryCount)
                        //Clear Fragment Back Stack
                        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }
                }


                val fragmentPopped = fragmentManager.popBackStackImmediate(strFragmentName, 0)
                if (!fragmentPopped) {
                    fragmentTransaction.addToBackStack(strFragmentName)
                    fragmentTransaction.commit()
                    //tvbarHomeActivityToolbarTitle?.setText(title)
                    Log.e(TAG, "BACKSTACK COUNT =" + fragmentManager.backStackEntryCount)
                }

                /*fragmentTransaction.addToBackStack(strFragmentName)
                fragmentTransaction.commit()*/
                //}
                tvbarHomeActivityToolbarTitle?.text = title

                current_title = title
                when {
                    isShowBackButton -> {
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        toolbar?.setNavigationIcon(R.drawable.ic_left_arrow)
                        tvbarHomeActivityToolbarTitle!!.setTextColor(ContextCompat.getColor(mActivity, R.color.black))
                        tvbarHomeActivityToolbarTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_size_14))
                        setFontTypeFace(getString(R.string.font_regular), tvbarHomeActivityToolbarTitle, assets)
                        tvbarHomeActivityToolbarTitle.background = null
                    }
                    isBlackTitle -> {
                        tvbarHomeActivityToolbarTitle!!.setTextColor(ContextCompat.getColor(mActivity, R.color.black))
                        tvbarHomeActivityToolbarTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_size_14))
                        setFontTypeFace(getString(R.string.font_regular), tvbarHomeActivityToolbarTitle, assets)
                        tvbarHomeActivityToolbarTitle.background = null
                    }
                    isAnimation -> {
                        fragmentTransaction.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right)
                    }
                    else -> {
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        tvbarHomeActivityToolbarTitle.background = ContextCompat.getDrawable(mActivity, R.drawable.ic_activityhub_green)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun addEventCalendarDialogue() {

        fragment = Frag_Event_Directory()

        val addCalendarEvent = AddCalendarEvent(mActivity, mContext, object : CommonCallBack {

            override fun onFailure(success: Boolean, message: String) {
                if (success) {
                    showSnackBar(layout_main, message)
                }
                onBackFragmentHandling()
                //loadFragment(fragment, true, resources.getString(R.string.event_directory), false, true)
            }

            override fun onSuccess(map: HashMap<String, String>) {
                showSnackBar(layout_main, map[MESSAGE]!!)
                onBackFragmentHandling()
                //loadFragment(fragment, true, resources.getString(R.string.event_directory), false, true)
            }

        })
        addCalendarEvent.show()

    }

    fun setToolbarTitle(title: String, isShowBackButton: Boolean, isBlackTitle: Boolean) {

        if (isShowBackButton) {
            toolbar?.setNavigationIcon(R.drawable.ic_back_arrow)
        } else {
            toolbar?.navigationIcon = null
        }

        if (isBlackTitle) {
            tvbarHomeActivityToolbarTitle!!.setTextColor(ContextCompat.getColor(mActivity, R.color.black))
            tvbarHomeActivityToolbarTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_size_14))
            setFontTypeFace(getString(R.string.font_regular), tvbarHomeActivityToolbarTitle, assets)
            tvbarHomeActivityToolbarTitle.background = null
        } else {
            tvbarHomeActivityToolbarTitle.background = ContextCompat.getDrawable(mActivity, R.drawable.ic_activityhub_green)
        }
        tvbarHomeActivityToolbarTitle!!.text = title

    }

    override fun onBackPressed() {

        Log.d(TAG, "backStackEntryCount backStackEntryCount--->${supportFragmentManager.backStackEntryCount}")
        val frag = supportFragmentManager.findFragmentByTag(supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1).tag)
        if (frag is Frag_Event_Details) {
            //changeToolbarTitle(resources.getString(R.string.event_directory), 2)
            if (sessionManager[GO_FROM_USER_EVENT_LIST, true]) {
                if (appcontroller.getEvent_data().is_add_calender == 1) {
                    onBackFragmentHandling()
                } else {
                    addEventCalendarDialogue()
                }
            } else {
                onBackFragmentHandling()
            }
        } else {
            onBackFragmentHandling()
        }
    }

    fun changeToolbarTitle(title: String, type: Int) {
        if (type == 1) {
            tvbarHomeActivityToolbarTitle.background = null
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            tvbarHomeActivityToolbarTitle!!.setTextColor(ContextCompat.getColor(mActivity, R.color.black))
            tvbarHomeActivityToolbarTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_size_14))
            setFontTypeFace(getString(R.string.font_regular), tvbarHomeActivityToolbarTitle, assets)
            tvbarHomeActivityToolbarTitle?.text = title
        } else if (type == 2) {
            tvbarHomeActivityToolbarTitle.background = null
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar?.setNavigationIcon(R.drawable.ic_left_arrow)
            tvbarHomeActivityToolbarTitle!!.setTextColor(ContextCompat.getColor(mActivity, R.color.black))
            tvbarHomeActivityToolbarTitle!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.font_size_14))
            setFontTypeFace(getString(R.string.font_regular), tvbarHomeActivityToolbarTitle, assets)
            tvbarHomeActivityToolbarTitle?.text = title
        } else if (type == 3) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            tvbarHomeActivityToolbarTitle.setText(title)
            tvbarHomeActivityToolbarTitle.background = ContextCompat.getDrawable(mActivity, R.drawable.ic_activityhub_green)
        }
    }

}
