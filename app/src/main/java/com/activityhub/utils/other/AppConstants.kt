package com.activityhub.utils.other



enum class Gender {
    MALE,
    FEMALE,
    OTHER
}



object PermissionType{

    const val REQUEST_CODE_STORAGE_PERMISSION_FOR_PROFILE_PHOTO = 601
    const val REQUEST_CODE_STORAGE_PERMISSION_FOR_COVER_PHOTO = 602
    const val REQUEST_CODE_CAMERA_PERMISSION_FOR_PROFILE_PHOTO = 605
    const val REQUEST_CODE_CAMERA_PERMISSION_FOR_COVER_PHOTO = 606
    const val MY_PERMISSION_REQUEST_READ_STORAGE_PROFILE_IMAGE = 3
    const val REQUEST_CAMERA = 1
    const val SELECT_FILE = 2
    const val RESULT_LOAD_PROFILE_IMAGE = 111
    const val RESULT_LOAD_COVER_IMAGE = 0

}

object Login{

    const val USER_ACTIVE = "active"
    const val USER_DEACTIVE= "deactive"

}

object Common{

    const val FCM_TOKEN = "fcm_token"
    const val AUTH_TOKEN = "auth_token"
    const val GO_FROM_USER_EVENT_LIST = "user_event_list"
    const val GO_BACK_EVENT_DETAILS = "go_back_event_details"
    const val COMPLETED_EVENT_DIALOG = "completed_event_dialog"
    const val IS_FILTER = "filter"
    const val NOTIFICATION_CHANNEL_ID = "bubble"

    const val ID = "id"
    const val NAME = "name"
    const val SUB_ID = "sub_id"
    const val MESSAGE = "message"
    const val DATA = "data"

    var EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})"
    var PHONE_PATTERN = "^[0-9]{10}$"




}