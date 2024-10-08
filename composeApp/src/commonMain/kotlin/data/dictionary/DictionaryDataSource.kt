package data.dictionary

import oats.mobile.sylhetidictionary.DictionaryDatabase
import oats.mobile.sylhetidictionary.DictionaryEntry

interface DictionaryDataSource {
    suspend fun getAll(): List<DictionaryEntry>
    suspend fun getEntries(entryIds: List<String>): List<DictionaryEntry>
    suspend fun searchEnglish(query: String): List<DictionaryEntry>
    suspend fun searchSylhetiLatin(query: String): List<DictionaryEntry>
    suspend fun searchBengali(query: String): List<DictionaryEntry>
    suspend fun searchSylhetiBengali(query: String): List<DictionaryEntry>
    suspend fun searchNagri(query: String): List<DictionaryEntry>
}

class DictionaryDataSourceImpl(db: DictionaryDatabase) : DictionaryDataSource {

    private val queries = db.dictionaryDatabaseQueries

    override suspend fun getAll() = queries.getAll().executeAsList()

    override suspend fun getEntries(entryIds: List<String>) =
        queries.getEntries(entryIds).executeAsList()

    override suspend fun searchEnglish(query: String) =
        queries.searchEnglish(query).executeAsList()

    override suspend fun searchSylhetiLatin(query: String) =
        queries.searchSylhetiLatin(query).executeAsList()

    override suspend fun searchBengali(query: String) =
        queries.searchBengali(query).executeAsList()

    override suspend fun searchSylhetiBengali(query: String) =
        queries.searchSylhetiBengali(query).executeAsList()

    override suspend fun searchNagri(query: String) =
        queries.searchNagri(query).executeAsList()
}
