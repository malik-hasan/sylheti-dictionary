package oats.mobile.sylhetidictionary.utility

object UnicodeUtility {

    val STOP_CHAR_MAP = mapOf(
        ' ' to setOf('.', '/', '(', ')', '-') // hyphen must be last
    )

    const val GLOB_SPECIAL_CHARS = "[]*?"

    val CASE_MAP = ('a'..'z').associateWith { setOf(it.uppercaseChar()) } +
        ('A'..'Z').associateWith { setOf(it.lowercaseChar()) }

    val NON_INITIAL_CHARS = listOf('ɱ', 'ŋ', 'ɽ', 'ʂ')

    // chars found in Sylheti IPA fields:
    val SYLHETI_IPA_CHARS = listOf(
        'a', 'b', 'd', 'ɖ', 'ʤ', 'e', 'ɛ', 'f', 'ɡ', 'h', 'i', 'ɪ', 'k', 'l', 'm', 'ɱ', 'n', 'ɳ', 'ŋ',
        'o', 'ɔ', 'p', 'r', 'ɾ', 'ɽ', 's', 'ʂ', 'ʃ', 't', 'ʈ', 'ʧ', 'u', 'ʊ', 'x', 'z', 'ʒ'
    ).withIndex().associate { it.value to it.index }

    val SYLHETI_IPA_SORTER = Comparator<String> { str1, str2 ->
        var i1 = 0
        var i2 = 0
        while (i1 < str1.length && i2 < str2.length) {
            val char1 = str1[i1]
            val char2 = str2[i2]

            val sortValue1 = SYLHETI_IPA_CHARS[char1]
            val sortValue2 = SYLHETI_IPA_CHARS[char2]

            val comparison = if (sortValue1 == null && sortValue2 == null) {
                char1.compareTo(char2)
            } else compareValues(sortValue1, sortValue2)

            if (comparison != 0) return@Comparator comparison

            i1++
            i2++
        }

        str1.length - str2.length
    }

    val LATIN_IPA_CHAR_MAP = mapOf(
        'c' to setOf('ʧ'),
        'd' to setOf('ɖ', 'ɽ'),
        'e' to setOf('ɛ'),
        'g' to setOf('ɡ'),
        'i' to setOf('ɪ'),
        'j' to setOf('ʤ', 'ʒ'),
        'm' to setOf('ɱ'),
        'n' to setOf('ŋ', 'ɱ', 'ɳ'),
        'o' to setOf('ɔ'),
        'r' to setOf('ɽ', 'ɾ'),
        's' to setOf('ʂ', 'ʃ'),
        't' to setOf('ʈ'),
        'u' to setOf('ʊ'),
        'w' to setOf('ʊ', 'ɔ'),
        'y' to setOf('ɪ', 'ɛ')
    )
    
    private const val BENGALI_HOSHONTO = '্'
    private const val SYLHETI_HOSHONTO = '꠆'
    val HOSHONTO = setOf(BENGALI_HOSHONTO, SYLHETI_HOSHONTO)

    private val BENGALI_DIACRITICS = setOf('া', 'ি', 'ী', 'ু', 'ূ', 'ৃ', 'ৄ', 'ে', 'ৈ', 'ো', 'ৌ', 'ৗ', 'ঁ', 'ং', 'ঃ', '়', '্', 'ৢ', 'ৣ')
    private val NAGRI_DIACRITICS = setOf('ꠣ', 'ꠤ', 'ꠥ', 'ꠦ', 'ꠧ', 'ꠂ', '꠆', 'ꠋ', '꠬')
    val ABUGIDA_DIACRITICS = BENGALI_DIACRITICS + NAGRI_DIACRITICS
}
