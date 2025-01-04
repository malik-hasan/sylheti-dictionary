package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import ui.app.LocalShowNagri
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.utils.SDString
import utility.validateStrings

@Composable
fun EntryDefinitions(
    entry: DictionaryEntry,
    showDivider: Boolean,
    modifier: Modifier = Modifier,
    language: Language = LocalLanguage.current,
    showNagri: Boolean = LocalShowNagri.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current
) {
    with(entry) {
        val definitions = remember {
            mapOf(
                FieldTag.EN to definitionEN,
                FieldTag.BN to definitionBN,
                FieldTag.BNIPA to definitionBNIPA,
                FieldTag.NAGRI to definitionNagri.takeIf { showNagri },
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
            Column(modifier) {
                if (showDivider) EntryDivider()

                if (language != Language.BN) EnglishDefinition()

                if (FieldTag.bengaliTags.any { it in definitions }) {
                    TaggedField(
                        tag = stringResource(Res.string.bengali),
                        bodies = listOfNotNull(
                            definitions[FieldTag.BN]?.let {
                                SDString(it, highlightRegex, SearchScript.BENGALI)
                            },
                            definitions[FieldTag.BNIPA]?.let {
                                SDString(it, mappedIpaHighlightRegex, SearchScript.LATIN)
                            }
                        )
                    )
                }

                if (language == Language.BN) EnglishDefinition()

                // There is no Sylheti Bengali definitions so no need to account for it
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
