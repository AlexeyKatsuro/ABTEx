package com.e.btex.ui.plot

import androidx.lifecycle.ViewModel
import com.e.btex.di.ViewModelKey
import com.e.btex.di.scope.FragmentScoped
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class PlotBuilder{

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributePlotFragment(): PlotFragment

    @Binds
    @IntoMap
    @ViewModelKey(PlotViewModel::class)
    internal abstract fun bindPlotViewModel(plotViewModel: PlotViewModel): ViewModel
}