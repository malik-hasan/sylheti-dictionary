package oats.mobile.sylhetidictionary.di.utils

import co.touchlab.kermit.Logger
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

inline fun KoinComponent.injectLogger() =
    inject<Logger> { parametersOf(this::class.simpleName) }

inline fun Koin.injectLogger() =
    inject<Logger> { parametersOf(null) }
