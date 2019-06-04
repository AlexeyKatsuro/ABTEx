package com.e.btex.ui.datebase

import androidx.lifecycle.ViewModel
import com.e.btex.di.ViewModelKey
import com.e.btex.di.scope.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class DatabaseInfoBuilder{

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeDatabaseInfoFragment(): DatabaseInfoFragment

    @Binds
    @IntoMap
    @ViewModelKey(DatabaseInfoViewModel::class)
    internal abstract fun bindDatabaseInfoViewModel(databaseInfoViewModel: DatabaseInfoViewModel): ViewModel
}