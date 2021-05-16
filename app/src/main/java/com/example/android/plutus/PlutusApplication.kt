package com.example.android.plutus

import android.app.Application
import timber.log.Timber

class PlutusApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}