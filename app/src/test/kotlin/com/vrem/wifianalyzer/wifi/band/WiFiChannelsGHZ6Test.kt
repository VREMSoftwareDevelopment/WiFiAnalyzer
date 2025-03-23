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
import java.util.Locale

class WiFiChannelsGHZ6Test {
    private val fixture: WiFiChannelsGHZ6 = WiFiChannelsGHZ6()

    @Test
    fun inRange() {
        assertThat(fixture.inRange(5925)).isTrue()
        assertThat(fixture.inRange(7125)).isTrue()
    }

    @Test
    fun notInRange() {
        assertThat(fixture.inRange(5924)).isFalse()
        assertThat(fixture.inRange(7126)).isFalse()
    }

    @Test
    fun wiFiChannelByFrequency() {
        assertThat(fixture.wiFiChannelByFrequency(5953).channel).isEqualTo(1)
        assertThat(fixture.wiFiChannelByFrequency(5955).channel).isEqualTo(1)
        assertThat(fixture.wiFiChannelByFrequency(5957).channel).isEqualTo(1)
        assertThat(fixture.wiFiChannelByFrequency(6413).channel).isEqualTo(93)
        assertThat(fixture.wiFiChannelByFrequency(6415).channel).isEqualTo(93)
        assertThat(fixture.wiFiChannelByFrequency(6417).channel).isEqualTo(93)
    }

    @Test
    fun wiFiChannelByFrequencyNotFound() {
        assertThat(fixture.wiFiChannelByFrequency(5952)).isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByFrequency(7098)).isEqualTo(WiFiChannel.UNKNOWN)
    }

    @Test
    fun wiFiChannelByChannel() {
        assertThat(fixture.wiFiChannelByChannel(1).frequency).isEqualTo(5955)
        assertThat(fixture.wiFiChannelByChannel(97).frequency).isEqualTo(6435)
    }

    @Test
    fun wiFiChannelByChannelNotFound() {
        assertThat(fixture.wiFiChannelByChannel(0)).isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByChannel(230)).isEqualTo(WiFiChannel.UNKNOWN)
    }

    @Test
    fun wiFiChannelFirst() {
        assertThat(fixture.wiFiChannelFirst().channel).isEqualTo(1)
    }

    @Test
    fun wiFiChannelLast() {
        assertThat(fixture.wiFiChannelLast().channel).isEqualTo(229)
    }

    @Test
    fun availableChannels() {
        assertThat(fixture.availableChannels(Locale.US.country)).hasSize(58)
        assertThat(fixture.availableChannels(Locale.UK.country)).hasSize(58)
    }

    @Test
    fun wiFiChannelByFrequency2GHZ() {
        // setup
        val wiFiChannelPair: WiFiChannelPair = fixture.wiFiChannelPairs().first()
        // execute
        val actual: WiFiChannel = fixture.wiFiChannelByFrequency(2000, wiFiChannelPair)
        // validate
        assertThat(actual).isEqualTo(WiFiChannel.UNKNOWN)
    }

    @Test
    fun wiFiChannelByFrequency6GHZInRange() {
        // setup
        val wiFiChannelPair: WiFiChannelPair = fixture.wiFiChannelPairs().first()
        // execute
        val actual: WiFiChannel =
            fixture.wiFiChannelByFrequency(wiFiChannelPair.first.frequency, wiFiChannelPair)
        // validate
        assertThat(actual).isEqualTo(wiFiChannelPair.first)
    }

    @Test
    fun wiFiChannels() {
        // setup
        val expected = (1..229).toList()
        // execute
        val actual = fixture.wiFiChannels()
        // validate
        assertThat(actual).hasSize(expected.size)
        val actualChannels = actual.map { it.channel }.toList()
        assertThat(actualChannels).hasSize(expected.size).containsAll(expected)
    }

}