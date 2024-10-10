package models

import androidx.room.Entity
import androidx.room.PrimaryKey
import models.search.settings.SearchScript

@Entity
data class RecentSearch(
    val term: String,
    val script: SearchScript,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
