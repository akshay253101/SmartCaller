package com.beetlestance.smartcaller.ui.logs

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.databinding.FragmentCallLogsBinding
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.di.viewmodelfactory.ViewModelFactory
import com.beetlestance.smartcaller.ui.base.SmartCallerFragment
import com.beetlestance.smartcaller.ui.contacts.ContactsFragment
import com.beetlestance.smartcaller.ui.logs.adapter.CallLogsAdapter
import com.beetlestance.smartcaller.utils.callmanager.CallStateManager
import com.beetlestance.smartcaller.utils.callmanager.CallStateManager.CallState
import com.beetlestance.smartcaller.utils.extensions.hasPermissions
import com.beetlestance.smartcaller.utils.extensions.showPermissionDeniedDialog
import com.beetlestance.smartcaller.utils.showSoftInput
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CallLogsFragment :
    SmartCallerFragment<FragmentCallLogsBinding>(R.layout.fragment_call_logs) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dispatchers: AppCoroutineDispatchers

    @Inject
    lateinit var callStateManager: CallStateManager

    private val viewModel: CallLogsViewModel by viewModels { viewModelFactory }

    private var callLogsAdapter: CallLogsAdapter? = null

    override fun onViewCreated(binding: FragmentCallLogsBinding, savedInstanceState: Bundle?) {
        checkPermission()
        initRecyclerView()
        addObserver()
        setViewListeners()
    }

    private fun initRecyclerView() {
        callLogsAdapter = CallLogsAdapter(viewModel)
        requireBinding().fragmentCallLogsRecyclerView.adapter = callLogsAdapter
    }

    private fun addObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.callLogPagedData.collectLatest {
                withContext(dispatchers.io) {
                    callLogsAdapter?.submitData(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(dispatchers.main) {
            callStateManager.callState.collect { state ->
                if (state != CallState.IDLE) callLogsAdapter?.refresh()
            }
        }
    }

    private fun setViewListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (requireBinding().rootFragmentCallLogs.progress != 0f) {
                requireBinding().rootFragmentCallLogs.transitionToStart()
            } else {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }

        requireBinding().fragmentCallLogsEditLayout.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.executeQuery(text.toString())
        }

        requireBinding().fragmentCallLogsOpenSearchView.setOnClickListener {
            requireBinding().fragmentCallLogsEditLayout.requestFocus()
            requireActivity().showSoftInput(requireBinding().fragmentCallLogsEditText)
            requireBinding().rootFragmentCallLogs.transitionToEnd()
        }
    }


    private fun checkPermission() {
        when {
            requireContext().hasPermissions(READ_CALL_LOGS_PERMISSION) -> Unit
            shouldShowRequestPermissionRationale(READ_CALL_LOGS_PERMISSION) -> {
                requireContext().showPermissionDeniedDialog(
                    description = R.string.permission_denied,
                    onPositiveResponse = { settingsIntent ->
                        startActivityForResult(settingsIntent, 1239)
                    },
                    onNegativeResponse = {}
                )
            }
            else -> requestPermissions(arrayOf(READ_CALL_LOGS_PERMISSION), 1045)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1047 && permissions[0] == ContactsFragment.READ_CONTACTS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) viewModel.observeCollLogs()
        }
    }

    override fun onDestroyView() {
        callLogsAdapter = null
        super.onDestroyView()
    }

    companion object {
        const val READ_CALL_LOGS_PERMISSION = Manifest.permission.READ_CALL_LOG
    }
}