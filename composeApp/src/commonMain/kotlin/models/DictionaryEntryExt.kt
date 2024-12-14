package models

import oats.mobile.sylhetidictionary.DictionaryEntry

val DictionaryEntry.displayIPA: String
    get() = citationIPA ?: lexemeIPA

val DictionaryEntry.displayBengali: String?
    get() = citationBengali ?: lexemeBengali

val DictionaryEntry.displayNagri: String?
    get() = citationNagri ?: lexemeNagri
