package com.walkmansit.realworld

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RealWorldApplication : Application() {

    override fun onCreate() {
        Log.d("RealWorldApplication","onCreate")
        super.onCreate()
    }
}