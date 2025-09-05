// IntermitApp.kt
package com.samoggino.intermit

import android.app.Application
import com.samoggino.intermit.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class IntermitApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@IntermitApp)
            modules(listOf(databaseModule, appModule))
        }
    }
}
