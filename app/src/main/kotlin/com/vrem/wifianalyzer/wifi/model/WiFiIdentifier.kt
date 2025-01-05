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

import com.vrem.util.EMPTY

typealias SSID = String
typealias BSSID = String

data class WiFiIdentifier(val ssidRaw: SSID = String.EMPTY, val bssid: BSSID = String.EMPTY) : Comparable<WiFiIdentifier> {

    val ssid = when {
        ssidRaw.isEmpty() -> "*hidden*"
        else -> ssidRaw
    }

    val title: String
        get() = "$ssid ($bssid)"

    fun equals(other: WiFiIdentifier, ignoreCase: Boolean = false): Boolean =
        ssid.equals(other.ssidRaw, ignoreCase) && bssid.equals(other.bssid, ignoreCase)

    override fun compareTo(other: WiFiIdentifier): Int =
        compareBy<WiFiIdentifier> { it.ssidRaw }.thenBy { it.bssid }.compare(this, other)

    companion object {
        val EMPTY = WiFiIdentifier()
    }
}