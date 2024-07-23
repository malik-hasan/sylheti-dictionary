package data.favorites

import androidx.room.Database
import androidx.room.RoomDatabase
import data.RoomCompileAppeaser
import models.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class FavoritesDatabase : RoomDatabase(), RoomCompileAppeaser {

    companion object {
        const val FILENAME = "favorites.db"
    }

    abstract fun dao(): FavoritesDao

    override fun clearAllTables() = super.clearAllTables()
}
