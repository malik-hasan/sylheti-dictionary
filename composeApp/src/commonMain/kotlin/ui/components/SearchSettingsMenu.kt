package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import models.search.settings.LatinSearchLanguage
import models.search.settings.SearchPosition
import models.search.settings.SearchScript
import org.jetbrains.compose.resources.stringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.search_language
import sylhetidictionary.composeapp.generated.resources.search_parts
import sylhetidictionary.composeapp.generated.resources.search_script
import ui.screens.search.SearchEvent
import ui.screens.search.SearchState

@Composable
fun SearchSettingsMenu(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit
) {

    DropdownMenu(
        expanded = state.settingsMenuOpen,
        onDismissRequest = { onEvent(SearchEvent.ToggleSettingsMenu(false)) }
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(stringResource(Res.string.search_parts))
            MultiChoiceSegmentedButtonRow(Modifier.height(IntrinsicSize.Min)) {
                with(SearchPosition.entries) {
                    forEachIndexed { index, searchPosition ->
                        SegmentedButton(
                            modifier = Modifier.fillMaxHeight(),
                            checked = state.searchPositions[index],
                            onCheckedChange = {},
                            shape = SegmentedButtonDefaults.itemShape(index, size)
                        ) { Text(stringResource(searchPosition.label)) }
                    }
                }
            }

            Text(stringResource(Res.string.search_script))
            SingleChoiceSegmentedButtonRow(Modifier.height(IntrinsicSize.Min)) {
                val searchScripts = SearchScript.entries
                searchScripts.forEachIndexed { index, searchScript ->
                    SegmentedButton(
                        modifier = Modifier.fillMaxHeight(),
                        selected = false,
                        onClick = {},
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index, count = searchScripts.size
                        )
                    ) {
                        Text(
                            searchScript.toString(), textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Text(stringResource(Res.string.search_language))
            MultiChoiceSegmentedButtonRow(Modifier.height(IntrinsicSize.Min)) {
                val searchLanguages = LatinSearchLanguage.entries
                searchLanguages.forEachIndexed { index, searchLanguage ->
                    SegmentedButton(
                        modifier = Modifier.fillMaxHeight(),
                        checked = true,
                        onCheckedChange = {},
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index, count = searchLanguages.size
                        )
                    ) {
                        Text(searchLanguage.toString())
                    }
                }
            }
        }
    }
}
