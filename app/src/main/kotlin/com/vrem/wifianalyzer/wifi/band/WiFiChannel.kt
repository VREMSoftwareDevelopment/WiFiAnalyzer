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
package com.vrem.wifianalyzer.wifi.band

data class WiFiChannel(val channel: Int = 0, val frequency: Int = 0) : Comparable<WiFiChannel> {
    fun inRange(value: Int): Boolean =
        value in frequency - ALLOWED_RANGE..frequency + ALLOWED_RANGE

    override fun compareTo(other: WiFiChannel): Int =
        compareBy<WiFiChannel> { it.channel }.thenBy { it.frequency }.compare(this, other)

    companion object {
        val UNKNOWN = WiFiChannel()
        private const val ALLOWED_RANGE = WiFiChannels.FREQUENCY_SPREAD / 2
    }

}