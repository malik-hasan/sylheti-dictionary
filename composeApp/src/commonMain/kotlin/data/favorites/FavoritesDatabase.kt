package data.favorites

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import models.Favorite

expect object FavoritesDatabaseConstructor : RoomDatabaseConstructor<FavoritesDatabase>

@Database(entities = [Favorite::class], version = 1)
@ConstructedBy(FavoritesDatabaseConstructor::class)
abstract class FavoritesDatabase : RoomDatabase() {

    companion object {
        const val FILENAME = "favorites.db"
    }

    abstract fun dao(): FavoritesDao

//    override fun clearAllTables() = super.clearAllTables()
}
