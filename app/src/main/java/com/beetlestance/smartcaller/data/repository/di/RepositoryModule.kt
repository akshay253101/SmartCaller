package com.beetlestance.smartcaller.data.repository.di

import com.beetlestance.smartcaller.data.repository.ContactsRepository
import com.beetlestance.smartcaller.data.repository.ContactsRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    /**
     *  @provides [ContactsRepository]
     */
    @Provides
    @Singleton
    fun provideContactsRepository(
        contactsRepoImpl: ContactsRepositoryImpl
    ): ContactsRepository = contactsRepoImpl
}
