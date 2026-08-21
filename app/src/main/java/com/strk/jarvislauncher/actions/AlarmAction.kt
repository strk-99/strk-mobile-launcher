package com.strk.jarvislauncher.actions

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock

/**
 * Zero-permission wins — AlarmClock intents are a stable public Android API,
 * work identically across OEMs including Samsung's Clock app. Build this
 * module FIRST to get the end-to-end voice pipeline proven before tackling
 * anything permission-heavy.
 */
object AlarmAction {

    fun setAlarm(context: Context, spokenTime: String?) {
        TODO(
            "Parse spokenTime (e.g. '7 AM', 'seven thirty') into hour/minute ints. " +
            "Intent(AlarmClock.ACTION_SET_ALARM).putExtra(AlarmClock.EXTRA_HOUR, h)" +
            ".putExtra(AlarmClock.EXTRA_MINUTES, m) — then context.startActivity(intent)"
        )
    }

    fun setTimer(context: Context, spokenDuration: String?) {
        TODO(
            "Parse spokenDuration (e.g. '10 minutes') into total seconds. " +
            "Intent(AlarmClock.ACTION_SET_TIMER).putExtra(AlarmClock.EXTRA_LENGTH, seconds)" +
            ".putExtra(AlarmClock.EXTRA_SKIP_UI, true) for a fully silent set, or false to confirm visually."
        )
    }
}
