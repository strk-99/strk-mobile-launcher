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
 * Populated from the apps actually installed on the target device (Samsung F15 5G).
 * Still confirm every entry with `adb shell pm list packages -3` before relying on
 * this — a wrong package string here is silently ineffective (not dangerous, just
 * not protecting anything), so don't treat this list as verified until you have.
 */
object SecurityDenylist {

    val LAUNCH_ONLY_PACKAGES: Set<String> = setOf(
        "com.phonepe.app",                          // PhonePe
        "com.google.android.apps.nbu.paisa.user",   // Google Pay
        "com.csam.icici.bank.imobile",              // ICICI iMobile Pay
        "com.samsung.android.securefolder",         // Samsung Secure Folder
        "com.google.android.apps.authenticator2",   // Authenticator — verify this is the
                                                     // installed one and not Microsoft
                                                     // Authenticator (com.azure.authenticator)
        // TODO: Zoho Payroll is also installed and handles sensitive payroll/financial
        // data — add it once its exact package ID is confirmed via adb.
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
