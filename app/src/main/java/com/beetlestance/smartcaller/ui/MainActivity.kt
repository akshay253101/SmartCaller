package com.beetlestance.smartcaller.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.utils.PhoneStateReceiver
import com.beetlestance.smartcaller.utils.hasPermissions
import com.beetlestance.smartcaller.utils.shouldShowPermissionRationale
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var phoneStateReceiver: PhoneStateReceiver

    private val requestReadLogPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted.not()) checkPermission()
        }

    private val startActivityForResult =
        registerForActivityResult(StartActivityForResult()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addObserver()
        checkPermission()
    }

    private fun addObserver() {
        phoneStateReceiver.callState.observe(this) {
            Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        when {
            hasPermissions(READ_CALL_LOGS_PERMISSION) -> Unit
            shouldShowPermissionRationale(READ_CALL_LOGS_PERMISSION) -> {
                showReadLogsPermissionDeniedDialog()
            }
            else -> requestReadLogPermissionLauncher.launch(READ_CALL_LOGS_PERMISSION)

        }
    }

    private fun showReadLogsPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(this)
            .setMessage(getString(R.string.permission_denied_for_read_logs))
            .setPositiveButton(getString(R.string.general_key_okay)) { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult.launch(intent)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.general_key_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    companion object {
        const val READ_CALL_LOGS_PERMISSION = Manifest.permission.READ_CALL_LOG
    }

}