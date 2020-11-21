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

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class WiFiChannelCountryGHZ5Test {
    private val channelsSet1: SortedSet<Int> = sortedSetOf(36, 40, 44, 48, 52, 56, 60, 64)
    private val channelsSet2: SortedSet<Int> = sortedSetOf(100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140, 144)
    private val channelsSet3: SortedSet<Int> = sortedSetOf(149, 153, 157, 161, 165)
    private val fixture = WiFiChannelCountryGHZ5()

    @Test
    fun testChannelsAustraliaCanada() {
        val expected = channelsSet1.union(sortedSetOf(100, 104, 108, 112, 116, 132, 136, 140, 144)).union(channelsSet3)
        listOf("AU", "CA").forEach {
            val actual = fixture.findChannels(it)
            assertEquals(expected.size, actual.size)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun testChannelsChinaSouthKorea() {
        val expected = channelsSet1.union(channelsSet3)
        listOf("CN", "KR").forEach {
            val actual = fixture.findChannels(it)
            assertEquals(expected.size, actual.size)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun testChannelsJapanTurkeySouthAfrica() {
        val expected = channelsSet1.union(channelsSet2)
        listOf("JP", "TR", "ZA").forEach {
            val actual = fixture.findChannels(it)
            assertEquals(expected.size, actual.size)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun testChannelsIsrael() {
        val expected = channelsSet1
        val actual = fixture.findChannels("IL")
        assertEquals(expected.size, actual.size)
        assertEquals(expected, actual)
    }

    @Test
    fun testChannelsRussia() {
        val expected = channelsSet1.union(sortedSetOf(132, 136, 140, 144)).union(channelsSet3)
        val actual = fixture.findChannels("RU")
        assertEquals(expected.size, actual.size)
        assertEquals(expected, actual)
    }

    @Test
    fun testChannelsETSI() {
        val countriesETSI = listOf(
                "AT",      // ETSI Austria
                "BE",      // ETSI Belgium
                "CH",      // ETSI Switzerland
                "CY",      // ETSI Cyprus
                "CZ",      // ETSI Czechia
                "DE",      // ETSI Germany
                "DK",      // ETSI Denmark
                "EE",      // ETSI Estonia
                "ES",      // ETSI Spain
                "FI",      // ETSI Finland
                "FR",      // ETSI France
                "GR",      // ETSI Greece
                "HU",      // ETSI Hungary
                "IE",      // ETSI Ireland
                "IS",      // ETSI Iceland
                "IT",      // ETSI Italy
                "LI",      // ETSI Liechtenstein
                "LT",      // ETSI Lithuania
                "LU",      // ETSI Luxembourg
                "LV",      // ETSI Latvia
                "MT",      // ETSI Malta
                "NL",      // ETSI Netherlands
                "NO",      // ETSI Norway
                "PL",      // ETSI Poland
                "PT",      // ETSI Portugal
                "RO",      // ETSI Romania
                "SE",      // ETSI Sweden
                "SI",      // ETSI Slovenia
                "SK"       // ETSI Slovakia
        )

        val expected = channelsSet1
                .union(sortedSetOf(100, 104, 108, 112, 116, 120, 124, 128, 132, 136, 140))
                .union(sortedSetOf(149, 153, 157, 161, 165, 169, 173))
        countriesETSI.forEach {
            val actual = fixture.findChannels(it)
            assertEquals(expected.size, actual.size)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun testChannelsOther() {
        val expected = channelsSet1.union(channelsSet2).union(channelsSet3)
        listOf("US", "BR", "XYZ").forEach {
            val actual = fixture.findChannels(it)
            assertEquals(expected.size, actual.size)
            assertEquals(expected, actual)
        }
    }

}