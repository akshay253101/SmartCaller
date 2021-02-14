package com.beetlestance.smartcaller.ui.blocked

import androidx.lifecycle.ViewModel
import com.beetlestance.smartcaller.di.scopes.ChildFragmentScoped
import com.beetlestance.smartcaller.di.viewmodelfactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class BlockedFragmentModule {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributesBlockedFragment(): BlockedFragment

    @Binds
    @IntoMap
    @ViewModelKey(BlockedViewModel::class)
    abstract fun bindsBlockedViewModel(viewModel: BlockedViewModel): ViewModel

}