package com.strk.jarvislauncher

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MainApplication : Application() {

    companion object {
        const val WAKE_WORD_CHANNEL_ID = "wake_word_listening_channel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        // TODO: init lightweight singletons only (SecurityDenylist, SettingsRepository).
        // Do NOT init Vosk/Porcupine here — they should only load when the user
        // actually toggles voice ON. Application.onCreate() runs on every process
        // start, so anything heavy here defeats the whole lazy-load RAM strategy.
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                WAKE_WORD_CHANNEL_ID,
                "Voice Assistant Listening",
                NotificationManager.IMPORTANCE_LOW // low = no sound/heads-up, just a quiet persistent icon
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
