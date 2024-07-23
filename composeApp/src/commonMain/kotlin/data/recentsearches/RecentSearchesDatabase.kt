package data.recentsearches

import androidx.room.Database
import androidx.room.RoomDatabase
import data.RoomCompileAppeaser
import models.RecentSearch

@Database(entities = [RecentSearch::class], version = 1)
abstract class RecentSearchesDatabase : RoomDatabase(), RoomCompileAppeaser {

    companion object {
        const val FILENAME = "recentSearches.db"
    }

    abstract fun dao(): RecentSearchesDao

    override fun clearAllTables() = super.clearAllTables()
}
