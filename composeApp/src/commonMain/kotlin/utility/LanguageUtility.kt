package utility

import data.settings.PreferencesDataSource
import models.settings.Language

expect suspend fun PreferencesDataSource.refreshLanguage()

expect fun setAppOSLanguage(language: Language)
