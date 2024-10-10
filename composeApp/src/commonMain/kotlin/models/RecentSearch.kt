package models

import androidx.room.Entity
import androidx.room.PrimaryKey
import models.search.settings.SearchScript

@Entity
data class RecentSearch(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val term: String,
    val script: SearchScript
)
