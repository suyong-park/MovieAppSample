package com.example.samplenaverlogin

import android.app.Application
import com.example.samplenaverlogin.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MovieApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieApp)
            androidLogger()
            modules(networkModule)
        }
    }
}