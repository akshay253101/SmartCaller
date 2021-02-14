package com.beetlestance.smartcaller.ui.dashboard

import com.beetlestance.smartcaller.di.scopes.FragmentScoped
import com.beetlestance.smartcaller.ui.blocked.BlockedFragmentModule
import com.beetlestance.smartcaller.ui.contacts.ContactsFragmentModule
import com.beetlestance.smartcaller.ui.logs.CallLogsFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DashboardFragmentModule {

    @FragmentScoped
    @ContributesAndroidInjector(
        modules = [
            BlockedFragmentModule::class,
            CallLogsFragmentModule::class,
            ContactsFragmentModule::class
        ]
    )
    abstract fun contributesDashboardFragment(): DashboardFragment

}