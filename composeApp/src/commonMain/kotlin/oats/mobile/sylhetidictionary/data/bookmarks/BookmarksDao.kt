package oats.mobile.sylhetidictionary.data.bookmarks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarksDao {
    @Query("SELECT * FROM Bookmark")
    fun bookmarksFlow(): Flow<List<Bookmark>>

    @Query("SELECT EXISTS(SELECT * FROM Bookmark WHERE entry_id = :entryId)")
    fun isBookmarkFlow(entryId: String): Flow<Boolean>

    @Insert
    suspend fun addBookmark(entry: Bookmark)

    @Delete
    suspend fun removeBookmark(entry: Bookmark)
}
