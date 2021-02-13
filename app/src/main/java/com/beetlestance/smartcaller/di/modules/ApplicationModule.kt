package com.beetlestance.smartcaller.di.modules

import android.content.Context
import com.beetlestance.smartcaller.SmartCallerApplication
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.di.ApplicationContext
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

/**
 * [ApplicationModule] provides app level static objects
 */
@Module
class ApplicationModule {

    /**
     *  @provides application context
     *
     *  @use @ApplicationContext val context : Context
     *
     */
    @Provides
    @Singleton
    @ApplicationContext
    fun provideApplicationContext(smartCallerApplication: SmartCallerApplication): Context =
        smartCallerApplication.applicationContext

    @Singleton
    @Provides
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )
}