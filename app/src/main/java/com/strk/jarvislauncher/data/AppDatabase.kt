package com.strk.jarvislauncher.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.strk.jarvislauncher.data.entities.CommandHistoryEntry

@Database(entities = [CommandHistoryEntry::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun commandHistoryDao(): CommandHistoryDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "jarvis_launcher.db"
                ).build().also { INSTANCE = it }
            }
    }
}
