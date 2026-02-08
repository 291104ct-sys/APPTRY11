
 import androidx.activity.ComponentActivity
 import androidx.activity.compose.setContent
 import androidx.compose.animation.core.animateFloatAsState
 import androidx.compose.foundation.Image
 import androidx.compose.foundation.background
 import androidx.compose.foundation.layout.*
 import androidx.compose.foundation.shape.CircleShape
 import androidx.compose.material3.*
 import androidx.compose.runtime.*
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.draw.clip
 import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.layout.ContentScale
 import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.res.painterResource
 import androidx.compose.ui.unit.dp
 import kotlinx.coroutines.delay


     var selected by remember { mutableStateOf(presetListDefault.first()) }
     var usageList by remember { mutableStateOf(listOf<AppUsage>()) }
     var usageMsg by remember { mutableStateOf<String?>(null) }
     var isFocusActive by remember { mutableStateOf(FocusPrefs.isFocusActive(ctx)) }
     


     var isTimerRunning by remember { mutableStateOf(false) }

     // Unproductive Time Tracking
     var unproductiveMillis by remember { mutableStateOf(0L) }
     // Character State
     var climberProgress by remember { mutableStateOf(FocusPrefs.getClimberProgress(ctx)) }
     val animatedProgress by animateFloatAsState(targetValue = climberProgress)
​
     // Unproductive Tracking
     val unproductiveApps = listOf("com.instagram.android", "com.zhiliaoapp.musically", "com.google.android.youtube")
     var lastUnproductiveMillis by remember { mutableStateOf(0L) }

     // Timer Effect


             delay(1000L)
             timeLeftSeconds -= 1
         } else if (timeLeftSeconds == 0) {
         } else if (timeLeftSeconds == 0 && isTimerRunning) {
             isTimerRunning = false
             // Goal Completed -> Move Up
             climberProgress = (climberProgress + 0.1f).coerceAtMost(1f)
             FocusPrefs.updateClimberProgress(ctx, 0.1f)
         }
     }

     // Unproductive Tracking Effect
     // Unproductive Monitoring Effect
     LaunchedEffect(isFocusActive) {
         while (isFocusActive) {


             val currentUnproductive = stats.filter { unproductiveApps.contains(it.packageName) }
                 .sumOf { it.millis }
             unproductiveMillis = currentUnproductive
             delay(5000L) // Refresh every 5 seconds
             
             if (lastUnproductiveMillis != 0L && currentUnproductive > lastUnproductiveMillis) {
                 // User spent more time on distracting apps -> Move Down
                 climberProgress = (climberProgress - 0.05f).coerceAtLeast(0f)
                 FocusPrefs.updateClimberProgress(ctx, -0.05f)
             }
             lastUnproductiveMillis = currentUnproductive
             delay(10000L) // Check every 10 seconds
         }
     }

     Column(modifier = Modifier.padding(16.dp)) {
     Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
         Text("FocusGuard", style = MaterialTheme.typography.headlineSmall)
         Spacer(modifier = Modifier.height(12.dp))

         // Permissions shortcuts
         Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
             Button(onClick = { openUsageAccessSettings(ctx) }) { Text("Usage Access") }
             Button(onClick = { openAccessibilitySettings(ctx) }) { Text("Blocking Access") }
         
         // Mountain Visualizer
         Box(modifier = Modifier
             .fillMaxWidth()
             .height(200.dp)
             .background(Color(0xFFBBDEFB), MaterialTheme.shapes.medium)
         ) {
             // Simple Mountain Background placeholder
             Text("THE MOUNTAIN", modifier = Modifier.align(Alignment.TopCenter).padding(8.dp), style = MaterialTheme.typography.labelSmall)
             
             // Climber Character
             Box(modifier = Modifier
                 .fillMaxSize()
                 .padding(bottom = (animatedProgress * 150).dp), // Moves up based on progress
                 contentAlignment = Alignment.BottomCenter
             ) {
                 // Since I cannot upload images, I'll use a placeholder circle. 
                 // Please add your image to res/drawable and use Image(painterResource(R.drawable.climber)...)
                 Surface(
                     modifier = Modifier.size(60.dp),
                     shape = CircleShape,
                     color = Color.White,
                     border = BorderStroke(2.dp, Color.Brown)
                 ) {
                     Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                         Text("🧗", style = MaterialTheme.typography.headlineMedium)
                     }
                 }
             }
         }
         Spacer(modifier = Modifier.height(12.dp))

         // Focus Mode & Timer
         Spacer(modifier = Modifier.height(16.dp))
​
         // Timer Card
         Card(
             modifier = Modifier.fillMaxWidth(),
             colors = CardDefaults.cardColors(containerColor = if (isFocusActive) Color(0xFFE8F5E9) else Color(0xFFF5F5F5))
         ) {
             Column(modifier = Modifier.padding(16.dp)) {
                 Text("Current Goal: ${selected.name}", style = MaterialTheme.typography.titleMedium)
             Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                 Text(
                     text = String.format("%02d:%02d", timeLeftSeconds / 60, timeLeftSeconds % 60),
                     style = MaterialTheme.typography.displayLarge
                 )
                 
                 Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                     Button(onClick = { 


         Spacer(modifier = Modifier.height(16.dp))

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
         Text("Progress: ${(climberProgress * 100).toInt()}% of the peak reached", style = MaterialTheme.typography.bodyMedium)
         LinearProgressIndicator(progress = animatedProgress, modifier = Modifier.fillMaxWidth())

         Spacer(modifier = Modifier.height(16.dp))


         // Presets
         Text("Presets", style = MaterialTheme.typography.titleMedium)
         Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
         Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
             presetListDefault.take(3).forEach { preset ->
                 AssistChip(
                 FilterChip(
                     selected = selected.id == preset.id,
                     onClick = { 
                         selected = preset 
                         timeLeftSeconds = preset.focusMin * 60
                     },
                     label = { Text(preset.name) },
                     selected = selected.id == preset.id
                     label = { Text(preset.name) }
                 )
             }
         }

         Spacer(modifier = Modifier.height(12.dp))

         // Usage tracking
         Text("Real-time App Usage", style = MaterialTheme.typography.titleMedium)
         Button(onClick = {
             usageList = getTodayUsage(ctx).take(10)
         }) {
             Text("Refresh Usage")
         }
         Spacer(modifier = Modifier.height(16.dp))
         
         usageList.forEach { u ->
             Text("${u.packageName.split(".").last()}: ${u.millis / 60000} min")
         Button(onClick = { openAccessibilitySettings(ctx) }, modifier = Modifier.fillMaxWidth()) {
             Text("Configure App Blocking")
         }
     }
