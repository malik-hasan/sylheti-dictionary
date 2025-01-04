package ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import models.FieldTag
import models.settings.Language
import oats.mobile.sylhetidictionary.Example
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english
import sylhetidictionary.composeapp.generated.resources.example
import sylhetidictionary.composeapp.generated.resources.sylheti
import ui.app.LocalLanguage
import ui.app.LocalShowNagri
import ui.screens.search.LocalHighlightRegex
import ui.screens.search.LocalMappedIpaHighlightRegex
import ui.theme.bengaliBodyFontFamily
import ui.theme.latinBodyFontFamily
import ui.utils.SDString
import utility.validateStrings

@Composable
fun EntryExample(
    example: Example,
    index: Int,
    language: Language = LocalLanguage.current,
    showNagri: Boolean = LocalShowNagri.current,
    highlightRegex: Regex = LocalHighlightRegex.current,
    mappedIpaHighlightRegex: Regex = LocalMappedIpaHighlightRegex.current
) {
    Text(stringResource(Res.string.example, index + 1))

    with(example) {
        val examples = remember {
            mapOf(
                FieldTag.BENGALI to exampleBengali,
                FieldTag.NAGRI to exampleNagri.takeIf { showNagri },
                FieldTag.IPA to exampleIPA,
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
                    body = SDString(example, highlightRegex, latinBodyFontFamily)
                )
            }
        }

        if (FieldTag.sylhetiTags.any { it in examples }) {
            TaggedField(
                tag = stringResource(Res.string.sylheti),
                bodies = listOfNotNull(
                    examples[FieldTag.BENGALI]?.let {
                        SDString(it, highlightRegex, bengaliBodyFontFamily)
                    },
                    examples[FieldTag.IPA]?.let {
                        SDString(it, mappedIpaHighlightRegex, latinBodyFontFamily)
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
                        SDString(it, highlightRegex, bengaliBodyFontFamily)
                    },
                    examples[FieldTag.BNIPA]?.let {
                        SDString(it, mappedIpaHighlightRegex, latinBodyFontFamily)
                    }
                )
            )
        }

        if (language == Language.BN) EnglishExample()
    }
}
