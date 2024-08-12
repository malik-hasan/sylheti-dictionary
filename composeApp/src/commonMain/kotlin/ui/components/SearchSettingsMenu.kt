package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
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

@Composable
fun SearchSettingsMenu(
    expanded: Boolean,
    onDismiss: () -> Unit
) {
    DropdownMenu(expanded, onDismiss) {
        Column(Modifier.padding(16.dp)) {
            Text("Part of word to search")
            MultiChoiceSegmentedButtonRow(Modifier.height(IntrinsicSize.Min)) {
                val searchPositions = SearchPosition.entries
                searchPositions.forEachIndexed { index, searchPosition ->
                    SegmentedButton(
                        modifier = Modifier.fillMaxHeight(),
                        checked = true,
                        onCheckedChange = {},
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index, count = searchPositions.size
                        )
                    ) {
                        Text(searchPosition.name)
                    }
                }
            }
            Text("Search script")
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
            Text("Search language")
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
