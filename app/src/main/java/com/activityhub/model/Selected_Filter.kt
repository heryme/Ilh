package com.activityhub.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Selected_Filter(

        @SerializedName("diseaseGroupId")
        var `diseaseGroupId`: Int,

        @SerializedName("eventCategoryId")
        var `eventCategoryId`: Int,

        @SerializedName("date")
        var `date`: String,

        @SerializedName("intensity")
        var `intensity`: String,

        @SerializedName("priceRangeFree")
        var `priceRangeFree`: String,

        /*@SerializedName("priceRangeFreeRange")
        var `priceRangeFreeRange`: String,*/

        @SerializedName("priceMin")
        var `priceMin`: String,

        @SerializedName("priceMax")
        var `priceMax`: String,

        @SerializedName("isClear")
        var `isClear`: Boolean


) : Serializable