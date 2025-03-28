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

import androidx.annotation.StringRes
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R

typealias Available = () -> Boolean

internal val availableGHZ2: Available = { true }
internal val availableGHZ5: Available = { MainContext.INSTANCE.wiFiManagerWrapper.is5GHzBandSupported() }
internal val availableGHZ6: Available = { MainContext.INSTANCE.wiFiManagerWrapper.is6GHzBandSupported() }

/**
 * @see <a href="https://en.wikipedia.org/wiki/List_of_WLAN_channels">List of WLAN channels</a>
 *
 * WiFi Bands
 *  2.4GHz: 1, 2412 -> 13, 2472 (-1, 2402 -> 15, 2482)
 *  5GHz: 36, 5180 -> 177, 5885 (30, 5150 -> 184, 5920)
 *  6GHz: 1, 5955 -> 233, 7115 (-5, 5925 -> 235, 7125)
 */
private val channelRangeGHZ2 = WiFiChannelPair(WiFiChannel(-1, 2402), WiFiChannel(15, 2482))
private val graphChannelsGHZ2 = (1..13).associate { it to "$it" }

private val channelRangeGHZ5 = WiFiChannelPair(WiFiChannel(30, 5150), WiFiChannel(184, 5920))
private val graphChannelsGHZ5 = listOf(34, 50, 66, 82, 98, 113, 129, 147, 163, 178).associateWith {
    when (it) {
        113 -> "114"
        129 -> "130"
        178 -> "179"
        else -> "$it"
    }
}

private val channelRangeGHZ6 = WiFiChannelPair(WiFiChannel(-5, 5925), WiFiChannel(235, 7125))
private val graphChannelsGHZ6 = listOf(1, 31, 63, 95, 128, 160, 192, 222).associateWith {
    when (it) {
        128 -> "127"
        160 -> "159"
        192 -> "191"
        222 -> "223"
        else -> "$it"
    }
}

enum class WiFiBand(@StringRes val textResource: Int, val wiFiChannels: WiFiChannels, val available: Available) {
    GHZ2(R.string.wifi_band_2ghz, WiFiChannels(channelRangeGHZ2, graphChannelsGHZ2, 1), availableGHZ2),
    GHZ5(R.string.wifi_band_5ghz, WiFiChannels(channelRangeGHZ5, graphChannelsGHZ5), availableGHZ5),
    GHZ6(R.string.wifi_band_6ghz, WiFiChannels(channelRangeGHZ6, graphChannelsGHZ6), availableGHZ6);

    val ghz2: Boolean get() = GHZ2 == this
    val ghz5: Boolean get() = GHZ5 == this
    val ghz6: Boolean get() = GHZ6 == this

    companion object {
        fun find(frequency: Int): WiFiBand = WiFiBand.entries.firstOrNull { it.wiFiChannels.inRange(frequency) } ?: GHZ2
    }

}