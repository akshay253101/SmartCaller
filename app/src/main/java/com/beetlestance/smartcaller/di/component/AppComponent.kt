package com.beetlestance.smartcaller.di.component

import com.beetlestance.smartcaller.SmartCallerApplication
import com.beetlestance.smartcaller.di.modules.ActivityModule
import com.beetlestance.smartcaller.di.modules.ApplicationModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * [AppComponent] Component running on application scope
 *  AndroidInjectionModule provides injection into dagger base classes
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        ApplicationModule::class
    ]
)
interface AppComponent : AndroidInjector<SmartCallerApplication> {

    /**
     *  @builder inject modules on app initialization
     *  @see [SmartCallerApplication.applicationInjector]
     */
    @Component.Factory
    abstract class Builder : AndroidInjector.Factory<SmartCallerApplication>

}