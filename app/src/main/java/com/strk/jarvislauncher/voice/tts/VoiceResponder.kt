package com.strk.jarvislauncher.voice.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * Wraps Android's built-in TextToSpeech — already offline-capable once the
 * en-US voice pack is downloaded (one-time, via device Settings > Text-to-speech,
 * or we can prompt the user to install it on first run). No extra RAM cost
 * beyond what the OS already manages for this system service.
 */
class VoiceResponder(context: Context) {

    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                isReady = true
            }
            // TODO: if status != SUCCESS or language data missing, surface a
            // one-time prompt directing the user to install the offline voice pack.
        }
    }

    fun speak(text: String) {
        if (!isReady) return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
