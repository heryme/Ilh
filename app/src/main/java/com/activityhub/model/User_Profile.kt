package com.activityhub.model
import com.google.gson.annotations.SerializedName


data class GetProfileModel(
    @SerializedName("status_code") var statusCode: Int = 0,
    @SerializedName("message") var message: String = "",
    @SerializedName("data") var data: Data = Data()
)

data class Data(
    @SerializedName("user_id") var userId: Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("result_survey") var resultSurvey: String = "",
    @SerializedName("weight") var weight: String = "",
    @SerializedName("activity_level") var activityLevel: String = "",
    @SerializedName("status") var status: String = "",
    @SerializedName("gender") var gender: String = "",
    @SerializedName("date_of_birth") var dateOfBirth: String = "",
    @SerializedName("blood_type") var bloodType: String = "",
    @SerializedName("height") var height: String = "",
    @SerializedName("profile_thumb_url") var profileThumbUrl: Any = Any(),
    @SerializedName("condition") var condition: List<Condition> = listOf(),
    @SerializedName("intrest") var intrest: List<Intrest> = listOf(),
    @SerializedName("profile_picture") var profilePicture: String = "",
    @SerializedName("weight_array") var weightArray: List<WeightArray> = listOf()
)

data class Intrest(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("intrest_name") var intrestName: String = ""
)

data class WeightArray(
    @SerializedName("user_id") var userId: Int = 0,
    @SerializedName("weight") var weight: String = ""
)

data class Condition(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("condition_name") var conditionName: String = ""
)
/*
data class GetProfileModel(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("message")
    var message: String, // Profile Get successfully !
    @SerializedName("status_code")
    var statusCode: Int // 200
) {
    data class Data(
        @SerializedName("activity_level")
        var activityLevel: String, // Moderate
        @SerializedName("blood_type")
        var bloodType: String, // B
        @SerializedName("condition")
        var condition: List<Condition>,
        @SerializedName("date_of_birth")
        var dateOfBirth: String, // 06-03-2019
        @SerializedName("email")
        var email: String, // osm@yopmail.com
        @SerializedName("gender")
        var gender: String, // GENDER
        @SerializedName("height")
        var height: String, // 5
        @SerializedName("intrest")
        var intrest: List<Intrest>,
        @SerializedName("name")
        var name: String, // osm
        @SerializedName("profile_picture")
        var profilePicture: String, // http://139.59.24.105/icst_app/public/uploads/profile/default.png
        @SerializedName("profile_thumb_url")
        var profileThumbUrl: Any, // null
        @SerializedName("result_survey")
        var resultSurvey: String, // Weekly
        @SerializedName("status")
        var status: String, // Active
        @SerializedName("user_id")
        var userId: Int, // 52
        @SerializedName("weight")
        var weight: String // 6
    ) {
        data class Condition(
            @SerializedName("condition_name")
            var conditionName: String, // testing4
            @SerializedName("id")
            var id: Int // 7
        )

        data class Intrest(
            @SerializedName("id")
            var id: Int, // 4
            @SerializedName("intrest_name")
            var intrestName: String // d3
        )
    }
}*/
