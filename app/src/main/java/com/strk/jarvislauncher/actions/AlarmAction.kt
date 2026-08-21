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

    // Common spoken number words only — this is rule-based parsing per the
    // project's no-on-device-LLM constraint, not a general number parser.
    private val WORD_NUMBERS = mapOf(
        "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5,
        "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9, "ten" to 10,
        "eleven" to 11, "twelve" to 12, "thirteen" to 13, "fourteen" to 14,
        "fifteen" to 15, "twenty" to 20, "thirty" to 30, "forty" to 40,
        "forty five" to 45, "fifty" to 50
    )

    fun setAlarm(context: Context, spokenTime: String?) {
        val (hour, minute) = spokenTime?.let(::parseTime) ?: return
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
            .putExtra(AlarmClock.EXTRA_HOUR, hour)
            .putExtra(AlarmClock.EXTRA_MINUTES, minute)
            .putExtra(AlarmClock.EXTRA_SKIP_UI, false)
        if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
    }

    fun setTimer(context: Context, spokenDuration: String?) {
        val seconds = spokenDuration?.let(::parseDurationSeconds) ?: return
        val intent = Intent(AlarmClock.ACTION_SET_TIMER)
            .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
            .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
    }

    /** Handles "7 AM", "7:30 pm", "seven", "seven thirty" — device's 24h hour/minute. */
    private fun parseTime(spoken: String): Pair<Int, Int>? {
        val text = spoken.trim().lowercase()

        Regex("""(\d{1,2})(?::(\d{2}))?\s*(am|pm)?""").find(text)?.let { m ->
            var hour = m.groupValues[1].toIntOrNull() ?: return null
            val minute = m.groupValues[2].toIntOrNull() ?: 0
            if (hour !in 0..23) return null
            when (m.groupValues[3]) {
                "pm" -> if (hour < 12) hour += 12
                "am" -> if (hour == 12) hour = 0
            }
            return hour to minute
        }

        val words = text.split(Regex("\\s+"))
        val hour = WORD_NUMBERS[words.getOrNull(0)] ?: return null
        val minute = WORD_NUMBERS[words.drop(1).joinToString(" ")] ?: 0
        var resolvedHour = hour
        if (text.contains("pm") && resolvedHour < 12) resolvedHour += 12
        return resolvedHour to minute
    }

    /** Handles "10 minutes", "1 hour", "90 seconds" — total seconds. */
    private fun parseDurationSeconds(spoken: String): Int? {
        val match = Regex("""(\d+)\s*(second|minute|hour)""").find(spoken.trim().lowercase()) ?: return null
        val amount = match.groupValues[1].toIntOrNull() ?: return null
        return when (match.groupValues[2]) {
            "second" -> amount
            "minute" -> amount * 60
            "hour" -> amount * 3600
            else -> null
        }
    }
}
