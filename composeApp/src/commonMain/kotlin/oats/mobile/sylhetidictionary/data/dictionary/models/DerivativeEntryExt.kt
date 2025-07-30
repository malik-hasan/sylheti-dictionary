package oats.mobile.sylhetidictionary.data.dictionary.models

import oats.mobile.sylhetidictionary.DerivativeEntry
import oats.mobile.sylhetidictionary.DictionaryEntry

fun DerivativeEntry.toDictionaryEntry() = DictionaryEntry(
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
