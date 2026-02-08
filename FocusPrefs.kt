package com.example.focusguard

import android.content.Context

object FocusPrefs {
    private const val PREFS_NAME = "focus_guard_prefs"
    private const val KEY_FOCUS_ACTIVE = "focus_active"
    private const val KEY_CLIMBER_PROGRESS = "climber_progress"
    private const val KEY_PENALTY_VALUE = "penalty_value"

    fun setFocusActive(context: Context, isActive: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FOCUS_ACTIVE, isActive).apply()
    }

    fun isFocusActive(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FOCUS_ACTIVE, false)
    }

    fun getClimberProgress(context: Context): Float {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_CLIMBER_PROGRESS, 0.0f)
    }

    fun updateClimberProgress(context: Context, delta: Float) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = prefs.getFloat(KEY_CLIMBER_PROGRESS, 0.0f)
        val newValue = (current + delta).coerceIn(0.0f, 1.0f)
        prefs.edit().putFloat(KEY_CLIMBER_PROGRESS, newValue).apply()
    }

    fun getPenaltyValue(context: Context): Float {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getFloat(KEY_PENALTY_VALUE, 0.05f) // Default 5%
    }

    fun setPenaltyValue(context: Context, value: Float) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putFloat(KEY_PENALTY_VALUE, value).apply()
    }
}
