package models

class CharSets {

    // "a", "b", "d", "e", "f", "h", "i", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "x", "z",
    // "ŋ", "ɔ", "ɖ", "ɛ", "ɡ", "ɪ", "ɱ", "ɳ", "ɽ", "ɾ", "ʂ", "ʃ", "ʈ", "ʊ", "ʒ", "ʤ", "ʧ"
    val ipaCharMaps = mapOf(
        "c" to "ʧ",
        "d" to setOf("ɖ", "ɽ"),
        "e" to "ɛ",
        "g" to "ɡ", // If after "n" make it optional
//        "h" to "", // If after s, c, k make it optional
        "i" to "ɪ",
        "j" to setOf("ʤ", "ʒ"),
        "k" to "x",
        "m" to "ɱ",
        "n" to setOf("ŋ", "ɱ", "ɳ"),
        "o" to "ɔ",
        "r" to setOf("ɽ", "ɾ"),
        "s" to setOf("ʂ", "ʃ"),
        "t" to "ʈ",
        "u" to "ʊ",
        "w" to setOf("ʊ", "ɔ"),
        "y" to setOf("ɪ", "ɛ")
    )
}