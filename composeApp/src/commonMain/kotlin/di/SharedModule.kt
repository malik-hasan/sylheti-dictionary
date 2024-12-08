package di

import data.dictionary.DictionaryDataSource
import oats.mobile.sylhetidictionary.DictionaryDatabase
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ui.app.AppViewModel
import ui.screens.search.entry.EntryViewModel
import ui.screens.search.search.SearchViewModel
import ui.screens.settings.SettingsViewModel

val sharedModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModel { parameters -> EntryViewModel(parameters.get(), get(), get()) }

    single { DictionaryDataSource(DictionaryDatabase(get()).dictionaryDatabaseQueries) }
}
