package data.favorites

import data.dictionary.DictionaryDataSource
import models.Favorite
import oats.mobile.sylhetidictionary.DictionaryEntry

class FavoritesRepository(
    private val dictionaryDataSource: DictionaryDataSource,
    private val favoritesDao: FavoritesDao
) {

    suspend fun getFavorites(): List<DictionaryEntry> {
        val favorites = favoritesDao.getFavorites().map { it.entryId }
        return dictionaryDataSource.getEntries(favorites)
    }

    suspend fun checkFavorite(entryId: String) = favoritesDao.checkFavorite(entryId)

    suspend fun addFavorite(entryId: String) = favoritesDao.addFavorite(Favorite(entryId))

    suspend fun removeFavorite(entryId: String) = favoritesDao.removeFavorite(Favorite(entryId))
}
