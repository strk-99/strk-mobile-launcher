package com.strk.jarvislauncher.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.strk.jarvislauncher.voice.wakeword.WakeWordManager

/**
 * Home of THE toggle — "Voice Assistant: ON/OFF". This is the single control
 * point for the whole wake-word RAM/battery tradeoff we designed around:
 * OFF by default, user explicitly opts into always-listening mode.
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: setContentView(R.layout.activity_settings)
        // TODO: voiceToggleSwitch.isChecked = <read persisted state>
        // TODO: voiceToggleSwitch.setOnCheckedChangeListener { _, isChecked ->
        //     if (isChecked) WakeWordManager.start(this) else WakeWordManager.stop(this)
        // }
        // TODO: also surface a live "current denylisted apps" read-only list here
        // for transparency — user should be able to see what's excluded from automation.
    }
}
