package models

enum class FieldTag {
    EN, // english
    BN, // bengali in Bengali script
    BNIPA, // bengali in IPA
    IPA, // sylheti in IPA
    BENGALI, // sylheti in Bengali script
    NAGRI; // sylheti in Nagri script

    companion object {
        val bengaliTags = listOf(BN, BNIPA)
        val sylhetiTags = listOf(IPA, BENGALI, NAGRI)
    }
}
