package models

import androidx.room.Entity

@Entity(primaryKeys = ["term", "language"])
data class RecentSearch(
    val term: String,
    val language: String // TODO: enum
)
