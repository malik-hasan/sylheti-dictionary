package oats.mobile.sylhetidictionary.data.bookmarks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarksDao {
    @Query("SELECT * FROM Bookmark")
    fun bookmarksFlow(): Flow<List<Bookmark>>

    @Query("SELECT EXISTS(SELECT * FROM Bookmark WHERE entryId = :entryId)")
    fun isBookmarkFlow(entryId: String): Flow<Boolean>

    @Upsert
    suspend fun addBookmark(entry: Bookmark)

    @Delete
    suspend fun removeBookmark(entry: Bookmark)
}
