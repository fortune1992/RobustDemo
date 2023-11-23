package com.freebrio.robustdemo.patch

import android.content.Context

object SharePrefUtils {

    fun getString(context: Context, prefName: String, key: String, defaultValue: String? = null): String? {
        val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue)
    }

    fun putString(context: Context, prefName: String, key: String, value: String?) {
        val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getBoolean(
        context: Context,
        prefName: String,
        key: String,
        defaultValue: Boolean = false
    ): Boolean {
        val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun putBoolean(context: Context, prefName: String, key: String, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun clear(context: Context, prefName: String) {
        val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }


}
