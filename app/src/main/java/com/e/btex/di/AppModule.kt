/*
 * Copyright 2018 LWO LLC
 */

package com.e.btex.di

import android.content.Context
import com.e.btex.base.BTExApp
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideContext(application: BTExApp ): Context = application.applicationContext
}
