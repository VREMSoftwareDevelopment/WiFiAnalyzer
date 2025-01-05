/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.accesspoint

import android.app.AlertDialog
import android.view.View
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.wifi.model.WiFiDetail

@OpenClass
class AccessPointPopup {
    fun show(view: View): AlertDialog {
        val alertDialog: AlertDialog = AlertDialog.Builder(view.context)
            .setView(view)
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.cancel(); }
            .create()
        alertDialog.show()
        return alertDialog
    }

    fun attach(view: View, wiFiDetail: WiFiDetail) {
        view.setOnClickListener {
            try {
                show(AccessPointDetail().makeViewDetailed(wiFiDetail))
            } catch (e: Exception) {
                // do nothing
            }
        }
    }

}