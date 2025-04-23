package oats.mobile.sylhetidictionary.ui.models

enum class FieldTag {
    ENGLISH, // english
    BENGALI, // bengali in Bengali script
    BENGALI_IPA, // bengali in IPA
    IPA, // sylheti in IPA
    EASTERN_NAGRI, // sylheti in Bengali script
    SYLHETI_NAGRI; // sylheti in Sylheti script

    companion object {
        val bengaliTags = listOf(BENGALI, BENGALI_IPA)
        val sylhetiTags = listOf(IPA, EASTERN_NAGRI, SYLHETI_NAGRI)
    }
}
