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
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter
import org.junit.runners.Parameterized.Parameters
import kotlin.test.Test

@RunWith(Parameterized::class)
class WiFiChannelsParameterizedTest {
    private val countries: List<WiFiChannelCountry> = WiFiChannelCountry.findAll()
        .filter { !it.locale.country.isEmpty() && !it.locale.displayName.isEmpty() }

    @Parameter(0)
    lateinit var wiFiBand: WiFiBand

    @Parameter(1)
    lateinit var expectedWiFiInfo: ExpectedWiFiInfo

    @Parameter(2)
    lateinit var fixture: WiFiChannels

    @Test
    fun inRange() {
        assertThat(fixture.inRange(expectedWiFiInfo.expectedChannels.first().frequency)).isTrue()
        assertThat(fixture.inRange(expectedWiFiInfo.expectedChannels.last().frequency)).isTrue()
    }

    @Test
    fun notInRange() {
        assertThat(fixture.inRange(expectedWiFiInfo.expectedChannels.first().frequency - 1)).isFalse()
        assertThat(fixture.inRange(expectedWiFiInfo.expectedChannels.last().frequency + 1)).isFalse()
    }

    @Test
    fun wiFiChannelByFrequencyInRange() {
        expectedWiFiInfo.expectedChannels.forEach { expected ->
            assertThat(fixture.wiFiChannelByFrequency(expected.frequency)).isEqualTo(expected)
        }
    }

    @Test
    fun wiFiChannelByFrequencyOutOfRange() {
        assertThat(fixture.wiFiChannelByFrequency(expectedWiFiInfo.expectedChannels.first().frequency - 1))
            .isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByFrequency(expectedWiFiInfo.expectedChannels.last().frequency + 1))
            .isEqualTo(WiFiChannel.UNKNOWN)
    }

    @Test
    fun wiFiChannelByChannelInRange() {
        expectedWiFiInfo.expectedChannels.forEach { expected ->
            assertThat(fixture.wiFiChannelByChannel(expected.channel)).isEqualTo(expected)
        }
    }

    @Test
    fun wiFiChannelByChannelNotInRange() {
        assertThat(fixture.wiFiChannelByChannel(expectedWiFiInfo.expectedChannels.first().channel - 1))
            .isEqualTo(WiFiChannel.UNKNOWN)
        assertThat(fixture.wiFiChannelByChannel(expectedWiFiInfo.expectedChannels.last().channel + 1))
            .isEqualTo(WiFiChannel.UNKNOWN)
    }

    @Test
    fun channelRange() {
        assertThat(fixture.channelRange.first).isEqualTo(expectedWiFiInfo.expectedChannels.first())
        assertThat(fixture.channelRange.second).isEqualTo(expectedWiFiInfo.expectedChannels.last())
    }

    @Test
    fun activeChannels() {
        fixture.activeChannels.forEach { (wiFiWidth, wiFiWidthChannels) ->
            val expected = expectedWiFiInfo.expectedActiveChannels
                .filter { (expectedWiFiWidth) -> expectedWiFiWidth == wiFiWidth }
                .map { (_, expectedWiFiWidthChannels) ->
                    expectedWiFiWidthChannels
                }
                .first()
            assertThat(wiFiWidthChannels)
                .describedAs("$wiFiWidth")
                .containsExactlyElementsOf(expected)
        }
    }

    @Test
    fun wiFiWidthUsingChannelInRange() {
        fixture.activeChannels.forEach { (wiFiWidth, channels) ->
            if (wiFiBand == WiFiBand.GHZ2 && wiFiWidth == WiFiWidth.MHZ_40) {
                return@forEach
            }
            channels.forEach { channel ->
                assertThat(fixture.wiFiWidthByChannel(channel))
                    .describedAs("$wiFiWidth | Channel: $channel")
                    .isEqualTo(wiFiWidth)
            }
        }
    }

    @Test
    fun wiFiWidthUsingChannelNotInRange() {
        assertThat(fixture.wiFiWidthByChannel(wiFiBand.wiFiChannels.channelRange.first.channel - 1))
            .isEqualTo(WiFiWidth.MHZ_20)
        assertThat(fixture.wiFiWidthByChannel(wiFiBand.wiFiChannels.channelRange.second.channel + 1))
            .isEqualTo(WiFiWidth.MHZ_20)
    }

    @Test
    fun availableChannels() {
        assertThat(fixture.availableChannels).containsExactlyElementsOf(expectedWiFiInfo.expectedAvailableChannels)
    }

    @Test
    fun graphChannels() {
        assertThat(fixture.graphChannels.keys).containsExactlyElementsOf(expectedWiFiInfo.expectedGraphChannels.keys)
        assertThat(fixture.graphChannels.values).containsExactlyElementsOf(expectedWiFiInfo.expectedGraphChannels.values)
    }

    @Test
    fun availableChannelsUsingWiFiBandAndCountry() {
        countries.forEach { country ->
            assertThat(fixture.availableChannels(wiFiBand, country.countryCode).map { it -> it.channel })
                .describedAs("Country: ${country.countryCode}")
                .containsExactlyElementsOf(country.channels(wiFiBand))
        }
    }

    @Test
    fun availableChannelsUsingWiFiWidthWiFiBandAndCountry() {
        WiFiWidth.entries.forEach { wiFiWidth ->
            countries.forEach { country ->
                assertThat(fixture.availableChannels(wiFiWidth, wiFiBand, country.countryCode))
                    .describedAs("$wiFiWidth | Country: $country")
                    .containsExactlyElementsOf(expectedWiFiInfo.availableChannels(wiFiWidth, wiFiBand, country.countryCode))
            }
        }
    }

    @Test
    fun ratingChannels() {
        countries.forEach { country ->
            assertThat(fixture.ratingChannels(wiFiBand, country.countryCode))
                .describedAs("$wiFiBand | Country: ${country.countryCode}")
                .containsExactlyElementsOf(expectedWiFiInfo.expectedRatingChannels(wiFiBand, country.countryCode))
        }
    }

    @Test
    fun wiFiChannels() {
        assertThat(fixture.wiFiChannels()).containsAll(expectedWiFiInfo.expectedChannels)
    }

    @Test
    fun graphChannelCount() {
        assertThat(fixture.graphChannelCount()).isEqualTo(expectedWiFiInfo.expectedChannelsCount)
    }

    @Test
    fun graphChannelByFrequencyInRange() {
        expectedWiFiInfo.expectedChannels.forEach { (channel, frequency) ->
            assertThat(fixture.graphChannelByFrequency(frequency))
                .describedAs("Channel: $channel | Frequency: $frequency")
                .isEqualTo(expectedWiFiInfo.expectedGraphChannels[channel] ?: String.EMPTY)
        }
    }

    @Test
    fun graphChannelByFrequencyOutOfRange() {
        assertThat(fixture.graphChannelByFrequency(expectedWiFiInfo.expectedChannels.first().frequency - 1)).isEmpty()
        assertThat(fixture.graphChannelByFrequency(expectedWiFiInfo.expectedChannels.last().frequency + 1)).isEmpty()
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0}")
        fun data() = listOf(
            arrayOf(WiFiBand.GHZ2, expectedWiFiInfoGHZ2, wiFiChannelsGHZ2),
            arrayOf(WiFiBand.GHZ5, expectedWiFiInfoGHZ5, wiFiChannelsGHZ5),
            arrayOf(WiFiBand.GHZ6, expectedWiFiInfoGHZ6, wiFiChannelsGHZ6)
        )
    }

}
