package com.beetlestance.smartcaller.ui.contacts

import androidx.lifecycle.ViewModel
import com.beetlestance.smartcaller.di.scopes.ChildFragmentScoped
import com.beetlestance.smartcaller.di.viewmodelfactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ContactsFragmentModule {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    abstract fun contributesContactsFragment(): ContactsFragment

    @Binds
    @IntoMap
    @ViewModelKey(ContactsViewModel::class)
    abstract fun bindsContactsViewModel(viewModel: ContactsViewModel): ViewModel

}