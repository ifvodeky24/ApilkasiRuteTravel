package com.example.aplikasirutetravel

import android.app.Application
import com.example.aplikasirutetravel.BuildConfig
import com.mapbox.mapboxsdk.Mapbox
import timber.log.Timber

open class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}