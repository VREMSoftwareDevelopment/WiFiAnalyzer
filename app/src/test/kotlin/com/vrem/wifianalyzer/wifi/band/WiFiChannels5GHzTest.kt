/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class WiFiChannels5GHzTest {
    private val fixture: WiFiChannels5GHz = WiFiChannels5GHz()

    @Test
    fun testWiFiChannelByFrequency() {
        validateFrequencyToChannel(5180, 5320, 36)
        validateFrequencyToChannel(5500, 5720, 100)
        validateFrequencyToChannel(5745, 5885, 149)
    }

    private fun validateFrequencyToChannel(frequencyStart: Int, frequencyEnd: Int, channelStart: Int) {
        var channel = channelStart
        var frequency = frequencyStart
        while (frequency <= frequencyEnd) {
            assertEquals(channel, fixture.wiFiChannelByFrequency(frequency).channel)
            channel += 2
            frequency += 10
        }
    }

    @Test
    fun testWiFiChannelByFrequencyFail() {
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(5149))
        assertEquals(WiFiChannel.UNKNOWN, fixture.wiFiChannelByFrequency(5896))
    }

    @Test
    fun testWiFiChannelFirst() {
        assertEquals(36, fixture.wiFiChannelFirst().channel)
    }

    @Test
    fun testWiFiChannelLast() {
        assertEquals(177, fixture.wiFiChannelLast().channel)
    }

    @Test
    fun testWiFiChannelPair() {
        validatePair(36, 64, fixture.wiFiChannelPairFirst(Locale.JAPAN.country))
        validatePair(36, 64, fixture.wiFiChannelPairFirst(String.EMPTY))
        validatePair(36, 64, fixture.wiFiChannelPairFirst("XYZ"))
    }

    @Test
    fun testWiFiChannelPairs() {
        val wiFiChannelPairs: List<WiFiChannelPair> = fixture.wiFiChannelPairs()
        assertEquals(3, wiFiChannelPairs.size)
        validatePair(36, 64, wiFiChannelPairs[0])
        validatePair(100, 144, wiFiChannelPairs[1])
        validatePair(149, 177, wiFiChannelPairs[2])
    }

    private fun validatePair(expectedFirst: Int, expectedSecond: Int, pair: WiFiChannelPair) {
        assertEquals(expectedFirst, pair.first.channel)
        assertEquals(expectedSecond, pair.second.channel)
    }

    @Test
    fun testWiFiChannelByFrequency5GHZ() {
        // setup
        val wiFiChannelPair: WiFiChannelPair = fixture.wiFiChannelPairs()[1]
        // execute
        val actual: WiFiChannel = fixture.wiFiChannelByFrequency(2000, wiFiChannelPair)
        // validate
        assertEquals(WiFiChannel.UNKNOWN, actual)
    }

    @Test
    fun testWiFiChannelByFrequency5GHZInRange() {
        // setup
        val wiFiChannelPair: WiFiChannelPair = fixture.wiFiChannelPairs()[1]
        // execute
        val actual: WiFiChannel = fixture.wiFiChannelByFrequency(wiFiChannelPair.first.frequency, wiFiChannelPair)
        // validate
        assertEquals(wiFiChannelPair.first, actual)
    }
}
