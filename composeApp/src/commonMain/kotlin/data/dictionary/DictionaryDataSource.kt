package data.dictionary

import oats.mobile.sylhetidictionary.DictionaryDatabase
import oats.mobile.sylhetidictionary.DictionaryEntry

interface DictionaryDataSource {
    suspend fun getAll(): List<DictionaryEntry>
    suspend fun getEntries(entryIds: List<String>): List<DictionaryEntry>
    suspend fun searchAll(simpleQuery: String, positionedQuery: String): List<DictionaryEntry>
    suspend fun searchEnglish(simpleQuery: String, positionedQuery: String): List<DictionaryEntry>
    suspend fun searchSylhetiLatin(simpleQuery: String, positionedQuery: String): List<DictionaryEntry>
    suspend fun searchBengali(simpleQuery: String): List<DictionaryEntry>
    suspend fun searchSylhetiBengali(positionedQuery: String): List<DictionaryEntry>
    suspend fun searchNagri(searchTerm: String, positionedQuery: String): List<DictionaryEntry>
}

class DictionaryDataSourceImpl(db: DictionaryDatabase) : DictionaryDataSource {

    private val queries = db.dictionaryDatabaseQueries

    override suspend fun getAll() = queries.getAll().executeAsList()

    override suspend fun getEntries(entryIds: List<String>) =
        queries.getEntries(entryIds).executeAsList()

    override suspend fun searchAll(simpleQuery: String, positionedQuery: String) =
        queries.searchAll(simpleQuery, positionedQuery).executeAsList()

    override suspend fun searchEnglish(simpleQuery: String, positionedQuery: String) =
        queries.searchEnglish(simpleQuery, positionedQuery).executeAsList()

    override suspend fun searchSylhetiLatin(simpleQuery: String, positionedQuery: String) =
        queries.searchSylhetiLatin(simpleQuery, positionedQuery).executeAsList()

    override suspend fun searchBengali(simpleQuery: String) =
        queries.searchBengali(simpleQuery).executeAsList()

    override suspend fun searchSylhetiBengali(positionedQuery: String) =
        queries.searchSylhetiBengali(positionedQuery).executeAsList()

    override suspend fun searchNagri(searchTerm: String, positionedQuery: String) =
        queries.searchNagri(searchTerm, positionedQuery).executeAsList()
}
