package com.walkmansit.realworld

import android.app.Application
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RealWorldApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    fun enableStrickMode(){
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()       // Log violations to Logcat
                    //.penaltyDialog()   // Show dialog on violations
                    //.penaltyDeath()    // Crash the app on violation (for testing only!)
                    .build()
            )

            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
    }
}
