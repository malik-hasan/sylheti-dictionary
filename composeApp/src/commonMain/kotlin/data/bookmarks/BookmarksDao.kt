package data.bookmarks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import models.Bookmark

@Dao
interface BookmarksDao {
    @Query("SELECT * FROM Bookmark")
    fun getBookmarks(): Flow<List<Bookmark>>

    @Query("SELECT EXISTS(SELECT * FROM Bookmark WHERE entry_id = :entryId)")
    suspend fun checkBookmark(entryId: String): Boolean

    @Insert
    suspend fun addBookmark(entry: Bookmark)

    @Delete
    suspend fun removeBookmark(entry: Bookmark)
}
