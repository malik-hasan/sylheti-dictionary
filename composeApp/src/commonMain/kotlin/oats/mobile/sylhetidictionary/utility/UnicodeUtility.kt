package oats.mobile.sylhetidictionary.utility

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
    ).withIndex().associate { it.value to it.index }

    // ALL non-ipa chars go first
//    val SYLHETI_IPA_SORTER = Comparator<String> { a, b ->
//        for (i in 0 until minOf(a.length, b.length)) {
//            with(SYLHETI_IPA_CHARS) {
//                indexOf(a[i]).compareTo(indexOf(b[i]))
//            }.takeIf { it != 0 }
//                ?.let { return@Comparator it }
//        }
//
//        a.length - b.length
//    }

//    val SYLHETI_IPA_SORTER = Comparator<String> { str1, str2 ->
//        var i1 = 0
//        var i2 = 0
//        while (i1 < str1.length && i2 < str2.length) {
//            var sortIndex1 = SYLHETI_IPA_CHARS[str1[i1]]
//            while (sortIndex1 == null && ++i1 < str1.length) {
//                sortIndex1 = SYLHETI_IPA_CHARS[str1[i1]]
//            }
//
//            var sortIndex2 = SYLHETI_IPA_CHARS[str2[i2]]
//            while (sortIndex2 == null && ++i2 < str2.length) {
//                sortIndex2 = SYLHETI_IPA_CHARS[str2[i2]]
//            }
//
//            if (sortIndex1 == null || sortIndex2 == null) break
//
//            val comparison = sortIndex1.compareTo(sortIndex2)
//            if (comparison != 0) {
//                return@Comparator comparison
//            } else {
//                i1++
//                i2++
//            }
//        }
//
//        val indexComparison = i2.compareTo(i1)
//        if (indexComparison != 0) {
//            indexComparison
//        } else str1.length - str2.length
//    }

    val SYLHETI_IPA_SORTER = Comparator<String> { str1, str2 ->
        val sortValues1 = str1.mapNotNull { SYLHETI_IPA_CHARS[it] }
        val sortValues2 = str2.mapNotNull { SYLHETI_IPA_CHARS[it] }

        sortValues1.zip(sortValues2)
            .firstNotNullOfOrNull { (sortValue1, sortValue2) ->
                sortValue1.compareTo(sortValue2).takeIf { it != 0 }
            } ?: run {
                val sortValuesSizeComparison = sortValues2.size - sortValues1.size
                if (sortValuesSizeComparison != 0) {
                    sortValuesSizeComparison
                } else str1.length - str2.length
            }
    }

//        val comparison = validIndices1.zip(validIndices2)
//            .map { (i1, i2) ->
//                compareValues(SYLHETI_IPA_CHARS[str1[i1]], SYLHETI_IPA_CHARS[str2[i2]])
//            }.firstOrNull { it != 0 }
//
//        if (comparison != null) {
//            comparison
//        } else {
//            val validCount1 = validIndices1.count()
//            val validCount2 = validIndices2.count()
//            if (validCount1 != validCount2) {
//                validCount2 - validCount1
//            } else {
//                str1.length - str2.length
//            }
//        }
//    }

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
