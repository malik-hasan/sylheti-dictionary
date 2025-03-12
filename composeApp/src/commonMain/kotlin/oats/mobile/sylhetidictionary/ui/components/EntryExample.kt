package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.Example
import oats.mobile.sylhetidictionary.models.FieldTag
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.models.settings.Language
import oats.mobile.sylhetidictionary.ui.app.LocalLanguage
import oats.mobile.sylhetidictionary.ui.screens.search.LocalHighlightRegex
import oats.mobile.sylhetidictionary.ui.utils.SDString
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
    language: Language = LocalLanguage.current,
    highlightRegex: Regex = LocalHighlightRegex.current
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showIndex) Text("${index + 1}.")

        Column {
            with(example) {
                val examples = remember(example) {
                    mapOf(
                        FieldTag.IPA to exampleIPA,
                        FieldTag.EASTERN_NAGRI to exampleEN,
                        FieldTag.SYLHETI_NAGRI to exampleSN,
                        FieldTag.ENGLISH to exampleEnglish,
                        FieldTag.BENGALI to exampleBengali,
                        FieldTag.BENGALI_IPA to exampleBengaliIPA
                    ).validateStrings()
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
