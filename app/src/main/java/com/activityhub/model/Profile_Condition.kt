package com.activityhub.model

import com.google.gson.annotations.SerializedName


data class Profile_Condition(
        @SerializedName("data")
        var `data`: List<Data>,
        @SerializedName("message")
        var message: String,
        @SerializedName("status_code")
        var statusCode: Int
) {
    data class Data(
            @SerializedName("condition_id")
            var conditionId: Int,
            @SerializedName("condition_name")
            var conditionName: String,
            @SerializedName("status")
            var status: Boolean
    )
}