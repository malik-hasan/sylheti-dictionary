package ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.ipa_help
import ui.components.DrawerIconButton
import ui.components.SDScreen
import ui.theme.TextLinkStyle
import utility.UnicodeUtility.LATIN_IPA_CHAR_MAP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IpaHelpScreen() {
    SDScreen(
        topBar = {
            TopAppBar(
                navigationIcon = { DrawerIconButton() },
                title = { Text(stringResource(Res.string.ipa_help)) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withLink(LinkAnnotation.Url(
                        url = "https://en.wikipedia.org/wiki/International_Phonetic_Alphabet",
                        styles = TextLinkStyle
                    )) { append("What is IPA?") }
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = buildAnnotatedString {
                    withLink(LinkAnnotation.Url(
                        url = "https://en.wikipedia.org/wiki/Help:IPA/Sylheti",
                        styles = TextLinkStyle
                    )) { append("How does IPA represent Sylheti sounds?") }
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "How do I search in IPA?",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "For convenience this app supports the following equivalences between Latin and IPA chars so that you may search using a typical QWERTY keyboard.",
                style = MaterialTheme.typography.bodyMedium
            )

            LATIN_IPA_CHAR_MAP.forEach { (char, charSet) ->
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Black)) {
                            append("$char — ")
                        }
                        append("$char, ${charSet.joinToString(", ")}")
                    }
                )
            }
        }
    }
}
