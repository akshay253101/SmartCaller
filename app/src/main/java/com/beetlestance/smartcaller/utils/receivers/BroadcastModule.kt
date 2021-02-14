package com.beetlestance.smartcaller.utils.receivers

import android.content.BroadcastReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
abstract class BroadcastModule {

    @ContributesAndroidInjector
    abstract fun contributesBroadCastReceiver(): PhoneStateReceivers
}