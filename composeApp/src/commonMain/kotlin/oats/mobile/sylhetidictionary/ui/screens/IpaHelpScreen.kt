package oats.mobile.sylhetidictionary.ui.screens

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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import oats.mobile.sylhetidictionary.ui.components.DrawerIconButton
import oats.mobile.sylhetidictionary.ui.components.SDScreen
import oats.mobile.sylhetidictionary.ui.components.SDTopAppBar
import oats.mobile.sylhetidictionary.ui.theme.latinBodyFontFamily
import oats.mobile.sylhetidictionary.ui.theme.textLinkStyle
import oats.mobile.sylhetidictionary.utility.UnicodeUtility
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.how_does_ipa_rep_sylheti
import sylhetidictionary.composeapp.generated.resources.how_to_search_ipa
import sylhetidictionary.composeapp.generated.resources.ipa_equivalences
import sylhetidictionary.composeapp.generated.resources.ipa_help
import sylhetidictionary.composeapp.generated.resources.ipa_url
import sylhetidictionary.composeapp.generated.resources.sylheti_ipa_url
import sylhetidictionary.composeapp.generated.resources.what_is_ipa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IpaHelpScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    SDScreen(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SDTopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = { DrawerIconButton() },
                title = { Text(stringResource(Res.string.ipa_help)) }
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(scaffoldPadding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withLink(LinkAnnotation.Url(
                        url = stringResource(Res.string.ipa_url),
                        styles = textLinkStyle
                    )) { append(stringResource(Res.string.what_is_ipa)) }
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = buildAnnotatedString {
                    withLink(LinkAnnotation.Url(
                        url = stringResource(Res.string.sylheti_ipa_url),
                        styles = textLinkStyle
                    )) { append(stringResource(Res.string.how_does_ipa_rep_sylheti)) }
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.how_to_search_ipa),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(Res.string.ipa_equivalences),
                style = MaterialTheme.typography.bodyMedium
            )

            UnicodeUtility.LATIN_IPA_CHAR_MAP.forEach { (char, charSet) ->
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Black)) {
                            append("$char — ")
                        }
                        append(charSet.joinToString(", "))
                    },
                    fontFamily = latinBodyFontFamily
                )
            }
        }
    }
}
