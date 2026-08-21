package com.strk.jarvislauncher.launcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.strk.jarvislauncher.databinding.ActivityLauncherBinding

/**
 * Home-screen replacement. Shows when the user presses Home, once this
 * app is set as default launcher.
 *
 * BUILD ORDER (per priority list):
 * 1. App drawer grid (via InstalledAppsRepository) — done.
 * 2. Mic button UI (visual only — wire to WakeWordService toggle later)
 * 3. Search bar with fuzzy app-name matching (reuse voice/nlu/FuzzyMatcher)
 */
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = AppDrawerAdapter(InstalledAppsRepository.getInstalledApps(this)) { app ->
            packageManager.getLaunchIntentForPackage(app.packageName)?.let { startActivity(it) }
        }
        binding.appGrid.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
        binding.appGrid.adapter = adapter

        // TODO: filter `adapter`'s list as binding.searchBar changes, once
        // voice/nlu/FuzzyMatcher.bestMatch is implemented — see README build order.
        // TODO: wire binding.micFab to the WakeWordService toggle once voice/wakeword/ lands.
    }

    override fun onBackPressed() {
        // Deliberately do nothing — this is Home, back shouldn't exit.
    }

    private companion object {
        const val GRID_SPAN_COUNT = 4
    }
}
