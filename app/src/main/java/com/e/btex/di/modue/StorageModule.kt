package com.e.btex.di.modue

import android.content.Context
import androidx.room.Room
import com.e.btex.data.AppDataBase
import com.e.btex.data.preferences.PreferenceStorage
import com.e.btex.data.preferences.SharedPreferenceStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDataBase =
        Room.databaseBuilder(context, AppDataBase::class.java, AppDataBase::class.java.name).build()

    @Provides
    fun provideSensorDao(dataBase: AppDataBase) = dataBase.sensorsDao()

    @Singleton
    @Provides
    fun providePreferences(context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)
}