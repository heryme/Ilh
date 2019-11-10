package com.activityhub.model


import com.google.gson.annotations.SerializedName

data class Education_Category(
        @SerializedName("data")
        val `data`: List<Data>,
        @SerializedName("message")
        val message: String,
        @SerializedName("status_code")
        val statusCode: Int
) {
    data class Data(
            @SerializedName("category_image")
            val categoryImage: String,
            @SerializedName("category_name")
            val categoryName: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("select_category_image")
            val selectCategoryImage: String
    )
}
