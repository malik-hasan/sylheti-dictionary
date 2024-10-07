package data.bookmarks

import data.dictionary.DictionaryDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import models.Bookmark
import oats.mobile.sylhetidictionary.DictionaryEntry

class BookmarksRepository(
    private val dictionaryDataSource: DictionaryDataSource,
    private val bookmarksDao: BookmarksDao
) {

    fun getBookmarks(): Flow<List<DictionaryEntry>> =
        bookmarksDao.getBookmarks().map { bookmarks ->
            val ids = bookmarks.map { it.entryId }
            dictionaryDataSource.getEntries(ids)
        }

    suspend fun checkBookmark(entryId: String) = bookmarksDao.checkBookmark(entryId)

    suspend fun addBookmark(entryId: String) = bookmarksDao.addBookmark(Bookmark(entryId))

    suspend fun removeBookmark(entryId: String) = bookmarksDao.removeBookmark(Bookmark(entryId))
}
