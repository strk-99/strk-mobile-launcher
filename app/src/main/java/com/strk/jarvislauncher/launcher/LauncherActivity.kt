package com.strk.jarvislauncher.launcher

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.strk.jarvislauncher.R
import com.strk.jarvislauncher.databinding.ActivityLauncherBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Home-screen replacement — JARVIS-style radial layout. Shows when the user
 * presses Home, once this app is set as default launcher.
 *
 * Center "reactor" button opens the full app grid (AppDrawerActivity); the
 * ring shows a handful of favorite apps for one-tap access. Search bar and
 * mic FAB stay as placeholders — see their TODOs below.
 */
class LauncherActivity : AppCompatActivity() {

    private val clockHandler = Handler(Looper.getMainLooper())
    private lateinit var timeText: TextView
    private val clockTicker = object : Runnable {
        override fun run() {
            updateClock()
            clockHandler.postDelayed(this, CLOCK_TICK_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timeText = binding.timeText
        buildDayStrip(binding.dayStrip)
        buildRadialMenu(binding.radialMenu)

        // TODO: filter apps as binding.searchBar changes, using
        // voice/nlu/FuzzyMatcher.bestMatch (now implemented) against app labels.
        // TODO: wire binding.micFab to the WakeWordService toggle once voice/wakeword/ lands.
        // TODO: FAVORITE_COUNT below just takes the first N apps alphabetically —
        // replace with a real user-configurable favorites list (see SettingsActivity).
    }

    override fun onResume() {
        super.onResume()
        clockHandler.post(clockTicker)
    }

    override fun onPause() {
        super.onPause()
        clockHandler.removeCallbacks(clockTicker)
    }

    override fun onBackPressed() {
        // Deliberately do nothing — this is Home, back shouldn't exit.
    }

    private fun updateClock() {
        timeText.text = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Calendar.getInstance().time)
    }

    private fun buildDayStrip(dayStrip: LinearLayout) {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val cursor = Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY) }
        val format = SimpleDateFormat("EEE", Locale.getDefault())

        repeat(7) {
            val isToday = cursor.get(Calendar.DAY_OF_WEEK) == today
            val label = TextView(this).apply {
                text = format.format(cursor.time).uppercase(Locale.getDefault())
                textSize = 11f
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                setTextColor(ContextCompat.getColor(context, if (isToday) R.color.hud_cyan else R.color.hud_text_dim))
                if (isToday) setTypeface(typeface, Typeface.BOLD)
            }
            dayStrip.addView(label)
            cursor.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    private fun buildRadialMenu(radialMenu: RadialMenuLayout) {
        radialMenu.addView(buildReactorView())
        InstalledAppsRepository.getInstalledApps(this).take(FAVORITE_COUNT).forEach { app ->
            radialMenu.addView(buildRingIconView(app))
        }
    }

    private fun buildReactorView(): View {
        val size = dpToPx(96)
        return View(this).apply {
            layoutParams = ViewGroup.LayoutParams(size, size)
            background = ContextCompat.getDrawable(context, R.drawable.bg_arc_reactor)
            foreground = rippleForeground()
            isClickable = true
            isFocusable = true
            contentDescription = getString(R.string.app_drawer_description)
            setOnClickListener { startActivity(Intent(this@LauncherActivity, AppDrawerActivity::class.java)) }
        }
    }

    private fun buildRingIconView(app: InstalledApp): View {
        val size = dpToPx(56)
        val padding = dpToPx(10)
        return ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(size, size)
            background = ContextCompat.getDrawable(context, R.drawable.bg_radial_icon)
            foreground = rippleForeground()
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            setPadding(padding, padding, padding, padding)
            setImageDrawable(packageManager.getApplicationIcon(app.appInfo))
            contentDescription = app.label
            isClickable = true
            isFocusable = true
            setOnClickListener {
                packageManager.getLaunchIntentForPackage(app.packageName)?.let { startActivity(it) }
            }
        }
    }

    private fun rippleForeground() = TypedValue().let {
        theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, it, true)
        ContextCompat.getDrawable(this, it.resourceId)
    }

    private fun dpToPx(dp: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()

    private companion object {
        const val FAVORITE_COUNT = 8
        const val CLOCK_TICK_MS = 30_000L
    }
}
