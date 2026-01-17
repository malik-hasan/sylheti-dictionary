package oats.mobile.sylhetidictionary

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import oats.mobile.sylhetidictionary.ui.app.App
import oats.mobile.sylhetidictionary.ui.app.AppViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class MainActivity : AppCompatActivity(), KoinComponent {

    private val vm: AppViewModel by viewModel()

    companion object {
        const val EXTRA_PROCESS_TEXT_SEARCH_TERM = "${BuildConfig.APPLICATION_ID}.extra.PROCESS_TEXT_SEARCH_TERM"
    }

    // copied from EdgeToEdge
    private val defaultLightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
    private val defaultDarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val processTextSearchTerm = intent.getCharSequenceExtra(EXTRA_PROCESS_TEXT_SEARCH_TERM)?.toString()

        setContent {
            val theme by vm.theme.collectAsStateWithLifecycle()
            val darkTheme = theme.isDarkTheme()
            LaunchedEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = if (darkTheme) {
                        SystemBarStyle.dark(Color.TRANSPARENT)
                    } else SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                    navigationBarStyle = if (darkTheme) {
                        SystemBarStyle.dark(defaultDarkScrim)
                    } else SystemBarStyle.light(defaultLightScrim, defaultDarkScrim)
                )
            }

            App(processTextSearchTerm)
        }

        findViewById<View>(android.R.id.content).viewTreeObserver.run {
            addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw() = vm.assetLoaded.value?.let {
                        removeOnPreDrawListener(this)
                        true
                    } ?: false
                }
            )
        }
    }
}
