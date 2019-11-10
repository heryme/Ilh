package com.activityhub.restapi

interface ApiResponseInterface {

    fun getApiResponse(apiResponseManager: ApiResponseManager<*>)

    fun onFailure(apiResponseManager: ApiResponseManager<*>, error_message: String, error: Boolean)

}
