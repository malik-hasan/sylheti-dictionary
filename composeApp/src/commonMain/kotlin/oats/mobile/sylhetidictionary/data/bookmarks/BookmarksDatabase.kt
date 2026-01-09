package oats.mobile.sylhetidictionary.data.bookmarks

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import oats.mobile.sylhetidictionary.data.bookmarks.Bookmark

expect object BookmarksDatabaseConstructor : RoomDatabaseConstructor<BookmarksDatabase> {
    override fun initialize(): BookmarksDatabase
}

@Database(entities = [Bookmark::class], version = 1)
@ConstructedBy(BookmarksDatabaseConstructor::class)
abstract class BookmarksDatabase : RoomDatabase() {

    companion object {
        const val FILENAME = "bookmarks.db"
    }

    abstract fun dao(): BookmarksDao
}
