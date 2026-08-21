package com.strk.jarvislauncher.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.strk.jarvislauncher.data.entities.CommandHistoryEntry

@Dao
interface CommandHistoryDao {
    @Insert
    suspend fun insert(entry: CommandHistoryEntry)

    @Query("SELECT * FROM command_history ORDER BY timestamp DESC LIMIT 50")
    suspend fun getRecent(): List<CommandHistoryEntry>

    // TODO: add a clearAll() for a "reset my data" option in Settings
}
