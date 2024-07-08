package oats.mobile.sylhetidictionary

import android.app.Application
import di.KoinInitializer

class AndroidDictionaryApp: Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInitializer(this).init()
    }
}
