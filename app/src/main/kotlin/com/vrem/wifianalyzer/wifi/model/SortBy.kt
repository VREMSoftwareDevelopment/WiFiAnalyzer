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
package com.vrem.wifianalyzer.wifi.model

fun sortBySSID(): Comparator<WiFiDetail> =
    compareBy<WiFiDetail> { it.wiFiIdentifier.ssid }
        .thenByDescending { it.wiFiSignal.level }
        .thenBy { it.wiFiIdentifier.bssid }

fun sortByStrength(): Comparator<WiFiDetail> =
    compareByDescending<WiFiDetail> { it.wiFiSignal.level }
        .thenBy { it.wiFiIdentifier.ssid }
        .thenBy { it.wiFiIdentifier.bssid }

fun sortByChannel(): Comparator<WiFiDetail> =
    compareBy<WiFiDetail> { it.wiFiSignal.primaryFrequency }
        .thenByDescending { it.wiFiSignal.level }
        .thenBy { it.wiFiIdentifier.ssid }
        .thenBy { it.wiFiIdentifier.bssid }

fun sortByDefault(): Comparator<WiFiDetail> =
    compareBy<WiFiDetail> { it.wiFiIdentifier.ssid }.thenBy { it.wiFiIdentifier.bssid }

enum class SortBy(
    val sort: Comparator<WiFiDetail>,
) {
    STRENGTH(sortByStrength()),
    SSID(sortBySSID()),
    CHANNEL(sortByChannel()),
}
