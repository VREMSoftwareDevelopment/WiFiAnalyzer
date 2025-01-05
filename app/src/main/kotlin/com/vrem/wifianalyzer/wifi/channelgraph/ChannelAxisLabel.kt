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
package com.vrem.wifianalyzer.wifi.channelgraph

import com.jjoe64.graphview.LabelFormatter
import com.jjoe64.graphview.Viewport
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import com.vrem.wifianalyzer.wifi.band.WiFiChannelPair
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y
import com.vrem.wifianalyzer.wifi.graphutils.MIN_Y

internal class ChannelAxisLabel(private val wiFiBand: WiFiBand, private val wiFiChannelPair: WiFiChannelPair) : LabelFormatter {
    override fun formatLabel(value: Double, isValueX: Boolean): String {
        val valueAsInt = (value + if (value < 0) -0.5 else 0.5).toInt()
        return when {
            isValueX -> findChannel(valueAsInt)
            valueAsInt in (MIN_Y + 1)..MAX_Y -> valueAsInt.toString()
            else -> String.EMPTY
        }
    }

    override fun setViewport(viewport: Viewport) {
        // ignore
    }

    private fun findChannel(value: Int): String {
        val wiFiChannels = wiFiBand.wiFiChannels
        val wiFiChannel = wiFiChannels.wiFiChannelByFrequency(value, wiFiChannelPair)
        return if (wiFiChannel == WiFiChannel.UNKNOWN) {
            String.EMPTY
        } else {
            val channel = wiFiChannel.channel
            val countryCode = MainContext.INSTANCE.settings.countryCode()
            if (wiFiChannels.channelAvailable(countryCode, channel)) {
                channel.toString()
            } else {
                String.EMPTY
            }
        }
    }

}