package oats.mobile.sylhetidictionary

import android.app.Application
import di.initKoin
import org.koin.android.ext.koin.androidContext

class AndroidDictionaryApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AndroidDictionaryApp)
        }
    }
}
