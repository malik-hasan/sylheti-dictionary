package oats.mobile.sylhetidictionary.di

import oats.mobile.sylhetidictionary.DictionaryDatabase
import oats.mobile.sylhetidictionary.data.dictionary.DictionaryRepository
import oats.mobile.sylhetidictionary.ui.app.AppViewModel
import oats.mobile.sylhetidictionary.ui.screens.search.entry.EntryViewModel
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchViewModel
import oats.mobile.sylhetidictionary.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModel { parameters -> EntryViewModel(parameters.get(), get(), get()) }

    single { DictionaryRepository(DictionaryDatabase(get()).dictionaryDatabaseQueries) }
}
