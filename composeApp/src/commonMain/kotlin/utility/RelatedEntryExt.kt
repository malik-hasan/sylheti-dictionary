package utility

import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.RelatedEntry

fun RelatedEntry.toDictionaryEntry() = DictionaryEntry(
    entryId = entryId,
    lexemeIPA = lexemeIPA,
    lexemeBengali = lexemeBengali,
    lexemeNagri = lexemeNagri,
    citationIPA = citationIPA,
    citationBengali = citationBengali,
    citationNagri = citationNagri,
    senseId = senseId,
    partOfSpeech = partOfSpeech,
    gloss = gloss,
    definitionEN = definitionEN,
    definitionBN = definitionBN,
    definitionBNIPA = definitionBNIPA,
    definitionIPA = definitionIPA,
    definitionNagri = definitionNagri
)
