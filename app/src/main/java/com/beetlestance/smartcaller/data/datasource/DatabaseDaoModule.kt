package com.beetlestance.smartcaller.data.datasource

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseDaoModule {

    @Provides
    @Singleton
    fun provideContactsDao(db: AppDatabase) = db.contactsDao()

}