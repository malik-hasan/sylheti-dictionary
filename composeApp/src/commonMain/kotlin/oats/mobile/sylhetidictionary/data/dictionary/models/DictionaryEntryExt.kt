package oats.mobile.sylhetidictionary.data.dictionary.models

import oats.mobile.sylhetidictionary.DictionaryEntry

val DictionaryEntry.displayIPA: String
    get() = citationIPA ?: lexemeIPA

val DictionaryEntry.displayEN: String?
    get() = citationEN ?: lexemeEN

val DictionaryEntry.displaySN: String?
    get() = citationSN ?: lexemeSN
