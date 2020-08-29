/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.net.wifi.ScanResult
import com.vrem.util.buildMinVersionM

typealias ChannelWidth = Int

private val channelWidth20: ChannelWidth = if (buildMinVersionM()) ScanResult.CHANNEL_WIDTH_20MHZ else 0
private val channelWidth40: ChannelWidth = if (buildMinVersionM()) ScanResult.CHANNEL_WIDTH_40MHZ else 1
private val channelWidth80: ChannelWidth = if (buildMinVersionM()) ScanResult.CHANNEL_WIDTH_80MHZ else 2
private val channelWidth160: ChannelWidth = if (buildMinVersionM()) ScanResult.CHANNEL_WIDTH_160MHZ else 3
private val channelWidth80Plus: ChannelWidth = if (buildMinVersionM()) ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ else 4

enum class WiFiWidth(val channelWidth: ChannelWidth, val frequencyWidth: Int) {
    MHZ_20(channelWidth20, 20),
    MHZ_40(channelWidth40, 40),
    MHZ_80(channelWidth80, 80),
    MHZ_160(channelWidth160, 160),
    MHZ_80_PLUS(channelWidth80Plus, 80);

    val frequencyWidthHalf: Int = frequencyWidth / 2

    companion object {
        fun findOne(channelWidth: ChannelWidth): WiFiWidth =
                values().firstOrNull { it.channelWidth == channelWidth } ?: MHZ_20
    }
}