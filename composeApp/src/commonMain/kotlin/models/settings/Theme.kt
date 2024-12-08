package models.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import models.search.settings.SettingEnum
import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.auto
import sylhetidictionary.composeapp.generated.resources.dark
import sylhetidictionary.composeapp.generated.resources.light

enum class Theme(override val label: StringResource): SettingEnum {
    Auto(Res.string.auto),
    Light(Res.string.light),
    Dark(Res.string.dark);

    @Composable
    fun isDarkTheme() = if (this == Auto) isSystemInDarkTheme() else this == Dark
}
