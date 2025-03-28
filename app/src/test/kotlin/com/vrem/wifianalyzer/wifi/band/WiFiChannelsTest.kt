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
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import java.util.Locale
import kotlin.test.Test

class WiFiChannelsTest {
    private val currentLocale: Locale = Locale.getDefault()

    data class TestData(
        val wiFiBand: WiFiBand,
        val expectedChannels: List<WiFiChannel>,
        val expectedChannelsCount: Int,
        val expectedGraphChannels: Map<Int, String>,
    )

    private val testData = listOf(
        TestData(
            WiFiBand.GHZ2,
            (-1..15).map { WiFiChannel(it, 2407 + it * FREQUENCY_SPREAD) },
            17,
            (1..13).associateWith { "$it" }
        ),
        TestData(
            WiFiBand.GHZ5,
            (30..184).map { WiFiChannel(it, 5150 + (it - 30) * FREQUENCY_SPREAD) },
            77,
            listOf(34, 50, 66, 82, 98, 113, 129, 147, 163, 178).associateWith {
                when (it) {
                    113 -> "114"
                    129 -> "130"
                    178 -> "179"
                    else -> "$it"
                }
            }
        ),
        TestData(
            WiFiBand.GHZ6,
            (-5..235).map { WiFiChannel(it, 5950 + it * FREQUENCY_SPREAD) },
            120,
            listOf(1, 31, 63, 95, 128, 160, 192, 222).associateWith {
                when (it) {
                    128 -> "127"
                    160 -> "159"
                    192 -> "191"
                    222 -> "223"
                    else -> "$it"
                }
            }
        )
    )

    @Before
    fun setUp() {
        Locale.setDefault(Locale.US)
    }

    @After
    fun tearDown() {
        Locale.setDefault(currentLocale)
    }

    @Test
    fun inRange() {
        testData.forEach { (wiFiBand, expectedChannels) ->
            assertThat(wiFiBand.wiFiChannels.inRange(expectedChannels.first().frequency)).describedAs(wiFiBand.name).isTrue()
            assertThat(wiFiBand.wiFiChannels.inRange(expectedChannels.last().frequency)).describedAs(wiFiBand.name).isTrue()
        }
    }

    @Test
    fun notInRange() {
        testData.forEach { (wiFiBand, expectedChannels) ->
            assertThat(wiFiBand.wiFiChannels.inRange(expectedChannels.first().frequency - 1)).describedAs(wiFiBand.name).isFalse()
            assertThat(wiFiBand.wiFiChannels.inRange(expectedChannels.last().frequency + 1)).describedAs(wiFiBand.name).isFalse()
        }
    }

    @Test
    fun wiFiChannelByFrequencyInRange() {
        testData.forEach { (wiFiBand, expectedChannels) ->
            expectedChannels.forEach { expected ->
                assertThat(wiFiBand.wiFiChannels.wiFiChannelByFrequency(expected.frequency)).describedAs(wiFiBand.name).isEqualTo(expected)
            }
        }
    }

    @Test
    fun wiFiChannelByFrequencyOutOfRange() {
        testData.forEach { (wiFiBand, expectedChannels) ->
            assertThat(wiFiBand.wiFiChannels.wiFiChannelByFrequency(expectedChannels.first().frequency - 1)).describedAs(wiFiBand.name)
                .isEqualTo(WiFiChannel.UNKNOWN)
            assertThat(wiFiBand.wiFiChannels.wiFiChannelByFrequency(expectedChannels.last().frequency + 1)).describedAs(wiFiBand.name)
                .isEqualTo(WiFiChannel.UNKNOWN)
        }
    }

    @Test
    fun wiFiChannelByChannelInRange() {
        testData.forEach { (wiFiBand, expectedChannels) ->
            expectedChannels.forEach { expected ->
                assertThat(wiFiBand.wiFiChannels.wiFiChannelByChannel(expected.channel)).describedAs(wiFiBand.name).isEqualTo(expected)
            }
        }
    }

    @Test
    fun wiFiChannelByChannelNotInRange() {
        testData.forEach { (wiFiBand, expectedChannels) ->
            assertThat(wiFiBand.wiFiChannels.wiFiChannelByChannel(expectedChannels.first().channel - 1)).describedAs(wiFiBand.name)
                .isEqualTo(WiFiChannel.UNKNOWN)
            assertThat(wiFiBand.wiFiChannels.wiFiChannelByChannel(expectedChannels.last().channel + 1)).describedAs(wiFiBand.name)
                .isEqualTo(WiFiChannel.UNKNOWN)
        }
    }

    @Test
    fun channelRange() {
        testData.forEach { (wiFiBand, expectedChannels) ->
            assertThat(wiFiBand.wiFiChannels.channelRange.first).describedAs(wiFiBand.name).isEqualTo(expectedChannels.first())
            assertThat(wiFiBand.wiFiChannels.channelRange.second).describedAs(wiFiBand.name).isEqualTo(expectedChannels.last())
        }
    }

    @Test
    fun graphChannels() {
        testData.forEach { (wiFiBand, _, _, expectedGraphChannels) ->
            assertThat(wiFiBand.wiFiChannels.graphChannels.keys).describedAs(wiFiBand.name)
                .containsExactlyElementsOf(expectedGraphChannels.keys)
            assertThat(wiFiBand.wiFiChannels.graphChannels.values).describedAs(wiFiBand.name)
                .containsExactlyElementsOf(expectedGraphChannels.values)
        }
    }

    @Test
    fun availableChannels() {
        Locale.getAvailableLocales().forEach { locale ->
            testData.forEach { (wiFiBand) ->
                // setup
                val expected = WiFiChannelCountry(locale).channels(wiFiBand)
                // execute
                val actual = wiFiBand.wiFiChannels.availableChannels(wiFiBand, locale.country)
                // validate
                assertThat(actual.map { it -> it.channel }).describedAs(wiFiBand.name).containsExactlyElementsOf(expected)
            }
        }
    }

    @Test
    fun wiFiChannels() {
        testData.forEach { (wiFiBand, expectedChannels) ->
            assertThat(wiFiBand.wiFiChannels.wiFiChannels()).describedAs(wiFiBand.name).containsAll(expectedChannels)
        }
    }

    @Test
    fun graphChannelCount() {
        testData.forEach { (wiFiBand, _, expectedChannelsCount) ->
            assertThat(wiFiBand.wiFiChannels.graphChannelCount()).describedAs(wiFiBand.name).isEqualTo(expectedChannelsCount)
        }
    }

    @Test
    fun graphChannelByFrequencyInRange() {
        testData.forEach { (wiFiBand, expectedChannels, _, expectedGraphChannels) ->
            expectedChannels.forEach { (channel, frequency) ->
                assertThat(wiFiBand.wiFiChannels.graphChannelByFrequency(frequency)).describedAs("$wiFiBand.name | Channel: $channel | Frequency: $frequency")
                    .isEqualTo(expectedGraphChannels[channel] ?: String.EMPTY)
            }
        }
    }

    @Test
    fun graphChannelByFrequencyOutOfRange() {
        testData.forEach { (wiFiBand, expectedChannels) ->
            assertThat(wiFiBand.wiFiChannels.graphChannelByFrequency(expectedChannels.first().frequency - 1)).describedAs(wiFiBand.name)
                .isEmpty()
            assertThat(wiFiBand.wiFiChannels.graphChannelByFrequency(expectedChannels.last().frequency + 1)).describedAs(wiFiBand.name)
                .isEmpty()
        }
    }
}