package oats.mobile.sylhetidictionary.utility

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.data.dictionary.models.displayIPA

actual suspend fun List<DictionaryEntry>.getScrollCharIndexes() = withContext(Dispatchers.Default) {
    asSequence()
        .mapIndexedNotNull { i, entry ->
            entry.displayIPA.firstOrNull()?.let { firstChar ->
                val scrollChar = firstChar.takeIf { it in UnicodeUtility.SYLHETI_IPA_CHARS } ?: '-'
                scrollChar to i
            }
        }.distinctBy { it.first }
        .associate { it }
}
