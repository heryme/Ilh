package com.activityhub.utils.extension

import android.content.Context
import android.support.v7.app.AppCompatDialog
import com.activityhub.utils.other.ProgressDialog

fun getProgressDialog(context: Context): AppCompatDialog {

    val isCalncelable = false
    val myCustomProgressDialog = ProgressDialog(context)
    myCustomProgressDialog.setCancelable(isCalncelable)
    myCustomProgressDialog.show()
    return myCustomProgressDialog

}

fun dismissDialog(context: Context, mProgressDialog: AppCompatDialog) {
    try {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    } catch (e: Exception) {

    }

}