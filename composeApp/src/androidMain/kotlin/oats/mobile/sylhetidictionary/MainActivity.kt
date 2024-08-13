package oats.mobile.sylhetidictionary

import App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import data.settings.PreferenceKey
import data.settings.PreferencesRepository
import kotlinx.coroutines.launch
import models.EN
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

        val languageCode = AppCompatDelegate.getApplicationLocales().get(0)?.language ?: EN
        Logger.d("LOCALE: Refreshing locale preference: $languageCode")
        lifecycleScope.launch {
            preferences.put(PreferenceKey.LOCALE, languageCode)
        }
    }
}
