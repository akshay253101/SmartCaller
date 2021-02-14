package com.beetlestance.smartcaller.ui.logs

import androidx.lifecycle.ViewModel
import com.beetlestance.smartcaller.di.scopes.ChildFragmentScoped
import com.beetlestance.smartcaller.di.viewmodelfactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CallLogsFragmentModule {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributesCallLogsFragment(): CallLogsFragment

    @Binds
    @IntoMap
    @ViewModelKey(CallLogsViewModel::class)
    abstract fun bindsCallLogsViewModel(viewModel: CallLogsViewModel): ViewModel

}