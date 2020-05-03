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
package com.vrem.wifianalyzer.wifi.band

import com.vrem.wifianalyzer.R

enum class WiFiBand(val textResource: Int, val wiFiChannels: WiFiChannels) {
    GHZ2(R.string.wifi_band_2ghz, WiFiChannelsGHZ2()),
    GHZ5(R.string.wifi_band_5ghz, WiFiChannelsGHZ5());

    fun toggle(): WiFiBand = if (GHZ5()) GHZ2 else GHZ5

    fun GHZ5(): Boolean = GHZ5 == this

    companion object {
        @JvmStatic
        fun find(frequency: Int): WiFiBand = values().find { it.wiFiChannels.inRange(frequency) }
                ?: GHZ2
    }

}