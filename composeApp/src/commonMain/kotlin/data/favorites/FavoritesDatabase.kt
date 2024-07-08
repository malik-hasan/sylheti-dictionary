package data.favorites

import androidx.room.Database
import androidx.room.RoomDatabase
import models.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}
