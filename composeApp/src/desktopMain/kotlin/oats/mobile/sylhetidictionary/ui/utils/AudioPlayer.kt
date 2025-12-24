package oats.mobile.sylhetidictionary.ui.utils

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import oats.mobile.sylhetidictionary.di.utils.injectLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.HttpURLConnection
import java.net.URI
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent
import kotlin.coroutines.cancellation.CancellationException

actual class AudioPlayer: KoinComponent {

    private val logger by injectLogger()
    private val scope: CoroutineScope by inject()

    private var playbackJob: Job? = null
    private var audioClip: Clip? = null

    actual fun play(url: String) {
        release()
        playbackJob = scope.launch(Dispatchers.IO) {
            try {
                ensureActive()
                val connection = (URI(url).toURL().openConnection() as HttpURLConnection).apply {
                    setRequestProperty("User-Agent", "SylhetiDictionary.app")
                    connect()
                }

                ensureActive()
                val audioInputStream = AudioSystem.getAudioInputStream(connection.inputStream)
                val decodedFormat = audioInputStream.format.run {
                    AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        sampleRate,
                        16,  // 16-bit audio
                        channels,
                        channels * 2,  // 2 bytes per sample (16-bit)
                        sampleRate,
                        false  // little-endian
                    )
                }

                ensureActive()
                val decodedStream = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream)

                ensureActive()
                audioClip = AudioSystem.getClip().apply {
                    open(decodedStream)

                    val playbackComplete = CompletableDeferred<Unit>(coroutineContext.job)
                    addLineListener { event ->
                        if (event.type == LineEvent.Type.STOP) {
                            playbackComplete.complete(Unit)
                        }
                    }

                    logger.d("AudioPlayer: starting")
                    start()
                    playbackComplete.await()
                    logger.d("AudioPlayer: finished")
                }
            } catch (e: Exception) {
                if (e is CancellationException) {
                    logger.d("AudioPlayer: cancelled")
                    throw e
                } else logger.e("AudioPlayer: ${e.message}")
            }
        }

        playbackJob?.invokeOnCompletion {
            logger.d("AudioPlayer: closing")
            audioClip?.close()
            audioClip = null
        }
    }

    actual fun release() {
        playbackJob?.cancel()
    }
}
