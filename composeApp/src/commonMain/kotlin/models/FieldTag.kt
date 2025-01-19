package models

enum class FieldTag {
    EN, // english
    BN, // bengali in Bengali script
    BNIPA, // bengali in IPA
    IPA, // sylheti in IPA
    EASTERN_NAGRI, // sylheti in Bengali script
    SYLHETI_NAGRI; // sylheti in Sylheti script

    companion object {
        val bengaliTags = listOf(BN, BNIPA)
        val sylhetiTags = listOf(IPA, EASTERN_NAGRI, SYLHETI_NAGRI)
    }
}
