package oats.mobile.sylhetidictionary.ui.utils

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class AudioPlayer: KoinComponent {

    private val context: Context by inject()
    private val player: ExoPlayer = ExoPlayer.Builder(context).build()

    actual fun play(url: String) = player.run {
        setMediaItem(MediaItem.fromUri(url))
        prepare()
        play()
    }

    actual fun release() = player.release()
}
