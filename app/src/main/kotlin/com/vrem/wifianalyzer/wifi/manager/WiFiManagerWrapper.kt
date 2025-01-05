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
import com.vrem.annotation.OpenClass
import com.vrem.util.buildMinVersionR

@OpenClass
class WiFiManagerWrapper(
    private val wifiManager: WifiManager,
    private val wiFiSwitch: WiFiSwitch = WiFiSwitch(wifiManager)
) {
    fun wiFiEnabled(): Boolean =
        try {
            wifiManager.isWifiEnabled
        } catch (e: Exception) {
            false
        }

    fun enableWiFi(): Boolean =
        try {
            wiFiEnabled() || wiFiSwitch.on()
        } catch (e: Exception) {
            false
        }

    fun disableWiFi(): Boolean =
        try {
            !wiFiEnabled() || wiFiSwitch.off()
        } catch (e: Exception) {
            false
        }

    @Suppress("DEPRECATION")
    fun startScan(): Boolean =
        try {
            wifiManager.startScan()
        } catch (e: Exception) {
            false
        }

    @SuppressLint("MissingPermission")
    fun scanResults(): List<ScanResult> =
        try {
            wifiManager.scanResults ?: listOf()
        } catch (e: Exception) {
            listOf()
        }

    @Suppress("DEPRECATION")
    fun wiFiInfo(): WifiInfo? =
        try {
            wifiManager.connectionInfo
        } catch (e: Exception) {
            null
        }


    fun is5GHzBandSupported(): Boolean =
        wifiManager.is5GHzBandSupported

    fun is6GHzBandSupported(): Boolean =
        if (minVersionR()) {
            wifiManager.is6GHzBandSupported
        } else {
            false
        }

    fun isScanThrottleEnabled(): Boolean =
        if (minVersionR()) {
            wifiManager.isScanThrottleEnabled
        } else {
            false
        }

    fun minVersionR(): Boolean = buildMinVersionR()

}

