package di

import data.dictionary.DictionaryDataSource
import data.dictionary.DictionaryDataSourceImpl
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ui.AppViewModel
import ui.screens.search.SearchViewModel
import ui.screens.settings.SettingsViewModel

val sharedModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)

    single {
        DictionaryDataSourceImpl(DictionaryDatabase(get()))
    }.bind(DictionaryDataSource::class)
}
