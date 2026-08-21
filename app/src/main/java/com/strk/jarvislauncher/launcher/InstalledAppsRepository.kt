package com.strk.jarvislauncher.launcher

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

data class InstalledApp(
    val label: String,
    val packageName: String,
    val appInfo: ApplicationInfo
)

/**
 * Single source of truth for "what apps exist on this phone" — used by both
 * the app drawer AND voice NLU's app-name matching (see voice/nlu/IntentParser.kt).
 * Querying PackageManager at runtime (rather than hardcoding package names)
 * is what makes this self-updating as apps are installed/uninstalled — this
 * matters for apps like your "DSO STRK" one where we can't reliably guess
 * the package ID ahead of time.
 */
object InstalledAppsRepository {

    fun getInstalledApps(context: Context): List<InstalledApp> {
        val pm = context.packageManager
        val apps = pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0))
        return apps
            .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
            .map { InstalledApp(label = pm.getApplicationLabel(it).toString(), packageName = it.packageName, appInfo = it) }
            .sortedBy { it.label.lowercase() }
        // TODO: cache this list; invalidate on ACTION_PACKAGE_ADDED / _REMOVED
        // broadcast instead of re-querying PackageManager on every drawer open.
    }
}
