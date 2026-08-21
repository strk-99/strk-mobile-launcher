package com.strk.jarvislauncher.launcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.strk.jarvislauncher.databinding.ActivityAppDrawerBinding

/**
 * Full alphabetical app grid — reached by tapping the reactor button on the
 * radial home screen (LauncherActivity). Normal back-stack screen, unlike
 * Home: back should close it and return to the radial view.
 */
class AppDrawerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAppDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = AppDrawerAdapter(InstalledAppsRepository.getInstalledApps(this)) { app ->
            packageManager.getLaunchIntentForPackage(app.packageName)?.let { startActivity(it) }
        }
        binding.appGrid.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
        binding.appGrid.adapter = adapter

        // TODO: filter `adapter`'s list as binding.searchBar changes, using
        // voice/nlu/FuzzyMatcher.bestMatch (now implemented) against app labels.
    }

    private companion object {
        const val GRID_SPAN_COUNT = 4
    }
}
