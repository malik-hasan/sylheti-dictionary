package oats.mobile.sylhetidictionary.models

import oats.mobile.sylhetidictionary.DictionaryEntry

actual val List<DictionaryEntry>.scrollCharIndexes
    get() = emptyMap<Char, Int>()
