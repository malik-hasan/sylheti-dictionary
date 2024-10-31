package data.dictionary

import oats.mobile.sylhetidictionary.DictionaryDatabaseQueries

class DictionaryDataSource(private val queries: DictionaryDatabaseQueries) {
    
    fun getEntries(entryIds: List<String>) =
        queries.getEntries(entryIds).executeAsList()

    fun searchAll(simpleQuery: String, positionedQuery: String, searchDefinitions: Boolean) = with(queries) {
        transactionWithResult {
            val result = searchAllEntries(positionedQuery).executeAsList().toMutableList()
            if (searchDefinitions) {
                result += searchAllDefinitions(simpleQuery).executeAsList()
            }
            result
        }
    }

    fun searchEnglish(simpleQuery: String, positionedQuery: String, searchDefinitions: Boolean) = with(queries) {
        transactionWithResult {
            val result = searchEnglishEntry(positionedQuery).executeAsList().toMutableList()
            if (searchDefinitions) {
                result += searchEnglishDefinition(simpleQuery).executeAsList()
            }
            result
        }
    }

    fun searchSylhetiLatin(simpleQuery: String, positionedQuery: String, searchDefinitions: Boolean) = with(queries) {
        transactionWithResult {
            val result = searchSylhetiLatinEntry(positionedQuery).executeAsList().toMutableList()
            if (searchDefinitions) {
                result += searchSylhetiLatinDefinition(simpleQuery).executeAsList()
            }
            result
        }
    }

    fun searchBengali(simpleQuery: String) =
        queries.searchBengaliDefinition(simpleQuery).executeAsList()

    fun searchSylhetiBengali(positionedQuery: String) =
        queries.searchSylhetiBengaliEntry(positionedQuery).executeAsList()

    fun searchNagri(simpleQuery: String, positionedQuery: String, searchDefinitions: Boolean) = with(queries) {
        transactionWithResult {
            val result = searchNagriEntry(positionedQuery).executeAsList().toMutableList()
            if (searchDefinitions) {
                result += searchNagriDefinition(simpleQuery).executeAsList()
            }
            result
        }
    }
}
