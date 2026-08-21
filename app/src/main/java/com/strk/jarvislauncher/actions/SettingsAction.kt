package com.strk.jarvislauncher.actions

import android.content.Context

object SettingsAction {
    fun toggleFlashlight(context: Context) {
        TODO(
            "CameraManager.setTorchMode(cameraId, enabled) — works silently, " +
            "no restriction, unlike WiFi/Bluetooth which need the Settings.Panel " +
            "workaround (see workflow doc section 2 for why)"
        )
    }
    // TODO: brightness (WRITE_SETTINGS, manual grant), DND (ACCESS_NOTIFICATION_POLICY,
    // manual grant), dark mode (UiModeManager, no permission) — add as separate functions
    // following this same pattern once flashlight is proven working.
}
