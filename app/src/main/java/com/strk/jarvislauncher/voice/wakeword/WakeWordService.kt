package com.strk.jarvislauncher.voice.wakeword

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.strk.jarvislauncher.MainApplication

/**
 * Foreground service that runs Porcupine listening for "Hi Strk".
 *
 * LIFECYCLE — this is the critical RAM/battery discipline we agreed on:
 * - ONLY started when the user flips the voice toggle ON in Settings.
 * - NEVER auto-starts at boot (no BOOT_COMPLETED receiver — deliberate).
 * - When toggled OFF, this service is fully STOPPED and Porcupine's
 *   native resources released — not paused, not backgrounded. Verify
 *   with a memory profiler that RAM actually drops after stop(), not
 *   just that the service object is gone.
 * - On wake-word detection, this hands off to SpeechToTextEngine
 *   (voice/stt/) which loads the Vosk model ONLY at that moment,
 *   processes one command, then releases itself again. This service
 *   itself stays resident (it's cheap, ~15-30MB) but never holds the
 *   heavier STT model.
 */
class WakeWordService : Service() {

    private var porcupineManager: Any? = null // TODO: ai.picovoice.porcupine.PorcupineManager instance

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildNotification())
        startListening()
        return START_STICKY
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, MainApplication.WAKE_WORD_CHANNEL_ID)
            .setContentTitle("Listening for \"Hi Strk\"")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now) // TODO: replace with real icon
            .setOngoing(true)
            .build()
    }

    private fun startListening() {
        // TODO:
        // 1. Load assets/models/hi_strk.ppn (custom Porcupine keyword file)
        // 2. PorcupineManager.Builder(...).setKeyword(...).build(context) { onWakeWordDetected() }
        // 3. porcupineManager.start()
    }

    private fun onWakeWordDetected() {
        // TODO: launch SpeechToTextEngine to capture + transcribe the command
        // that follows, hand result to voice/nlu/IntentParser
    }

    override fun onDestroy() {
        // TODO: porcupineManager?.delete() — release native resources, don't just null the reference
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val NOTIFICATION_ID = 1001
    }
}
