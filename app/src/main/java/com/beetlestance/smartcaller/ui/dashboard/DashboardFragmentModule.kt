package com.beetlestance.smartcaller.ui.dashboard

import com.beetlestance.smartcaller.di.scopes.FragmentScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DashboardFragmentModule {


    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun contributesDashboardFragment(): DashboardFragment

}