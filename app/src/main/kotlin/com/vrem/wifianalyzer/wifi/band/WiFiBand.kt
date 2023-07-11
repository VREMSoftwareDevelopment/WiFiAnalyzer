/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
internal val available5GHz: Available = { MainContext.INSTANCE.wiFiManagerWrapper.is5GHzBandSupported() }
internal val available6GHz: Available = { MainContext.INSTANCE.wiFiManagerWrapper.is6GHzBandSupported() }

enum class WiFiBand(@StringRes val textResource: Int, val wiFiChannels: WiFiChannels, val available: Available) {
    GHZ2(R.string.wifi_band_2ghz, WiFiChannelsGHZ2(), availableGHZ2),
    band5GHz(R.string.wifi_band_5ghz, WiFiChannels5GHz(), available5GHz),
    band6GHz(R.string.wifi_band_6ghz, WiFiChannels6GHz(), available6GHz);

    val ghz2: Boolean get() = GHZ2 == this
    val is5GHz: Boolean get() = band5GHz == this
    val is6GHz: Boolean get() = band6GHz == this

    companion object {
        fun find(frequency: Int): WiFiBand = values().find { it.wiFiChannels.inRange(frequency) }
            ?: GHZ2
    }

}
