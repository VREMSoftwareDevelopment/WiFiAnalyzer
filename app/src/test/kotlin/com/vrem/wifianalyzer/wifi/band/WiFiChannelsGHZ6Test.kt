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

import com.vrem.util.EMPTY
import org.junit.Assert.*
import org.junit.Test
import java.util.Locale

class WiFiChannelsGHZ6Test {
    private val fixture: WiFiChannelsGHZ6 = WiFiChannelsGHZ6()

    @Test
    fun testInRange() {
        assertTrue(fixture.inRange(5925))
        assertTrue(fixture.inRange(7125))
    }

    @Test
    fun testNotInRange() {
        assertFalse(fixture.inRange(5924))
        assertFalse(fixture.inRange(7126))
    }

    @Test
    fun testWiFiChannelByFrequency() {
        assertEquals(1, fixture.wiFiChannelByFrequency(5953).channel)
        assertEquals(1, fixture.wiFiChannelByFrequency(5955).channel)
        assertEquals(1, fixture.wiFiChannelByFrequency(5957).channel)
        assertEquals(93, fixture.wiFiChannelByFrequency(6413).channel)
        assertEquals(93, fixture.wiFiChannelByFrequency(6415).channel)
        assertEquals(93, fixture.wiFiChannelByFrequency(6417).channel)
    }

    @Test
    fun testWiFiChannelByFrequencyNotFound() {
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(5952))
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(6418))
    }

    @Test
    fun testWiFiChannelByChannel() {
        assertEquals(5955, fixture.wiFiChannelByChannel(1).frequency)
        assertEquals(6415, fixture.wiFiChannelByChannel(93).frequency)
    }

    @Test
    fun testWiFiChannelByChannelNotFound() {
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByChannel(0))
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByChannel(94))
    }

    @Test
    fun testWiFiChannelFirst() {
        assertEquals(1, fixture.wiFiChannelFirst().channel)
    }

    @Test
    fun testWiFiChannelLast() {
        assertEquals(229, fixture.wiFiChannelLast().channel)
    }

    @Test
    fun testWiFiChannelPair() {
        validatePair(1, 29, fixture.wiFiChannelPairFirst(Locale.US.country))
        validatePair(1, 29, fixture.wiFiChannelPairFirst(String.EMPTY))
        validatePair(1, 29, fixture.wiFiChannelPairFirst("XYZ"))
    }

    @Test
    fun testWiFiChannelPairs() {
        val wiFiChannelPairs: List<WiFiChannelPair> = fixture.wiFiChannelPairs()
        assertEquals(7, wiFiChannelPairs.size)
        validatePair(1, 29, wiFiChannelPairs[0])
        validatePair(33, 61, wiFiChannelPairs[1])
        validatePair(65, 93, wiFiChannelPairs[2])
        validatePair(97, 125, wiFiChannelPairs[3])
        validatePair(129, 157, wiFiChannelPairs[4])
        validatePair(161, 189, wiFiChannelPairs[5])
        validatePair(193, 229, wiFiChannelPairs[6])
    }

    private fun validatePair(expectedFirst: Int, expectedSecond: Int, pair: WiFiChannelPair) {
        assertEquals(expectedFirst, pair.first.channel)
        assertEquals(expectedSecond, pair.second.channel)
    }

    @Test
    fun testAvailableChannels() {
        assertEquals(58, fixture.availableChannels(Locale.US.country).size)
        assertEquals(58, fixture.availableChannels(Locale.UK.country).size)
    }

    @Test
    fun testWiFiChannelByFrequency2GHZ() {
        // setup
        val wiFiChannelPair: WiFiChannelPair = fixture.wiFiChannelPairs()[0]
        // execute
        val actual: WiFiChannel = fixture.wiFiChannelByFrequency(2000, wiFiChannelPair)
        // validate
        assertEquals(WiFiChannel.UNKNOWN, actual)
    }

    @Test
    fun testWiFiChannelByFrequency6GHZInRange() {
        // setup
        val wiFiChannelPair: WiFiChannelPair = fixture.wiFiChannelPairs()[0]
        // execute
        val actual: WiFiChannel =
            fixture.wiFiChannelByFrequency(wiFiChannelPair.first.frequency, wiFiChannelPair)
        // validate
        assertEquals(wiFiChannelPair.first, actual)
    }
}