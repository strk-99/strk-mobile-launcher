package com.strk.jarvislauncher.launcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Home-screen replacement. Shows when the user presses Home, once this
 * app is set as default launcher.
 *
 * BUILD ORDER (per priority list):
 * 1. App drawer grid (via InstalledAppsRepository) — get this solid first.
 * 2. Mic button UI (visual only — wire to WakeWordService toggle later)
 * 3. Search bar with fuzzy app-name matching (reuse voice/nlu/FuzzyMatcher)
 */
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: setContentView(R.layout.activity_launcher)
        // TODO: RecyclerView.adapter = AppDrawerAdapter(InstalledAppsRepository.getInstalledApps(this))
    }

    override fun onBackPressed() {
        // Deliberately do nothing — this is Home, back shouldn't exit.
    }
}
