package com.activityhub.activity.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.activityhub.R
import com.activityhub.activity.auth.Act_Login
import kotlinx.android.synthetic.main.activity_welcome.*

class Act_Welcome : BaseActivity(), View.OnClickListener {

    private var viewPagerAdapter: MyViewPagerAdapter? = null
    private var dots: Array<TextView?>? = null
    private var layouts: IntArray? = null

    companion object {
        val isFirstTime = "isFirstTime"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        initComponent()
        initToolbar()
        initListeners()
        initData()
    }

    override fun onClick(view: View?) {
        when (view) {
            btnWelcomeActivityContinue -> {
                val current = getItem(+1)
                if (current < layouts!!.size) {
                    vpWelcomeActivity.currentItem = current
                } else {
                    launchHomeScreen()
                }
            }
            btnWelcomeActivitySkip -> {
                launchHomeScreen()
            }
        }
    }

    override fun initComponent() {
    }

    override fun initToolbar() {
        changeStatusBarColor()
    }

    override fun initListeners() {
        btnWelcomeActivityContinue.setOnClickListener(this)
        btnWelcomeActivitySkip.setOnClickListener(this)
    }

    override fun initData() {
        initSlider()
    }

    private fun initSlider() {
        layouts = intArrayOf(R.layout.welcome_slider_one, R.layout.welcome_slider_two)

        addBottomDots(0)

        val viewPagerPageChangeListener = object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                addBottomDots(position)

                if (position == layouts!!.size - 1) {
                    btnWelcomeActivitySkip.visibility = View.VISIBLE
                } else {
                    btnWelcomeActivitySkip.visibility = View.VISIBLE
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(arg0: Int) {

            }
        }

        viewPagerAdapter = MyViewPagerAdapter()
        vpWelcomeActivity?.adapter = viewPagerAdapter
        vpWelcomeActivity?.addOnPageChangeListener(viewPagerPageChangeListener)
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts!!.size)

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        llWelcomeActivityDots?.removeAllViews()
        for (i in 0 until dots!!.size) {
            dots!![i] = TextView(this)
            dots!![i]!!.text = Html.fromHtml("&#8226;")
            dots!![i]!!.textSize = 20F
            dots!![i]!!.setTextColor(colorsInactive[currentPage])
            llWelcomeActivityDots!!.addView(dots!![i])
        }

        if (dots!!.isNotEmpty())
            dots!![currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    private fun getItem(i: Int): Int {
        return vpWelcomeActivity!!.currentItem + i
    }

    private fun launchHomeScreen() {
        sessionManager.put(isFirstTime, true)
        startActivity(Intent(this@Act_Welcome, Act_Login::class.java))
        finish()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater!!.inflate(layouts!![position], container, false)
            container.addView(view)

            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

}
