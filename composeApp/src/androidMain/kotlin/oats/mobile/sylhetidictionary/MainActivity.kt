package oats.mobile.sylhetidictionary

import App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : AppCompatActivity(), KoinComponent {

    private val preferences: PreferencesRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }

    override fun onResume() {
        super.onResume()

        AppCompatDelegate.getApplicationLocales().get(0)?.language?.let { appLocale ->
            lifecycleScope.launch {
                preferences.put(PreferenceKey.LOCALE, appLocale)
            }
        }
    }
}
