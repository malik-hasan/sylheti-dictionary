package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import oats.mobile.sylhetidictionary.models.search.settings.SearchLanguage
import oats.mobile.sylhetidictionary.models.search.settings.SearchPosition
import oats.mobile.sylhetidictionary.models.search.settings.SearchScript
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchEvent
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchSettingsEvent
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchSettingsState
import oats.mobile.sylhetidictionary.ui.screens.search.search.SearchState
import oats.mobile.sylhetidictionary.ui.theme.latinDisplayFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.also_search
import sylhetidictionary.composeapp.generated.resources.deep_search
import sylhetidictionary.composeapp.generated.resources.filter
import sylhetidictionary.composeapp.generated.resources.filter_part_of_speech
import sylhetidictionary.composeapp.generated.resources.in_definitions
import sylhetidictionary.composeapp.generated.resources.in_examples
import sylhetidictionary.composeapp.generated.resources.search_language
import sylhetidictionary.composeapp.generated.resources.search_languages
import sylhetidictionary.composeapp.generated.resources.search_position
import sylhetidictionary.composeapp.generated.resources.search_script

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSettingsMenu(
    searchState: SearchState,
    onSearchEvent: (SearchEvent) -> Unit,
    settingsState: SearchSettingsState,
    onSettingsEvent: (SearchSettingsEvent) -> Unit
) {
    DropdownMenu(
        expanded = searchState.settingsMenuExpanded,
        onDismissRequest = { onSearchEvent(SearchEvent.ToggleSettingsMenu(false)) }
    ) {
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable(null, null) {
                    focusManager.clearFocus()
                },
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SettingLabel(
                    iconPainter = painterResource(Res.drawable.search_position),
                    label = stringResource(Res.string.search_position),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                SingleChoiceSegmentedButtonRow {
                    with(SearchPosition.entries) {
                        forEachIndexed { i, entry ->
                            val label = stringResource(entry.label)
                            SegmentedButton(
                                modifier = Modifier.weight(label.length.coerceIn(5, 8).toFloat()),
                                selected = settingsState.position == entry,
                                onClick = { onSettingsEvent(SearchSettingsEvent.SelectPosition(entry)) },
                                shape = SegmentedButtonDefaults.itemShape(i, size)
                            ) { Text(label) }
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SettingLabel(
                    iconPainter = painterResource(Res.drawable.search_script),
                    label = stringResource(Res.string.search_script),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                SingleChoiceSegmentedButtonRow(Modifier.height(IntrinsicSize.Max)) {
                    with(SearchScript.entries) {
                        forEachIndexed { i, entry ->
                            val label = stringResource(entry.label)
                            SegmentedButton(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(label.length.coerceIn(5, 7).toFloat()),
                                selected = settingsState.script == entry,
                                onClick = { onSettingsEvent(SearchSettingsEvent.SelectScript(entry)) },
                                shape = SegmentedButtonDefaults.itemShape(i, size)
                            ) { Text(label) }
                        }
                    }
                }
            }

            with(settingsState.languages.toList()) {
                AnimatedVisibility(
                    visible = isNotEmpty(),
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SettingLabel(
                            iconPainter = painterResource(Res.drawable.search_language),
                            label = stringResource(Res.string.search_languages),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        MultiChoiceSegmentedButtonRow {
                            forEachIndexed { i, (language, checked) ->
                                SegmentedButton(
                                    checked = checked,
                                    onCheckedChange = { selected ->
                                        onSettingsEvent(SearchSettingsEvent.SelectLanguage(language, selected))
                                    },
                                    shape = SegmentedButtonDefaults.itemShape(i, size)
                                ) { Text(stringResource(language.label)) }
                            }
                        }
                    }
                }
            }

            Column {
                SettingLabel(
                    iconPainter = painterResource(Res.drawable.deep_search),
                    label = stringResource(Res.string.also_search),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                val easternNagriEnabled = settingsState.script == SearchScript.EASTERN_NAGRI
                val bengaliEasternNagriEnabled = easternNagriEnabled
                        && settingsState.languages[SearchLanguage.EasternNagri.BENGALI] == true
                        && settingsState.languages[SearchLanguage.EasternNagri.SYLHETI] != true
                val sylhetiEasternNagriEnabled = easternNagriEnabled
                        && settingsState.languages[SearchLanguage.EasternNagri.SYLHETI] == true
                        && settingsState.languages[SearchLanguage.EasternNagri.BENGALI] != true

                AnimatedVisibility(!sylhetiEasternNagriEnabled) {
                    CheckboxSearchSetting(
                        label = stringResource(Res.string.in_definitions),
                        checked = settingsState.searchDefinitions || bengaliEasternNagriEnabled,
                        enabled = !bengaliEasternNagriEnabled
                    ) { onSettingsEvent(SearchSettingsEvent.ToggleSearchDefinitions(it)) }
                }

                CheckboxSearchSetting(
                    label = stringResource(Res.string.in_examples),
                    checked = settingsState.searchExamples
                ) { onSettingsEvent(SearchSettingsEvent.ToggleSearchExamples(it)) }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                SettingLabel(
                    iconPainter = painterResource(Res.drawable.filter),
                    label = stringResource(Res.string.filter_part_of_speech),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {
                    searchState.partOfSpeechFilters.forEach { partOfSpeech ->
                        InputChip(
                            modifier = Modifier.wrapContentSize(),
                            selected = true,
                            onClick = { onSearchEvent(SearchEvent.ApplyPartOfSpeechFilter(partOfSpeech, false)) },
                            label = {
                                Text(
                                    text = partOfSpeech,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = latinDisplayFontFamily
                                )
                            },
                            trailingIcon = { Icon(Icons.Default.Close, "close") },
                            colors = InputChipDefaults.inputChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                selectedTrailingIconColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                    }

                    var filterQuery by remember { mutableStateOf("") }
                    LaunchedEffect(filterQuery) {
                        delay(150)
                        onSearchEvent(SearchEvent.GetPartOfSpeechSuggestions(filterQuery))
                    }

                    val suggestions = with(searchState) {
                        remember(partOfSpeechSuggestions, partOfSpeechFilters) {
                            partOfSpeechSuggestions - partOfSpeechFilters
                        }
                    }
                    val (allowExpanded, onExpandedChange) = remember { mutableStateOf(false) }
                    val expanded = allowExpanded && suggestions.isNotEmpty()

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = onExpandedChange
                    ) {
                        val interactionSource = remember { MutableInteractionSource() }
                        BasicTextField(
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
                            value = filterQuery,
                            onValueChange = { filterQuery = it },
                            textStyle = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = latinDisplayFontFamily
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                hintLocales = LocaleList(Locale("en"))
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            interactionSource = interactionSource
                        ) { innerTextField ->
                            val isFocused by interactionSource.collectIsFocusedAsState()
                            Box(
                                modifier = Modifier
                                    .size(width = 150.dp, height = 32.dp)
                                    .border(
                                        width = 1.dp,
                                        color = with(MaterialTheme.colorScheme) {
                                            if (isFocused) secondary else outline
                                        },
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.Center
                            ) { innerTextField() }
                        }

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { onExpandedChange(false) },
                            modifier = Modifier.heightIn(max = 150.dp)
                        ) {
                            suggestions.forEach { partOfSpeech ->
                                DropdownMenuItem(
                                    text = { Text(partOfSpeech) },
                                    onClick = {
                                        onSearchEvent(SearchEvent.ApplyPartOfSpeechFilter(partOfSpeech, true))
                                        focusManager.clearFocus()
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
