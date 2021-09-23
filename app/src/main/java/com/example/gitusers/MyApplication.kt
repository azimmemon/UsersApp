package com.example.gitusers

import android.app.Application
import com.example.gitusers.koin.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startModule()
    }

    private fun startModule() {
        startKoin {
            androidContext(this@MyApplication)
            modules(Modules.getAll())
        }
    }

}