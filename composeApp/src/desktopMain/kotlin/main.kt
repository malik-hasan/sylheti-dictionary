import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.initKoin
import di.platformModule
import di.sharedModule

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "SylhetiDictionary",
        ) {
            App()
        }
    }
}