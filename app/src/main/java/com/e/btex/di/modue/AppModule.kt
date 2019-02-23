/*
 * Copyright 2018 LWO LLC
 */

package com.e.btex.di.modue

import android.content.Context
import androidx.room.Room
import com.e.btex.base.BTExApp
import com.e.btex.data.AppDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: BTExApp): Context = application.applicationContext

}
