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
package com.vrem.wifianalyzer.wifi.channelgraph

import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannels
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y
import com.vrem.wifianalyzer.wifi.graphutils.MIN_Y
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ChannelAxisLabelTest {
    private val fixture = ChannelAxisLabel(WiFiBand.GHZ2)

    @Test
    fun yAxis() {
        // execute & verify
        assertThat(fixture.formatLabel(MIN_Y.toDouble(), false)).isEqualTo(String.EMPTY)
        assertThat(fixture.formatLabel(MIN_Y + 1.toDouble(), false)).isEqualTo("-99")
        assertThat(fixture.formatLabel(MAX_Y.toDouble(), false)).isEqualTo("0")
        assertThat(fixture.formatLabel(MAX_Y + 1.toDouble(), false)).isEqualTo(String.EMPTY)
    }

    @Test
    fun xAxis() {
        // setup
        val (channel, frequency) = WiFiBand.GHZ2.wiFiChannels.wiFiChannelFirst()
        // execute
        val actual = fixture.formatLabel(frequency.toDouble(), true)
        // validate
        assertThat(actual).isEqualTo("" + channel)
    }

    @Test
    fun xAxisWithFrequencyInRange() {
        // setup
        val (channel, frequency) = WiFiBand.GHZ2.wiFiChannels.wiFiChannelFirst()
        // execute & validate
        assertThat(fixture.formatLabel(frequency - 2.toDouble(), true)).isEqualTo("" + channel)
        assertThat(fixture.formatLabel(frequency + 2.toDouble(), true)).isEqualTo("" + channel)
    }

    @Test
    fun xAxisWithFrequencyNotAllowedInLocale() {
        // setup
        val (_, frequency) = WiFiBand.GHZ2.wiFiChannels.wiFiChannelLast()
        // execute
        val actual = fixture.formatLabel(frequency.toDouble() + WiFiChannels.FREQUENCY_OFFSET.toDouble(), true)
        // validate
        assertThat(actual).isEqualTo(String.EMPTY)
    }

    @Test
    fun xAxisWithUnknownFrequencyReturnEmptyString() {
        // setup
        val wiFiChannels = WiFiBand.GHZ2.wiFiChannels
        val (_, frequency) = wiFiChannels.wiFiChannelFirst()
        // execute
        val actual = fixture.formatLabel(frequency - WiFiChannels.FREQUENCY_OFFSET.toDouble(), true)
        // validate
        assertThat(actual).isEqualTo(String.EMPTY)
    }
}