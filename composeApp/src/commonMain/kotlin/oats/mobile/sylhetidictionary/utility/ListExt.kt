package oats.mobile.sylhetidictionary.utility

import oats.mobile.sylhetidictionary.DictionaryEntry

expect suspend fun List<DictionaryEntry>.getScrollCharIndexes(): Map<Char, Int>
