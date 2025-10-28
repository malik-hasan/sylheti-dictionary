package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.ui.theme.latinDisplayFontFamily

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TaggedEntryCard(
    tag: String?,
    entry: DictionaryEntry,
    featureBengaliDefinitions: Boolean,
    includeAnimation: Boolean,
    navigateToEntry: (String) -> Unit,
    setBookmark: (Boolean) -> Unit
) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        tag?.let {
            FieldTag(
                tag = it,
                tagFontFamily = latinDisplayFontFamily,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        EntryCard(
            entry = entry,
            featureBengaliDefinitions = featureBengaliDefinitions,
            includeAnimation = includeAnimation,
            navigateToEntry = navigateToEntry,
            setBookmark = setBookmark
        )
    }
}
