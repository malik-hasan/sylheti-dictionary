package oats.mobile.sylhetidictionary

import App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import co.touchlab.kermit.Logger
import data.settings.PreferencesRepository
import kotlinx.coroutines.launch
import models.Language
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

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

        val locale = AppCompatDelegate.getApplicationLocales().get(0) ?: Locale.getDefault()
        Logger.d("LOCALE: refreshing language preference from: $locale")
        lifecycleScope.launch {
            preferences.setLanguage(Language.fromCode(locale.language))
        }
    }
}
