package com.activityhub.restapi

import com.activityhub.model.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {


    //Sign Up
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("user-register")
    fun signUp(@Field("name") name: String,
               @Field("email") email: String,
               @Field("password") password: String,
               @Field("device_token") device_token: String): Call<ResponseBody>


    //Sign In
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("user-login")
    fun signIn(@Field("email") email: String,
               @Field("password") password: String,
               @Field("device_token") device_token: String): Call<ResponseBody>


    //Forgot Password
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("forgot-password")
    fun forgotPassword(@Field("email") email: String): Call<ResponseBody>


    //Get Condition
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("getCondition")
    fun getCondition(@Header("authorization") inToken: String,
                     @Field("no") no: String): Call<Profile_Condition>

    //Get Interest
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("getIntrest")
    fun getInterest(@Header("authorization") inToken: String,
                    @Field("no") no: String): Call<Get_Intrest>

    //Update Your Profile
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("updateProfile")
    fun updateProfile(@Header("authorization") inToken: String,
                      @Field("condition_id") conditionId: String,
                      @Field("date_of_birth") dob: String,
                      @Field("gender") gender: String,
                      @Field("blood_type") bloodType: String,
                      @Field("height") height: String,
                      @Field("activity_level") activityLevel: String,
                      @Field("current_weight") current_weight: String,
                      @Field("update_weight") update_weight: String,
                      @Field("intrest") intrest: String,
                      @Field("result_survey") resultSurvey: String): Call<ResponseBody>

    //Update Email
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("updateEmail")
    fun updateEmail(@Header("authorization") inToken: String,
                    @Field("old_email") old_email: String,
                    @Field("new_email") new_email: String): Call<ResponseBody>


    //Update Email
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("updatePassword")
    fun updatePassword(@Header("authorization") inToken: String,
                       @Field("old_password") old_email: String,
                       @Field("new_password") new_email: String): Call<ResponseBody>

    //Update Image
    @Multipart
    @Headers("accept:application/json")
    @POST("updateProfilePicture")
    fun updateProfilePicture(@Header("authorization") inToken: String,
                             @Part file: MultipartBody.Part?): Call<ResponseBody>

    //Get Proifle
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("getProfile")
    fun getProfile(@Header("authorization") inToken: String,
                   @Field("no") no: String): Call<GetProfileModel>

    //Get Event List
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("eventList")
    fun getEventList(@Header("authorization") inToken: String,
                     @Field("dieses_group") dieses_group: String,
                     @Field("category_id") category_id: String,
                     @Field("event_date") event_date: String,
                     @Field("intensity") intensity: String,
                     @Field("price_min") price_min: String,
                     @Field("price_max") price_max: String,
                     @Field("seach_keyword") seach_keyword: String,
                     @Field("search_location") search_location: String): Call<Event>


    //Event Filter
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("eventFilter")
    fun eventFilter(@Header("authorization") inToken: String,
                    @Field("no") no: String): Call<Event_Filter>

    //Get User Event
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("getUserEvent")
    fun getUserEvent(
            @Header("authorization") inToken: String,
            @Field("month") month: Int,
            @Field("year") year: Int): Call<Event>


    //Add Event
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("addToCalender")
    fun addEventIntoCalendar(@Header("authorization") authorization: String,
                             @Field("event_id") event_id: String): Call<CommonResponse>


    //Delete Event
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("deleteUserEvent")
    fun deleteEvent(@Header("authorization") authorization: String,
                    @Field("event_id") event_id: String): Call<CommonResponse>

    //Get Complete event
    @Headers("accept:application/json")
    @GET("completeevent")
    fun completedEvent(@Header("authorization") authorization: String): Call<Completed_Event>

    //Rating Event
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("ratingevent")
    fun ratingEvent(@Header("authorization") authorization: String,
                    @Field("event_id") event_id: Int,
                    @Field("star_ratting") star_ratting: Float): Call<CommonResponse>

    //Education Category
    @Headers("accept:application/json")
    @GET("education_category")
    fun getEducationCategory(@Header("authorization") authorization: String): Call<Education_Category>

    //Education Subcategory
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("education_sub_category")
    fun getEducationSubcategory(@Header("authorization") authorization: String,
                                @Field("category_id") category_id: String): Call<Education_Subcategory>

    //Education Subcategory Video List
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("education_sub_category_detail")
    fun getCategoryVideoList(@Header("authorization") authorization: String,
                             @Field("category_id") category_id: String,
                             @Field("sub_category_id") sub_category_id: String): Call<Category_Video_List>


    //Get Assesment List
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("assesmentList")
    fun getAssessmentList(@Header("authorization") authorization: String,
                          @Field("form_no") form_no: Int,
                          @Field("page_no") page_no: Int): Call<Assesment>


    //User Event Attend Or Not
    @FormUrlEncoded
    @Headers("accept:application/json")
    @POST("UserEventAttend")
    fun userEventAttend(@Header("authorization") authorization: String,
                          @Field("event_id") event_id: Int): Call<CommonResponse>

}