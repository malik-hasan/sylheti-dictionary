package data.recentsearches

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import models.RecentSearch

@Dao
interface RecentSearchesDao {
    @Query("SELECT * FROM RecentSearch")
    suspend fun getRecentSearches(): List<RecentSearch>

    @Insert
    suspend fun cacheSearch(recentSearch: RecentSearch)
}
