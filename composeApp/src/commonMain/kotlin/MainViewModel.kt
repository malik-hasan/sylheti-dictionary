import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainViewModel(preferences: PreferencesRepository): ViewModel() {

    val language = preferences
        .flow(PreferenceKey.LANGUAGE, Language.English.code)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Language.English.code)
}
