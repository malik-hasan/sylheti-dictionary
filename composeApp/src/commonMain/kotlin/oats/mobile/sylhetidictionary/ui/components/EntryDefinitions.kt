package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.models.FieldTag
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.models.settings.Language
import oats.mobile.sylhetidictionary.ui.app.LocalLanguage
import oats.mobile.sylhetidictionary.ui.screens.search.LocalAnimatedContentScope
import oats.mobile.sylhetidictionary.ui.screens.search.LocalHighlightRegex
import oats.mobile.sylhetidictionary.ui.screens.search.LocalSharedTransitionScope
import oats.mobile.sylhetidictionary.ui.utils.SDString
import oats.mobile.sylhetidictionary.ui.utils.ifTrue
import oats.mobile.sylhetidictionary.utility.validateStrings
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.sylheti

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EntryDefinitions(
    entry: DictionaryEntry,
    modifier: Modifier = Modifier,
    includeAnimation: Boolean = true,
    language: Language = LocalLanguage.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    sharedTransitionScope: SharedTransitionScope = LocalSharedTransitionScope.current,
    animatedContentScope: AnimatedContentScope = LocalAnimatedContentScope.current
) {
    with(sharedTransitionScope) {
        with(entry) {
            val definitions = remember {
                mapOf(
                    FieldTag.ENGLISH to definitionEnglish,
                    FieldTag.BENGALI to definitionBengali,
                    FieldTag.BENGALI_IPA to definitionBengaliIPA,
                    FieldTag.SYLHETI_NAGRI to definitionSN,
                    FieldTag.IPA to definitionIPA
                ).validateStrings()
            }

            @Composable
            fun EnglishDefinition() {
                definitions[FieldTag.ENGLISH]?.let { definition ->
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
                                definitions[FieldTag.BENGALI]?.let {
                                    SDString(it, highlightRegex, SearchScript.EASTERN_NAGRI)
                                },
                                definitions[FieldTag.BENGALI_IPA]?.let {
                                    SDString(it, highlightRegex, SearchScript.LATIN)
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
                                SDString(definitions[it]!!, highlightRegex, SearchScript.LATIN)
                            }
                        )
                    }
                }
            }
        }
    }
}
