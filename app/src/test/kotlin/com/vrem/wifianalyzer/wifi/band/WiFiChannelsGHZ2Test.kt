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
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.Locale

class WiFiChannelsGHZ2Test {
    private val fixture: WiFiChannelsGHZ2 = WiFiChannelsGHZ2()

    @Test
    fun inRange() {
        assertThat(fixture.inRange(2400)).isTrue()
        assertThat(fixture.inRange(2499)).isTrue()
    }

    @Test
    fun notInRange() {
        assertThat(fixture.inRange(2399)).isFalse()
        assertThat(fixture.inRange(2500)).isFalse()
    }

    @Test
    fun wiFiChannelByFrequency() {
        assertThat(fixture.wiFiChannelByFrequency(2410).channel).isEqualTo(1)
        assertThat(fixture.wiFiChannelByFrequency(2412).channel).isEqualTo(1)
        assertThat(fixture.wiFiChannelByFrequency(2414).channel).isEqualTo(1)
        assertThat(fixture.wiFiChannelByFrequency(2437).channel).isEqualTo(6)
        assertThat(fixture.wiFiChannelByFrequency(2442).channel).isEqualTo(7)
        assertThat(fixture.wiFiChannelByFrequency(2470).channel).isEqualTo(13)
        assertThat(fixture.wiFiChannelByFrequency(2472).channel).isEqualTo(13)
        assertThat(fixture.wiFiChannelByFrequency(2474).channel).isEqualTo(13)
        assertThat(fixture.wiFiChannelByFrequency(2482).channel).isEqualTo(14)
        assertThat(fixture.wiFiChannelByFrequency(2484).channel).isEqualTo(14)
        assertThat(fixture.wiFiChannelByFrequency(2486).channel).isEqualTo(14)
    }

    @Test
    fun wiFiChannelByFrequencyNotFound() {
        assertThat(fixture.wiFiChannelByFrequency(2399)).isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByFrequency(2409)).isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByFrequency(2481)).isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByFrequency(2481)).isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByFrequency(2487)).isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByFrequency(2500)).isEqualTo(WiFiChannel.UNKNOWN)
    }

    @Test
    fun wiFiChannelByChannel() {
        assertThat(fixture.wiFiChannelByChannel(1).frequency).isEqualTo(2412)
        assertThat(fixture.wiFiChannelByChannel(6).frequency).isEqualTo(2437)
        assertThat(fixture.wiFiChannelByChannel(7).frequency).isEqualTo(2442)
        assertThat(fixture.wiFiChannelByChannel(13).frequency).isEqualTo(2472)
        assertThat(fixture.wiFiChannelByChannel(14).frequency).isEqualTo(2484)
    }

    @Test
    fun wiFiChannelByChannelNotFound() {
        assertThat(fixture.wiFiChannelByChannel(0)).isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByChannel(15)).isEqualTo(WiFiChannel.UNKNOWN)
    }

    @Test
    fun wiFiChannelFirst() {
        assertThat(fixture.wiFiChannelFirst().channel).isEqualTo(1)
    }

    @Test
    fun wiFiChannelLast() {
        assertThat(fixture.wiFiChannelLast().channel).isEqualTo(14)
    }

    @Test
    fun wiFiChannelPairs() {
        val pair: List<WiFiChannelPair> = fixture.wiFiChannelPairs()
        assertThat(pair).hasSize(1)
        validatePair(pair[0])
    }

    @Test
    fun wiFiChannelPair() {
        validatePair(fixture.wiFiChannelPairFirst(Locale.US.country))
        validatePair(fixture.wiFiChannelPairFirst(String.EMPTY))
    }

    private fun validatePair(pair: WiFiChannelPair) {
        assertThat(pair.first.channel).isEqualTo(1)
        assertThat(pair.second.channel).isEqualTo(14)
    }

    @Test
    fun availableChannels() {
        assertThat(fixture.availableChannels(Locale.US.country)).hasSize(11)
        assertThat(fixture.availableChannels(Locale.UK.country)).hasSize(13)
    }

    @Test
    fun wiFiChannelByFrequency2GHZ() {
        // setup
        val wiFiChannelPair: WiFiChannelPair = fixture.wiFiChannelPairs()[0]
        // execute
        val actual: WiFiChannel = fixture.wiFiChannelByFrequency(2000, wiFiChannelPair)
        // validate
        assertThat(actual).isEqualTo(WiFiChannel.UNKNOWN)
    }

    @Test
    fun wiFiChannelByFrequency2GHZInRange() {
        // setup
        val wiFiChannelPair: WiFiChannelPair = fixture.wiFiChannelPairs()[0]
        // execute
        val actual: WiFiChannel = fixture.wiFiChannelByFrequency(wiFiChannelPair.first.frequency, wiFiChannelPair)
        // validate
        assertThat(actual).isEqualTo(wiFiChannelPair.first)
    }
}