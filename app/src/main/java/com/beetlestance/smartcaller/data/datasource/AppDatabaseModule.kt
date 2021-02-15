package com.beetlestance.smartcaller.data.datasource

import android.content.Context
import androidx.room.Room
import com.beetlestance.smartcaller.di.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "smart_caller.db"
    ).allowMainThreadQueries().build()

}