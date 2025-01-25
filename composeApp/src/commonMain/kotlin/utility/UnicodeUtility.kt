package utility

object UnicodeUtility {

    val STOP_CHAR_MAP = mapOf(
        ' ' to setOf('.', '/', '(', ')', '-') // hyphen must be last
    )

    const val GLOB_SPECIAL_CHARS = "[]*?"

    val CASE_MAP = ('a'..'z').associateWith { setOf(it.uppercaseChar()) } +
        ('A'..'Z').associateWith { setOf(it.lowercaseChar()) }

    // chars found in Sylheti IPA fields:
    val SYLHETI_IPA_CHARS = setOf(
        'a', 'b', 'd', 'ɖ', 'ʤ', 'ɖ', 'e', 'ɛ', 'f', 'ɡ', 'h', 'i', 'ɪ', 'k', 'l', 'm', 'ɱ', 'n', 'ɳ', 'ŋ',
        'o', 'ɔ', 'p', 'r', 'ɾ', 'ɽ', 's', 'ʂ', 'ʃ', 't', 'ʈ', 'ʧ', 'u', 'ʊ', 'x', 'z', 'ʒ'
    )

    val SORTER = Comparator<String> { a, b ->
        // TODO
        if (a == b) return@Comparator 0

        for (i in 0..minOf(a.length, b.length)) {
            when {
                a[i] < b[i] -> return@Comparator -1
                a[i] > b[i] -> return@Comparator 1
            }
        }

        1
    }

    val SYLHETI_IPA_SORTER = compareBy<String> { word ->
        val letter = word.firstOrNull { it in SYLHETI_IPA_CHARS }
        // TODO

        SYLHETI_IPA_CHARS.indexOf(letter)

        when (letter) {
            'a' -> 0
            'b' -> 1
            'd' -> 2
            'ɖ' -> 3
            'ʤ' -> 4
            'e' -> 5
            'ɛ' -> 6
            'f' -> 7
            'ɡ' -> 8
            'h' -> 9
            'i' -> 10
            'ɪ' -> 11
            'k' -> 12
            'l' -> 13
            'm' -> 14
            'ɱ' -> 15
            'n' -> 16
            'ɳ' -> 17
            'ŋ' -> 18
            'o' -> 19
            'ɔ' -> 20
            'p' -> 21
            'r' -> 22
            'ɾ' -> 23
            'ɽ' -> 24
            's' -> 25
            'ʂ' -> 26
            'ʃ' -> 27
            't' -> 28
            'ʈ' -> 29
            'ʧ' -> 30
            'u' -> 31
            'ʊ' -> 32
            'x' -> 33
            'z' -> 34
            'ʒ' -> 35
            else -> Int.MIN_VALUE
        }
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
