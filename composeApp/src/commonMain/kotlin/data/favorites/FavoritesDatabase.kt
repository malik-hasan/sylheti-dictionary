package data.favorites

import androidx.room.Database
import androidx.room.RoomDatabase
import data.RoomCompileAppeaser
import models.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class FavoritesDatabase : RoomDatabase(), RoomCompileAppeaser {
    abstract fun favoritesDao(): FavoritesDao

    override fun clearAllTables() = super.clearAllTables()
}
