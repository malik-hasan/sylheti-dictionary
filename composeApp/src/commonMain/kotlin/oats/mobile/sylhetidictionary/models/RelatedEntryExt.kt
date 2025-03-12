package oats.mobile.sylhetidictionary.models

import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.RelatedEntry

fun RelatedEntry.toDictionaryEntry() = DictionaryEntry(
    entryId = entryId,
    lexemeIPA = lexemeIPA,
    lexemeEN = lexemeEN,
    lexemeSN = lexemeSN,
    citationIPA = citationIPA,
    citationEN = citationEN,
    citationSN = citationSN,
    senseId = senseId,
    partOfSpeech = partOfSpeech,
    gloss = gloss,
    definitionEnglish = definitionEnglish,
    definitionBengali = definitionBengali,
    definitionBengaliIPA = definitionBengaliIPA,
    definitionIPA = definitionIPA,
    definitionSN = definitionSN
)
