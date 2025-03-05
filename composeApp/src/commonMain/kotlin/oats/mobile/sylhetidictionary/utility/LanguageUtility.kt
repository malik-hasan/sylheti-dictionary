package oats.mobile.sylhetidictionary.utility

import oats.mobile.sylhetidictionary.data.settings.PreferencesRepository
import oats.mobile.sylhetidictionary.models.settings.Language

expect suspend fun PreferencesRepository.refreshLanguage()

expect fun setAppOSLanguage(language: Language)
