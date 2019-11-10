package com.activityhub.model

import com.google.gson.annotations.SerializedName

data class Completed_Event(
        @SerializedName("status_code") var statusCode: Int = 0, //200
        @SerializedName("message") var message: String = "", //Completed Event List
        @SerializedName("data") var data: List<Data> = listOf()
) {
    data class Data(
            @SerializedName("event_id") var eventId: Int = 0, //11
            @SerializedName("title") var title: String = "", //Activ 8 - Sports Classes
            @SerializedName("event_end_date") var eventEndDate: String = "" //2019-04-18
    )
}