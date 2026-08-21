package com.strk.jarvislauncher.voice.stt

import android.content.Context

/**
 * Wraps Vosk (offline STT, en-us small model ~50MB). This is the RAM-costly
 * stage of the pipeline (~250-350MB while active) — LAZY LOAD DISCIPLINE:
 *
 *   1. init() — only called right after wake-word detection fires
 *   2. transcribe() — captures mic audio, returns recognized text
 *   3. release() — MUST be called after every single command, even on
 *      error/timeout paths. Do not let this linger "just in case the user
 *      speaks again soon" — that's exactly the kind of creeping resident
 *      memory that causes the RAM issue on an 8GB device with other apps
 *      also running.
 *
 * Model file lives at assets/models/vosk-model-small-en-us/ — download
 * separately from https://alphacephei.com/vosk/models (not committed to
 * git directly given its size; document the download step in README).
 */
class SpeechToTextEngine(private val context: Context) {

    private var model: Any? = null // TODO: org.vosk.Model
    private var recognizer: Any? = null // TODO: org.vosk.Recognizer

    fun init() {
        // TODO: Model(context.assets, "models/vosk-model-small-en-us")
        // Run on a background thread — model loading is disk I/O + can take ~1-2s.
    }

    suspend fun transcribe(): String {
        TODO(
            "Capture AudioRecord stream, feed to Recognizer, return final result text. " +
            "Add a hard timeout (~6-8s) so a silent/confused mic doesn't hold the model " +
            "resident indefinitely — always fall through to release()."
        )
    }

    fun release() {
        // TODO: recognizer = null; model = null; System.gc() is NOT reliable here —
        // ensure Vosk's own native handles are explicitly closed if the library exposes a close()/free().
    }
}
