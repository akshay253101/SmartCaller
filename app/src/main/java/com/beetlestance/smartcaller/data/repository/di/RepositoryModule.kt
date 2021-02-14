package com.beetlestance.smartcaller.data.repository.di

import com.beetlestance.smartcaller.data.repository.ContactsRepo
import com.beetlestance.smartcaller.data.repository.ContactsRepoImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    /**
     *  @provides [ContactsRepo]
     */
    @Provides
    @Singleton
    fun provideContactsRepository(
        contactsRepoImpl: ContactsRepoImpl
    ): ContactsRepo = contactsRepoImpl
}
