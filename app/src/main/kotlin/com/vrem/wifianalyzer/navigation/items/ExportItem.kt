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
package com.vrem.wifianalyzer.navigation.items

import android.content.Intent
import android.widget.Toast
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.export.Export
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.model.WiFiDetail

internal class ExportItem(
    private val export: Export,
) : NavigationItem {
    override fun activate(
        mainActivity: MainActivity,
        navigationMenu: NavigationMenu,
    ) {
        val wiFiDetails: List<WiFiDetail> =
            MainContext.INSTANCE.scannerService
                .wiFiData()
                .wiFiDetails
        if (wiFiDetails.isEmpty()) {
            Toast.makeText(mainActivity, R.string.no_data, Toast.LENGTH_LONG).show()
            return
        }
        val intent: Intent = export.export(mainActivity, wiFiDetails)
        if (!exportAvailable(mainActivity, intent)) {
            Toast.makeText(mainActivity, R.string.export_not_available, Toast.LENGTH_LONG).show()
            return
        }
        runCatching { mainActivity.startActivity(intent) }
            .getOrElse {
                Toast
                    .makeText(mainActivity, it.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun exportAvailable(
        mainActivity: MainActivity,
        chooser: Intent,
    ): Boolean = chooser.resolveActivity(mainActivity.packageManager) != null
}
