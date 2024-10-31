package di

import data.dictionary.DictionaryDataSource
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ui.AppViewModel
import ui.screens.search.SearchViewModel
import ui.screens.settings.SettingsViewModel

val sharedModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)

    single { DictionaryDataSource(DictionaryDatabase(get()).dictionaryDatabaseQueries) }
}
