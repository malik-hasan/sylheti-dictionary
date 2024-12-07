package models.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import models.search.settings.SettingEnum
import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.auto
import sylhetidictionary.composeapp.generated.resources.dark
import sylhetidictionary.composeapp.generated.resources.light

enum class Theme(override val label: StringResource, val isDarkTheme: @Composable () -> Boolean): SettingEnum {
    Auto(Res.string.auto, { isSystemInDarkTheme() }),
    Light(Res.string.light, { false }),
    Dark(Res.string.dark, { true })
}
