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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.SortedSet

class WiFiChannelCountryGHZ6Test {
    private val channelsSet: SortedSet<Int> = sortedSetOf(
        1, 5, 9, 13, 17, 21, 25, 29,
        33, 37, 41, 45, 49, 53, 57, 61,
        65, 69, 73, 77, 81, 85, 89, 93,
        97, 101, 105, 109, 113, 117, 121, 125,
        129, 133, 137, 141, 145, 149, 153, 157,
        161, 165, 169, 173, 177, 181, 185, 189,
        193, 197, 201, 205, 209, 213, 217, 221, 225, 229
    )

    private val fixture = WiFiChannelCountryGHZ6()

    @Test
    fun channelsForWorld() {
        listOf("GB", "XYZ", "US", "AU", "AE")
            .forEach { _ -> validateChannels(channelsSet, fixture.findChannels()) }
    }

    private fun validateChannels(expected: SortedSet<Int>, actual: SortedSet<Int>) {
        assertThat(actual).hasSize(expected.size)
        assertThat(actual).containsAll(expected)
    }

}