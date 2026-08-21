package com.strk.jarvislauncher.actions

/**
 * ============================================================
 * HARD SECURITY BOUNDARY — not a suggestion, not a "best effort."
 * ============================================================
 *
 * Any package listed here may ONLY ever be launched (ActionExecutor's
 * simplest "open app" path). No deep-link pre-filling, no Accessibility
 * Service interaction, no OCR/screen-reading, no auto-fill, ever —
 * regardless of what a future voice command or NLU intent claims to want.
 *
 * This list is checked at the ActionExecutor dispatch layer (not just
 * trusted to callers), so even a bug in NLU parsing can't accidentally
 * route a risky action at one of these packages.
 *
 * ADD TO THIS LIST: any banking app, payment app (PhonePe, GPay, iMobile),
 * authenticator/2FA app, Secure Folder, or anything handling credentials.
 *
 * TODO: populate exact package names once confirmed via:
 *   adb shell pm list packages -3
 * Examples to verify and add:
 *   "com.phonepe.app"
 *   "com.google.android.apps.nbu.paisa.user"   // GPay
 *   "com.csam.icici.bank.imobile"               // iMobile — verify exact ID
 *   "com.google.android.apps.authenticator2"    // or whichever authenticator is installed
 *   "com.samsung.knox.securefolder"             // verify exact ID
 */
object SecurityDenylist {

    val LAUNCH_ONLY_PACKAGES: Set<String> = setOf(
        // TODO: fill in real package names — see comment above
    )

    fun isRestricted(packageName: String): Boolean = packageName in LAUNCH_ONLY_PACKAGES

    /**
     * Called by every action handler before doing anything beyond a plain launch.
     * Throws deliberately rather than silently downgrading — a silent downgrade
     * could mask a bug where a restricted action almost went through.
     */
    fun assertActionAllowed(packageName: String, actionType: String) {
        if (isRestricted(packageName) && actionType != "LAUNCH_ONLY") {
            throw SecurityException(
                "Blocked: attempted '$actionType' on denylisted package '$packageName'. " +
                "Only LAUNCH_ONLY actions are permitted for this app."
            )
        }
    }
}
