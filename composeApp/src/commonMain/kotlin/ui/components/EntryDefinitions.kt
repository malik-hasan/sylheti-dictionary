package ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.FieldTag
import models.search.settings.SearchScript
import models.settings.Language
import oats.mobile.sylhetidictionary.DictionaryEntry
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.sylheti
import ui.app.LocalLanguage
import ui.screens.search.LocalAnimatedContentScope
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.screens.search.LocalSharedTransitionScope
import ui.utils.SDString
import ui.utils.ifTrue
import utility.validateStrings

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EntryDefinitions(
    entry: DictionaryEntry,
    modifier: Modifier = Modifier,
    includeAnimation: Boolean = true,
    language: Language = LocalLanguage.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current
) {
    with(sharedTransitionScope) {
        with(entry) {
            val definitions = remember {
                mapOf(
                    FieldTag.EN to definitionEN,
                    FieldTag.BN to definitionBN,
                    FieldTag.BNIPA to definitionBNIPA,
                    FieldTag.SYLHETI_NAGRI to definitionNagri,
                    FieldTag.IPA to definitionIPA
                ).validateStrings()
            }

            @Composable
            fun EnglishDefinition() {
                definitions[FieldTag.EN]?.let { definition ->
                    TaggedField(
                        tag = stringResource(Res.string.english),
                        body = SDString(definition, highlightRegex, SearchScript.LATIN)
                    )
                }
            }

            if (definitions.isNotEmpty()) {
                Column(modifier
                    .padding(horizontal = 16.dp)
                    .ifTrue(includeAnimation) {
                        sharedBounds(
                            sharedContentState = rememberSharedContentState("definitions-$entryId"),
                            animatedVisibilityScope = animatedContentScope
                        )
                    }
                ) {
                    if (language != Language.BN) EnglishDefinition()

                    if (FieldTag.bengaliTags.any { it in definitions }) {
                        TaggedField(
                            tag = stringResource(Res.string.bengali),
                            bodies = listOfNotNull(
                                definitions[FieldTag.BN]?.let {
                                    SDString(it, highlightRegex, SearchScript.EASTERN_NAGRI)
                                },
                                definitions[FieldTag.BNIPA]?.let {
                                    SDString(it, mappedIpaHighlightRegex, SearchScript.LATIN)
                                }
                            )
                        )
                    }

                    if (language == Language.BN) EnglishDefinition()

                    // There is no Bengali script Sylheti definitions so no need to account for it
                    FieldTag.sylhetiTags.filter { it in definitions }.takeIf { it.isNotEmpty() }?.let { sylhetiTags ->
                        TaggedField(
                            tag = stringResource(Res.string.sylheti),
                            bodies = sylhetiTags.map {
                                SDString(definitions[it]!!, mappedIpaHighlightRegex, SearchScript.LATIN)
                            }
                        )
                    }
                }
            }
        }
    }
}
