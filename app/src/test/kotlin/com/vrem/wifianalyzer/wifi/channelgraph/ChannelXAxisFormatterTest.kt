/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.patrykandpatrick.vico.views.cartesian.CartesianMeasuringContext
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.mock

class ChannelXAxisFormatterTest {
    private val context: CartesianMeasuringContext = mock()
    private val fixture = channelXAxisFormatter(WiFiBand.GHZ2)

    @Test
    fun formatLabelWithFirstFrequencyInRange() {
        // setup
        val frequency = WiFiBand.GHZ2.wiFiChannels.channelRange.first.frequency
        // execute
        val actual = fixture.format(context, frequency + 10.0, null)
        // validate
        assertThat(actual).isEqualTo("1")
    }

    @Test
    fun formatLabelWithLastFrequencyInRange() {
        // setup
        val frequency = WiFiBand.GHZ2.wiFiChannels.channelRange.second.frequency
        // execute
        val actual = fixture.format(context, frequency - 10.0, null)
        // validate
        assertThat(actual).isEqualTo("13")
    }

    @Test
    fun formatLabelWithFirstFrequencyNotInRange() {
        // setup
        val frequency = WiFiBand.GHZ2.wiFiChannels.channelRange.first.frequency
        // execute
        val actual = fixture.format(context, frequency.toDouble(), null)
        // validate
        assertThat(actual).isEqualTo("\u200B")
    }

    @Test
    fun formatLabelWithNegativeValue() {
        // execute
        val actual = fixture.format(context, -1.0, null)
        // validate
        assertThat(actual).isEqualTo("\u200B")
    }

    @Test
    fun formatLabelWithLastFrequencyNotInRange() {
        // setup
        val frequency = WiFiBand.GHZ2.wiFiChannels.channelRange.first.frequency
        // execute
        val actual = fixture.format(context, frequency.toDouble(), null)
        // validate
        assertThat(actual).isEqualTo("\u200B")
    }
}
