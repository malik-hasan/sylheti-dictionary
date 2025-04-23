package oats.mobile.sylhetidictionary.data.recentsearches

import androidx.room.Entity
import kotlinx.datetime.Clock
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchScript

@Entity(primaryKeys = ["term", "script"])
data class RecentSearch(
    val term: String,
    val script: SearchScript,
    val timestamp: Long = Clock.System.now().epochSeconds
)
