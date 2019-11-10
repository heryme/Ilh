package com.activityhub.utils.other

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.activityhub.R

class SessionManager(private val context: Context) {

    private val resources: Resources

    companion object {

        private lateinit var pref: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        lateinit var instance: SessionManager
        // Shared pref mode
        private val PRIVATE_MODE = 0

    }

    init {
        instance = this
        resources = context.resources
        pref = context.getSharedPreferences(resources.getString(R.string.appSharedPref), PRIVATE_MODE)
        editor = pref.edit()
    }


    fun put(key: String, value: String): String {
        editor.putString(key, value)
        editor.commit()
        return key
    }

    fun put(key: String, value: Boolean): String {
        editor.putBoolean(key, value)
        editor.commit()
        return key
    }

    fun put(key: String, value: Int): String {
        editor.putInt(key, value)
        editor.commit()
        return key
    }

    fun put(key: String, value: Float): String {
        editor.putFloat(key, value)
        editor.commit()
        return key
    }


    fun remove(key: String) {
        editor.remove(key)
    }

    operator fun get(key: String, value: String): String = pref.getString(key, value)

    operator fun get(key: String, value: Boolean): Boolean = pref.getBoolean(key, value)
    /*  public boolean get(String key) {
        return pref.getBoolean(key, false);
    }*/

    operator fun get(key: String, value: Int): Int = pref.getInt(key, value)

    operator fun get(key: String, value: Float): Float = pref.getFloat(key, value)

    fun logout() {
        /* editor.clear()*/
        editor.remove(context.getString(R.string.isLogin))
        editor.remove(context.getString(R.string.token))
        editor.remove(context.getString(R.string.id))
        editor.remove(context.getString(R.string.name))
        editor.remove(context.getString(R.string.device_token))
        editor.remove(context.getString(R.string.emailId))
        editor.remove(context.getString(R.string.profileImage))
        editor.commit()
    }


}