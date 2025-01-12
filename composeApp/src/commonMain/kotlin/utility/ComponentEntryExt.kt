package utility

import oats.mobile.sylhetidictionary.ComponentEntry
import oats.mobile.sylhetidictionary.DictionaryEntry

fun ComponentEntry.toDictionaryEntry() = DictionaryEntry(
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

val ComponentEntry.isPrimaryComponent get() = isPrimary.toInt() != 0
