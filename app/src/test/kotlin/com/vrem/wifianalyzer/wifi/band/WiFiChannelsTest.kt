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
import org.junit.After
import org.junit.Before
import java.util.Locale
import kotlin.test.Test

class WiFiChannelsTest {
    private val currentLocale: Locale = Locale.getDefault()

    private data class TestData(
        val wiFiBand: WiFiBand,
        val fixture: WiFiChannels,
        val expectedChannels: List<WiFiChannel>,
        val expectedChannelsCount: Int,
        val expectedActiveChannels: Map<WiFiWidth, List<Int>>,
        val expectedGraphChannels: Map<Int, String>,
        val expectedAvailableChannels: List<Int>
    )

    private val testData = listOf(
        TestData(
            WiFiBand.GHZ2,
            wiFiChannelsGHZ2,
            (-1..15).map { WiFiChannel(it, 2407 + it * FREQUENCY_SPREAD) },
            17,
            mapOf(
                (WiFiWidth.MHZ_20 to listOf(1, 2, 3, 6, 7, 8, 11, 12, 13)),
                (WiFiWidth.MHZ_40 to listOf(3, 7, 11))
            ),
            (1..13).associateWith { "$it" },
            listOf(1, 2, 3, 6, 7, 8, 11, 12, 13)
        ),
        TestData(
            WiFiBand.GHZ5,
            wiFiChannelsGHZ5,
            (30..184).map { WiFiChannel(it, 5150 + (it - 30) * FREQUENCY_SPREAD) },
            77,
            mapOf(
                (WiFiWidth.MHZ_20 to ((32..144 step WiFiWidth.MHZ_20.step) + (149..177 step WiFiWidth.MHZ_20.step))),
                (WiFiWidth.MHZ_40 to ((38..142 step WiFiWidth.MHZ_40.step) + (151..175 step WiFiWidth.MHZ_40.step))),
                (WiFiWidth.MHZ_80 to ((42..138 step WiFiWidth.MHZ_80.step) + (155..171 step WiFiWidth.MHZ_80.step))),
                (WiFiWidth.MHZ_160 to ((50..114 step WiFiWidth.MHZ_160.step) + 163))
            ),
            listOf(34, 50, 66, 82, 98, 113, 129, 147, 163, 178).associateWith {
                when (it) {
                    113 -> "114"
                    129 -> "130"
                    178 -> "179"
                    else -> "$it"
                }
            },
            ((42..138 step WiFiWidth.MHZ_80.step) + (155..171 step WiFiWidth.MHZ_80.step) +
                    (50..114 step WiFiWidth.MHZ_160.step) + 163)
                .toSortedSet()
                .toList()
        ),
        TestData(
            WiFiBand.GHZ6,
            wiFiChannelsGHZ6,
            (-5..235).map { WiFiChannel(it, 5950 + it * FREQUENCY_SPREAD) },
            120,
            mapOf(
                (WiFiWidth.MHZ_20 to (1..233 step WiFiWidth.MHZ_20.step).toList()),
                (WiFiWidth.MHZ_40 to (3..227 step WiFiWidth.MHZ_40.step).toList()),
                (WiFiWidth.MHZ_80 to (7..215 step WiFiWidth.MHZ_80.step).toList()),
                (WiFiWidth.MHZ_160 to (15..207 step WiFiWidth.MHZ_160.step).toList()),
                (WiFiWidth.MHZ_320 to (31..191 step WiFiWidth.MHZ_320.step).toList())
            ),
            listOf(1, 31, 63, 95, 128, 160, 192, 222).associateWith {
                when (it) {
                    128 -> "127"
                    160 -> "159"
                    192 -> "191"
                    222 -> "223"
                    else -> "$it"
                }
            },
            ((15..207 step WiFiWidth.MHZ_160.step) + (31..191 step WiFiWidth.MHZ_320.step))
                .toSortedSet()
                .toList(),

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
        testData.forEach { (wiFiBand, fixture, expectedChannels) ->
            assertThat(fixture.inRange(expectedChannels.first().frequency)).describedAs(wiFiBand.name).isTrue()
            assertThat(fixture.inRange(expectedChannels.last().frequency)).describedAs(wiFiBand.name).isTrue()
        }
    }

    @Test
    fun notInRange() {
        testData.forEach { (wiFiBand, fixture, expectedChannels) ->
            assertThat(fixture.inRange(expectedChannels.first().frequency - 1))
                .describedAs(wiFiBand.name)
                .isFalse()
            assertThat(fixture.inRange(expectedChannels.last().frequency + 1))
                .describedAs(wiFiBand.name)
                .isFalse()
        }
    }

    @Test
    fun wiFiChannelByFrequencyInRange() {
        testData.forEach { (wiFiBand, fixture, expectedChannels) ->
            expectedChannels.forEach { expected ->
                assertThat(fixture.wiFiChannelByFrequency(expected.frequency))
                    .describedAs(wiFiBand.name)
                    .isEqualTo(expected)
            }
        }
    }

    @Test
    fun wiFiChannelByFrequencyOutOfRange() {
        testData.forEach { (wiFiBand, fixture, expectedChannels) ->
            assertThat(fixture.wiFiChannelByFrequency(expectedChannels.first().frequency - 1))
                .describedAs(wiFiBand.name)
                .isEqualTo(WiFiChannel.UNKNOWN)
            assertThat(fixture.wiFiChannelByFrequency(expectedChannels.last().frequency + 1))
                .describedAs(wiFiBand.name)
                .isEqualTo(WiFiChannel.UNKNOWN)
        }
    }

    @Test
    fun wiFiChannelByChannelInRange() {
        testData.forEach { (wiFiBand, fixture, expectedChannels) ->
            expectedChannels.forEach { expected ->
                assertThat(fixture.wiFiChannelByChannel(expected.channel))
                    .describedAs(wiFiBand.name)
                    .isEqualTo(expected)
            }
        }
    }

    @Test
    fun wiFiChannelByChannelNotInRange() {
        testData.forEach { (wiFiBand, fixture, expectedChannels) ->
            assertThat(fixture.wiFiChannelByChannel(expectedChannels.first().channel - 1))
                .describedAs(wiFiBand.name)
                .isEqualTo(WiFiChannel.UNKNOWN)
            assertThat(fixture.wiFiChannelByChannel(expectedChannels.last().channel + 1))
                .describedAs(wiFiBand.name)
                .isEqualTo(WiFiChannel.UNKNOWN)
        }
    }

    @Test
    fun channelRange() {
        testData.forEach { (wiFiBand, fixture, expectedChannels) ->
            assertThat(fixture.channelRange.first)
                .describedAs(wiFiBand.name)
                .isEqualTo(expectedChannels.first())
            assertThat(fixture.channelRange.second)
                .describedAs(wiFiBand.name)
                .isEqualTo(expectedChannels.last())
        }
    }

    @Test
    fun activeChannels() {
        testData.forEach { (wiFiBand, fixture, _, _, expectedActiveChannels) ->
            fixture.activeChannels.forEach { (wiFiWidth, wiFiWidthChannels) ->
                val expected = expectedActiveChannels
                    .filter { (expectedWiFiWidth) -> expectedWiFiWidth == wiFiWidth }
                    .map { (_, expectedWiFiWidthChannels) ->
                        expectedWiFiWidthChannels
                    }
                    .first()
                assertThat(wiFiWidthChannels)
                    .describedAs("$wiFiBand $wiFiWidth")
                    .containsExactlyElementsOf(expected)
            }
        }
    }

    @Test
    fun availableChannels() {
        testData.forEach { (wiFiBand, fixture, _, _, _, _, expectedAvailableChannels) ->
            assertThat(fixture.availableChannels)
                .describedAs("$wiFiBand")
                .containsExactlyElementsOf(expectedAvailableChannels)
        }
    }

    @Test
    fun graphChannels() {
        testData.forEach { (wiFiBand, fixture, _, _, _, expectedGraphChannels) ->
            assertThat(fixture.graphChannels.keys)
                .describedAs(wiFiBand.name)
                .containsExactlyElementsOf(expectedGraphChannels.keys)
            assertThat(fixture.graphChannels.values)
                .describedAs(wiFiBand.name)
                .containsExactlyElementsOf(expectedGraphChannels.values)
        }
    }

    @Test
    fun availableChannelsUsingCountry() {
        Locale.getAvailableLocales().forEach { locale ->
            testData.forEach { (wiFiBand, fixture) ->
                assertThat(fixture.availableChannels(wiFiBand, locale.country).map { it -> it.channel })
                    .describedAs(wiFiBand.name)
                    .containsExactlyElementsOf(WiFiChannelCountry(locale).channels(wiFiBand))
            }
        }
    }

    @Test
    fun wiFiChannels() {
        testData.forEach { (wiFiBand, fixture, expectedChannels) ->
            assertThat(fixture.wiFiChannels())
                .describedAs(wiFiBand.name)
                .containsAll(expectedChannels)
        }
    }

    @Test
    fun graphChannelCount() {
        testData.forEach { (wiFiBand, fixture, _, expectedChannelsCount) ->
            assertThat(fixture.graphChannelCount())
                .describedAs(wiFiBand.name)
                .isEqualTo(expectedChannelsCount)
        }
    }

    @Test
    fun graphChannelByFrequencyInRange() {
        testData.forEach { (wiFiBand, fixture, expectedChannels, _, _, expectedGraphChannels) ->
            expectedChannels.forEach { (channel, frequency) ->
                assertThat(fixture.graphChannelByFrequency(frequency))
                    .describedAs("$wiFiBand.name | Channel: $channel | Frequency: $frequency")
                    .isEqualTo(expectedGraphChannels[channel] ?: String.EMPTY)
            }
        }
    }

    @Test
    fun graphChannelByFrequencyOutOfRange() {
        testData.forEach { (wiFiBand, fixture, expectedChannels) ->
            assertThat(fixture.graphChannelByFrequency(expectedChannels.first().frequency - 1))
                .describedAs(wiFiBand.name)
                .isEmpty()
            assertThat(fixture.graphChannelByFrequency(expectedChannels.last().frequency + 1))
                .describedAs(wiFiBand.name)
                .isEmpty()
        }
    }

}