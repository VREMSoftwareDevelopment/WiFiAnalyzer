/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.export

import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiSignal.Companion.FREQUENCY_UNITS
import java.util.Locale.ENGLISH

private val header = String.format(ENGLISH,
        "Time Stamp|SSID|BSSID|Strength|Primary Channel|Primary Frequency|Center Channel|Center Frequency|Width (Range)|Distance|802.11mc|Security%n")

fun getData(wiFiDetails: List<WiFiDetail>, timestamp: String): String =
        header + wiFiDetails.joinToString(separator = String.EMPTY, transform = toExportString(timestamp))

private fun toExportString(timestamp: String): (WiFiDetail) -> String = { wiFiDetail: WiFiDetail ->
    with(wiFiDetail) {
        String.format(ENGLISH, "%s|%s|%s|%ddBm|%d|%d%s|%d|%d%s|%d%s (%d - %d)|%s|%s|%s%n",
                timestamp,
                wiFiIdentifier.ssid,
                wiFiIdentifier.bssid,
                wiFiSignal.level,
                wiFiSignal.primaryWiFiChannel().channel,
                wiFiSignal.primaryFrequency,
                FREQUENCY_UNITS,
                wiFiSignal.centerWiFiChannel().channel,
                wiFiSignal.centerFrequency,
                FREQUENCY_UNITS,
                wiFiSignal.wiFiWidth.frequencyWidth,
                FREQUENCY_UNITS,
                wiFiSignal.frequencyStart(),
                wiFiSignal.frequencyEnd(),
                wiFiSignal.distance(),
                wiFiSignal.is80211mc,
                capabilities)
    }
}