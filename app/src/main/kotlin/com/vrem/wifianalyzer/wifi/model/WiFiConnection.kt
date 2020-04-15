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

data class WiFiConnection(val SSID: String = "", val BSSID: String = "") :
        Comparable<WiFiConnection> {

    constructor(SSID: String = "", BSSID: String = "", ipAddress: String = "", linkSpeed: Int = LINK_SPEED_INVALID) :
            this(SSID, BSSID) {
        this.ipAddress = ipAddress
        this.linkSpeed = linkSpeed
    }

    var ipAddress: String = ""
    var linkSpeed: Int = LINK_SPEED_INVALID

    override fun compareTo(other: WiFiConnection): Int {
        var result = SSID.compareTo(other.SSID)
        if (result == 0) {
            result = BSSID.compareTo(other.BSSID)
        }
        return result
    }

    fun isConnected(): Boolean = EMPTY != this

    companion object {
        const val LINK_SPEED_INVALID = -1
        val EMPTY = WiFiConnection()
    }
}

