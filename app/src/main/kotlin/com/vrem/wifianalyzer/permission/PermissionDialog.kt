/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.vrem.wifianalyzer.permission

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.vrem.annotation.OpenClass
import com.vrem.util.buildMinVersionP
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.R

@OpenClass
class PermissionDialog(
    private val mainActivity: MainActivity,
) : DefaultLifecycleObserver {
    internal var alertDialog: AlertDialog? = null

    fun show(): View? {
        val view = mainActivity.layoutInflater.inflate(R.layout.info_permission, null)
        val visibility = if (buildMinVersionP()) View.VISIBLE else View.GONE
        view.findViewById<View>(R.id.throttling)!!.visibility = visibility
        alertDialog = buildAlertDialog(view)
        mainActivity.lifecycle.addObserver(this)
        alertDialog?.show()
        return view
    }

    override fun onStop(owner: LifecycleOwner) {
        alertDialog?.dismiss()
        alertDialog = null
    }

    private fun buildAlertDialog(view: View): AlertDialog =
        AlertDialog
            .Builder(mainActivity)
            .setView(view)
            .setTitle(R.string.app_full_name)
            .setIcon(R.drawable.ic_app)
            .setPositiveButton(android.R.string.ok, OkClick(mainActivity.permissionLauncher))
            .setNegativeButton(android.R.string.cancel, CancelClick(mainActivity))
            .create()

    internal class OkClick(
        private val permissionLauncher: ActivityResultLauncher<String>,
    ) : DialogInterface.OnClickListener {
        override fun onClick(
            alertDialog: DialogInterface,
            which: Int,
        ) {
            alertDialog.dismiss()
            permissionLauncher.launch(ApplicationPermission.PERMISSION)
        }
    }

    internal class CancelClick(
        private val activity: Activity,
    ) : DialogInterface.OnClickListener {
        override fun onClick(
            alertDialog: DialogInterface,
            which: Int,
        ) {
            alertDialog.dismiss()
            activity.finish()
        }
    }
}
