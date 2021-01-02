/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import java.util.*

class WiFiChannelsGHZ2Test {
    private val fixture: WiFiChannelsGHZ2 = WiFiChannelsGHZ2()

    @Test
    fun testInRange() {
        assertTrue(fixture.inRange(2400))
        assertTrue(fixture.inRange(2499))
    }

    @Test
    fun testNotInRange() {
        assertFalse(fixture.inRange(2399))
        assertFalse(fixture.inRange(2500))
    }

    @Test
    fun testWiFiChannelByFrequency() {
        assertEquals(1, fixture.wiFiChannelByFrequency(2410).channel)
        assertEquals(1, fixture.wiFiChannelByFrequency(2412).channel)
        assertEquals(1, fixture.wiFiChannelByFrequency(2414).channel)
        assertEquals(6, fixture.wiFiChannelByFrequency(2437).channel)
        assertEquals(7, fixture.wiFiChannelByFrequency(2442).channel)
        assertEquals(13, fixture.wiFiChannelByFrequency(2470).channel)
        assertEquals(13, fixture.wiFiChannelByFrequency(2472).channel)
        assertEquals(13, fixture.wiFiChannelByFrequency(2474).channel)
        assertEquals(14, fixture.wiFiChannelByFrequency(2482).channel)
        assertEquals(14, fixture.wiFiChannelByFrequency(2484).channel)
        assertEquals(14, fixture.wiFiChannelByFrequency(2486).channel)
    }

    @Test
    fun testWiFiChannelByFrequencyNotFound() {
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(2399))
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(2409))
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(2481))
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(2481))
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(2487))
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(2500))
    }

    @Test
    fun testWiFiChannelByChannel() {
        assertEquals(2412, fixture.wiFiChannelByChannel(1).frequency)
        assertEquals(2437, fixture.wiFiChannelByChannel(6).frequency)
        assertEquals(2442, fixture.wiFiChannelByChannel(7).frequency)
        assertEquals(2472, fixture.wiFiChannelByChannel(13).frequency)
        assertEquals(2484, fixture.wiFiChannelByChannel(14).frequency)
    }

    @Test
    fun testWiFiChannelByChannelNotFound() {
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByChannel(0))
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByChannel(15))
    }

    @Test
    fun testWiFiChannelFirst() {
        assertEquals(1, fixture.wiFiChannelFirst().channel)
    }

    @Test
    fun testWiFiChannelLast() {
        assertEquals(14, fixture.wiFiChannelLast().channel)
    }

    @Test
    fun testWiFiChannelPairs() {
        val pair: List<WiFiChannelPair> = fixture.wiFiChannelPairs()
        assertEquals(1, pair.size)
        validatePair(1, 14, pair[0])
    }

    @Test
    fun testWiFiChannelPair() {
        validatePair(1, 14, fixture.wiFiChannelPairFirst(Locale.US.country))
        validatePair(1, 14, fixture.wiFiChannelPairFirst(String.EMPTY))
    }

    private fun validatePair(expectedFirst: Int, expectedSecond: Int, pair: WiFiChannelPair) {
        assertEquals(expectedFirst, pair.first.channel)
        assertEquals(expectedSecond, pair.second.channel)
    }

    @Test
    fun testAvailableChannels() {
        assertEquals(11, fixture.availableChannels(Locale.US.country).size)
        assertEquals(13, fixture.availableChannels(Locale.UK.country).size)
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
    fun testWiFiChannelByFrequency2GHZInRange() {
        // setup
        val wiFiChannelPair: WiFiChannelPair = fixture.wiFiChannelPairs()[0]
        // execute
        val actual: WiFiChannel = fixture.wiFiChannelByFrequency(wiFiChannelPair.first.frequency, wiFiChannelPair)
        // validate
        assertEquals(wiFiChannelPair.first, actual)
    }
}