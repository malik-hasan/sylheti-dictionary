package data.bookmarks

import data.dictionary.DictionaryDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import models.Bookmark
import oats.mobile.sylhetidictionary.DictionaryEntry

class BookmarksRepository(
    private val dao: BookmarksDao,
    private val dictionary: DictionaryDataSource
) {

    fun getBookmarks(): Flow<List<DictionaryEntry>> =
        dao.getBookmarks().map { bookmarks ->
            val ids = bookmarks.map { it.entryId }
            dictionary.getEntries(ids)
        }

    suspend fun checkBookmark(entryId: String) = dao.checkBookmark(entryId)

    suspend fun addBookmark(entryId: String) = dao.addBookmark(Bookmark(entryId))

    suspend fun removeBookmark(entryId: String) = dao.removeBookmark(Bookmark(entryId))
}
