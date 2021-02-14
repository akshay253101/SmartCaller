package com.beetlestance.smartcaller.data.datasource

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseDaoModule {

    @Provides
    @Singleton
    fun provideBlockedContactsDao(db: AppDatabase) = db.blockedContactsDao()

}