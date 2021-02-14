package com.beetlestance.smartcaller.di.component

import com.beetlestance.smartcaller.SmartCallerApplication
import com.beetlestance.smartcaller.data.datasource.AppDatabaseModule
import com.beetlestance.smartcaller.data.datasource.DatabaseDaoModule
import com.beetlestance.smartcaller.data.repository.di.RepositoryModule
import com.beetlestance.smartcaller.di.modules.ActivityModule
import com.beetlestance.smartcaller.di.modules.ApplicationModule
import com.beetlestance.smartcaller.utils.receivers.BroadcastModule
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
        ApplicationModule::class,
        AppDatabaseModule::class,
        DatabaseDaoModule::class,
        RepositoryModule::class,
        BroadcastModule::class
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