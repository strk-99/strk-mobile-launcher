package com.strk.jarvislauncher.voice.nlu

/**
 * One entry in the rule-based command table. Deliberately NOT an LLM prompt —
 * see our RAM discussion: a flexible on-device LLM would cost 1-2GB active,
 * risky on an 8GB device. This pattern + FuzzyMatcher combo gets most of the
 * "feels smart" experience for near-zero cost instead.
 *
 * Example entries (to be populated in IntentParser.commandTable):
 *   CommandPattern(intent = "CALL_CONTACT", triggers = listOf("call *", "phone *", "dial *"))
 *   CommandPattern(intent = "OPEN_APP", triggers = listOf("open *", "launch *", "start *"))
 *   CommandPattern(intent = "SET_ALARM", triggers = listOf("set alarm for *", "wake me at *"))
 */
data class CommandPattern(
    val intent: String,
    val triggers: List<String>, // "*" marks a variable slot (contact name, app name, time, etc.)
)
