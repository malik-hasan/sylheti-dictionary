package oats.mobile.sylhetidictionary.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.ui.theme.bengaliBodyFontFamily
import oats.mobile.sylhetidictionary.ui.theme.latinBodyFontFamily
import oats.mobile.sylhetidictionary.utility.UnicodeUtility

@Composable
fun AnnotatedString.Builder.appendHighlighted(dictionaryString: SDString) = with(dictionaryString) {
    appendHighlighted(
        text = text,
        highlightRegex = highlightRegex,
        fontFamily = script?.let { if (it == SearchScript.EASTERN_NAGRI) bengaliBodyFontFamily else latinBodyFontFamily }
    )
}

@Composable
fun AnnotatedString.Builder.appendHighlighted(
    text: String,
    highlightRegex: Regex = Regex(""),
    fontFamily: FontFamily? = null
) {
    val trimmedText = text.trim(',', ' ')
    var currentIndex = 0
    withStyle(SpanStyle(fontFamily = fontFamily)) {
        highlightRegex.findAll(trimmedText).filter { it.value.isNotEmpty() }.forEach { match ->
            val highlightRange = match.range
            if (currentIndex < highlightRange.first) {
                append(trimmedText.substring(currentIndex, highlightRange.first))
            }
            withStyle(SpanStyle(background = MaterialTheme.colorScheme.primaryContainer)) {
                val stopIndex = if (trimmedText[highlightRange.last] in UnicodeUtility.HOSHONTO) {
                    highlightRange.last // exclusive
                } else highlightRange.last + 1 // inclusive

                append(trimmedText.substring(highlightRange.first, stopIndex))
                currentIndex = appendHighlightedConjuncts(trimmedText, stopIndex)
            }
        }
        if (currentIndex < trimmedText.length) {
            append(trimmedText.substring(currentIndex))
        }
    }
}

private fun AnnotatedString.Builder.appendHighlightedConjuncts(text: String, currentIndex: Int): Int {
    if (currentIndex >= text.length) return currentIndex

    return when (text[currentIndex]) {
        in UnicodeUtility.HOSHONTO -> {
            val stopIndex = if (text[currentIndex + 1] in UnicodeUtility.ABUGIDA_DIACRITICS) {
                currentIndex + 1
            } else currentIndex + 2

            append(text.substring(currentIndex, stopIndex))
            appendHighlightedConjuncts(text, stopIndex)
        }

        in UnicodeUtility.ABUGIDA_DIACRITICS -> {
            val stopIndex = text.withIndex().indexOfFirst { (i, char) ->
                i > currentIndex && char !in UnicodeUtility.ABUGIDA_DIACRITICS
            }
            if (stopIndex < 0) {
                append(text.substring(currentIndex))
                Int.MAX_VALUE
            } else {
                append(text.substring(currentIndex, stopIndex))
                stopIndex
            }
        }

        else -> currentIndex
    }
}
