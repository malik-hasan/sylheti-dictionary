package models.settings

import org.jetbrains.compose.resources.StringResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.bengali
import sylhetidictionary.composeapp.generated.resources.english

enum class Language(val code: String, val label: StringResource, val indicator: Char) {
    EN("en", Res.string.english, 'A'),
    BN("bn", Res.string.bengali, 'ক');

    companion object {
        fun fromCode(code: String) = if (code == BN.code) BN else EN
    }
}
