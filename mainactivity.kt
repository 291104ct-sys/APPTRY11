
     private const val KEY_FOCUS_ACTIVE = "focus_active"
     private const val KEY_CLIMBER_PROGRESS = "climber_progress"
     private const val KEY_PENALTY_VALUE = "penalty_value"

     fun setFocusActive(context: Context, isActive: Boolean) {


         val newValue = (current + delta).coerceIn(0.0f, 1.0f)
         prefs.edit().putFloat(KEY_CLIMBER_PROGRESS, newValue).apply()
     }
​
     fun getPenaltyValue(context: Context): Float {
         val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
         return prefs.getFloat(KEY_PENALTY_VALUE, 0.05f) // Default 5%
     }
​
     fun setPenaltyValue(context: Context, value: Float) {
         val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
         prefs.edit().putFloat(KEY_PENALTY_VALUE, value).apply()
     }
 }
