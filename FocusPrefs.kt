
     private const val PREFS_NAME = "focus_guard_prefs"
     private const val KEY_FOCUS_ACTIVE = "focus_active"
     private const val KEY_CLIMBER_PROGRESS = "climber_progress"

     fun setFocusActive(context: Context, isActive: Boolean) {


         val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
         return prefs.getBoolean(KEY_FOCUS_ACTIVE, false)
     }
​
     fun getClimberProgress(context: Context): Float {
         val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
         return prefs.getFloat(KEY_CLIMBER_PROGRESS, 0.0f)
     }
​
     fun updateClimberProgress(context: Context, delta: Float) {
         val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
         val current = prefs.getFloat(KEY_CLIMBER_PROGRESS, 0.0f)
         val newValue = (current + delta).coerceIn(0.0f, 1.0f)
         prefs.edit().putFloat(KEY_CLIMBER_PROGRESS, newValue).apply()
     }
 
