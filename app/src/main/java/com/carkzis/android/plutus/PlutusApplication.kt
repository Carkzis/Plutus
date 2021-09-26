package com.carkzis.android.plutus

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application class to set up Hilt and Timber.
 */
@HiltAndroidApp
class PlutusApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}