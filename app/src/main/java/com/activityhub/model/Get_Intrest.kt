package com.activityhub.model
import com.google.gson.annotations.SerializedName


data class Get_Intrest(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("message")
    var message: String, // Intrest Get successfully !
    @SerializedName("status_code")
    var statusCode: Int // 200
) {
    data class Data(
        @SerializedName("intrest_id")
        var intrestId: Int, // 7
        @SerializedName("intrest_name")
        var intrestName: String, // demo125
        @SerializedName("status")
        var status: String // false
    )
}