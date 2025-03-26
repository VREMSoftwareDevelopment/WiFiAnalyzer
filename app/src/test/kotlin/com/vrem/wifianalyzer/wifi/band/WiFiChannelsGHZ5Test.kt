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

import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.wifi.band.WiFiChannels.Companion.FREQUENCY_SPREAD
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.Locale

class WiFiChannelsGHZ5Test {
    private val expectedChannels: List<WiFiChannel> = (30..179).map { WiFiChannel(it, 5150 + (it - 30) * FREQUENCY_SPREAD) }
    private val expectedGraphChannels = listOf(42, 58, 74, 90, 106, 122, 138, 156, 171)
    private val fixture: WiFiChannelsGHZ5 = WiFiChannelsGHZ5()

    @Test
    fun inRange() {
        assertThat(fixture.inRange(expectedChannels.first().frequency)).isTrue()
        assertThat(fixture.inRange(expectedChannels.last().frequency)).isTrue()
    }

    @Test
    fun notInRange() {
        assertThat(fixture.inRange(expectedChannels.first().frequency - 1)).isFalse()
        assertThat(fixture.inRange(expectedChannels.last().frequency + 1)).isFalse()
    }

    @Test
    fun wiFiChannelByFrequencyInRange() {
        expectedChannels.forEach { expected ->
            // execute
            val actual = fixture.wiFiChannelByFrequency(expected.frequency)
            // validate
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun wiFiChannelByFrequencyOutOfRange() {
        assertThat(fixture.wiFiChannelByFrequency(expectedChannels.first().frequency - 1)).isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByFrequency(expectedChannels.last().frequency + 1)).isEqualTo(WiFiChannel.UNKNOWN)
    }

    @Test
    fun wiFiChannelByChannelInRange() {
        expectedChannels.forEach { expected ->
            // execute
            val actual = fixture.wiFiChannelByChannel(expected.channel)
            // validate
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun wiFiChannelByChannelNotInRange() {
        assertThat(fixture.wiFiChannelByChannel(expectedChannels.first().channel - 1)).isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByChannel(expectedChannels.last().channel + 1)).isEqualTo(WiFiChannel.UNKNOWN)
    }

    @Test
    fun channelRange() {
        assertThat(fixture.channelRange.first).isEqualTo(expectedChannels.first())
        assertThat(fixture.channelRange.second).isEqualTo(expectedChannels.last())
    }

    @Test
    fun graphChannels() {
        assertThat(fixture.graphChannels).containsAll(expectedGraphChannels)
    }

    @Test
    fun availableChannels() {
        assertThat(fixture.availableChannels(Locale.US.country)).hasSize(28)
        assertThat(fixture.availableChannels(Locale.CANADA.country)).hasSize(22)
        assertThat(fixture.availableChannels(Locale.JAPAN.country)).hasSize(20)
    }

    @Test
    fun wiFiChannels() {
        // execute
        val actual = fixture.wiFiChannels()
        // validate
        assertThat(actual).containsAll(expectedChannels)
    }

    @Test
    fun graphChannelCount() {
        assertThat(fixture.graphChannelCount()).isEqualTo(expectedChannels.size / 2)
    }

    @Test
    fun graphChannelByFrequency() {
        expectedChannels.forEach { it ->
            // setup
            val expected = if (expectedGraphChannels.contains(it.channel)) "${it.channel}" else String.EMPTY
            // execute
            val actual = fixture.graphChannelByFrequency(it.frequency)
            // validate
            assertThat(actual).describedAs("Channel: $it").isEqualTo(expected)
        }
    }

}