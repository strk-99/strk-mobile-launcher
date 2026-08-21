package com.strk.jarvislauncher.actions

import android.content.Context
import com.strk.jarvislauncher.voice.nlu.ParsedIntent

/**
 * Single dispatch point — every parsed voice intent flows through here.
 * Centralizing dispatch (rather than letting IntentParser call actions
 * directly) is what makes SecurityDenylist enforcement reliable: there's
 * exactly one place where "am I allowed to do this" gets checked.
 */
class ActionExecutor(private val context: Context) {

    fun execute(intent: ParsedIntent) {
        when (intent.intentName) {
            "CALL_CONTACT" -> CallAction.execute(context, intent.slotValue)
            "OPEN_APP" -> AppLaunchAction.execute(context, intent.slotValue)
            "SET_ALARM" -> AlarmAction.setAlarm(context, intent.slotValue)
            "SET_TIMER" -> AlarmAction.setTimer(context, intent.slotValue)
            "TOGGLE_FLASHLIGHT" -> SettingsAction.toggleFlashlight(context)
            "CHECK_CALENDAR" -> TODO("CalendarAction — query CalendarContract, speak results via VoiceResponder")
            "SEND_MESSAGE" -> TODO("MessageAction — ACTION_SENDTO pre-fill, never silent SmsManager.sendTextMessage")
            else -> TODO("Fallback: speak \"I didn't catch that\" via VoiceResponder")
        }
        // NOTE: every action.execute() above is responsible for calling
        // SecurityDenylist.assertActionAllowed() internally before doing
        // anything beyond a plain launch — see AppLaunchAction.kt for the pattern.
    }
}
