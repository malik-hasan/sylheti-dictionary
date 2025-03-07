package oats.mobile.sylhetidictionary.data.bookmarks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bookmark(
    @PrimaryKey
    val entryId: String
)
