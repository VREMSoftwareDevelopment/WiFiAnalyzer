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
package com.vrem.wifianalyzer.wifi.manager

import android.annotation.TargetApi
import android.net.wifi.WifiManager
import android.os.Build
import com.vrem.annotation.OpenClass
import com.vrem.util.buildMinVersionQ
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.startWiFiSettings

@OpenClass
class WiFiSwitch(private val wifiManager: WifiManager) {
    fun on(): Boolean = enable(true)

    fun off(): Boolean = enable(false)

    fun startWiFiSettings(): Unit = MainContext.INSTANCE.mainActivity.startWiFiSettings()

    fun minVersionQ(): Boolean = buildMinVersionQ()

    private fun enable(enabled: Boolean): Boolean = if (minVersionQ()) enableWiFiAndroidQ() else enableWiFiLegacy(enabled)

    @TargetApi(Build.VERSION_CODES.Q)
    private fun enableWiFiAndroidQ(): Boolean {
        startWiFiSettings()
        return true
    }

    @Suppress("DEPRECATION")
    private fun enableWiFiLegacy(enabled: Boolean): Boolean = wifiManager.setWifiEnabled(enabled)

}