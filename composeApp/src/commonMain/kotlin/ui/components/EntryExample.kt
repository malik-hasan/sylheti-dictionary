package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import models.FieldTag
import models.search.settings.SearchScript
import models.settings.Language
import oats.mobile.sylhetidictionary.Example
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.example
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
    modifier: Modifier = Modifier,
    language: Language = LocalLanguage.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current
) {
    Column(modifier) {
        Text(
            text = stringResource(Res.string.example, index + 1),
            style = MaterialTheme.typography.titleSmall,
        )

        with(example) {
            val examples = remember {
                mapOf(
                    FieldTag.IPA to exampleIPA,
                    FieldTag.BENGALI to exampleBengali,
                    FieldTag.NAGRI to exampleNagri,
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
                        examples[FieldTag.BENGALI]?.let {
                            SDString(it, highlightRegex, SearchScript.BENGALI)
                        },
                        examples[FieldTag.NAGRI]?.let {
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
                            SDString(it, highlightRegex, SearchScript.BENGALI)
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
