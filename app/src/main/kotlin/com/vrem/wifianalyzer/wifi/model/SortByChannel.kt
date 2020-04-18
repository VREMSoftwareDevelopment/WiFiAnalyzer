/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.model

class SortByChannel : Comparator<WiFiDetail> {
    override fun compare(lhs: WiFiDetail, rhs: WiFiDetail): Int = when {
        lhs.wiFiSignal.getPrimaryWiFiChannel().channel != rhs.wiFiSignal.getPrimaryWiFiChannel().channel ->
            lhs.wiFiSignal.getPrimaryWiFiChannel().channel.compareTo(rhs.wiFiSignal.getPrimaryWiFiChannel().channel)
        rhs.wiFiSignal.level != lhs.wiFiSignal.level -> rhs.wiFiSignal.level.compareTo(lhs.wiFiSignal.level)
        lhs.SSID != rhs.SSID -> lhs.SSID.compareTo(rhs.SSID, true)
        else -> lhs.BSSID.compareTo(rhs.BSSID, true)
    }
}
