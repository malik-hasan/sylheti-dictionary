package oats.mobile.sylhetidictionary.utility

fun <T> Map<T, String?>.validateStrings() = this
    .filterValues { !it.isNullOrBlank() }
    .mapValues { it.value as String }
