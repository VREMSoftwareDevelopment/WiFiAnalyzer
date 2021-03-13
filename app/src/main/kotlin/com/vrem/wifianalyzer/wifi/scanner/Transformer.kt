/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.net.wifi.WifiInfo
import com.vrem.annotation.OpenClass
import com.vrem.util.EMPTY
import com.vrem.util.buildMinVersionM
import com.vrem.util.buildMinVersionR
import com.vrem.wifianalyzer.wifi.model.*
import com.vrem.wifianalyzer.wifi.model.WiFiWidth

@OpenClass
internal class Transformer {

    internal fun transformWifiInfo(wifiInfo: WifiInfo?): WiFiConnection =
            if (wifiInfo == null || wifiInfo.networkId == -1) {
                WiFiConnection.EMPTY
            } else {
                val ssid = convertSSID(wifiInfo.ssid ?: String.EMPTY)
                val wiFiIdentifier = WiFiIdentifier(ssid, wifiInfo.bssid ?: String.EMPTY)
                WiFiConnection(wiFiIdentifier, convertIpAddress(wifiInfo.ipAddress), wifiInfo.linkSpeed)
            }

    internal fun transformCacheResults(cacheResults: List<CacheResult>): List<WiFiDetail> =
            cacheResults.map { transform(it) }

    internal fun transformToWiFiData(cacheResults: List<CacheResult>, wifiInfo: WifiInfo?): WiFiData =
            WiFiData(transformCacheResults(cacheResults), transformWifiInfo(wifiInfo))

    internal fun channelWidth(scanResult: ScanResult): ChannelWidth =
            if (minVersionM()) {
                scanResult.channelWidth
            } else {
                WiFiWidth.MHZ_20.channelWidth
            }

    internal fun wiFiStandard(scanResult: ScanResult): WiFiStandardId =
            if (minVersionR()) {
                scanResult.wifiStandard
            } else {
                WiFiStandard.UNKNOWN.wiFiStandardId
            }

    internal fun centerFrequency(scanResult: ScanResult, wiFiWidth: WiFiWidth): Int =
            if (minVersionM()) {
                wiFiWidth.calculateCenter(scanResult.frequency, scanResult.centerFreq0)
            } else {
                scanResult.frequency
            }

    internal fun mc80211(scanResult: ScanResult): Boolean = minVersionM() && scanResult.is80211mcResponder

    internal fun minVersionM(): Boolean = buildMinVersionM()

    internal fun minVersionR(): Boolean = buildMinVersionR()

    private fun transform(cacheResult: CacheResult): WiFiDetail {
        val scanResult = cacheResult.scanResult
        val wiFiWidth = WiFiWidth.findOne(channelWidth(scanResult))
        val centerFrequency = centerFrequency(scanResult, wiFiWidth)
        val mc80211 = mc80211(scanResult)
        val wiFiStandard = WiFiStandard.findOne(wiFiStandard(scanResult))
        val wiFiSignal = WiFiSignal(scanResult.frequency, centerFrequency, wiFiWidth, cacheResult.average, mc80211, wiFiStandard)
        val wiFiIdentifier = WiFiIdentifier(
                if (scanResult.SSID == null) String.EMPTY else scanResult.SSID,
                if (scanResult.BSSID == null) String.EMPTY else scanResult.BSSID)
        return WiFiDetail(
                wiFiIdentifier,
                if (scanResult.capabilities == null) String.EMPTY else scanResult.capabilities,
                wiFiSignal)
    }

}