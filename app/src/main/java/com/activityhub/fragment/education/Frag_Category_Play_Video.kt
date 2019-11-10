package com.activityhub.fragment.education

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cn.jzvd.JZDataSource
import cn.jzvd.JZUtils
import cn.jzvd.Jzvd.*
import cn.jzvd.JzvdStd
import com.activityhub.R
import com.activityhub.adapter.Adp_Category_Play_Video
import com.activityhub.fragment.common.Frag_Base
import com.activityhub.utils.extension.TouchDetectableScrollView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.support.v4.os.HandlerCompat.postDelayed
import android.view.*
import com.activityhub.activity.home.Act_Home
import com.activityhub.fragment.auth.Frag_Account
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener


class Frag_Category_Play_Video : Frag_Base() {

    private lateinit var recycler_category_video: RecyclerView
    private lateinit var text_video_question: TextView
    private lateinit var text_question_desc: TextView
    private lateinit var text_video_length: TextView
    lateinit var category_videoplayer: JzvdStd
    lateinit var scroll_video: TouchDetectableScrollView
    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_category_play_video, container, false)
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
//        (activity as Act_Home).setToolbarTitle(getString(R.string.education), true, true)
    }

    override fun initListeners() {
        recycler_category_video.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        scroll_video.myScrollChangeListener = object : TouchDetectableScrollView.OnMyScrollChangeListener {
            override fun onScrollUp() {
            }

            override fun onScrollDown() {
            }

            override fun onBottomReached() {

                if (category_videoplayer.jzDataSource == null || category_videoplayer.jzDataSource.urlsMap.isEmpty()
                        || category_videoplayer.jzDataSource.currentUrl == null) {
                    Toast.makeText(context, resources.getString(cn.jzvd.R.string.no_url), Toast.LENGTH_SHORT).show()
                    return
                }
                if (category_videoplayer.currentState == CURRENT_STATE_PLAYING) {
                    Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ")
                    category_videoplayer.mediaInterface.pause()
                    category_videoplayer.onStatePause()
                }
            }

        }

    }

    override fun initData() {

        val video_position = appcontroller.getVideo_position()
        val video_list = appcontroller.getVideo_data()

        text_video_question.text = video_list[video_position].videoName
        text_video_length.text = video_list[video_position].time
        text_question_desc.text = video_list[video_position].videoDescription

        recycler_category_video.adapter =
                Adp_Category_Play_Video(video_list, video_position, activity!!, this)

        val video_thumb_url = video_list[video_position].video_thumb

        if (video_list[video_position].videoUrl != "") {
            val videoURL = appcontroller.getProxy(activity!!).getProxyUrl(video_list[video_position].videoUrl)
            val jzDataSource = JZDataSource(videoURL, "")

            category_videoplayer.setUp(
                    jzDataSource,
                    JzvdStd.SCREEN_NORMAL)
        }

        if (video_thumb_url != "") {

            Glide.with(appcontroller.getAppContext())
                    .load(video_thumb_url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(object : RequestListener<String, GlideDrawable> {
                        override fun onResourceReady(resource: GlideDrawable?, model: String?, target: com.bumptech.glide.request.target.Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                            category_videoplayer.thumbImageView.scaleType = ImageView.ScaleType.FIT_XY
                            return false
                        }

                        override fun onException(e: java.lang.Exception?, model: String?, target: com.bumptech.glide.request.target.Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                            category_videoplayer.thumbImageView.scaleType = ImageView.ScaleType.CENTER
                            category_videoplayer.thumbImageView.setImageResource(R.drawable.ic_video)
                            return false
                        }

                    })
                    .error(R.drawable.ic_video).into(category_videoplayer.thumbImageView)

        } else {
            category_videoplayer.thumbImageView.scaleType = ImageView.ScaleType.CENTER
            category_videoplayer.thumbImageView.setImageResource(R.drawable.ic_video)
        }

    }

    fun initVideo(video_position: Int) {

        appcontroller.setVideo_position(video_position)

        initData()

        scroll_video.postDelayed({
            scroll_video.smoothScrollTo(0, 0)
        }, 100)

    }

    override fun initIDs(rootView: View) {
        recycler_category_video = rootView.findViewById(R.id.recycler_category_video)
        category_videoplayer = rootView.findViewById(R.id.category_videoplayer)
        text_video_question = rootView.findViewById(R.id.text_video_question)
        text_video_length = rootView.findViewById(R.id.text_video_length)
        text_question_desc = rootView.findViewById(R.id.text_question_desc)
        scroll_video = rootView.findViewById(R.id.scroll_video)
    }

    override fun onStop() {
        super.onStop()
        if (category_videoplayer.currentState == CURRENT_STATE_AUTO_COMPLETE) return
        if (category_videoplayer.currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            category_videoplayer.mediaInterface.pause()
            backPress()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.clear()
        inflater?.inflate(R.menu.profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
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
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}

