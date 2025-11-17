package oats.mobile.sylhetidictionary

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle

class ProcessTextActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && intent.action == Intent.ACTION_PROCESS_TEXT) {
            intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
                ?.takeIf { it.isNotBlank() }
                ?.let { processText ->
                    Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra(MainActivity.EXTRA_PROCESS_TEXT_SEARCH_TERM, processText)
                    }.let(::startActivity)
                }
        }

        finish()
    }
}
