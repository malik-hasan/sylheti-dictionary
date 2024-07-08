package di

import data.favorites.FavoritesRepository
import data.settings.SettingsRepository
import org.koin.dsl.module

val dataModule = module {
    single { SettingsRepository(get()) }
    single { FavoritesRepository(get(), get()) }
//    single {
//        Room.databaseBuilder(
//            get(), DictionaryDatabase::class.java, "sylhetiLexicon.db"
//        ).createFromAsset("sylhetiLexicon.db").build().dictionaryDao()
//    }
//
//    single {
//        Room.databaseBuilder(
//            get(), RecentSearchesDatabase::class.java, "recentSearches.db"
//        ).build().recentSearchesDao()
//    }
}
