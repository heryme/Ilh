package com.activityhub.model


import com.google.gson.annotations.SerializedName

data class Category_Video_List(
        @SerializedName("data")
        val `data`: Data,
        @SerializedName("message")
        val message: String,
        @SerializedName("status_code")
        val statusCode: Int
) {
    data class Data(
            @SerializedName("id")
            val id: Int,
            @SerializedName("sub_category_description")
            val subCategoryDescription: String,
            @SerializedName("sub_category_image")
            val subCategoryImage: String,
            @SerializedName("sub_category_name")
            val subCategoryName: String,
            @SerializedName("sub_category_selected_image")
            val subCategorySelectedImage: String,
            @SerializedName("video_array")
            val videoArray: List<VideoArray>
    ) {
        data class VideoArray(
                @SerializedName("video_description")
                val videoDescription: String,
                @SerializedName("video_name")
                val videoName: String,
                @SerializedName("video_url")
                val videoUrl: String,
                @SerializedName("video_thumb")
                val video_thumb: String,
                @SerializedName("time")
                val time:String
        )
    }
}