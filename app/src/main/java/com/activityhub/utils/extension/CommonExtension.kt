package com.activityhub.utils.extension

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.activityhub.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


private var toast: Toast? = null

@SuppressLint("ShowToast")
fun Toast(msg: Any?, isShort: Boolean = true, app: Context) {
    msg?.let {
        if (toast == null) {
            toast = Toast.makeText(app, msg.toString(), Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg.toString())
        }
        toast!!.duration = if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        toast!!.show()
    }
}

//
//fun LoggE(tag: String, message: Any) {
//    val isLog = true
//    try {
//        Log.e(tag, "==>$message")
//    } catch (e: Exception) {
//    }
//}

fun isNetWork(context: Context): Boolean {
    val flag: Boolean = isNetworkAvailable(context)
    return flag
}

fun isNetworkAvailable(context: Context): Boolean {

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null
}

fun isTablet(context: Context): Boolean {
    return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

fun makeTextViewClickeble(context: Context,
                          textView: TextView, link: String,
                          staringPoint: Int,
                          endingPoint: Int) {
    val ss = SpannableString(context.getString(R.string.terms_and_condition))
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = true
            ds.isFakeBoldText = true
            ds.color = Color.WHITE
        }
    }
    ss.setSpan(clickableSpan,
            staringPoint,
            endingPoint,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    textView.text = ss
    textView.movementMethod = LinkMovementMethod.getInstance()
    textView.highlightColor = Color.WHITE
}

fun getImageRequestBody(path: String, key: String): MultipartBody.Part? {// create images file  in  MultipartBody.Part
    val file = File(path)
    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
    return MultipartBody.Part.createFormData(key, file.name, requestFile)
}

fun formattedDateFromString(inputFormat: String, outputFormat: String, inputDate: String): String {

    var inputFormat = inputFormat
    var outputFormat = outputFormat
    if (inputFormat == "") { // if inputFormat = "", set a default input format.
        inputFormat = "yyyy-MM-dd hh:mm:ss"
    }
    if (outputFormat == "") {
        outputFormat = "EEEE d 'de' MMMM 'del' yyyy" // if inputFormat = "", set a default output format.
    }
    var parsed: Date? = null
    var outputDate = ""

    val df_input = SimpleDateFormat(inputFormat, Locale.getDefault())
    val df_output = SimpleDateFormat(outputFormat, Locale.getDefault())

    // You can set a different Locale, This example set a locale of Country Mexico.
    //SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new Locale("es", "MX"));
    //SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new Locale("es", "MX"));

    try {
        parsed = df_input.parse(inputDate)
        outputDate = df_output.format(parsed)
    } catch (e: Exception) {
        Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.message)
    }

    return outputDate

}

fun convertDate(inputFormat: String,
                outputFormat: String,
                inputDate: String): String {
    val formatter = SimpleDateFormat(inputFormat)
    var date: Date? = null
    try {
        date = formatter.parse(inputDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    val newFormat = SimpleDateFormat(outputFormat)
    val finalString = newFormat.format(date)
    Log.e("TAG", finalString)
    return finalString

}


//  Open Gmail
fun openGmail(context: Context, sendToEmail: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        val strTo = arrayOf(sendToEmail)
        intent.putExtra(Intent.EXTRA_EMAIL, strTo)
        intent.type = "message/rfc822"
        intent.setPackage("com.google.android.gm")
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        android.widget.Toast.makeText(context, context.resources?.getString(R.string.no_app_found), android.widget.Toast.LENGTH_SHORT).show()
    }

}

fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
    if (view.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = view.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        view.requestLayout()
    }
}





