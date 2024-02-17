/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.SortedSet

class WiFiChannelCountryGHZ2Test {
    private val channelsSet1: SortedSet<Int> = sortedSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
    private val channelsSet2: SortedSet<Int> = sortedSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)
    private val fixture = WiFiChannelCountryGHZ2()

    @Test
    fun channelsForUSAndSimilar() {
        listOf("AS", "CA", "CO", "DO", "FM", "GT", "GU", "MP", "MX", "PA", "PR", "UM", "US", "UZ", "VI")
            .forEach { validateChannels(channelsSet1, fixture.findChannels(it)) }
    }

    @Test
    fun channelsForWorld() {
        listOf("GB", "XYZ", "AU", "AE")
            .forEach { validateChannels(channelsSet2, fixture.findChannels(it)) }
    }

    private fun validateChannels(expected: SortedSet<Int>, actual: SortedSet<Int>) {
        assertThat(actual).hasSize(expected.size)
        assertThat(actual).containsAll(expected)
    }

}