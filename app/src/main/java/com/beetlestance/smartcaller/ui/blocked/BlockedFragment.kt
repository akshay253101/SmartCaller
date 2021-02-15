package com.beetlestance.smartcaller.ui.blocked

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.databinding.DialogAddNewContactBinding
import com.beetlestance.smartcaller.databinding.FragmentBlockedBinding
import com.beetlestance.smartcaller.di.viewmodelfactory.ViewModelFactory
import com.beetlestance.smartcaller.ui.base.SmartCallerFragment
import com.beetlestance.smartcaller.ui.blocked.adapter.BlockedContactsAdapter
import com.beetlestance.smartcaller.utils.extensions.createMaterialAlertDialog
import com.beetlestance.smartcaller.utils.isValidPhoneNumber
import com.beetlestance.smartcaller.utils.showSoftInput
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class BlockedFragment :
    SmartCallerFragment<FragmentBlockedBinding>(R.layout.fragment_blocked) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: BlockedViewModel by viewModels { viewModelFactory }

    private var blockedContactsAdapter: BlockedContactsAdapter? = null

    private var addNewContactDialog: AlertDialog? = null

    override fun onViewCreated(binding: FragmentBlockedBinding, savedInstanceState: Bundle?) {
        initRecyclerView()
        addObserver()
        setViewListeners()
    }

    private fun initRecyclerView() {
        blockedContactsAdapter = BlockedContactsAdapter(viewModel)
        requireBinding().fragmentBlockedContactsRecyclerView.adapter = blockedContactsAdapter
    }

    private fun addObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.blockedContacts.collect {
                blockedContactsAdapter?.submitList(it)
            }
        }
    }

    private fun setViewListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (requireBinding().rootFragmentBlockedContacts.progress != 0f) {
                requireBinding().rootFragmentBlockedContacts.transitionToStart()
            } else {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }

        requireBinding().fragmentBlockedContactsOpenSearchView.setOnClickListener {
            requireBinding().fragmentBlockedContactsEditLayout.requestFocus()
            requireActivity().showSoftInput(requireBinding().fragmentBlockedContactsEditText)
            requireBinding().rootFragmentBlockedContacts.transitionToEnd()
        }

        requireBinding().fragmentBlockedContactsEditLayout.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.executeQuery(text.toString())
        }

        requireBinding().fragmentBlockedContactsAddNewNumber.setOnClickListener {
            addNewContact()
        }
    }

    private fun addNewContact() {
        if (addNewContactDialog?.isShowing == true) return
        val view = View.inflate(context, R.layout.dialog_add_new_contact, null)
        val viewBinding = DialogAddNewContactBinding.bind(view)
        addNewContactDialog = requireContext().createMaterialAlertDialog(
            title = R.string.add_contacts_title,
            negativeActionMsg = R.string.general_key_cancel,
            positiveActionMsg = R.string.general_key_add,
            positiveAction = {
                val name = viewBinding.dialogAddNewContactEditTextName.text.toString()
                val number = viewBinding.dialogAddNewContactEditTextNumber.text.toString()
                if (number.isValidPhoneNumber()) {
                    viewModel.addToBlockList(name, number)
                    addNewContactDialog?.dismiss()
                }
            },
            negativeAction = {
                addNewContactDialog?.dismiss()
            },
            viewBinding = viewBinding
        )

        viewBinding.dialogAddNewContactEditLayoutNumber.editText?.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isValidPhoneNumber()) {
                viewBinding.dialogAddNewContactEditLayoutNumber.error = null
                viewBinding.dialogAddNewContactEditLayoutNumber.isErrorEnabled = false
            } else {
                viewBinding.dialogAddNewContactEditLayoutNumber.error = "Not a valid mobile number"
            }
        }

        addNewContactDialog?.create()
        addNewContactDialog?.show()


    }

    override fun onDestroyView() {
        blockedContactsAdapter = null
        super.onDestroyView()
    }
}