package data.recentsearches

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import models.RecentSearch
import models.search.settings.SearchScript

@Dao
interface RecentSearchesDao {
    @Query("SELECT * FROM RecentSearch ORDER BY id DESC LIMIT 10")
    suspend fun getRecentSearches(): List<RecentSearch>

    @Query("SELECT * FROM RecentSearch WHERE script = :script ORDER BY id DESC LIMIT 10")
    suspend fun getRecentSearches(script: SearchScript): List<RecentSearch>

    @Insert
    suspend fun insert(recentSearch: RecentSearch)

    @Query("DELETE FROM RecentSearch " +
            "WHERE id = (SELECT MIN(id) FROM RecentSearch WHERE script = :script) " +
            "AND (SELECT COUNT(id) FROM RecentSearch WHERE script = :script) >= 10")
    suspend fun deleteOldestSearch(script: SearchScript)

    suspend fun cacheSearch(recentSearch: RecentSearch) {
        deleteOldestSearch(recentSearch.script)
        insert(recentSearch)
    }
}
