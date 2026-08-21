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
        val inputTokens = tokenize(input)
        if (inputTokens.isEmpty()) return null

        var best: String? = null
        var bestScore = 0.0
        for (candidate in candidates) {
            val score = similarity(inputTokens, tokenize(candidate))
            if (score > bestScore) {
                bestScore = score
                best = candidate
            }
        }
        return best?.takeIf { bestScore >= threshold }
    }

    private fun tokenize(text: String): Set<String> =
        text.lowercase().split(Regex("[^a-z0-9]+")).filter { it.isNotBlank() }.toSet()

    /**
     * Token Jaccard similarity, falling back to whole-string edit distance when there's
     * no token overlap at all — covers STT mangling a single word ("whatsapp" -> "what's app").
     */
    private fun similarity(a: Set<String>, b: Set<String>): Double {
        if (a.isEmpty() || b.isEmpty()) return 0.0
        val jaccard = a.intersect(b).size.toDouble() / a.union(b).size
        if (jaccard > 0.0) return jaccard

        val joinedA = a.sorted().joinToString("")
        val joinedB = b.sorted().joinToString("")
        val maxLen = maxOf(joinedA.length, joinedB.length)
        if (maxLen == 0) return 0.0
        return 1.0 - (levenshtein(joinedA, joinedB).toDouble() / maxLen)
    }

    private fun levenshtein(a: String, b: String): Int {
        val dp = Array(a.length + 1) { IntArray(b.length + 1) }
        for (i in 0..a.length) dp[i][0] = i
        for (j in 0..b.length) dp[0][j] = j
        for (i in 1..a.length) {
            for (j in 1..b.length) {
                dp[i][j] = if (a[i - 1] == b[j - 1]) {
                    dp[i - 1][j - 1]
                } else {
                    1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
                }
            }
        }
        return dp[a.length][b.length]
    }
}
