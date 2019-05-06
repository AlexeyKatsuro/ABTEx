package com.e.btex.connection

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ServiceBuilder {

    @ContributesAndroidInjector
    internal abstract fun contributeBleService(): AqsService
}