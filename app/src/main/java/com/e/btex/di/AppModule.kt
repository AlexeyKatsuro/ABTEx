/*
 * Copyright 2018 LWO LLC
 */

package com.e.btex.di

import com.e.btex.data.MyEx
import com.e.btex.data.MyExImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(): MyEx{
        return MyExImpl()
    }

}
