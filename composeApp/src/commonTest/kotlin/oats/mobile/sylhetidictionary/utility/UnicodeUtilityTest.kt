package oats.mobile.sylhetidictionary.utility

import kotlin.test.Test
import kotlin.test.assertEquals

class UnicodeUtilityTest {

    @Test
    fun shouldSortSylhetiIpaAlphabetically() {
        val sylhetiIpaChars = UnicodeUtility.SYLHETI_IPA_CHARS.keys.map { it.toString() }
        assertEquals(
            sylhetiIpaChars,
            sylhetiIpaChars.shuffled().sortedWith(UnicodeUtility.SYLHETI_IPA_SORTER)
        )
    }

    @Test
    fun shouldSortSylhetiIpaWords() {
        val sampleWords = listOf(
            " haʃ faʃ xɔɾ",
            "-a",
            "-bar",
            "-bar bar",
            "a",
            "a-",
            "alɛda",
            "bar",
            "bar bar",
            "bar bar-",
            "bar-",
            "bar-bar",
            "bar-bar-",
            "barbar",
            "barbar-",
            "bɔdmaʃ",
            "bɔɾ bɔɾia",
            "dʊti",
            "ɖɛɡsi",
            "fɛxna",
            "fik dani",
            "fɔɪlfa",
            "haʃ",
            "haʃ faʃ ",
            "haʃ faʃ xɔɾ",
            "il",
            "kial xɔɾ",
            "kunandi",
            "mɛnɾa",
            "nɔbɔɪ",
            "saɔa",
            "ʊfxaɾ ɔ",
            "xal",
            "zamaɪ",
            "zinɔ",
            "zɔldi xɔɾi"
        )

        assertEquals(
            sampleWords,
            sampleWords.shuffled().sortedWith(UnicodeUtility.SYLHETI_IPA_SORTER)
        )
    }
}
