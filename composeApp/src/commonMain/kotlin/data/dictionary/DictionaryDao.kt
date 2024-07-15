package data.dictionary

//@Dao
//interface DictionaryDao {
//    @Transaction
//    @Query("SELECT * FROM DictionaryEntry")
//    suspend fun getAllData(): List<DictionaryData>
//
//    @Transaction
//    @Query("SELECT * FROM Domain")
//    suspend fun getAllDomains(): List<DomainWithEntries>
//
////    @Transaction
////    @Query("""SELECT entry_id FROM DictionaryEntry WHERE
////            lexeme_ipa LIKE '%' || :query || '%' OR
////            citation_ipa LIKE '%' || :query || '%' OR
////            definition_ipa LIKE '%' || :query || '%'
////            UNION
////            SELECT entry_id FROM Example WHERE example_ipa LIKE '%' || :query || '%'
////
////    """)
////    suspend fun searchSylLatin(query: String): List<DictionaryData>
//
//    @Transaction
//    @Query("SELECT * FROM DictionaryEntry WHERE entry_id IN (:entryIds)")
//    suspend fun getEntries(entryIds: List<String>): List<DictionaryData>
//
////    @Query("SELECT * FROM DictionaryEntry WHERE lexeme_ipa REGEXP :query")
////    @RawQuery
////    suspend fun searchSylLatin(query: SupportSQLiteQuery): List<DictionaryData>
//}
