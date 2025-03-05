package oats.mobile.sylhetidictionary.data.bookmarks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bookmark(
    @PrimaryKey
    @ColumnInfo("entry_id")
    val entryId: String
)

@Entity
data class Bookmark(
    @PrimaryKey
    @ColumnInfo("entry_id")
    val entryId: String
)
