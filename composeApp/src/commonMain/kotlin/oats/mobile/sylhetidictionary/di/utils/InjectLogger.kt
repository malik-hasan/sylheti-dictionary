package oats.mobile.sylhetidictionary.di.utils

import co.touchlab.kermit.Logger
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

fun KoinComponent.injectLogger() =
    inject<Logger> { parametersOf(this::class.simpleName) }

fun Koin.injectLogger(tag: String? = null) =
    inject<Logger> { parametersOf(tag) }
