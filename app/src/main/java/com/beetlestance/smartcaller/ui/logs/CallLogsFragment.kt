package com.beetlestance.smartcaller.ui.logs

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.databinding.FragmentCallLogsBinding
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.di.viewmodelfactory.ViewModelFactory
import com.beetlestance.smartcaller.ui.base.SmartCallerFragment
import com.beetlestance.smartcaller.ui.logs.adapter.CallLogsAdapter
import com.beetlestance.smartcaller.utils.extensions.hasPermissions
import com.beetlestance.smartcaller.utils.extensions.showPermissionDeniedDialog
import com.beetlestance.smartcaller.utils.showSoftInput
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CallLogsFragment :
    SmartCallerFragment<FragmentCallLogsBinding>(R.layout.fragment_call_logs) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dispatchers: AppCoroutineDispatchers

    private val viewModel: CallLogsViewModel by viewModels { viewModelFactory }

    private var callLogsAdapter: CallLogsAdapter? = null

    private val requestReadLogPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted.not()) checkPermission()
        }

    private val startActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

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
        viewLifecycleOwner.lifecycleScope.launch(dispatchers.io) {
            viewModel.callLogPagedData.collect {
                callLogsAdapter?.submitData(it)
            }
        }
    }

    private fun checkPermission() {
        when {
            requireContext().hasPermissions(READ_CALL_LOGS_PERMISSION) -> Unit
            shouldShowRequestPermissionRationale(READ_CALL_LOGS_PERMISSION) -> {
                requireContext().showPermissionDeniedDialog(
                    description = R.string.permission_denied,
                    onPositiveResponse = { settingsIntent ->
                        startActivityForResult.launch(settingsIntent)
                    },
                    onNegativeResponse = {}
                )
            }
            else -> requestReadLogPermissionLauncher.launch(READ_CALL_LOGS_PERMISSION)
        }
    }

    private fun setViewListeners() {
        requireBinding().fragmentCallLogsOpenSearchView.setOnClickListener {
            requireBinding().fragmentCallLogsEditLayout.requestFocus()
            requireActivity().showSoftInput(requireBinding().fragmentCallLogsEditText)
            requireBinding().rootFragmentCallLogs.transitionToEnd()
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