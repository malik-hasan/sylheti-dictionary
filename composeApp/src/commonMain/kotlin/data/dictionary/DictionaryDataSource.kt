package data.dictionary

import oats.mobile.sylhetidictionary.DictionaryDatabase
import oats.mobile.sylhetidictionary.DictionaryEntry

interface DictionaryDataSource {
    suspend fun getAll(): List<DictionaryEntry>
    suspend fun getEntries(entryIds: List<String>): List<DictionaryEntry>
    suspend fun searchSylLatin(query: String): List<DictionaryEntry>
}

class DictionaryDataSourceImpl(db: DictionaryDatabase) : DictionaryDataSource {

    private val queries = db.dictionaryDatabaseQueries

    override suspend fun getAll() = queries.getAll().executeAsList()

    override suspend fun getEntries(entryIds: List<String>) =
        queries.getEntries(entryIds).executeAsList()

    override suspend fun searchSylLatin(query: String) =
        queries.searchSylLatin(query).executeAsList()
}
