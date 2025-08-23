package com.walkmansit.realworld

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RealWorldApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
