package oats.mobile.sylhetidictionary.utility

import co.touchlab.kermit.Logger
import oats.mobile.sylhetidictionary.data.preferences.PreferencesRepository
import oats.mobile.sylhetidictionary.models.settings.Language

expect suspend fun PreferencesRepository.refreshLanguage(logger: Logger = Logger.withTag("Refresh Language"))

expect fun setAppOSLanguage(language: Language)
