package com.example.focusguard

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class FocusAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return
            
            val isFocusActive = FocusPrefs.isFocusActive(this)
            // Block list for demo
            val blockedApps = listOf("com.google.android.youtube", "com.android.vending", "com.android.settings")

            if (isFocusActive && blockedApps.contains(packageName)) {
                val intent = Intent(this, RedirectActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                startActivity(intent)
            }
        }
    }

    override fun onInterrupt() {
    }
}
