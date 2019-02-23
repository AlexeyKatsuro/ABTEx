package com.e.btex.di.modue

import android.content.Context
import androidx.room.Room
import com.e.btex.data.AppDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDataBase =
        Room.databaseBuilder(context, AppDataBase::class.java, AppDataBase::class.java.name).build()


}