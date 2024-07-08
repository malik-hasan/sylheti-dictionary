package models.search.settings

enum class SearchScript(private val displayName: String) {
    Auto("Auto"),
    Latin("Latin/IPA"),
    Bengali("Bengali"),
    Nagri("Nagri");

    override fun toString() = displayName
}
