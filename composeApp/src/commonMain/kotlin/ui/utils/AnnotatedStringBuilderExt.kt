package ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import models.search.settings.SearchScript
import ui.theme.bengaliBodyFontFamily
import ui.theme.latinBodyFontFamily
import utility.UnicodeUtility

@Composable
fun AnnotatedString.Builder.appendHighlighted(dictionaryString: SDString) = with(dictionaryString) {
    appendHighlighted(
        text = text,
        highlightRegex = highlightRegex,
        fontFamily = script?.let { if (it == SearchScript.BENGALI) bengaliBodyFontFamily else latinBodyFontFamily }
    )
}

@Composable
fun AnnotatedString.Builder.appendHighlighted(
    text: String,
    highlightRegex: Regex = Regex(""),
    fontFamily: FontFamily? = null
) {
    var currentIndex = 0
    withStyle(SpanStyle(fontFamily = fontFamily)) {
        highlightRegex.findAll(text).filter { it.value.isNotEmpty() }.forEach { match ->
            val highlightRange = match.range
            if (currentIndex < highlightRange.first) {
                append(text.substring(currentIndex, highlightRange.first))
            }
            withStyle(SpanStyle(background = MaterialTheme.colorScheme.primaryContainer)) {
                val stopIndex = if (text[highlightRange.last] in UnicodeUtility.HOSHONTO) {
                    highlightRange.last // exclusive
                } else highlightRange.last + 1 // inclusive

                append(text.substring(highlightRange.first, stopIndex))
                currentIndex = appendHighlightedConjuncts(text, stopIndex)
            }
        }
        if (currentIndex < text.length) {
            append(text.substring(currentIndex))
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
