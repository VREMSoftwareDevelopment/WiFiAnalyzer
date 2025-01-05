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
package com.vrem.wifianalyzer

import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannelPair
import com.vrem.wifianalyzer.wifi.band.WiFiChannels

const val SIZE_MIN = 1024
const val SIZE_MAX = 4096

@OpenClass
class Configuration(val largeScreen: Boolean) {
    private var wiFiChannelPair = mutableMapOf<WiFiBand, WiFiChannelPair>()

    var size = SIZE_MAX

    val sizeAvailable: Boolean
        get() = size == SIZE_MAX

    fun wiFiChannelPair(countryCode: String): Unit =
        WiFiBand.entries.forEach {
            this.wiFiChannelPair[it] = it.wiFiChannels.wiFiChannelPairFirst(countryCode)
        }

    fun wiFiChannelPair(wiFiBand: WiFiBand): WiFiChannelPair =
        this.wiFiChannelPair[wiFiBand]!!

    fun wiFiChannelPair(wiFiBand: WiFiBand, wiFiChannelPair: WiFiChannelPair) {
        this.wiFiChannelPair[wiFiBand] = wiFiChannelPair
    }

    init {
        WiFiBand.entries.forEach {
            this.wiFiChannelPair[it] = WiFiChannels.UNKNOWN
        }

    }
}