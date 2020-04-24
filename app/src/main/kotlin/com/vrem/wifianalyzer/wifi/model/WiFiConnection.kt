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

import com.vrem.util.STRING_EMPTY

data class WiFiConnection(val SSID: String = STRING_EMPTY,
                          val BSSID: String = STRING_EMPTY,
                          val ipAddress: String = STRING_EMPTY,
                          val linkSpeed: Int = LINK_SPEED_INVALID) :
        Comparable<WiFiConnection> {

    fun connected(): Boolean = EMPTY != this

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WiFiConnection

        if (SSID != other.SSID) return false
        if (BSSID != other.BSSID) return false

        return true
    }

    override fun hashCode(): Int = 31 * SSID.hashCode() + BSSID.hashCode()

    override fun compareTo(other: WiFiConnection): Int =
            compareBy<WiFiConnection> { it.SSID }.thenBy { it.BSSID }.compare(this, other)

    companion object {
        const val LINK_SPEED_INVALID = -1

        @JvmField
        val EMPTY = WiFiConnection()
    }
}

