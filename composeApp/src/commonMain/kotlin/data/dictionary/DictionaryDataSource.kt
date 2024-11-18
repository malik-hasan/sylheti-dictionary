package data.dictionary

import oats.mobile.sylhetidictionary.DictionaryDatabaseQueries
import oats.mobile.sylhetidictionary.DictionaryEntry

class DictionaryDataSource(private val queries: DictionaryDatabaseQueries) {
    
    fun getEntries(entryIds: List<String>) =
        queries.getEntries(entryIds).executeAsList()

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
            val result = searchEnglishEntry(positionedQuery).executeAsList().toMutableList()
            if (searchDefinitions) {
                result += searchEnglishDefinition(query).executeAsList()
            }
            if (searchExamples) {
                result += searchEnglishExample(query).executeAsList()
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
            val result = searchSylhetiLatinEntry(positionedQuery).executeAsList().toMutableList()
            if (searchDefinitions) {
                result += searchSylhetiLatinDefinition(query).executeAsList()
            }
            if (searchExamples) {
                result += searchSylhetiLatinExample(query).executeAsList()
            }
            result
        }
    }

    fun searchBengali(
        query: String,
        searchDefinitions: Boolean,
        searchExamples: Boolean
    ) = with(queries) {
        val result = mutableListOf<DictionaryEntry>()
        transactionWithResult {
            if (searchDefinitions) {
                result += searchBengaliDefinition(query).executeAsList()
            }
            if (searchExamples) {
                result += searchBengaliExample(query).executeAsList()
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
            val result = searchSylhetiBengaliEntry(positionedQuery).executeAsList().toMutableList()
            if (searchExamples) {
                result += searchSylhetiBengaliExample(query).executeAsList()
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
            val result = searchNagriEntry(positionedQuery).executeAsList().toMutableList()
            if (searchDefinitions) {
                result += searchNagriDefinition(query).executeAsList()
            }
            if (searchExamples) {
                result += searchNagriExample(query).executeAsList()
            }
            result
        }
    }
}
