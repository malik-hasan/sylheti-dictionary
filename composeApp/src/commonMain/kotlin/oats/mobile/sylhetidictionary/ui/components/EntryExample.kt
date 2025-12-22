package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.Example
import oats.mobile.sylhetidictionary.data.preferences.models.search.SearchScript
import oats.mobile.sylhetidictionary.data.preferences.models.settings.Language
import oats.mobile.sylhetidictionary.ui.app.LocalLanguage
import oats.mobile.sylhetidictionary.ui.models.FieldTag
import oats.mobile.sylhetidictionary.ui.models.SDString
import oats.mobile.sylhetidictionary.ui.screens.search.LocalHighlightRegex
import oats.mobile.sylhetidictionary.utility.validateStrings
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.sylheti

@Composable
fun EntryExample(
    example: Example,
    index: Int,
    showIndex: Boolean,
    featureBengaliExamples: Boolean,
    modifier: Modifier = Modifier,
    language: Language = LocalLanguage.current,
    highlightRegex: Regex = LocalHighlightRegex.current
) = SelectionContainer {
    Row(
        modifier = modifier.padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showIndex) DisableSelection {
            Text("${index + 1}.")
        }

        Column {
            with(example) {
                val examples = remember(example) {
                    buildMap {
                        put(FieldTag.IPA, exampleIPA)
                        put(FieldTag.SYLHETI_NAGRI, exampleSN)
                        put(FieldTag.EASTERN_NAGRI, exampleEN)
                        put(FieldTag.ENGLISH, exampleEnglish)
                        if (featureBengaliExamples) {
                            put(FieldTag.BENGALI, exampleBengali)
                            put(FieldTag.BENGALI_IPA, exampleBengaliIPA)
                        }
                    }.validateStrings()
                }

                @Composable
                fun EnglishExample() {
                    examples[FieldTag.ENGLISH]?.let { example ->
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
                                SDString(it, highlightRegex, SearchScript.LATIN)
                            },
                            examples[FieldTag.SYLHETI_NAGRI]?.let {
                                SDString(it, highlightRegex)
                            },
                            examples[FieldTag.EASTERN_NAGRI]?.let {
                                SDString(it, highlightRegex, SearchScript.EASTERN_NAGRI)
                            }
                        )
                    )
                }

                if (language != Language.BN) EnglishExample()

                if (FieldTag.bengaliTags.any { it in examples }) {
                    TaggedField(
                        tag = stringResource(Res.string.bengali),
                        bodies = listOfNotNull(
                            examples[FieldTag.BENGALI]?.let {
                                SDString(it, highlightRegex, SearchScript.EASTERN_NAGRI)
                            },
                            examples[FieldTag.BENGALI_IPA]?.let {
                                SDString(it, highlightRegex, SearchScript.LATIN)
                            }
                        )
                    )
                }

                if (language == Language.BN) EnglishExample()
            }
        }
    }
}
