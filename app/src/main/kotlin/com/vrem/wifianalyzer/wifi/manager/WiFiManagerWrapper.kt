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

import android.annotation.SuppressLint
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.vrem.annotation.OpenClass
import com.vrem.util.buildMinVersionR

@OpenClass
class WiFiManagerWrapper(
    private val wifiManager: WifiManager,
    private val wiFiSwitch: WiFiSwitch = WiFiSwitch(wifiManager),
) {
    fun wiFiEnabled(): Boolean = runCatching { wifiManager.isWifiEnabled }.getOrDefault(false)

    fun enableWiFi(): Boolean = runCatching { wiFiEnabled() || wiFiSwitch.on() }.getOrDefault(false)

    fun disableWiFi(): Boolean = runCatching { !wiFiEnabled() || wiFiSwitch.off() }.getOrDefault(false)

    fun startScan(): Boolean = runCatching { wifiManager.startScan() }.getOrDefault(false)

    @SuppressLint("MissingPermission")
    fun scanResults(): List<ScanResult> = runCatching { wifiManager.scanResults ?: listOf() }.getOrDefault(listOf())

    fun wiFiInfo(): WifiInfo? = runCatching { wifiManager.connectionInfo }.getOrNull()

    fun is5GHzBandSupported(): Boolean = wifiManager.is5GHzBandSupported

    fun is6GHzBandSupported(): Boolean =
        if (minVersionR()) {
            wifiManager.is6GHzBandSupported
        } else {
            false
        }

    fun isScanThrottleEnabled(): Boolean =
        if (minVersionR()) {
            isScanThrottleEnabledR()
        } else {
            false
        }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun isScanThrottleEnabledR(): Boolean = wifiManager.isScanThrottleEnabled

    fun minVersionR(): Boolean = buildMinVersionR()
}
