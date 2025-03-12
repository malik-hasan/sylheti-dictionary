package oats.mobile.sylhetidictionary.data.bookmarks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarksRepository(private val dao: BookmarksDao) {

    val bookmarksFlow: Flow<Set<String>>
        get() = dao.bookmarksFlow().map { bookmarks ->
            bookmarks.mapTo(mutableSetOf()) { it.entryId }
        }

    fun isBookmarkFlow(entryId: String) = dao.isBookmarkFlow(entryId)

    suspend fun addBookmark(entryId: String) = dao.addBookmark(Bookmark(entryId))

    suspend fun removeBookmark(entryId: String) = dao.removeBookmark(Bookmark(entryId))
}
