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

import android.net.wifi.ScanResult
import com.vrem.util.buildMinVersionT
import kotlin.math.abs

private val CHANNEL_WIDTH_320MHZ = if (buildMinVersionT()) ScanResult.CHANNEL_WIDTH_320MHZ else 5

// 5GHz: 50, 82, 114, 163 | 6GHz: 15, 47, 79, 111, 143, 175, 207
private val frequency160Range = listOf(
    5250 to (5170 to 5329),
    5410 to (5330 to 5489),
    5650 to (5490 to 5730),
    5815 to (5735 to 5895),
    6025 to (5945 to 6104),
    6185 to (6105 to 6264),
    6345 to (6265 to 6424),
    6505 to (6425 to 6584),
    6665 to (6585 to 6744),
    6825 to (6745 to 6904),
    6985 to (6905 to 7065)
)
private val frequency160center = frequency160Range.map { it.first }

// 6GHz: 31, 95, 159, 191
private val frequency320Range = listOf(
    6100 to (5945 to 6264),
    6430 to (6265 to 6584),
    6750 to (6585 to 6904),
    6910 to (6905 to 7065)
)

// 6GHz: 31, 63, 95, 127, 159, 191
private val frequency320Center = listOf(6100, 6270, 6430, 6590, 6750, 6910)

typealias ChannelWidth = Int
typealias CalculateCenter = (primary: Int, center0: Int, center1: Int) -> Int

internal val calculateCenter20: CalculateCenter = { primary, _, _ -> primary }
internal val calculateCenter40: CalculateCenter = { primary, center0, _ ->
    if (abs(primary - center0) >= WiFiWidth.MHZ_40.frequencyWidthHalf) (primary + center0) / 2 else center0
}
internal val calculateCenter80: CalculateCenter = { _, center0, _ -> center0 }
internal val calculateCenter160: CalculateCenter = { primary, center0, center1 ->
    when {
        center1 in frequency160center -> center1
        center0 in frequency160center -> center0
        primary in frequency160center -> primary
        else -> frequency160Range.firstOrNull { primary in it.second.first..it.second.second }?.first ?: center1
    }
}
internal val calculateCenter320: CalculateCenter = { primary, center0, center1 ->
    when {
        center1 in frequency320Center -> center1
        center0 in frequency320Center -> center0
        primary in frequency320Center -> primary
        else -> frequency320Range.firstOrNull { primary in it.second.first..it.second.second }?.first ?: center1
    }
}

enum class WiFiWidth(val channelWidth: ChannelWidth, val frequencyWidth: Int, val guardBand: Int, val calculateCenter: CalculateCenter) {
    MHZ_20(ScanResult.CHANNEL_WIDTH_20MHZ, 20, 2, calculateCenter20),
    MHZ_40(ScanResult.CHANNEL_WIDTH_40MHZ, 40, 3, calculateCenter40),
    MHZ_80(ScanResult.CHANNEL_WIDTH_80MHZ, 80, 3, calculateCenter80),
    MHZ_160(ScanResult.CHANNEL_WIDTH_160MHZ, 160, 3, calculateCenter160),
    MHZ_80_PLUS(ScanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ, 80, 3, calculateCenter80),
    MHZ_320(CHANNEL_WIDTH_320MHZ, 320, 3, calculateCenter320);

    val frequencyWidthHalf: Int = frequencyWidth / 2

    companion object {
        fun findOne(channelWidth: ChannelWidth): WiFiWidth =
            WiFiWidth.entries.firstOrNull { it.channelWidth == channelWidth } ?: MHZ_20
    }
}