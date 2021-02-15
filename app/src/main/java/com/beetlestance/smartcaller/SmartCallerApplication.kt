package com.beetlestance.smartcaller

import android.os.Build
import com.beetlestance.smartcaller.di.component.DaggerAppComponent
import com.beetlestance.smartcaller.utils.extensions.createNotificationChannel
import com.beetlestance.smartcaller.utils.isAtLeastVersion
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class SmartCallerApplication : DaggerApplication() {

    /**
     *  Injects dagger modules in applications core component
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        if (isAtLeastVersion(Build.VERSION_CODES.O)) createNotificationChannel()
    }
}