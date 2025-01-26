package oats.mobile.sylhetidictionary.utility

import oats.mobile.sylhetidictionary.data.settings.PreferencesDataSource
import oats.mobile.sylhetidictionary.models.settings.Language

expect suspend fun PreferencesDataSource.refreshLanguage()

expect fun setAppOSLanguage(language: Language)
