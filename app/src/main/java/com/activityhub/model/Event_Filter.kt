package com.activityhub.model

import com.google.gson.annotations.SerializedName


data class Event_Filter(
        @SerializedName("status_code") var statusCode: Int = 0,
        @SerializedName("message") var message: String = "",
        @SerializedName("data") var data: Data = Data()
) {

    data class Data(
            @SerializedName("all_category") var allCategory: List<AllCategory> = listOf(),
            @SerializedName("all_dieses") var allDieses: List<AllDiese> = listOf(),
            @SerializedName("min_price") var minPrice: Double = 0.0,
            @SerializedName("max_price") var maxPrice: Double = 0.0
    )

    data class AllCategory(
            @SerializedName("category_id") var categoryId: Int = 0,
            @SerializedName("category_name") var categoryName: String = ""
    )

    data class AllDiese(
            @SerializedName("dieses_id") var diesesId: Int = 0,
            @SerializedName("dieases_name") var dieasesName: String = ""
    )
}