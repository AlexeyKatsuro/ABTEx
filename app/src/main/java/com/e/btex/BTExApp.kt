package com.e.btex

import android.app.Application
import com.e.btex.di.AppComponent
import com.e.btex.di.AppModule
import com.e.btex.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class BTExApp : Application() {

    private lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        appComponent = createAppComponent()
    }

    private fun createAppComponent(): AppComponent {
        return DaggerAppComponent.builder().appModule(AppModule()).build()
    }

    fun getAppComponent() = appComponent
}