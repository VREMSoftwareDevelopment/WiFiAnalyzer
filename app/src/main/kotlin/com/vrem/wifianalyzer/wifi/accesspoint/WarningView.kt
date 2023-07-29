/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.annotation.TargetApi
import android.os.Build
import android.view.View
import com.vrem.util.buildMinVersionP
import com.vrem.util.buildMinVersionT
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail

class WarningView(private val mainActivity: MainActivity) {

    fun update(wiFiData: WiFiData): Boolean {
        val registered = mainActivity.currentNavigationMenu().registered()
        val noData = noData(registered, wiFiData.wiFiDetails)
        val noLocation = noLocation(registered)
        val throttling = throttling(registered, noData, noLocation)
        val warning = noData || noLocation || throttling
        mainActivity.findViewById<View>(R.id.warning).visibility = if (warning) View.VISIBLE else View.GONE
        return warning
    }

    internal fun noData(registered: Boolean, wiFiDetails: List<WiFiDetail>): Boolean {
        val noData = registered && wiFiDetails.isEmpty()
        mainActivity.findViewById<View>(R.id.no_data).visibility = if (noData) View.VISIBLE else View.GONE
        return noData
    }

    internal fun noLocation(registered: Boolean): Boolean {
        val noLocation = registered && !MainContext.INSTANCE.permissionService.enabled()
        mainActivity.findViewById<View>(R.id.no_location).visibility = if (noLocation) View.VISIBLE else View.GONE
        return noLocation
    }

    internal fun throttling(registered: Boolean, noData: Boolean, noLocation: Boolean): Boolean =
        if (buildMinVersionT()) {
            throttlingAndroidT(registered)
        } else if (buildMinVersionP()) {
            throttlingAndroidP(registered, noData, noLocation)
        } else {
            throttlingLegacy()
        }

    @TargetApi(Build.VERSION_CODES.TIRAMISU)
    private fun throttlingAndroidT(registered: Boolean): Boolean {
        val throttling = registered && MainContext.INSTANCE.wiFiManagerWrapper.isScanThrottleEnabled()
        mainActivity.findViewById<View>(R.id.throttling).visibility = if (throttling) View.VISIBLE else View.GONE
        return throttling
    }

    @TargetApi(Build.VERSION_CODES.P)
    private fun throttlingAndroidP(registered: Boolean, noData: Boolean, noLocation: Boolean): Boolean {
        val throttling = registered && (noData || noLocation)
        mainActivity.findViewById<View>(R.id.throttling).visibility = if (throttling) View.VISIBLE else View.GONE
        return throttling
    }

    private fun throttlingLegacy(): Boolean {
        mainActivity.findViewById<View>(R.id.throttling).visibility = View.GONE
        return false
    }

}