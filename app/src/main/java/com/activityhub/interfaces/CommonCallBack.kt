package com.activityhub.interfaces


import java.util.*

interface CommonCallBack {

    fun onSuccess(map: HashMap<String, String>)

    fun onFailure(success: Boolean, message: String)
}
