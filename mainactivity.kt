 import androidx.activity.compose.setContent
 import androidx.compose.foundation.layout.*
 import androidx.compose.material3.Button
 import androidx.compose.material3.MaterialTheme
 import androidx.compose.material3.Text
 import androidx.compose.material3.*
 import androidx.compose.runtime.*
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.unit.dp
 import kotlinx.coroutines.delay
 import java.util.Calendar



     var usageMsg by remember { mutableStateOf<String?>(null) }
     var isFocusActive by remember { mutableStateOf(FocusPrefs.isFocusActive(ctx)) }
     
     // Timer State
     var timeLeftSeconds by remember { mutableStateOf(selected.focusMin * 60) }
     var isTimerRunning by remember { mutableStateOf(false) }
​
     // Unproductive Time Tracking
     var unproductiveMillis by remember { mutableStateOf(0L) }
     val unproductiveApps = listOf("com.instagram.android", "com.zhiliaoapp.musically", "com.google.android.youtube")
​
     // Timer Effect
     LaunchedEffect(isTimerRunning, timeLeftSeconds) {
         if (isTimerRunning && timeLeftSeconds > 0) {
             delay(1000L)
             timeLeftSeconds -= 1
         } else if (timeLeftSeconds == 0) {
             isTimerRunning = false
         }
     }
​
     // Unproductive Tracking Effect
     LaunchedEffect(isFocusActive) {
         while (isFocusActive) {
             val stats = getTodayUsage(ctx)
             val currentUnproductive = stats.filter { unproductiveApps.contains(it.packageName) }
                 .sumOf { it.millis }
             unproductiveMillis = currentUnproductive
             delay(5000L) // Refresh every 5 seconds
         }
     }

     Column(modifier = Modifier.padding(16.dp)) {


         Spacer(modifier = Modifier.height(12.dp))

         // Focus mode control
         Text("Focus Mode: ${if (isFocusActive) "ON" else "OFF"}", 
             color = if (isFocusActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
         Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
             Button(onClick = { 
                 FocusPrefs.setFocusActive(ctx, true)
                 isFocusActive = true
             }) {
                 Text("Start Focus")
         // Focus Mode & Timer
         Card(
             modifier = Modifier.fillMaxWidth(),
             colors = CardDefaults.cardColors(containerColor = if (isFocusActive) Color(0xFFE8F5E9) else Color(0xFFF5F5F5))
         ) {
             Column(modifier = Modifier.padding(16.dp)) {
                 Text("Current Goal: ${selected.name}", style = MaterialTheme.typography.titleMedium)
                 Text(
                     text = String.format("%02d:%02d", timeLeftSeconds / 60, timeLeftSeconds % 60),
                     style = MaterialTheme.typography.displayLarge
                 )
                 
                 Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                     Button(onClick = { 
                         isTimerRunning = !isTimerRunning
                         if (isTimerRunning) {
                             FocusPrefs.setFocusActive(ctx, true)
                             isFocusActive = true
                         }
                     }) {
                         Text(if (isTimerRunning) "Pause" else "Start Goal")
                     }
                     Button(onClick = { 
                         isTimerRunning = false
                         timeLeftSeconds = selected.focusMin * 60
                         FocusPrefs.setFocusActive(ctx, false)
                         isFocusActive = false
                     }) {
                         Text("Reset")
                     }
                 }
             }
             Button(onClick = { 
                 FocusPrefs.setFocusActive(ctx, false)
                 isFocusActive = false
             }) {
                 Text("End Focus")
         }
​
         Spacer(modifier = Modifier.height(16.dp))
​
         // Unproductive Alert
         if (unproductiveMillis > 0 && isFocusActive) {
             val mins = unproductiveMillis / 60000
             if (mins > 5) {
                 Surface(
                     color = Color.Red,
                     shape = MaterialTheme.shapes.medium,
                     modifier = Modifier.fillMaxWidth()
                 ) {
                     Text(
                         "ALERT: You've spent $mins mins on unproductive apps! Back to work!",
                         modifier = Modifier.padding(12.dp),
                         color = Color.White
                     )
                 }
             } else {
                 Text("Unproductive time today: $mins mins", style = MaterialTheme.typography.bodySmall)
             }
         }
​
         Spacer(modifier = Modifier.height(16.dp))

         // Presets
         Text("Choose a preset", style = MaterialTheme.typography.titleMedium)
         Spacer(modifier = Modifier.height(8.dp))
         presetListDefault.forEach { preset ->
             Button(
                 onClick = { selected = preset },
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(vertical = 4.dp)
             ) {
                 Text("${preset.name}: ${preset.focusMin} / ${preset.shortBreakMin}")
         Text("Presets", style = MaterialTheme.typography.titleMedium)
         Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
             presetListDefault.take(3).forEach { preset ->
                 AssistChip(
                     onClick = { 
                         selected = preset 
                         timeLeftSeconds = preset.focusMin * 60
                     },
                     label = { Text(preset.name) },
                     selected = selected.id == preset.id
                 )
             }
         }
         Spacer(modifier = Modifier.height(12.dp))
         Text("Selected: ${selected.name}")
         Text("Focus: ${selected.focusMin} min")
         Text("Short break: ${selected.shortBreakMin} min")
         Text("Long break: ${selected.longBreakMin} min after ${selected.longBreakAfter} sessions")

         Spacer(modifier = Modifier.height(20.dp))
         Spacer(modifier = Modifier.height(12.dp))

         // Usage tracking
         Text("App usage (top 30 today)", style = MaterialTheme.typography.titleMedium)
         Spacer(modifier = Modifier.height(8.dp))
         Text("Real-time App Usage", style = MaterialTheme.typography.titleMedium)
         Button(onClick = {
             usageMsg = null
             val list = getTodayUsage(ctx)
             if (list.isEmpty()) {
                 usageList = emptyList()
                 usageMsg = "No data yet. Enable Usage Access, use some apps, then tap again."
             } else {
                 usageList = list.take(30)
             }
             usageList = getTodayUsage(ctx).take(10)
         }) {
             Text("Load usage")
         }
         usageMsg?.let {
             Spacer(modifier = Modifier.height(8.dp))
             Text(it)
             Text("Refresh Usage")
         }
         Spacer(modifier = Modifier.height(8.dp))
         
         usageList.forEach { u ->
             Text("${u.packageName}: ${u.millis / 60000} min")
             Text("${u.packageName.split(".").last()}: ${u.millis / 60000} min")
         }
     }


     val stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)
     return stats
         .filter { it.totalTimeInForeground > 0 }
         .map { AppUsage(it.packageName, it.totalTimeInForeground) }
         .sortedByDescending { it.millis }
         ?.filter { it.totalTimeInForeground > 0 }
         ?.map { AppUsage(it.packageName, it.totalTimeInForeground) }
         ?.sortedByDescending { it.millis } ?: emptyList()
 }
