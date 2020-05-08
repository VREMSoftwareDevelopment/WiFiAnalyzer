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

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class WiFiChannelCountryGHZ5Test {
    private val channelsSet1: SortedSet<Int> = sortedSetOf(36, 40, 44, 48, 52, 56, 60, 64)
    private val channelsSet2: SortedSet<Int> = sortedSetOf(100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)
    private val channelsSet3: SortedSet<Int> = sortedSetOf(149, 153, 157, 161, 165)
    private val fixture = WiFiChannelCountryGHZ5()

    @Test
    fun testChannelsAustraliaCanada() {
        val exclude: SortedSet<Int> = sortedSetOf(120, 124, 128)
        val expectedSize = channelsSet1.size + channelsSet2.size + channelsSet3.size - exclude.size
        listOf("AU", "CA").forEach {
            val actual: Set<Int> = fixture.findChannels(it)
            assertEquals(expectedSize, actual.size)
            assertTrue(actual.containsAll(channelsSet1))
            assertTrue(actual.containsAll(channelsSet3))
            assertFalse(actual.containsAll(exclude))
        }
    }

    @Test
    fun testChannelsChinaSouthKorea() {
        val expectedSize = channelsSet1.size + channelsSet3.size
        listOf("CN", "KR").forEach {
            val actual: Set<Int> = fixture.findChannels(it)
            assertEquals(expectedSize, actual.size)
            assertTrue(actual.containsAll(channelsSet1))
            assertTrue(actual.containsAll(channelsSet3))
            assertFalse(actual.containsAll(channelsSet2))
        }
    }

    @Test
    fun testChannelsJapanTurkeySouthAfrica() {
        val expectedSize = channelsSet1.size + channelsSet2.size
        listOf("JP", "TR", "ZA").forEach {
            val actual: Set<Int> = fixture.findChannels(it)
            assertEquals(expectedSize, actual.size)
            assertTrue(actual.containsAll(channelsSet1))
            assertTrue(actual.containsAll(channelsSet2))
            assertFalse(actual.containsAll(channelsSet3))
        }
    }

    @Test
    fun testChannelsIsrael() {
        val expectedSize = channelsSet1.size
        val actual: Set<Int> = fixture.findChannels("IL")
        assertEquals(expectedSize, actual.size)
        assertTrue(actual.containsAll(channelsSet1))
        assertFalse(actual.containsAll(channelsSet2))
        assertFalse(actual.containsAll(channelsSet3))
    }

    @Test
    fun testChannelsOther() {
        val expectedSize = channelsSet1.size + channelsSet2.size + channelsSet3.size
        listOf("US", "RU", "XYZ").forEach {
            val actual: Set<Int> = fixture.findChannels(it)
            assertEquals(expectedSize, actual.size)
            assertTrue(actual.containsAll(channelsSet1))
            assertTrue(actual.containsAll(channelsSet2))
            assertTrue(actual.containsAll(channelsSet3))
        }
    }

}