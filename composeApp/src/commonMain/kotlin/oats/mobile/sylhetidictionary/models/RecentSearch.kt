package oats.mobile.sylhetidictionary.models

import androidx.room.Entity
import kotlinx.datetime.Clock
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript

@Entity(primaryKeys = ["term", "script"])
data class RecentSearch(
    val term: String,
    val script: SearchScript,
    val timestamp: Long = Clock.System.now().epochSeconds
)
