package oats.mobile.sylhetidictionary.ui.utils

expect class AudioPlayer() {
    fun play(url: String)
    fun release()
}

fun AudioPlayer.playPhone(path: String) {
    play("https://upload.wikimedia.org/wikipedia/commons/${path.first()}/$path.ogg")
}
