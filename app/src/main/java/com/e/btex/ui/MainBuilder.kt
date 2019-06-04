package com.e.btex.ui

import com.e.btex.di.ViewModelBuilder
import com.e.btex.di.scope.ActivityScoped
import com.e.btex.ui.bluetooth.SettingsBuilder
import com.e.btex.ui.datebase.DatabaseInfoBuilder
import com.e.btex.ui.plot.PlotBuilder
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainBuilder {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class,
            PlotBuilder::class,
            SettingsBuilder::class,
            DatabaseInfoBuilder::class
        ]
    )
    abstract fun mainActivityInjector(): MainActivity
}