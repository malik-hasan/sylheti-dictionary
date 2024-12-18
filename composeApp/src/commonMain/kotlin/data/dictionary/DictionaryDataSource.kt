package data.dictionary

import oats.mobile.sylhetidictionary.DictionaryDatabaseQueries
import oats.mobile.sylhetidictionary.DictionaryEntry

class DictionaryDataSource(private val queries: DictionaryDatabaseQueries) {
    
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
}
