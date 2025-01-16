package data.dictionary

import oats.mobile.sylhetidictionary.DictionaryDatabaseQueries
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.Variant
import kotlin.random.Random

class DictionaryDataSource(private val queries: DictionaryDatabaseQueries) {
    
    fun getEntry(entryId: String) = queries.getEntry(entryId).executeAsOne()

    fun getEntries(entryIds: Collection<String>) = queries.getEntries(entryIds).executeAsList()

    fun searchAll(
        query: String,
        positionedQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = with(queries) {
        transactionWithResult {
            val result = searchAllEntries(positionedQuery).executeAsList().toMutableList()

            if (searchDefinitions) {
                result += searchAllDefinitions(query).executeAsList()
            }
            if (searchExamples) {
                result += searchAllExamples(query).executeAsList()
            }

            result
        }
    }

    fun searchEnglish(
        query: String,
        positionedQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = with(queries) {
        transactionWithResult {
            val result = searchEnglishEntries(positionedQuery).executeAsList().toMutableList()
            if (searchDefinitions) {
                result += searchEnglishDefinitions(query).executeAsList()
            }
            if (searchExamples) {
                result += searchEnglishExamples(query).executeAsList()
            }
            result
        }
    }

    fun searchSylhetiLatin(
        query: String,
        positionedQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = with(queries) {
        transactionWithResult {
            val result = searchSylhetiLatinEntries(positionedQuery).executeAsList().toMutableList()
            if (searchDefinitions) {
                result += searchSylhetiLatinDefinitions(query).executeAsList()
            }
            if (searchExamples) {
                result += searchSylhetiLatinExamples(query).executeAsList()
            }
            result
        }
    }

    fun searchBengali(
        query: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = with(queries) {
        transactionWithResult {
            val result = mutableListOf<DictionaryEntry>()
            if (searchDefinitions) {
                result += searchBengaliDefinitions(query).executeAsList()
            }
            if (searchExamples) {
                result += searchBengaliExamples(query).executeAsList()
            }
            result
        }
    }

    fun searchSylhetiBengali(
        query: String,
        positionedQuery: String,
        searchExamples: Boolean
    ) = with(queries) {
        transactionWithResult {
            val result = searchSylhetiBengaliEntries(positionedQuery).executeAsList().toMutableList()
            if (searchExamples) {
                result += searchSylhetiBengaliExamples(query).executeAsList()
            }
            result
        }
    }

    fun searchNagri(
        query: String,
        positionedQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ): List<DictionaryEntry> = with(queries) {
        transactionWithResult {
            val result = searchNagriEntries(positionedQuery).executeAsList().toMutableList()
            if (searchDefinitions) {
                result += searchNagriDefinitions(query).executeAsList()
            }
            if (searchExamples) {
                result += searchNagriExamples(query).executeAsList()
            }
            result
        }
    }

    fun getExamples(entryId: String) = queries.getExamples(entryId).executeAsList()

    fun getVariants(entryId: String): List<Variant> {
        val allVariants = queries.getVariants(entryId).executeAsList() +
            queries.getAdditionalVariants(entryId) {
                returnedEntryId, citationIPA, lexemeIPA, citationBengali, lexemeBengali, citationNagri, lexemeNagri, variantType ->
                    Variant(
                        id = Random.nextLong(),
                        entryId = returnedEntryId,
                        variantIPA = citationIPA ?: lexemeIPA,
                        variantBengali = citationBengali ?: lexemeBengali,
                        variantNagri = citationNagri ?: lexemeNagri,
                        environment = variantType
                    )
            }.executeAsList()

        return allVariants
            .groupBy { it.variantIPA }
            .mapNotNull { grouping ->
                if (grouping.value.size > 1) {
                    val option1 = grouping.value.first()
                    val option2 = grouping.value.last()
                    Variant(
                        id = option1.id,
                        entryId = option1.entryId,
                        variantIPA = option1.variantIPA,
                        variantBengali = option1.variantBengali ?: option2.variantBengali,
                        variantNagri = option1.variantNagri ?: option2.variantNagri,
                        environment = option1.environment?.takeIf { it != "Unspecified Variant" } ?: option2.environment
                    )
                } else grouping.value.firstOrNull()
            }
    }

    fun getVariantEntries(entryId: String) = queries.variantEntry(entryId).executeAsList()

    fun getComponentLexemes(entryId: String) = queries.componentEntry(entryId).executeAsList()

    fun getRelatedEntries(senseId: String) = queries.relatedEntry(senseId).executeAsList()
}
