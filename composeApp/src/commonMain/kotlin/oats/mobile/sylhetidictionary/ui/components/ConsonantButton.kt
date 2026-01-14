package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import co.touchlab.kermit.Logger
import oats.mobile.sylhetidictionary.ui.utils.AudioPlayer
import oats.mobile.sylhetidictionary.ui.utils.playPhone
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun ConsonantButton(
    consonant: String,
    path: String,
    audioPlayer: AudioPlayer,
    tappedConsonant: String?,
    onClick: (String) -> Unit,
    density: Density = LocalDensity.current,
    logger: Logger = koinInject { parametersOf("IpaHelp") }
) {
    val textStyle = MaterialTheme.typography.bodyLarge
    Box(Modifier
        .clip(CircleShape)
        .background(
            if (consonant == tappedConsonant) {
                MaterialTheme.colorScheme.secondary
            } else ButtonDefaults.buttonColors().containerColor
        )
        .size(with(density) { textStyle.lineHeight.toDp() })
        .clickable(null, null) {
            onClick(consonant)
            logger.d("IPA_HELP: playing audio for $path")
            audioPlayer.playPhone(path)
        },
        contentAlignment = Alignment.Center
    ) {
        DisableSelection {
            Text(consonant, color = ButtonDefaults.buttonColors().contentColor)
        }
    }
}
