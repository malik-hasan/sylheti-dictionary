package data.recentsearches

import androidx.room.Database
import androidx.room.RoomDatabase
import data.RoomCompileAppeaser
import models.RecentSearch

@Database(entities = [RecentSearch::class], version = 1)
abstract class RecentSearchesDatabase : RoomDatabase(), RoomCompileAppeaser {
    abstract fun recentSearchesDao(): RecentSearchesDao

    override fun clearAllTables() = super.clearAllTables()
}
