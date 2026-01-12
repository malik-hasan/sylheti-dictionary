package oats.mobile.sylhetidictionary.ui.utils

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import oats.mobile.sylhetidictionary.di.utils.injectLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.HttpURLConnection
import java.net.URI
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.LineEvent
import kotlin.coroutines.cancellation.CancellationException

actual class AudioPlayer : KoinComponent {

    private val logger by injectLogger()
    private val scope: CoroutineScope by inject()

    private val clipMutex = Mutex()
    private var playbackJob: Job? = null
    private var clip: Clip? = null

    actual fun play(url: String) {
        playbackJob?.cancel()
        playbackJob = scope.launch(Dispatchers.IO) {
            var audioInputStream: AudioInputStream? = null
            var decodedAudioInputStream: AudioInputStream? = null
            try {
                logger.d("AudioPlayer: loading $url")
                ensureActive()
                val connection = (URI(url).toURL().openConnection() as HttpURLConnection).apply {
                    setRequestProperty("User-Agent", "SylhetiDictionary.app")
                    connect()
                }

                ensureActive()
                audioInputStream = AudioSystem.getAudioInputStream(connection.inputStream)
                val decodedFormat = audioInputStream.format.run {
                    AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        sampleRate,
                        16,
                        channels,
                        channels * 2,
                        sampleRate,
                        false
                    )
                }

                ensureActive()
                decodedAudioInputStream = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream)

                val completion = CompletableDeferred<Unit>(coroutineContext.job)
                clipMutex.withLock {
                    stopClip()
                    clip = AudioSystem.getClip()
                    clip?.run {
                        addLineListener { event ->
                            if (event.type == LineEvent.Type.STOP) {
                                completion.complete(Unit)
                            }
                        }

                        ensureActive()
                        open(decodedAudioInputStream)

                        ensureActive()
                        logger.d("AudioPlayer: starting")
                        start()

                    }
                }

                completion.await()
                logger.d("AudioPlayer: finished")
            } catch (e: CancellationException) {
                logger.d("AudioPlayer: cancelled")
                throw e
            } catch (e: Exception) {
                logger.e("AudioPlayer error: ${e.message}")
            } finally {
                clipMutex.withLock { stopClip() }
                audioInputStream?.close()
                decodedAudioInputStream?.close()
            }
        }
    }


    actual fun release() {
        playbackJob?.cancel()
        playbackJob = null
    }

    private fun stopClip() {
        clip?.run {
            logger.d("AudioPlayer: stopping")
            stop()
            flush()
            close()
        }
        clip = null
    }
}
