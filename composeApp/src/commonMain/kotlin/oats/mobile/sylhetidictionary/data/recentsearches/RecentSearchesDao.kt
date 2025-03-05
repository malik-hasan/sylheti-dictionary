package oats.mobile.sylhetidictionary.data.recentsearches

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript

@Dao
interface RecentSearchesDao {
    @Query("SELECT * FROM RecentSearch ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentSearches(): List<RecentSearch>

    @Query("SELECT * FROM RecentSearch WHERE script = :script ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentSearches(script: SearchScript): List<RecentSearch>

    @Query("SELECT * FROM RecentSearch WHERE term GLOB :suggestionQuery ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentSearches(suggestionQuery: String): List<RecentSearch>

    @Query("SELECT * FROM RecentSearch WHERE script = :script AND term GLOB :suggestionQuery ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentSearches(suggestionQuery: String, script: SearchScript): List<RecentSearch>

    @Upsert
    suspend fun insert(recentSearch: RecentSearch)

    @Query(
        "DELETE FROM RecentSearch " +
        "WHERE timestamp = (" +
            "SELECT timestamp FROM RecentSearch " +
            "WHERE script = :script " +
            "GROUP BY script " +
            "HAVING COUNT(*) >= 10 " +
            "ORDER BY timestamp " +
            "LIMIT 1" +
        ")"
    ) suspend fun deleteOldestSearch(script: SearchScript)

    @Transaction
    suspend fun cacheSearch(term: String, script: SearchScript) {
        deleteOldestSearch(script)
        insert(RecentSearch(term, script))
    }
}
