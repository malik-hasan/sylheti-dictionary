package data.recentsearches

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import models.RecentSearch
import models.search.settings.SearchScript

@Dao
interface RecentSearchesDao {
    @Query("SELECT term FROM RecentSearch ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentSearches(): List<String>

    @Query("SELECT term FROM RecentSearch WHERE script = :script ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentSearches(script: SearchScript): List<String>

    @Query("SELECT term FROM RecentSearch WHERE term GLOB :term || '*' ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentSearches(term: String): List<String>

    @Query("SELECT term FROM RecentSearch WHERE script = :script AND term GLOB :term || '*' ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentSearches(term: String, script: SearchScript): List<String>

    @Upsert
    suspend fun insert(recentSearch: RecentSearch)

    @Query("DELETE FROM RecentSearch " +
            "WHERE timestamp = (SELECT MIN(timestamp) FROM RecentSearch WHERE script = :script) " +
            "AND (SELECT COUNT(*) FROM RecentSearch WHERE script = :script) >= 10")
    suspend fun deleteOldestSearch(script: SearchScript)

    @Transaction
    suspend fun cacheSearch(term: String, script: SearchScript) {
        deleteOldestSearch(script)
        insert(RecentSearch(term, script))
    }
}
