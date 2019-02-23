/*
 * Copyright 2018 LWO LLC
 */

package com.e.btex.di

import com.e.btex.base.BTExApp
import com.e.btex.di.modue.AppModule
import com.e.btex.di.modue.StorageModule
import com.e.btex.ui.MainBuilder
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        StorageModule::class,
        MainBuilder::class
    ]
)
interface AppComponent : AndroidInjector<BTExApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<BTExApp>()
}

