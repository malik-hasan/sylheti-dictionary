package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import models.dictionary.DictionaryData
import data.favorites.FavoritesRepository
import org.jetbrains.compose.resources.painterResource
import ui.theme.bengaliFontFamily
import org.koin.compose.koinInject
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bookmark
import sylhetidictionary.composeapp.generated.resources.bookmark_border

@Composable
fun EntryCard(
    data: DictionaryData,
    favoritesRepository: FavoritesRepository = koinInject(),
    onFavorite: (entryId: String, isFavorite: Boolean) -> Unit
) {
    val entry = data.entry

    var isFavorite by remember { mutableStateOf(false) }
    LaunchedEffect(entry.entryId) {
        isFavorite = favoritesRepository.checkFavorite(entry.entryId)
    }

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(entry.citationIPA ?: entry.lexemeIPA)
                        (entry.citationBangla ?: entry.lexemeBangla)?.let {
                            withStyle(SpanStyle(fontFamily = bengaliFontFamily)) {
                                append(" • $it")
                            }
                        }
                        (entry.citationNagri ?: entry.lexemeNagri)?.let {
                            append(" • $it")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false, radius = 20.dp),
                        onClick = {
                            isFavorite = !isFavorite
                            onFavorite(entry.entryId, isFavorite)
                        }
                    ),
                    painter = painterResource(
                        if (isFavorite) {
                            Res.drawable.bookmark
                        } else Res.drawable.bookmark_border
                    ),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "favorite"
                )
            }

            Text(
                text = taggedField(
                    tag = entry.partOfSpeech?.lowercase() ?: "unknown",
                    body = entry.gloss ?: ""
                ),
                style = MaterialTheme.typography.bodyMedium
            )

            entry.definitionEN?.let { def ->
                Text(
                    text = taggedField(
                        tag = "english",
                        body = def
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (entry.definitionBN != null || entry.definitionBNIPA != null) {
                Text(
                    text = taggedField(
                        tag = "bengali",
                        body = buildAnnotatedString {
                            entry.definitionBN?.let {
                                withStyle(SpanStyle(fontFamily = bengaliFontFamily)) {
                                    append("$it ")
                                }
                            }
                            entry.definitionBNIPA?.let {
                                append(it)
                            }
                        }
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (entry.definitionNagri != null || entry.definitionIPA != null) {
                Text(
                    text = taggedField(
                        tag = "sylheti",
                        body = buildAnnotatedString {
                            entry.definitionNagri?.let {
                                append("$it ")
                            }
                            entry.definitionIPA?.let {
                                append(it)
                            }
                        }
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
