package com.beetlestance.smartcaller.di.viewmodelfactory

import androidx.lifecycle.ViewModelProvider
import com.beetlestance.smartcaller.di.viewmodelfactory.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 *
 * Connection between our ViewModelFactory and android ViewModelFactory
 */
@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}