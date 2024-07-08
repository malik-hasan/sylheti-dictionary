package di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ui.screens.search.SearchViewModel

actual val uiModule = module {
    viewModelOf(::SearchViewModel)
}
