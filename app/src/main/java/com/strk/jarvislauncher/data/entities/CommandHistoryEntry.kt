package com.strk.jarvislauncher.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local-only, never synced anywhere — see our zero-network-calls-in-voice-pipeline
 * rule. Purely for the user's own reference ("what did I say last time") and
 * for debugging the NLU's match accuracy during development.
 */
@Entity(tableName = "command_history")
data class CommandHistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rawTranscript: String,
    val matchedIntent: String?,
    val timestamp: Long
)
