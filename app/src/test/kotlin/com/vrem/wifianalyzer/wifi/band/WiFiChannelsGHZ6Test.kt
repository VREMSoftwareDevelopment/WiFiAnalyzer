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
        assertThat(fixture.wiFiChannelByFrequency(6418)).isEqualTo(WiFiChannel.UNKNOWN)
    }

    @Test
    fun wiFiChannelByChannel() {
        assertThat(fixture.wiFiChannelByChannel(1).frequency).isEqualTo(5955)
        assertThat(fixture.wiFiChannelByChannel(93).frequency).isEqualTo(6415)
    }

    @Test
    fun wiFiChannelByChannelNotFound() {
        assertThat(fixture.wiFiChannelByChannel(0)).isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByChannel(94)).isEqualTo(WiFiChannel.UNKNOWN)
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
    fun wiFiChannelPair() {
        validatePair(1, 29, fixture.wiFiChannelPairFirst(Locale.US.country))
        validatePair(1, 29, fixture.wiFiChannelPairFirst(String.EMPTY))
        validatePair(1, 29, fixture.wiFiChannelPairFirst("XYZ"))
    }

    @Test
    fun wiFiChannelPairs() {
        val wiFiChannelPairs: List<WiFiChannelPair> = fixture.wiFiChannelPairs()
        assertThat(wiFiChannelPairs).hasSize(7)
        validatePair(1, 29, wiFiChannelPairs[0])
        validatePair(33, 61, wiFiChannelPairs[1])
        validatePair(65, 93, wiFiChannelPairs[2])
        validatePair(97, 125, wiFiChannelPairs[3])
        validatePair(129, 157, wiFiChannelPairs[4])
        validatePair(161, 189, wiFiChannelPairs[5])
        validatePair(193, 229, wiFiChannelPairs[6])
    }

    private fun validatePair(expectedFirst: Int, expectedSecond: Int, pair: WiFiChannelPair) {
        assertThat(pair.first.channel).isEqualTo(expectedFirst)
        assertThat(pair.second.channel).isEqualTo(expectedSecond)
    }

    @Test
    fun availableChannels() {
        assertThat(fixture.availableChannels(Locale.US.country)).hasSize(58)
        assertThat(fixture.availableChannels(Locale.UK.country)).hasSize(58)
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
    fun wiFiChannelByFrequency6GHZInRange() {
        // setup
        val wiFiChannelPair: WiFiChannelPair = fixture.wiFiChannelPairs()[0]
        // execute
        val actual: WiFiChannel =
            fixture.wiFiChannelByFrequency(wiFiChannelPair.first.frequency, wiFiChannelPair)
        // validate
        assertThat(actual).isEqualTo(wiFiChannelPair.first)
    }
}