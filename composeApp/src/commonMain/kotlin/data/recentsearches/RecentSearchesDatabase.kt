package data.recentsearches

import androidx.room.Database
import androidx.room.RoomDatabase
import models.RecentSearch

@Database(entities = [RecentSearch::class], version = 1)
abstract class RecentSearchesDatabase : RoomDatabase() {
    abstract fun recentSearchesDao(): RecentSearchesDao
}
