package oats.mobile.sylhetidictionary.utility

import sylhetidictionary.composeapp.generated.resources.Res

const val DictionaryAsset = "sylhetiLexicon.db"

// Manually increment this whenever the dictionary asset is updated
const val DictionaryAssetVersion = 0

suspend fun readDictionaryAsset() = Res.readBytes("files/$DictionaryAsset")
