package data.dictionary

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import oats.mobile.sylhetidictionary.DictionaryDatabaseQueries
import oats.mobile.sylhetidictionary.DictionaryEntry
import oats.mobile.sylhetidictionary.Variant
import kotlin.random.Random

class DictionaryDataSource(private val queries: DictionaryDatabaseQueries) {
    
    suspend fun getEntry(entryId: String) = withContext(Dispatchers.IO) {
        queries.getEntry(entryId).awaitAsOne()
    }

    suspend fun getEntries(entryIds: Collection<String>) = withContext(Dispatchers.IO) {
        queries.getEntries(entryIds).awaitAsList()
    }

    suspend fun searchAll(
        query: String,
        positionedQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
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
    }

    suspend fun searchEnglish(
        query: String,
        positionedQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
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
    }

    suspend fun searchSylhetiLatin(
        query: String,
        positionedQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
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
    }

    suspend fun searchBengali(
        query: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
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
    }

    suspend fun searchSylhetiBengali(
        query: String,
        positionedQuery: String,
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
            transactionWithResult {
                val result = searchSylhetiBengaliEntries(positionedQuery).executeAsList().toMutableList()
                if (searchExamples) {
                    result += searchSylhetiBengaliExamples(query).executeAsList()
                }
                result
            }
        }
    }

    suspend fun searchNagri(
        query: String,
        positionedQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ): List<DictionaryEntry> = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
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
    }

    suspend fun getExamples(entryId: String) = withContext(Dispatchers.IO) {
        queries.getExamples(entryId).awaitAsList()
    }

    suspend fun getVariants(entryId: String) = withContext(Dispatchers.IO) {
        yield()
        val allVariants = with(queries) {
            transactionWithResult {
                getVariants(entryId).executeAsList() +
                getAdditionalVariants(entryId) { returnedEntryId, citationIPA, lexemeIPA, citationBengali, lexemeBengali, citationNagri, lexemeNagri, variantType ->
                    Variant(
                        id = Random.nextLong(),
                        entryId = returnedEntryId,
                        variantIPA = citationIPA ?: lexemeIPA,
                        variantBengali = citationBengali ?: lexemeBengali,
                        variantNagri = citationNagri ?: lexemeNagri,
                        environment = variantType
                    )
                }.executeAsList()
            }
        }

        allVariants
            .groupBy {
                yield()
                it.variantIPA
            }
            .mapNotNull { grouping ->
                yield()
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

    suspend fun getVariantEntries(entryId: String) = withContext(Dispatchers.IO) {
        queries.variantEntry(entryId).awaitAsList()
    }

    suspend fun getComponentLexemes(entryId: String) = withContext(Dispatchers.IO) {
        queries.componentEntry(entryId).awaitAsList()
    }

    suspend fun getRelatedEntries(senseId: String) = withContext(Dispatchers.IO) {
        queries.relatedEntry(senseId).awaitAsList()
    }
}
