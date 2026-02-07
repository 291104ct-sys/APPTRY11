package com.example.focusguard

data class PomodoroPreset(
    val id: String,          // stable key for saving/loading (e.g., "classic")
    val name: String,        // label shown in UI (e.g., "Classic")
    val focusMin: Int,       // focus duration
    val shortBreakMin: Int,  // short break duration
    val longBreakMin: Int,   // long break duration
    val longBreakAfter: Int  // long break after N focus sessions
)
