package com.beetlestance.smartcaller.ui.contacts

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.databinding.FragmentContactsBinding
import com.beetlestance.smartcaller.di.viewmodelfactory.ViewModelFactory
import com.beetlestance.smartcaller.ui.base.SmartCallerFragment
import com.beetlestance.smartcaller.ui.contacts.adapter.ContactsAdapter
import com.beetlestance.smartcaller.utils.extensions.hasPermissions
import com.beetlestance.smartcaller.utils.extensions.showPermissionDeniedDialog
import com.beetlestance.smartcaller.utils.showSoftInput
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ContactsFragment :
    SmartCallerFragment<FragmentContactsBinding>(R.layout.fragment_contacts) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ContactsViewModel by viewModels { viewModelFactory }

    private var contactsAdapter: ContactsAdapter? = null

    private val requestReadLogPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted.not()) checkPermission()
        }

    private val startActivityForResult = registerForActivityResult(StartActivityForResult()) {}

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
        lifecycleScope.launchWhenResumed {
            viewModel.contactsPagedData.collect {
                contactsAdapter?.submitData(it)
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
                        startActivityForResult.launch(settingsIntent)
                    },
                    onNegativeResponse = {}
                )
            }
            else -> requestReadLogPermissionLauncher.launch(READ_CONTACTS_PERMISSION)
        }
    }

    private fun setViewListeners() {
        requireBinding().fragmentContactsOpenSearchView.setOnClickListener {
            requireBinding().fragmentContactsEditLayout.requestFocus()
            requireActivity().showSoftInput(requireBinding().fragmentContactsEditText)
            requireBinding().rootFragmentContacts.transitionToEnd()
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