package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.search.settings.SettingEnum
import org.jetbrains.compose.resources.stringResource

@Composable
fun <E> SingleChoiceSegmentedSearchSetting(
    settingLabel: String,
    entries: Collection<E>,
    selection: E,
    onSelect: (E) -> Unit
) where E: Enum<E>, E: SettingEnum {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(settingLabel, Modifier.padding(start = 8.dp))
        SingleChoiceSegmentedButtonRow {
            with(entries) {
                forEachIndexed { i, entry ->
                    val label = stringResource(entry.label)
                    SegmentedButton(
                        modifier = Modifier.weight(label.length.toFloat().coerceIn(5f, 8f)),
                        selected = selection == entry,
                        onClick = { onSelect(entry) },
                        shape = SegmentedButtonDefaults.itemShape(i, size)
                    ) { Text(label) }
                }
            }
        }
    }
}
