package com.strk.jarvislauncher.voice.nlu

data class ParsedIntent(
    val intentName: String,
    val slotValue: String?, // e.g. contact name, app name, alarm time — the "*" match
    val confidence: Double
)

/**
 * Turns raw STT text into a ParsedIntent the actions/ActionExecutor can act on.
 * Fully offline, zero extra RAM — this is deliberately the "boring" rule-based
 * layer we chose over an on-device LLM given the 8GB RAM constraint.
 */
object IntentParser {

    private val commandTable = listOf(
        CommandPattern("CALL_CONTACT", listOf("call *", "phone *", "dial *")),
        CommandPattern("OPEN_APP", listOf("open *", "launch *", "start *")),
        CommandPattern("SET_ALARM", listOf("set alarm for *", "wake me at *")),
        CommandPattern("SET_TIMER", listOf("set timer for *", "start a * timer")),
        CommandPattern("SEND_MESSAGE", listOf("text * saying *", "message * saying *")),
        CommandPattern("TOGGLE_FLASHLIGHT", listOf("turn on flashlight", "turn off flashlight")),
        CommandPattern("CHECK_CALENDAR", listOf("what's on my calendar *", "do i have any events *")),
        // TODO: expand this table as we build out each action module —
        // keep it data-driven so adding a new voice command never requires
        // touching parser logic, just adding a row here.
    )

    fun parse(rawText: String): ParsedIntent? {
        TODO(
            "1. Normalize rawText (lowercase, strip punctuation)\n" +
            "2. For each CommandPattern, try to match trigger templates, " +
            "extracting '*' slots as capture groups\n" +
            "3. If no exact template matches, fall back to FuzzyMatcher against " +
            "trigger templates before giving up\n" +
            "4. Return null (not a guess) if nothing clears threshold — " +
            "ActionExecutor should then have the assistant say " +
            "\"I didn't catch that\" rather than doing something wrong"
        )
    }
}
