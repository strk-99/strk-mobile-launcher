package com.strk.jarvislauncher.voice.nlu

/**
 * Lightweight fuzzy string matching — NOT a model, just edit-distance /
 * token-overlap scoring. This is what lets "call mom", "phone mom", and
 * "dial mom" all resolve to the same intent+slot without needing an LLM.
 *
 * Also used by InstalledAppsRepository lookups so "open whatsapp" matches
 * even if the recognized text is slightly mangled by STT (e.g. "open what's app").
 */
object FuzzyMatcher {

    fun bestMatch(input: String, candidates: List<String>, threshold: Double = 0.7): String? {
        TODO(
            "Implement Levenshtein-distance-based scoring (or simple token " +
            "Jaccard similarity — cheaper, often good enough for short phrases). " +
            "Return null if best score < threshold rather than guessing wrong."
        )
    }
}
