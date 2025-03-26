package oats.mobile.sylhetidictionary.models

import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.utility.UnicodeUtility

actual val List<DictionaryEntry>.scrollCharIndexes
    get() = asSequence()
        .mapIndexedNotNull { i, entry ->
            entry.displayIPA.firstOrNull()?.let { firstChar ->
                val scrollChar = firstChar.takeIf { it in UnicodeUtility.SYLHETI_IPA_CHARS } ?: '-'
                scrollChar to i
            }
        }.distinctBy { it.first }
        .associate { it }
