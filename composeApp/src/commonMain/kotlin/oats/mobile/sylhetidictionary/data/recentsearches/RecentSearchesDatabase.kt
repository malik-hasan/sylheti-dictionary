package oats.mobile.sylhetidictionary.data.recentsearches

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import oats.mobile.sylhetidictionary.models.RecentSearch

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object RecentSearchesDatabaseConstructor : RoomDatabaseConstructor<RecentSearchesDatabase>

@Database(entities = [RecentSearch::class], version = 1)
@ConstructedBy(RecentSearchesDatabaseConstructor::class)
abstract class RecentSearchesDatabase : RoomDatabase() {

    companion object {
        const val FILENAME = "recentSearches.db"
    }

    abstract fun dao(): RecentSearchesDao
}
