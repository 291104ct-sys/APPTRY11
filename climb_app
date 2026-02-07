package com.example.apptry1


import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar


// -------- Presets --------
val presetListDefault = listOf(
   PomodoroPreset("classic", "Classic", 25, 5, 15, 4),
   PomodoroPreset("short", "Short", 15, 3, 10, 4),
   PomodoroPreset("deep", "Deep Work", 50, 10, 20, 2),
   PomodoroPreset("study", "Study Mode", 30, 5, 15, 3),
   PomodoroPreset("custom", "Custom", 25, 5, 15, 4)
)


// -------- Usage Model --------
data class AppUsage(val packageName: String, val millis: Long)


// -------- Activity --------
class MainActivity : ComponentActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContent { PomodoroScreen() }
   }
}


// -------- UI --------
@Composable
fun PomodoroScreen() {
   val ctx = LocalContext.current


   var selected by remember { mutableStateOf(presetListDefault.first()) }
   var usageList by remember { mutableStateOf(listOf<AppUsage>()) }
   var usageMsg by remember { mutableStateOf<String?>(null) }


   Column(modifier = Modifier.padding(16.dp)) {
       Text("FocusGuard", style = MaterialTheme.typography.headlineSmall)
       Spacer(modifier = Modifier.height(12.dp))


       // Permissions shortcuts
       Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
           Button(onClick = { openUsageAccessSettings(ctx) }) { Text("Usage Access") }
           Button(onClick = { openAccessibilitySettings(ctx) }) { Text("Blocking Access") }
       }


       Spacer(modifier = Modifier.height(12.dp))


       // Focus mode control (connects to blocking)
       Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
           Button(onClick = { FocusPrefs.setFocusActive(ctx, true) }) {
               Text("Start Focus")
           }
           Button(onClick = { FocusPrefs.setFocusActive(ctx, false) }) {
               Text("End Focus")
           }
       }


       Spacer(modifier = Modifier.height(16.dp))


       // Presets (simple buttons for hackathon speed)
       Text("Choose a preset", style = MaterialTheme.typography.titleMedium)
       Spacer(modifier = Modifier.height(8.dp))


       presetListDefault.forEach { preset ->
           Button(
               onClick = { selected = preset },
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(vertical = 4.dp)
           ) {
               Text("${preset.name}: ${preset.focusMin}/${preset.shortBreakMin}")
           }
       }


       Spacer(modifier = Modifier.height(12.dp))
       Text("Selected: ${selected.name}")
       Text("Focus: ${selected.focusMin} min")
       Text("Short break: ${selected.shortBreakMin} min")
       Text("Long break: ${selected.longBreakMin} min after ${selected.longBreakAfter} sessions")


       Spacer(modifier = Modifier.height(20.dp))


       // Usage tracking
       Text("App usage (top 30 today)", style = MaterialTheme.typography.titleMedium)
       Spacer(modifier = Modifier.height(8.dp))


       Button(onClick = {
           usageMsg = null
           val list = getTodayUsage(ctx)
           if (list.isEmpty()) {
               usageList = emptyList()
               usageMsg = "No data yet. Enable Usage Access, use some apps, then tap again."
           } else {
               usageList = list.take(30) // change to `list` to show everything
           }
       }) {
           Text("Load usage")
       }


       usageMsg?.let {
           Spacer(modifier = Modifier.height(8.dp))
           Text(it)
       }


       Spacer(modifier = Modifier.height(8.dp))
       usageList.forEach { u ->
           Text("${u.packageName}: ${u.millis / 60000} min")
       }
   }
}


// -------- Helpers --------
fun openUsageAccessSettings(context: Context) {
   context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
}


fun openAccessibilitySettings(context: Context) {
   context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
}


fun getTodayUsage(context: Context): List<AppUsage> {
   val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager


   val cal = Calendar.getInstance()
   val end = cal.timeInMillis
   cal.set(Calendar.HOUR_OF_DAY, 0)
   cal.set(Calendar.MINUTE, 0)
   cal.set(Calendar.SECOND, 0)
   cal.set(Calendar.MILLISECOND, 0)
   val start = cal.timeInMillis


   val stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
   return stats
       .filter { it.totalTimeInForeground > 0 }
       .map { AppUsage(it.packageName, it.totalTimeInForeground) }
       .sortedByDescending { it.millis }

