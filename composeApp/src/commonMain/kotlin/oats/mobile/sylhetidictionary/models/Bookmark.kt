package oats.mobile.sylhetidictionary.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bookmark(
    @PrimaryKey
    @ColumnInfo("entry_id")
    val entryId: String
)
