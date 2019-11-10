package com.activityhub.model

import com.google.gson.annotations.SerializedName

data class User_Events(
        @SerializedName("status_code") var statusCode: Int = 0, //200
        @SerializedName("message") var message: String = "", //Event Get Successfully
        @SerializedName("data") var data: List<Data> = listOf()
) {
    data class Data(
            @SerializedName("title") var title: String = "", //Activ 8 - Sports Classes
            @SerializedName("event_start_date") var eventStartDate: String = "", //2019-04-03
            @SerializedName("event_end_date") var eventEndDate: String = "", //2019-05-23
            @SerializedName("user_id") var userId: Int = 0, //12
            @SerializedName("event_id") var eventId: Int = 0, //11
            @SerializedName("event_start_time") var eventStartTime: String = "", //8:00 PM
            @SerializedName("event_end_time") var eventEndTime: String = "" //8:19 PM
    )
}