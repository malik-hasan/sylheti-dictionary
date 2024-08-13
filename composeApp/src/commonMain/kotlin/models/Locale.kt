package models

enum class Locale(val code: String) {
    English("en"),
    Bengali("bn");

    companion object {
        fun fromCode(code: String) = entries.find { it.code == code } ?: English
    }
}
