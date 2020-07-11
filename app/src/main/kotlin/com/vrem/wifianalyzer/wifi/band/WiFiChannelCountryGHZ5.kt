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
package com.vrem.wifianalyzer.wifi.band

import java.util.*

internal class WiFiChannelCountryGHZ5 {
    private val channelsSet1: SortedSet<Int> = sortedSetOf(36, 40, 44, 48, 52, 56, 60, 64)
    private val channelsSet2: SortedSet<Int> = sortedSetOf(100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)
    private val channelsSet3: SortedSet<Int> = sortedSetOf(149, 153, 157, 161, 165)
    private val channelsToExcludeCanada: SortedSet<Int> = sortedSetOf(120, 124, 128)
    private val channelsToExcludeIsrael: SortedSet<Int> = channelsSet2.union(channelsSet3).toSortedSet()
    private val channels: SortedSet<Int> = channelsSet1.union(channelsSet2).union(channelsSet3).toSortedSet()
    private val channelsToExclude: Map<String, SortedSet<Int>> = mapOf(
            "AU" to channelsToExcludeCanada,    // Australia
            "CA" to channelsToExcludeCanada,    // Canada
            "CN" to channelsSet2,               // China
            "IL" to channelsToExcludeIsrael,    // Israel
            "JP" to channelsSet3,               // Japan
            "KR" to channelsSet2,               // South Korea
            "TR" to channelsSet3,               // Turkey
            "ZA" to channelsSet3                // South Africa
    )

    fun findChannels(countryCode: String): SortedSet<Int> =
            channels.subtract(channelsToExclude[countryCode.capitalize()]?.toSortedSet()
                    ?: setOf())
                    .toSortedSet()

}