/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.scanner

import android.net.wifi.ScanResult
import android.net.wifi.WifiSsid
import com.vrem.util.buildMinVersionT
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

fun whenSsid(scanResult: ScanResult, ssid: String) {
    if (buildMinVersionT()) {
        val wifiSsid: WifiSsid = mock()
        whenever(scanResult.wifiSsid).thenReturn(wifiSsid)
        whenever(wifiSsid.toString()).thenReturn(ssid)
    } else {
        @Suppress("DEPRECATION")
        scanResult.SSID = ssid
    }
}
