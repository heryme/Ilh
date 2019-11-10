package com.activityhub.model

import com.google.gson.annotations.SerializedName

data class CommonResponse(
        @SerializedName("data")
        val `data`: List<Any>,
        @SerializedName("message")
        val message: String,
        @SerializedName("status_code")
        val statusCode: Int
)