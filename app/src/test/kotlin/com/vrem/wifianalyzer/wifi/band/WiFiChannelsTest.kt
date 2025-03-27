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
import java.util.Locale
import kotlin.test.Test

class WiFiChannelsTest {

    data class TestData(
        val tag: String,
        val fixture: WiFiChannels,
        val expectedChannels: List<WiFiChannel>,
        val expectedChannelsCount: Int,
        val expectedGraphChannels: Map<Int, String>,
        val countryChannelCount: List<Pair<Locale, Int>>
    )

    private val testData = listOf(
        TestData(
            "2.4 GHz",
            WiFiChannelsGHZ2(),
            (-1..15).map { WiFiChannel(it, 2407 + it * FREQUENCY_SPREAD) },
            17,
            (1..13).associateWith { "$it" },
            listOf(Locale.US to 11, Locale.UK to 13)
        ),
        TestData(
            "5 GHz",
            WiFiChannelsGHZ5(),
            (30..179).map { WiFiChannel(it, 5150 + (it - 30) * FREQUENCY_SPREAD) },
            75,
            listOf(42, 58, 74, 90, 106, 122, 138, 156, 171).associateWith {
                if (it == 156) "155" else "$it"
            },
            listOf(Locale.US to 28, Locale.CANADA to 22, Locale.JAPAN to 20)
        ),
        TestData(
            "6 GHz",
            WiFiChannelsGHZ6(),
            (-5..235).map { WiFiChannel(it, 5950 + it * FREQUENCY_SPREAD) },
            120,
            listOf(15, 47, 79, 110, 142, 174, 208).associateWith {
                when (it) {
                    110 -> "111"
                    142 -> "143"
                    174 -> "175"
                    208 -> "207"
                    else -> "$it"
                }
            },
            listOf(Locale.US to 58, Locale.UK to 58)
        )
    )

    @Test
    fun inRange() {
        testData.forEach { (tag, fixture, expectedChannels) ->
            assertThat(fixture.inRange(expectedChannels.first().frequency)).describedAs(tag).isTrue()
            assertThat(fixture.inRange(expectedChannels.last().frequency)).describedAs(tag).isTrue()
        }
    }

    @Test
    fun notInRange() {
        testData.forEach { (tag, fixture, expectedChannels) ->
            assertThat(fixture.inRange(expectedChannels.first().frequency - 1)).describedAs(tag).isFalse()
            assertThat(fixture.inRange(expectedChannels.last().frequency + 1)).describedAs(tag).isFalse()
        }
    }

    @Test
    fun wiFiChannelByFrequencyInRange() {
        testData.forEach { (tag, fixture, expectedChannels) ->
            expectedChannels.forEach { expected ->
                assertThat(fixture.wiFiChannelByFrequency(expected.frequency)).describedAs(tag).isEqualTo(expected)
            }
        }
    }

    @Test
    fun wiFiChannelByFrequencyOutOfRange() {
        testData.forEach { (tag, fixture, expectedChannels) ->
            assertThat(fixture.wiFiChannelByFrequency(expectedChannels.first().frequency - 1)).describedAs(tag)
                .isEqualTo(WiFiChannel.UNKNOWN)
            assertThat(fixture.wiFiChannelByFrequency(expectedChannels.last().frequency + 1)).describedAs(tag)
                .isEqualTo(WiFiChannel.UNKNOWN)
        }
    }

    @Test
    fun wiFiChannelByChannelInRange() {
        testData.forEach { (tag, fixture, expectedChannels) ->
            expectedChannels.forEach { expected ->
                assertThat(fixture.wiFiChannelByChannel(expected.channel)).describedAs(tag).isEqualTo(expected)
            }
        }
    }

    @Test
    fun wiFiChannelByChannelNotInRange() {
        testData.forEach { (tag, fixture, expectedChannels) ->
            assertThat(fixture.wiFiChannelByChannel(expectedChannels.first().channel - 1)).describedAs(tag)
                .isEqualTo(WiFiChannel.UNKNOWN)
            assertThat(fixture.wiFiChannelByChannel(expectedChannels.last().channel + 1)).describedAs(tag)
                .isEqualTo(WiFiChannel.UNKNOWN)
        }
    }

    @Test
    fun channelRange() {
        testData.forEach { (tag, fixture, expectedChannels) ->
            assertThat(fixture.channelRange.first).describedAs(tag).isEqualTo(expectedChannels.first())
            assertThat(fixture.channelRange.second).describedAs(tag).isEqualTo(expectedChannels.last())
        }
    }

    @Test
    fun graphChannels() {
        testData.forEach { (tag, fixture, _, _, expectedGraphChannels) ->
            assertThat(fixture.graphChannels.keys).describedAs(tag).containsExactlyElementsOf(expectedGraphChannels.keys)
            assertThat(fixture.graphChannels.values).describedAs(tag).containsExactlyElementsOf(expectedGraphChannels.values)
        }
    }

    @Test
    fun availableChannels() {
        testData.forEach { (tag, fixture, _, _, _, countryChannelCount) ->
            countryChannelCount.forEach { (locale, count) ->
                assertThat(fixture.availableChannels(locale.country)).describedAs(tag).hasSize(count)
            }
        }
    }

    @Test
    fun wiFiChannels() {
        testData.forEach { (tag, fixture, expectedChannels) ->
            assertThat(fixture.wiFiChannels()).describedAs(tag).containsAll(expectedChannels)
        }
    }

    @Test
    fun graphChannelCount() {
        testData.forEach { (tag, fixture, _, expectedChannelsCount) ->
            assertThat(fixture.graphChannelCount()).describedAs(tag).isEqualTo(expectedChannelsCount)
        }
    }

    @Test
    fun graphChannelByFrequencyInRange() {
        testData.forEach { (tag, fixture, expectedChannels, _, expectedGraphChannels) ->
            expectedChannels.forEach { (channel, frequency) ->
                assertThat(fixture.graphChannelByFrequency(frequency)).describedAs("$tag | Channel: $channel | Frequency: $frequency")
                    .isEqualTo(expectedGraphChannels[channel] ?: String.EMPTY)
            }
        }
    }

    @Test
    fun graphChannelByFrequencyOutOfRange() {
        testData.forEach { (tag, fixture, expectedChannels) ->
            assertThat(fixture.graphChannelByFrequency(expectedChannels.first().frequency - 1)).describedAs(tag).isEmpty()
            assertThat(fixture.graphChannelByFrequency(expectedChannels.last().frequency + 1)).describedAs(tag).isEmpty()
        }
    }
}