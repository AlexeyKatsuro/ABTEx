/*
 * Copyright 2018 LWO LLC
 */

package com.e.btex.di

import com.e.btex.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}
