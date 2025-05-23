package oats.mobile.sylhetidictionary.data.preferences.models.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import oats.mobile.sylhetidictionary.data.preferences.models.search.SettingEnum
import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.auto
import sylhetidictionary.composeapp.generated.resources.dark
import sylhetidictionary.composeapp.generated.resources.light

enum class Theme(override val label: StringResource): SettingEnum {
    Auto(Res.string.auto),
    Light(Res.string.light),
    Dark(Res.string.dark);

    val isDarkTheme
        @Composable get() = this == Dark
                || (this == Auto && isSystemInDarkTheme())
}
