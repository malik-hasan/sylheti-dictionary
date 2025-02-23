package utility

fun <T> Map<T, String?>.validateStrings() = filterValues { !it.isNullOrBlank() }.mapValues { it.value as String }
