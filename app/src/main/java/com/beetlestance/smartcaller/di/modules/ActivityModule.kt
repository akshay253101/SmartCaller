package com.beetlestance.smartcaller.di.modules

import com.beetlestance.smartcaller.di.scopes.ActivityScoped
import com.beetlestance.smartcaller.ui.MainActivity
import com.beetlestance.smartcaller.ui.dashboard.DashboardFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [DashboardFragmentModule::class]
    )
    internal abstract fun contributesMainActivity(): MainActivity
}