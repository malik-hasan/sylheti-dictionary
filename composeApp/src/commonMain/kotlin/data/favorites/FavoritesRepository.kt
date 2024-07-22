package data.favorites

import data.dictionary.DictionaryDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import models.Favorite
import oats.mobile.sylhetidictionary.DictionaryEntry

class FavoritesRepository(
    private val dictionaryDataSource: DictionaryDataSource,
    private val favoritesDao: FavoritesDao
) {

    fun getFavorites(): Flow<List<DictionaryEntry>> =
        favoritesDao.getFavorites().map { favorites ->
            val ids = favorites.map { it.entryId }
            dictionaryDataSource.getEntries(ids)
        }

    suspend fun checkFavorite(entryId: String) = favoritesDao.checkFavorite(entryId)

    suspend fun addFavorite(entryId: String) = favoritesDao.addFavorite(Favorite(entryId))

    suspend fun removeFavorite(entryId: String) = favoritesDao.removeFavorite(Favorite(entryId))
}
