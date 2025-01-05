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

import java.util.SortedSet

internal class WiFiChannelCountryGHZ6 {
    private val channelsSet1: Set<Int> = setOf(1, 5, 9, 13, 17, 21, 25, 29)
    private val channelsSet2: Set<Int> = setOf(33, 37, 41, 45, 49, 53, 57, 61)
    private val channelsSet3: Set<Int> = setOf(65, 69, 73, 77, 81, 85, 89, 93)
    private val channelsSet4: Set<Int> = setOf(97, 101, 105, 109, 113, 117, 121, 125)
    private val channelsSet5: Set<Int> = setOf(129, 133, 137, 141, 145, 149, 153, 157)
    private val channelsSet6: Set<Int> = setOf(161, 165, 169, 173, 177, 181, 185, 189)
    private val channelsSet7: Set<Int> = setOf(193, 197, 201, 205, 209, 213, 217, 221, 225, 229)
    private val channels: SortedSet<Int> = channelsSet1
        .union(channelsSet2)
        .union(channelsSet3)
        .union(channelsSet4)
        .union(channelsSet5)
        .union(channelsSet6)
        .union(channelsSet7)
        .toSortedSet()

    fun findChannels(): SortedSet<Int> = channels
}