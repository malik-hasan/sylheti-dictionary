package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.models.Route
import oats.mobile.sylhetidictionary.models.displayEN
import oats.mobile.sylhetidictionary.models.displayIPA
import oats.mobile.sylhetidictionary.models.displaySN
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.ui.app.LocalNavController
import oats.mobile.sylhetidictionary.ui.screens.search.LocalAnimatedContentScope
import oats.mobile.sylhetidictionary.ui.screens.search.LocalHighlightRegex
import oats.mobile.sylhetidictionary.ui.screens.search.LocalSharedTransitionScope
import oats.mobile.sylhetidictionary.ui.utils.SDString
import oats.mobile.sylhetidictionary.ui.utils.ifTrue
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.see

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ReferenceButton(
    referenceEntry: DictionaryEntry,
    entryId: String,
    includeAnimation: Boolean = true,
    navController: NavController = LocalNavController.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current
) {
    Box(Modifier.fillMaxWidth(), Alignment.Center) {
        with(sharedTransitionScope) {
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .ifTrue(includeAnimation) {
                        sharedBounds(
                            sharedContentState = rememberSharedContentState("see-variant-$entryId-${referenceEntry.entryId}"),
                            animatedVisibilityScope = animatedContentScope
                        )
                    },
                onClick = { navController.navigate(Route.Entry(referenceEntry.entryId)) }
            ) {
                TaggedField(
                    enableSelection = false,
                    tag = stringResource(Res.string.see),
                    bodies = listOfNotNull(
                        SDString(referenceEntry.displayIPA, highlightRegex, SearchScript.LATIN),
                        referenceEntry.displayEN?.let {
                            SDString(it, highlightRegex, SearchScript.EASTERN_NAGRI)
                        },
                        referenceEntry.displaySN?.let { SDString(it, highlightRegex) }
                    ),
                    separator = " • "
                )
            }
        }
    }
}
