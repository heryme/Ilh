package com.activityhub.restapi

/**
 * Created by Admin on 6-4-2017.
 */

class ApiError {
     val status_code: Int = 0
    private val message: String? = null

    fun status(): Int {
        return status_code
    }

    fun message(): String {
        return message!!
    }
}
