package com.beetlestance.smartcaller.utils.extensions

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.beetlestance.smartcaller.R
import com.beetlestance.smartcaller.utils.isAtLeastVersion
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.hasPermissions(permission: String): Boolean {
    return if (isAtLeastVersion(Build.VERSION_CODES.M)) {
        checkSelfPermission(permission)
    } else {
        ContextCompat.checkSelfPermission(this, permission)
    } == PackageManager.PERMISSION_GRANTED
}

fun FragmentActivity.requestRequiredPermissions(
    permissions: List<String>,
    requestCode: Int
): Boolean {
    val missingPermissions: MutableList<String> = mutableListOf()
    for (permission in permissions) {
        if (hasPermissions(permission).not()) missingPermissions.add(permission)
    }
    if (missingPermissions.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            this,
            missingPermissions.toTypedArray(),
            requestCode
        )
    }
    return missingPermissions.isEmpty()
}

fun Context.showPermissionDeniedDialog(
    @StringRes description: Int,
    onPositiveResponse: (Intent) -> Unit,
    onNegativeResponse: () -> Unit
) {
    MaterialAlertDialogBuilder(this)
        .setMessage(description)
        .setPositiveButton(R.string.general_key_okay) { dialog, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            onPositiveResponse(intent)
            dialog.dismiss()
        }
        .setNegativeButton(R.string.general_key_cancel) { dialog, _ ->
            onNegativeResponse()
            dialog.dismiss()
        }
        .show()
}

fun Context.createMaterialAlertDialog(
    @StringRes title: Int = R.string.empty_string,
    @StringRes message: Int = R.string.empty_string,
    @StringRes negativeActionMsg: Int = R.string.empty_string,
    @StringRes positiveActionMsg: Int = R.string.empty_string,
    positiveAction: () -> Unit = {},
    negativeAction: () -> Unit = {},
    @LayoutRes layoutId: Int? = null,
    @DrawableRes iconId: Int? = null,
    cancelable: Boolean = true,
    viewBinding: ViewDataBinding? = null,
): AlertDialog {
    val dialogBuilder = MaterialAlertDialogBuilder(this).setCancelable(cancelable)

    if (title != R.string.empty_string) dialogBuilder.setTitle(title)

    if (message != R.string.empty_string) dialogBuilder.setMessage(message)

    if (negativeActionMsg != R.string.empty_string) {
        dialogBuilder.setNegativeButton(negativeActionMsg) { dialog, _ ->
            negativeAction()
            dialog.cancel()
        }
    }

    if (positiveActionMsg != R.string.empty_string) {
        dialogBuilder.setPositiveButton(positiveActionMsg) { dialog, _ ->
            positiveAction()
            dialog.cancel()
        }
    }

    if (layoutId != null) dialogBuilder.setView(layoutId)
    else if (viewBinding != null) dialogBuilder.setView(viewBinding.root)

    if (iconId != null) dialogBuilder.setIcon(iconId)

    return dialogBuilder.create()
}

const val TESTING_CHANNEL_NAME: String = "Testing Purpose"
const val TESTING_CHANNEL_ID: String = "testing"
const val TESTING_CHANNEL_DESC: String = "For testing purpose only"

fun Context.testNotification(number: String) {
    // Build the notification and add the action.

    val newMessageNotification = NotificationCompat.Builder(this, TESTING_CHANNEL_NAME)
        .setChannelId(TESTING_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("A call has been blocked")
        .setContentText(number)
        .build()

    // Issue the notification.
    with(NotificationManagerCompat.from(this)) {
        notify(TESTING_CHANNEL_ID.hashCode(), newMessageNotification)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    val channel = NotificationChannel(TESTING_CHANNEL_ID, TESTING_CHANNEL_NAME, IMPORTANCE_DEFAULT)
    channel.description = TESTING_CHANNEL_DESC
    channel.setShowBadge(true)
    channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
    val notificationManager = NotificationManagerCompat.from(this)
    notificationManager.createNotificationChannel(channel)
}