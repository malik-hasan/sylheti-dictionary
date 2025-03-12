package oats.mobile.sylhetidictionary.models

import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.RelatedEntry

fun RelatedEntry.toDictionaryEntry() = DictionaryEntry(
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
