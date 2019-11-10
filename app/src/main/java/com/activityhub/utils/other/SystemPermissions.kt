package com.activityhub.utils.other

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast


object SystemPermissions {

    val RECORD_PERMISSION_REQUEST_CODE = 1
    val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2
    val CAMERA_PERMISSION_REQUEST_CODE = 3
    val SEND_SMS_REQUEST_CODE = 4
    val READ_CONTACTS_REQUEST_CODE = 5
    val LOCATION_REQUEST_CODE = 6
    val CALL_PERMISSION_REQUEST_CODE = 7
    val SMS_CODE = 8
    val CALENDAR_PERMISSION_REQUEST_CODE = 9

    fun checkPermissionForAccessFineLoc(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForAccessCoarseLoc(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionForLocation(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Toast.makeText(activity, "Location permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        }
    }

    fun requestForAllSMS(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS), SMS_CODE)
    }

    fun requestPermissionForExternalStorage(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    fun checkPermissionForWriteExternalStorage(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForReadExternalStorage(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForREAD_SMS(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForSEND_SMS(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForRECEIVE_SMS(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForCamera(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForSendSMS(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForReadContacts(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForCallPhone(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForReadPhone(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionForCallPhone(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
            //Toast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.READ_PHONE_STATE), CALL_PERMISSION_REQUEST_CODE)
        }
    }

    fun requestPermissionForRecord(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.RECORD_AUDIO), RECORD_PERMISSION_REQUEST_CODE)
        }
    }

    fun requestForAllPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECORD_AUDIO, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS), CAMERA_PERMISSION_REQUEST_CODE)
    }

    fun requestPermissionForCamera(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            Toast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    fun requestPermissionForSendSMS(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)) {
            Toast.makeText(activity, "Send SMS permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.SEND_SMS), SEND_SMS_REQUEST_CODE)
        }
    }

    fun requestPermissionForReadContacts(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
            Toast.makeText(activity, "Read Contacts permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf<String>(Manifest.permission.READ_CONTACTS), READ_CONTACTS_REQUEST_CODE)
        }
    }

    fun requestPermissionForReadCalendar(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_CALENDAR)) {
            //            Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CALENDAR), CALENDAR_PERMISSION_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_CALENDAR), CALENDAR_PERMISSION_REQUEST_CODE)
        }
    }

    fun requestPermissionForCalendar(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_CALENDAR)) {
            //            Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_CALENDAR), CALENDAR_PERMISSION_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_CALENDAR), CALENDAR_PERMISSION_REQUEST_CODE)
        }
    }


    fun checkPermissionForWriteCalendar(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForReadCalendar(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR)
        return result == PackageManager.PERMISSION_GRANTED
    }




    fun checkPermissionForRecord(activity: Activity): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) !== PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                        ActivityCompat.requestPermissions(context as Activity, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.GET_ACCOUNTS), 4)
                    } else {
                        ActivityCompat.requestPermissions(context as Activity, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.GET_ACCOUNTS), 4)
                    }
                    return false
                }
            }
        }
        return true
    }


}