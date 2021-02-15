package com.beetlestance.smartcaller.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.constants.SmartCallerRequiredPermissions
import com.beetlestance.smartcaller.databinding.ActivityMainBinding
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.di.viewmodelfactory.ViewModelFactory
import com.beetlestance.smartcaller.utils.callmanager.CallStateManager
import com.beetlestance.smartcaller.utils.extensions.requestRequiredPermissions
import com.beetlestance.smartcaller.utils.extensions.showPermissionDeniedDialog
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var callStateManager: CallStateManager

    @Inject
    lateinit var dispatchers: AppCoroutineDispatchers

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater)
        checkPermission()
        addObserver()
    }

    private fun addObserver() {
        lifecycleScope.launch(dispatchers.main) {
            callStateManager.callState.collect { state ->
                Toast.makeText(this@MainActivity, state.name, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermission() {
        requestRequiredPermissions(
            permissions = SmartCallerRequiredPermissions.permissions,
            requestCode = SmartCallerRequiredPermissions.REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != SmartCallerRequiredPermissions.REQUEST_CODE) return

        permissions@ for (index in permissions.indices) {
            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {
                handlePermissionDenial()
                break@permissions
            }
        }
    }

    private fun handlePermissionDenial() {
        showPermissionDeniedDialog(description = R.string.permission_denied,
            onPositiveResponse = { settingsIntent ->
                startActivityForResult.launch(settingsIntent)
            },
            onNegativeResponse = {
                finish()
            }
        )
    }

    private val startActivityForResult: ActivityResultLauncher<Intent> by lazy {
        registerForActivityResult(StartActivityForResult()) {}
    }

}