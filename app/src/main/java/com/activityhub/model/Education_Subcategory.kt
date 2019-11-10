package com.activityhub.model


import com.google.gson.annotations.SerializedName

data class Education_Subcategory(
        @SerializedName("data")
        val `data`: List<Data>,
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
            val subCategorySelectedImage: String
    )
}