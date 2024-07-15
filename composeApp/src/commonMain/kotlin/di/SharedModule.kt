package di

import data.dictionary.DictionaryDataSource
import data.dictionary.DictionaryDataSourceImpl
import data.settings.SettingsRepository
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    single { SettingsRepository(get()) }
    single {
        DictionaryDataSourceImpl(
            DictionaryDatabase(get())
        )
    }.bind(DictionaryDataSource::class)
//    single { FavoritesRepository(get(), get()) }
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
