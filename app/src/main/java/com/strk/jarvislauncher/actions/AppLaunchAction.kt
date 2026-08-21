package com.strk.jarvislauncher.actions

import android.content.Context
import com.strk.jarvislauncher.launcher.InstalledAppsRepository
import com.strk.jarvislauncher.voice.nlu.FuzzyMatcher

/**
 * Generic "open [app name]" catch-all — resolves spoken app name against
 * installed apps via fuzzy match, then launches. This is deliberately the
 * ONLY thing that happens for denylisted packages (see SecurityDenylist) —
 * no pre-filling, no follow-up automation, just open and stop.
 */
object AppLaunchAction {

    fun execute(context: Context, spokenAppName: String?) {
        if (spokenAppName == null) return
        val installedApps = InstalledAppsRepository.getInstalledApps(context)
        val matchedLabel = FuzzyMatcher.bestMatch(spokenAppName, installedApps.map { it.label }) ?: run {
            // TODO: VoiceResponder.speak("I couldn't find an app called $spokenAppName")
            return
        }
        val app = installedApps.first { it.label == matchedLabel }

        // Pattern every other action module should follow:
        SecurityDenylist.assertActionAllowed(app.packageName, actionType = "LAUNCH_ONLY")

        val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
        launchIntent?.let { context.startActivity(it) }
    }
}
