package utility

object UnicodeUtility {

    val STOP_CHAR_MAP = mapOf(
        ' ' to setOf('.', '/', '(', ')', '-') // hyphen must be last
    )

    const val GLOB_SPECIAL_CHARS = "[]*?"

    val CASE_MAP = ('a'..'z').associateWith { setOf(it.uppercaseChar()) } +
        ('A'..'Z').associateWith { setOf(it.lowercaseChar()) }

    // chars found in Sylheti IPA fields:
    val SYLHETI_IPA_CHARS = listOf(
        'a', 'b', 'd', 'ɖ', 'ʤ', 'e', 'ɛ', 'f', 'ɡ', 'h', 'i', 'ɪ', 'k', 'l', 'm', 'ɱ', 'n', 'ɳ', 'ŋ',
        'o', 'ɔ', 'p', 'r', 'ɾ', 'ɽ', 's', 'ʂ', 'ʃ', 't', 'ʈ', 'ʧ', 'u', 'ʊ', 'x', 'z', 'ʒ'
    )

    val SYLHETI_IPA_SORTER = Comparator<String> { a, b ->
        for (i in 0 until minOf(a.length, b.length)) {
            with(SYLHETI_IPA_CHARS) {
                indexOf(a[i]).compareTo(indexOf(b[i]))
            }.takeIf { it != 0 }
                ?.let { return@Comparator it }
        }

        a.length - b.length
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
    
    const val BENGALI_HOSHONTO = '্'
    const val SYLHETI_HOSHONTO = '꠆'
    val HOSHONTO = setOf(BENGALI_HOSHONTO, SYLHETI_HOSHONTO)

    val BENGALI_DIACRITICS = setOf('া', 'ি', 'ী', 'ু', 'ূ', 'ৃ', 'ৄ', 'ে', 'ৈ', 'ো', 'ৌ', 'ৗ', 'ঁ', 'ং', 'ঃ', '়', '্', 'ৢ', 'ৣ')
    val NAGRI_DIACRITICS = setOf('ꠣ', 'ꠤ', 'ꠥ', 'ꠦ', 'ꠧ', 'ꠂ', '꠆', 'ꠋ', '꠬')
    val ABUGIDA_DIACRITICS = BENGALI_DIACRITICS + NAGRI_DIACRITICS
}
