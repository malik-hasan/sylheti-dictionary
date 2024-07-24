package di

import MainViewModel
import data.dictionary.DictionaryDataSource
import data.dictionary.DictionaryDataSourceImpl
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ui.screens.search.SearchViewModel

val sharedModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::SearchViewModel)

    single {
        DictionaryDataSourceImpl(DictionaryDatabase(get()))
    }.bind(DictionaryDataSource::class)
}
