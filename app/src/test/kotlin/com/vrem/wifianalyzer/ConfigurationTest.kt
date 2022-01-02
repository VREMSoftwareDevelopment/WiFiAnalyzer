/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2022 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer

import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import com.vrem.wifianalyzer.wifi.band.WiFiChannelPair
import com.vrem.wifianalyzer.wifi.band.WiFiChannels
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class ConfigurationTest {
    private val fixture = Configuration(true)

    @Test
    fun testSizeAvailable() {
        // execute & validate
        assertTrue(fixture.sizeAvailable)
    }

    @Test
    fun testSizeIsNotAvailable() {
        // execute
        fixture.size = SIZE_MIN
        // validate
        assertFalse(fixture.sizeAvailable)
    }

    @Test
    fun testLargeScreen() {
        // execute & validate
        assertTrue(fixture.largeScreen)
    }

    @Test
    fun testWiFiChannelPairWithInit() {
        // execute & validate
        WiFiBand.values().forEach {
            assertEquals(WiFiChannels.UNKNOWN, fixture.wiFiChannelPair(it))
        }
    }

    @Test
    fun testWiFiChannelPairWithCountry() {
        // execute
        fixture.wiFiChannelPair(Locale.US.country)
        // validate
        WiFiBand.values().forEach {
            assertEquals(it.wiFiChannels.wiFiChannelPairFirst(Locale.US.country), fixture.wiFiChannelPair(it))
        }
    }

    @Test
    fun testWiFiChannelPairWithWiFiBand() {
        // setup
        val expected = WiFiChannelPair(WiFiChannel(1, 2), WiFiChannel(3, 4))
        // execute
        fixture.wiFiChannelPair(WiFiBand.GHZ5, expected)
        // validate
        assertEquals(expected, fixture.wiFiChannelPair(WiFiBand.GHZ5))
    }

}
