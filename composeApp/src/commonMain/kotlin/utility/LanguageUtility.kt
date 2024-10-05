package utility

import data.settings.PreferencesRepository
import models.Language

expect suspend fun PreferencesRepository.refreshLanguage()

expect fun setAppOSLanguage(language: Language)
