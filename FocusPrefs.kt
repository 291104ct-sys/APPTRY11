package com.example.focusguard

import android.content.Context

object FocusPrefs {
    private const val PREFS_NAME = "focus_guard_prefs"
    private const val KEY_FOCUS_ACTIVE = "focus_active"

    fun setFocusActive(context: Context, isActive: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FOCUS_ACTIVE, isActive).apply()
    }

    fun isFocusActive(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FOCUS_ACTIVE, false)
    }
}
