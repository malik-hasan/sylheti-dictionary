package utility

object UnicodeUtility {

    val STOP_CHAR_MAP = mapOf(
        ' ' to setOf('.', '/', '(', ')', '-') // hyphen must be last
    )

    const val GLOB_SPECIAL_CHARS = "[]*?"

    val CASE_MAP = ('a'..'z').associateWith { setOf(it.uppercaseChar()) } +
        ('A'..'Z').associateWith { setOf(it.lowercaseChar()) }

    // chars found in Sylheti IPA fields:
    // "a", "b", "d", "e", "f", "h", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "x", "z",
    // "ŋ", "ɔ", "ɖ", "ɛ", "ɡ", "ɪ", "ɱ", "ɳ", "ɽ", "ɾ", "ʂ", "ʃ", "ʈ", "ʊ", "ʒ", "ʤ", "ʧ"
    val LATIN_IPA_CHAR_MAP = mapOf(
        'c' to setOf('ʧ'),
        'd' to setOf('ɖ', 'ɽ'),
        'e' to setOf('ɛ'),
        'g' to setOf('ɡ'), // If after 'n' make it optional
//        'h' to '', // If after s, c, k make it optional
        'i' to setOf('ɪ'),
        'j' to setOf('ʤ', 'ʒ'),
        'k' to setOf('x'),
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

    const val BENGALI_HOSHONTO = '্'
    const val SYLHETI_HOSHONTO = '꠆'
    val HOSHONTO = setOf(BENGALI_HOSHONTO, SYLHETI_HOSHONTO)

    val BENGALI_DIACRITICS = setOf('া', 'ি', 'ী', 'ু', 'ূ', 'ৃ', 'ৄ', 'ে', 'ৈ', 'ো', 'ৌ', 'ৗ', 'ঁ', 'ং', 'ঃ', '়', '্', 'ৢ', 'ৣ')
    val NAGRI_DIACRITICS = setOf('ꠣ', 'ꠤ', 'ꠥ', 'ꠦ', 'ꠧ', 'ꠂ', '꠆', 'ꠋ', '꠬')
    val ABUGIDA_DIACRITICS = BENGALI_DIACRITICS + NAGRI_DIACRITICS
}
