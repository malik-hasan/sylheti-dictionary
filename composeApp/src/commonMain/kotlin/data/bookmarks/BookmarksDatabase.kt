package data.bookmarks

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import models.Bookmark

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object BookmarksDatabaseConstructor : RoomDatabaseConstructor<BookmarksDatabase>

@Database(entities = [Bookmark::class], version = 1)
@ConstructedBy(BookmarksDatabaseConstructor::class)
abstract class BookmarksDatabase : RoomDatabase() {

    companion object {
        const val FILENAME = "bookmarks.db"
    }

    abstract fun dao(): BookmarksDao
}
