package ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import models.Route
import models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.VariantEntry
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.see
import ui.app.LocalNavController
import ui.screens.search.LocalAnimatedContentScope
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.screens.search.LocalSharedTransitionScope
import ui.utils.SDString
import ui.utils.ifTrue

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SeeVariantButton(
    variantEntry: VariantEntry,
    entryId: String,
    includeAnimation: Boolean = true,
    navController: NavController = LocalNavController.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current
) {
    Box(Modifier.fillMaxWidth(), Alignment.Center) {
        with(sharedTransitionScope) {
            OutlinedButton(
                modifier = Modifier.ifTrue(includeAnimation) {
                    sharedBounds(
                        sharedContentState = rememberSharedContentState("see-variant-$entryId-${variantEntry.entryId}"),
                        animatedVisibilityScope = animatedContentScope
                    )
                },
                onClick = { navController.navigate(Route.Entry(variantEntry.entryId)) }
            ) {
                TaggedField(
                    enableSelection = false,
                    tag = stringResource(Res.string.see),
                    bodies = listOfNotNull(
                        SDString(variantEntry.citationIPA ?: variantEntry.lexemeIPA, mappedIpaHighlightRegex, SearchScript.LATIN),
                        (variantEntry.citationBengali ?: variantEntry.lexemeBengali)?.let {
                            SDString(it, highlightRegex, SearchScript.EASTERN_NAGRI)
                        },
                        (variantEntry.citationNagri ?: variantEntry.lexemeNagri)?.let { SDString(it, highlightRegex) }
                    ),
                    separator = " • "
                )
            }
        }
    }
}
