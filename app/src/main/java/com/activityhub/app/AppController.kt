package com.activityhub.app


import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.support.multidex.MultiDex
import com.activityhub.model.Category_Video_List
import com.activityhub.model.Event
import com.danikula.videocache.HttpProxyCacheServer


class AppController : Application() {


    private var event_data: Event.Data? = null
    private var video_data: List<Category_Video_List.Data.VideoArray>? = null
    private var video_position: Int = -1
    private var proxy: HttpProxyCacheServer? = null


    override fun onCreate() {
        super.onCreate()
        instance = this
//        TODO Remove when build
        //Fabric.with(this, Crashlytics())
    }

    fun getProxy(context: Context): HttpProxyCacheServer {
        val app = context.applicationContext as AppController

        if (app.proxy == null) {
            app.proxy = app.newProxy()
        }
        return app.proxy!!

    }

    private fun newProxy(): HttpProxyCacheServer {
        return HttpProxyCacheServer(this)
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        MultiDex.install(this)
    }

    fun isConnectInternet(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    companion object {
        @get:Synchronized
        var instance: AppController? = null
    }

    fun getEvent_data(): Event.Data {
        return this.event_data!!
    }

    fun setEvent_data(event_data: Event.Data) {
        this.event_data = event_data
    }

    fun setVideo_data(video_data: List<Category_Video_List.Data.VideoArray>?, video_position: Int) {
        this.video_data = video_data
        this.video_position = video_position
    }

    fun getVideo_data(): List<Category_Video_List.Data.VideoArray> {
        return this.video_data!!
    }

    fun getVideo_position(): Int {
        return this.video_position
    }

    fun setVideo_position(video_position: Int) {
        this.video_position = video_position
    }

    fun getAppContext(): AppController? {
        return instance
    }

}
