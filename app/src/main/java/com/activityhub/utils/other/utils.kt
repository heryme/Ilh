package com.activityhub.utils.other

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.activityhub.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.BitmapImageViewTarget
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun setScreenWithNoLimits(w: Window) {
    w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}


val DATE_FORMAT = "yyyy-MM-dd"  //or use "M/d/yyyy"

fun getDaysBetweenDates(start: String, end: String): Long {
    val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
    val startDate: Date
    val endDate: Date
    var numberOfDays: Long = 0
    try {
        startDate = dateFormat.parse(start)
        endDate = dateFormat.parse(end)
        Log.e("getDaysBetweenDates", "startDate =  $startDate endDate = $endDate")
        numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS)
        Log.e("getDaysBetweenDates", "numberOfDays =  $numberOfDays")
    } catch (e: ParseException) {
        e.printStackTrace()
        Log.e("getDaysBetweenDates", e.message)
    }

    return numberOfDays
}

private fun getUnitBetweenDates(startDate: Date, endDate: Date, unit: TimeUnit): Long {
    val timeDiff = endDate.time - startDate.time
    return unit.convert(timeDiff, TimeUnit.MILLISECONDS)
}

@SuppressLint("SimpleDateFormat")
fun convertDate(date: String, input_formate: String, output_formate: String): String {
    try {
        val Input_formate = SimpleDateFormat(input_formate)
        val date1 = Input_formate.parse(date)
        val Output_formate = SimpleDateFormat(output_formate)
        Log.e("convertDate", "app= " + Output_formate.format(date1))
        return Output_formate.format(date1)
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("convertDate", "" + e.message)
    }
    return ""

}

// hide keyboard for activity and fragment
fun hideSoftKeyboard(activity: Activity) {
    try {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    } catch (e: Exception) {

        Log.e("KEYBOARD", "Keyboard hideSoftKeyboard: Exception $e")

        e.message
    }

}

// hide keyboard for dialogue
fun hideSoftKeyboard(activity: Activity, dialog: Dialog) {
    try {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        imm.hideSoftInputFromWindow(dialog.currentFocus!!.windowToken, 0)
    } catch (e: Exception) {

        Log.e("KEYBOARD", "Keyboard hideSoftKeyboard: Exception $e")

        e.message
    }

}

fun checkBetweenDate(dateToCheck: String, startDate: String, endDate: String): Boolean {
    var res = false
    val fmt1 = SimpleDateFormat("yyyy-MM-dd") //22-05-2013
    val fmt2 = SimpleDateFormat("yyyy-MM-dd") //22-05-2013
    try {
        val requestDate = fmt2.parse(dateToCheck)
        val fromDate = fmt1.parse(startDate)
        val toDate = fmt1.parse(endDate)
        res = requestDate.compareTo(fromDate) >= 0 && requestDate.compareTo(toDate) <= 0
    } catch (pex: ParseException) {
        pex.printStackTrace()
    }

    return res
}


// show keyboard
fun showSoftKeyboard(activity: Activity) {
    try {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    } catch (e: Exception) {
        Log.e("KEYBOARD", "Keyboard showSoftKeyboard: Exception $e")
        e.printStackTrace()
    }

}

inline fun <reified T : Activity> Activity.goActivity(left_to_right: Int, bottom_to_top: Int) {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
    when {
        left_to_right == 1 -> overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right)
        bottom_to_top == 1 -> overridePendingTransition(R.anim.slide_in_up, R.anim.stay)
        else -> overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right)
    }
}

inline fun <reified T : Activity> Activity.goActivity(left_to_right: Int, bottom_to_top: Int, extra: Int) {
    val intent = Intent(this, T::class.java)
//        intent.putExtra()
    startActivity(intent)
    when {
        left_to_right == 1 -> overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right)
        bottom_to_top == 1 -> overridePendingTransition(R.anim.slide_in_up, R.anim.stay)
        else -> overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right)
    }
}

fun getDecimalFormat(value: Double): String? {
    return DecimalFormat("#.##").format(value)
}

fun setFontTypeFace(type: String, textView: TextView, assets: AssetManager) {
    val face = Typeface.createFromAsset(assets, type)
    textView.typeface = face
}

fun getCalendarUriBase(eventUri: Boolean): String {
    var calendarURI: Uri? = null
    try {
        calendarURI = if (eventUri)
            Uri.parse("content://com.android.calendar/")
        else
            Uri.parse("content://com.android.calendar/calendars")
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return calendarURI!!.toString()
}

fun openDialogActionOne(title_name: String, title_positive: String, activity: Context): Dialog {

    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.setWindowAnimations(R.style.animation_exit_down)
    dialog.setContentView(R.layout.dialogue_action_one)

    val text_dialogue_title = dialog.findViewById(R.id.text_dialogue_title) as TextView
    text_dialogue_title.text = title_name

    val text_positive = dialog.findViewById(R.id.text_positive) as TextView
    text_positive.text = title_positive

    dialog.show()

    return dialog

}

fun openDialogActionTwo(title_name: String, title_positive: String, title_negative: String, activity: Context): Dialog {

    val dialog = Dialog(activity)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.setWindowAnimations(R.style.animation_exit_down)
    dialog.setContentView(R.layout.dialogue_action_two)

    val text_dialogue_title = dialog.findViewById(R.id.text_dialogue_title) as TextView
    text_dialogue_title.text = title_name

    val text_positive = dialog.findViewById(R.id.text_positive) as TextView
    text_positive.text = title_positive

    val text_negative = dialog.findViewById(R.id.text_negative) as TextView
    text_negative.text = title_negative

    dialog.show()

    return dialog

}

fun loadImage(imagePath: String, context: Context, targetImageView: ImageView, placeholderImage: Int, errorImage: Int) {
//    Glide.with(context).load(imagePath)
//            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//            .skipMemoryCache(false)
//            .error(errorImage)
//            .into(targetImageView)

    Glide.with(context)
            .load(imagePath)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .skipMemoryCache(false)
            .listener(object : RequestListener<String, GlideDrawable> {
                override fun onResourceReady(resource: GlideDrawable?, model: String?, target: com.bumptech.glide.request.target.Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    targetImageView.scaleType = ImageView.ScaleType.FIT_XY
                    return false
                }

                override fun onException(e: java.lang.Exception?, model: String?, target: com.bumptech.glide.request.target.Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                    targetImageView.scaleType = ImageView.ScaleType.CENTER
                    targetImageView.setImageResource(errorImage)
                    return false
                }

            })
            .error(errorImage).into(targetImageView)
}

fun loadImageCenterCrop(imagePath: String, context: Context, targetImageView: ImageView, placeholderImage: Int) {
    Glide.with(context).load(imagePath)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .skipMemoryCache(false)
            .centerCrop()
            .into(targetImageView)
}

fun loadImageCircle(imagePath: String, context: Context, targetImageView: ImageView, placeholderImage: Int, errorImage: Int) {
    try {
        Glide.with(context).load(imagePath).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(false)
                .error(errorImage)
                .centerCrop().into(object : BitmapImageViewTarget(targetImageView) {
                    override fun setResource(resource: Bitmap) {
                        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, resource)
                        circularBitmapDrawable.isCircular = true
                        targetImageView.setImageDrawable(circularBitmapDrawable)
                    }
                })

//        TODO IF NOT WORKING ABOVE
        /* .bitmapTransform(new CircleTransform(mContext)).dontAnimate()*/
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun loadImageCircle(imagePath: Int, context: Context, targetImageView: ImageView) {
    Glide.with(context).load(imagePath)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .skipMemoryCache(false)
            .error(imagePath)
            .into(targetImageView)
}


//    fun requestFocus(view: View, context: Context) {
//        if (view.requestFocus()) {
//            getWindow(context).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
//        }
//    }
//
//    fun saveUserProfileData(mLogin: LoginModel, mContext: Context) {
//
//        MyApplication.getAppPref().userID = Integer.parseInt(mLogin.data[0].userId)
//
//
//    }


/*



fun showSnackbar(message: String, view: RelativeLayout, duration: Int) {
    snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.dismiss)) { snackbar?.dismiss() }
    snackbar?.setActionTextColor(Color.RED)
    snackbar?.setDuration(duration)?.show()
}

fun showSnackbar(message: String, view: RelativeLayout, color: Int, duration: Int) {
    snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.dismiss)) { snackbar?.dismiss() }
    snackbar?.setActionTextColor(ContextCompat.getColor(this, color))
    snackbar?.setDuration(duration)?.show()
}

fun showSnackbar(message: String, view: RelativeLayout, action_string: String, color: Int, duration: Int) {
    snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
            .setAction(action_string) { snackbar?.dismiss() }
    snackbar?.setActionTextColor(ContextCompat.getColor(this, color))
    snackbar?.setDuration(duration)?.show()
}


fun showSnack(message: String, view: LinearLayout) {
    snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
    snackbar?.show()
}

fun showSnackbar(message: String, view: LinearLayout) {
    snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.dismiss)) { snackbar?.dismiss() }
    snackbar?.setActionTextColor(Color.RED)
    snackbar?.setDuration(4000)?.show()
}

fun showSnackbar(message: String, view: LinearLayout, duration: Int) {
    snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.dismiss)) { snackbar?.dismiss() }
    snackbar?.setActionTextColor(Color.RED)
    snackbar?.setDuration(duration)?.show()
}

fun showSnackbar(message: String, view: LinearLayout, color: Int, duration: Int) {
    snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.dismiss)) { snackbar?.dismiss() }
    snackbar?.setActionTextColor(color)
    snackbar?.setDuration(duration)?.show()
}

fun showSnackbar(message: String, view: LinearLayout, action_string: String, color: Int, duration: Int) {
    snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
            .setAction(action_string) { snackbar?.dismiss() }
    snackbar?.setActionTextColor(color)
    snackbar?.setDuration(duration)?.show()
}*/


