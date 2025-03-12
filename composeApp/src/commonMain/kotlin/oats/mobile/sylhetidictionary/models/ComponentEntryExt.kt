package oats.mobile.sylhetidictionary.models

import oats.mobile.sylhetidictionary.ComponentEntry
import oats.mobile.sylhetidictionary.DictionaryEntry

fun ComponentEntry.toDictionaryEntry() = DictionaryEntry(
    entryId = entryId,
    lexemeIPA = lexemeIPA,
    lexemeBengali = lexemeBengali,
    lexemeSN = lexemeSN,
    citationIPA = citationIPA,
    citationBengali = citationBengali,
    citationSN = citationSN,
    senseId = senseId,
    partOfSpeech = partOfSpeech,
    gloss = gloss,
    definitionEN = definitionEN,
    definitionBN = definitionBN,
    definitionBNIPA = definitionBNIPA,
    definitionIPA = definitionIPA,
    definitionSN = definitionSN
)
