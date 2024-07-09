package data.favorites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import models.Favorite

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM Favorite")
    suspend fun getFavorites(): List<Favorite>

    @Query("SELECT EXISTS(SELECT * FROM Favorite WHERE entry_id = :entryId)")
    suspend fun checkFavorite(entryId: String): Boolean

    @Insert
    suspend fun addFavorite(entry: Favorite)

    @Delete
    suspend fun removeFavorite(entry: Favorite)
}