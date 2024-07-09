package data.dictionary

import oats.mobile.sylhetidictionary.DictionaryDatabase
import oats.mobile.sylhetidictionary.DictionaryEntry

interface DictionaryDataSource {
    suspend fun searchSylLatin(query: String): List<DictionaryEntry>
    suspend fun getAll(): List<DictionaryEntry>
}

class DictionaryDataSourceImpl(db: DictionaryDatabase) : DictionaryDataSource {

//    private val queries = db

    override suspend fun searchSylLatin(query: String): List<DictionaryEntry> {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<DictionaryEntry> {
        TODO("Not yet implemented")
    }

}
