package com.beetlestance.smartcaller.ui.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.databinding.FragmentContactsBinding
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.di.viewmodelfactory.ViewModelFactory
import com.beetlestance.smartcaller.ui.base.SmartCallerFragment
import com.beetlestance.smartcaller.ui.contacts.adapter.ContactsAdapter
import com.beetlestance.smartcaller.utils.extensions.hasPermissions
import com.beetlestance.smartcaller.utils.extensions.showPermissionDeniedDialog
import com.beetlestance.smartcaller.utils.showSoftInput
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsFragment :
    SmartCallerFragment<FragmentContactsBinding>(R.layout.fragment_contacts) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dispatchers: AppCoroutineDispatchers

    private val viewModel: ContactsViewModel by viewModels { viewModelFactory }

    private var contactsAdapter: ContactsAdapter? = null

    override fun onViewCreated(binding: FragmentContactsBinding, savedInstanceState: Bundle?) {
        checkPermission()
        initRecyclerView()
        addObserver()
        setViewListeners()
    }

    private fun initRecyclerView() {
        contactsAdapter = ContactsAdapter(viewModel)
        requireBinding().fragmentContactsRecyclerView.adapter = contactsAdapter
    }

    private fun addObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.contactsPagedData.collectLatest {
                withContext(dispatchers.io) {
                    contactsAdapter?.submitData(it)
                }
            }
        }
    }

    private fun checkPermission() {
        when {
            requireContext().hasPermissions(READ_CONTACTS_PERMISSION) -> Unit
            shouldShowRequestPermissionRationale(READ_CONTACTS_PERMISSION) -> {
                requireContext().showPermissionDeniedDialog(
                    description = R.string.permission_denied,
                    onPositiveResponse = { settingsIntent ->
                        startActivityForResult(settingsIntent, 1056)
                    },
                    onNegativeResponse = {}
                )
            }
            else -> requestPermissions(arrayOf(READ_CONTACTS_PERMISSION), 1045)
        }
    }

    private fun setViewListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (requireBinding().rootFragmentContacts.progress != 0f) {
                requireBinding().rootFragmentContacts.transitionToStart()
            } else {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }

        requireBinding().fragmentContactsEditLayout.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.executeQuery(text.toString())
        }

        requireBinding().fragmentContactsOpenSearchView.setOnClickListener {
            requireBinding().fragmentContactsEditLayout.requestFocus()
            requireActivity().showSoftInput(requireBinding().fragmentContactsEditText)
            requireBinding().rootFragmentContacts.transitionToEnd()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1045 && permissions[0] == READ_CONTACTS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) viewModel.observeContacts()
        }
    }

    override fun onDestroyView() {
        contactsAdapter = null
        super.onDestroyView()
    }

    companion object {
        const val READ_CONTACTS_PERMISSION = Manifest.permission.READ_CONTACTS
    }
}