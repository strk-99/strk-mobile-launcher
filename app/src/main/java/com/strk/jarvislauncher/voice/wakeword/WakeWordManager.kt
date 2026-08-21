package com.strk.jarvislauncher.voice.wakeword

import android.content.Context
import android.content.Intent

/**
 * Simple start/stop facade called from the Settings toggle switch.
 * Keeps LauncherActivity/SettingsActivity from needing to know Service
 * internals directly.
 */
object WakeWordManager {

    fun start(context: Context) {
        context.startForegroundService(Intent(context, WakeWordService::class.java))
        // TODO: persist "voiceEnabled = true" to SharedPreferences/DataStore
        // so state survives app process death and the toggle reflects reality.
    }

    fun stop(context: Context) {
        context.stopService(Intent(context, WakeWordService::class.java))
        // TODO: persist "voiceEnabled = false"
    }
}
