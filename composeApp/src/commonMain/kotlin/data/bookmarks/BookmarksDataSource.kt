package data.bookmarks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import models.Bookmark

class BookmarksDataSource(private val dao: BookmarksDao) {

    val bookmarksFlow: Flow<Set<String>>
        get() = dao.bookmarksFlow().map { bookmarks ->
            bookmarks.mapTo(mutableSetOf()) { it.entryId }
        }

    suspend fun checkBookmark(entryId: String) = dao.checkBookmark(entryId)

    suspend fun addBookmark(entryId: String) = dao.addBookmark(Bookmark(entryId))

    suspend fun removeBookmark(entryId: String) = dao.removeBookmark(Bookmark(entryId))
}
