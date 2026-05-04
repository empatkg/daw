package com.daw

import android.app.Application
import android.content.Context

class DawApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    companion object {
        lateinit var instance: DawApplication
            private set
    }
}