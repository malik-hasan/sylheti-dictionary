package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.FieldTag
import models.search.settings.SearchScript
import models.settings.Language
import oats.mobile.sylhetidictionary.Example
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.sylheti
import ui.app.LocalLanguage
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.utils.SDString
import utility.validateStrings

@Composable
fun EntryExample(
    example: Example,
    index: Int,
    showIndex: Boolean,
    language: Language = LocalLanguage.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showIndex) Text("${index + 1}.")

        Column {
            with(example) {
                val examples = remember {
                    mapOf(
                        FieldTag.IPA to exampleIPA,
                        FieldTag.EASTERN_NAGRI to exampleBengali,
                        FieldTag.SYLHETI_NAGRI to exampleNagri,
                        FieldTag.EN to exampleEN,
                        FieldTag.BN to exampleBN,
                        FieldTag.BNIPA to exampleBNIPA
                    ).validateStrings()
                }

                @Composable
                fun EnglishExample() {
                    examples[FieldTag.EN]?.let { example ->
                        TaggedField(
                            tag = stringResource(Res.string.english),
                            body = SDString(example, highlightRegex, SearchScript.LATIN)
                        )
                    }
                }

                if (FieldTag.sylhetiTags.any { it in examples }) {
                    TaggedField(
                        tag = stringResource(Res.string.sylheti),
                        bodies = listOfNotNull(
                            examples[FieldTag.IPA]?.let {
                                SDString(it, mappedIpaHighlightRegex, SearchScript.LATIN)
                            },
                            examples[FieldTag.EASTERN_NAGRI]?.let {
                                SDString(it, highlightRegex, SearchScript.EASTERN_NAGRI)
                            },
                            examples[FieldTag.SYLHETI_NAGRI]?.let {
                                SDString(it, highlightRegex)
                            }
                        )
                    )
                }

                if (language != Language.BN) EnglishExample()

                if (FieldTag.bengaliTags.any { it in examples }) {
                    TaggedField(
                        tag = stringResource(Res.string.bengali),
                        bodies = listOfNotNull(
                            examples[FieldTag.BN]?.let {
                                SDString(it, highlightRegex, SearchScript.EASTERN_NAGRI)
                            },
                            examples[FieldTag.BNIPA]?.let {
                                SDString(it, mappedIpaHighlightRegex, SearchScript.LATIN)
                            }
                        )
                    )
                }

                if (language == Language.BN) EnglishExample()
            }
        }
    }
}
