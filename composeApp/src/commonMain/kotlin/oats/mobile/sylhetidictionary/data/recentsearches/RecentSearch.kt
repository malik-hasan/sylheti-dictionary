package oats.mobile.sylhetidictionary.data.recentsearches

import androidx.room.Entity
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchScript
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Entity(primaryKeys = ["term", "script"])
data class RecentSearch(
    val term: String,
    val script: SearchScript,
    val timestamp: Long = Clock.System.now().epochSeconds
)
