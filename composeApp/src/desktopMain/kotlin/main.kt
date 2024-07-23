import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.initKoin

fun main() {
    initKoin()

    // TODO: copy db file

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "SylhetiDictionary",
        ) {
            App()
        }
    }
}