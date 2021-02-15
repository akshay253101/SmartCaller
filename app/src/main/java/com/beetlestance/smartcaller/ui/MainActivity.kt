package com.beetlestance.smartcaller.ui

import android.content.pm.PackageManager
import android.os.Bundle
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.constants.SmartCallerRequiredPermissions
import com.beetlestance.smartcaller.databinding.ActivityMainBinding
import com.beetlestance.smartcaller.di.AppCoroutineDispatchers
import com.beetlestance.smartcaller.utils.extensions.requestRequiredPermissions
import com.beetlestance.smartcaller.utils.extensions.showPermissionDeniedDialog
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var dispatchers: AppCoroutineDispatchers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainBinding.inflate(layoutInflater)
        checkPermission()
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
                startActivityForResult(settingsIntent,1045)
            },
            onNegativeResponse = {
                finish()
            }
        )
    }

}