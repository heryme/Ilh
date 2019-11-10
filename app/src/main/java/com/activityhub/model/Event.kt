package com.activityhub.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Event(
        @SerializedName("data")
        var `data`: List<Data>,
        @SerializedName("message")
        var message: String, // Event List
        @SerializedName("status_code")
        var statusCode: Int // 200
) {
    data class Data(
            @SerializedName("category_name")
            var categoryName: String, // Outdoor leisure
            @SerializedName("description")
            var description: String, // 12132gdfgfdgd
            @SerializedName("dieses_list")
            var diesesList: List<Dieses>,
            @SerializedName("event_end_date")
            var eventEndDate: String, // 2019-03-20
            @SerializedName("event_end_time")
            var eventEndTime: String,
            @SerializedName("event_id")
            var eventId: Int, // 3
            @SerializedName("event_image")
            var eventImage: String, // http://139.59.24.105/icst_app/public/uploads/event/default.png
            @SerializedName("event_start_date")
            var eventStartDate: String, // 2019-02-26
            @SerializedName("event_start_time")
            var eventStartTime: String,
            @SerializedName("intensity")
            var intensity: String, // Low
            @SerializedName("latitude")
            var latitude: String,
            @SerializedName("location")
            var location: String, // 132132
            @SerializedName("longitude")
            var longitude: String,
            @SerializedName("phone_number")
            var phoneNumber: String,
            @SerializedName("email")
            var email: String,
            @SerializedName("price")
            var price: String,
            @SerializedName("short_description")
            var shortDescription: String,
            @SerializedName("title")
            var title: String, // 1dffsdfsd
            @SerializedName("website_link")
            var websiteLink: String,
            @SerializedName("event_detail_image")
            var event_detail_image: String,
            @SerializedName("is_add_calender")
            var is_add_calender: Int,
            @SerializedName("rating_data")
            var rating_data: String
    ) : Serializable {
        data class Dieses(
                @SerializedName("dieases_name")
                var dieasesName: String, // Disease 2
                @SerializedName("event_dieses_id")
                var eventDiesesId: Int // 7
        ) : Serializable
    }
}