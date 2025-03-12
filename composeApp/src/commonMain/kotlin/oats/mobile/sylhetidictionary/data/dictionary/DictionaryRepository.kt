package oats.mobile.sylhetidictionary.data.dictionary

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.async.coroutines.awaitAsOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import oats.mobile.sylhetidictionary.DictionaryDatabaseQueries
import oats.mobile.sylhetidictionary.Variant
import kotlin.random.Random

class DictionaryRepository(private val queries: DictionaryDatabaseQueries) {
    
    suspend fun getEntry(entryId: String) = withContext(Dispatchers.IO) {
        queries.getEntry(entryId).awaitAsOne()
    }

    suspend fun getEntries(entryIds: Collection<String>) = withContext(Dispatchers.IO) {
        queries.getEntries(entryIds).awaitAsList()
    }

    suspend fun searchAll(
        positionedQuery: String,
        simpleQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
            when {
                searchDefinitions && searchExamples -> searchAllEntriesWithDefinitionsAndExamples(positionedQuery, simpleQuery)
                searchDefinitions -> searchAllEntriesWithDefinitions(positionedQuery, simpleQuery)
                searchExamples -> searchAllEntriesWithExamples(positionedQuery, simpleQuery)
                else -> searchAllEntries(positionedQuery)
            }.executeAsList()
        }
    }

    suspend fun searchEnglish(
        positionedQuery: String,
        simpleQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
            when {
                searchDefinitions && searchExamples -> searchEnglishEntriesWithDefinitionsAndExamples(positionedQuery, simpleQuery)
                searchDefinitions -> searchEnglishEntriesWithDefinitions(positionedQuery, simpleQuery)
                searchExamples -> searchEnglishEntriesWithExamples(positionedQuery, simpleQuery)
                else -> searchEnglishEntries(positionedQuery)
            }.executeAsList()
        }
    }

    suspend fun searchSylhetiLatin(
        positionedQuery: String,
        simpleQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
            when {
                searchDefinitions && searchExamples -> searchSylhetiLatinEntriesWithDefinitionsAndExamples(positionedQuery, simpleQuery)
                searchDefinitions -> searchSylhetiLatinEntriesWithDefinitions(positionedQuery, simpleQuery)
                searchExamples -> searchSylhetiLatinEntriesWithExamples(positionedQuery, simpleQuery)
                else -> searchSylhetiLatinEntries(positionedQuery)
            }.executeAsList()
        }
    }

    suspend fun searchLatin(
        positionedQuery: String,
        simpleQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
            when {
                searchDefinitions && searchExamples -> searchLatinEntriesWithDefinitionsAndExamples(positionedQuery, simpleQuery)
                searchDefinitions -> searchLatinEntriesWithDefinitions(positionedQuery, simpleQuery)
                searchExamples -> searchLatinEntriesWithExamples(positionedQuery, simpleQuery)
                else -> searchLatinEntries(positionedQuery)
            }.executeAsList()
        }
    }

    suspend fun searchBengaliEasternNagri(
        positionedQuery: String, // unused
        simpleQuery: String,
        searchDefinitions: Boolean, // unused
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
            if (searchExamples) {
                searchBengaliEasternNagriDefinitionsAndExamples(simpleQuery)
            } else searchBengaliEasternNagriDefinitions(simpleQuery)
        }.executeAsList()
    }

    suspend fun searchSylhetiEasternNagri(
        positionedQuery: String,
        simpleQuery: String,
        searchDefinitions: Boolean, // unused
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
            if (searchExamples) {
                searchSylhetiEasternNagriEntriesWithExamples(positionedQuery, simpleQuery)
            } else searchSylhetiEasternNagriEntries(positionedQuery)
        }.executeAsList()
    }

    suspend fun searchEasternNagri(
        positionedQuery: String,
        simpleQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
            when {
                searchDefinitions && searchExamples -> searchEasternNagriEntriesWithDefinitionsAndExamples(positionedQuery, simpleQuery)
                searchDefinitions -> searchEasternNagriEntriesWithDefinitions(positionedQuery, simpleQuery)
                searchExamples -> searchEasternNagriEntriesWithExamples(positionedQuery, simpleQuery)
                else -> searchSylhetiEasternNagriEntries(positionedQuery)
            }.executeAsList()
        }
    }

    suspend fun searchSylhetiNagri(
        positionedQuery: String,
        simpleQuery: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = withContext(Dispatchers.IO) {
        yield()
        with(queries) {
            when {
                searchDefinitions && searchExamples -> searchSylhetiNagriEntriesWithDefinitionsAndExamples(positionedQuery, simpleQuery)
                searchDefinitions -> searchSylhetiNagriEntriesWithDefinitions(positionedQuery, simpleQuery)
                searchExamples -> searchSylhetiNagriEntriesWithExamples(positionedQuery, simpleQuery)
                else -> searchSylhetiNagriEntries(positionedQuery)
            }.executeAsList()
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
                getAdditionalVariants(entryId) { returnedEntryId, citationIPA, lexemeIPA, citationEN, lexemeEN, citationSN, lexemeSN, variantType ->
                    Variant(
                        id = Random.nextLong(),
                        entryId = returnedEntryId,
                        variantIPA = citationIPA ?: lexemeIPA,
                        variantEN = citationEN ?: lexemeEN,
                        variantSN = citationSN ?: lexemeSN,
                        environment = variantType
                    )
                }.executeAsList()
            }
        }

        allVariants
            .groupBy {
                yield()
                it.variantIPA
            }.mapNotNull { grouping ->
                yield()
                if (grouping.value.size > 1) {
                    val option1 = grouping.value.first()
                    val option2 = grouping.value.last()
                    Variant(
                        id = option1.id,
                        entryId = option1.entryId,
                        variantIPA = option1.variantIPA,
                        variantEN = option1.variantEN ?: option2.variantEN,
                        variantSN = option1.variantSN ?: option2.variantSN,
                        environment = option1.environment?.takeIf { it != "Unspecified Variant" } ?: option2.environment
                    )
                } else grouping.value.firstOrNull()
            }
    }

    suspend fun getReferenceEntries(entryId: String) = withContext(Dispatchers.IO) {
        queries.getReferenceEntries(entryId).awaitAsList()
    }

    suspend fun getComponentLexemes(entryId: String) = withContext(Dispatchers.IO) {
        queries.componentEntry(entryId).awaitAsList()
    }

    suspend fun getRelatedEntries(senseId: String) = withContext(Dispatchers.IO) {
        queries.relatedEntry(senseId).awaitAsList()
    }
}
