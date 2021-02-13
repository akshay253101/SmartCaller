package com.beetlestance.smartcaller

import com.beetlestance.smartcaller.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class SmartCallerApplication : DaggerApplication() {

    /**
     *  Injects dagger modules in applications core component
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}