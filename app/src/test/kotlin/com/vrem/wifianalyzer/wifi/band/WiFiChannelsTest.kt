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
import com.vrem.util.findByCountryCode
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import java.util.Locale
import kotlin.test.Test

class WiFiChannelsTest {
    private val currentLocale: Locale = Locale.getDefault()

    private val fixtures = listOf(
        Triple(WiFiBand.GHZ2, wiFiChannelsGHZ2, expectedWiFiInfoGHZ2),
        Triple(WiFiBand.GHZ5, wiFiChannelsGHZ5, expectedWiFiInfoGHZ5),
        Triple(WiFiBand.GHZ6, wiFiChannelsGHZ6, expectedWiFiInfoGHZ6)
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
    fun fixturesCheck() {
        assertThat(fixtures).hasSize(WiFiBand.entries.size)
    }

    @Test
    fun inRange() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            assertThat(fixture.inRange(expectedWiFiInfo.expectedChannels.first().frequency)).describedAs("$wiFiBand").isTrue()
            assertThat(fixture.inRange(expectedWiFiInfo.expectedChannels.last().frequency)).describedAs("$wiFiBand").isTrue()
        }
    }

    @Test
    fun notInRange() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            assertThat(fixture.inRange(expectedWiFiInfo.expectedChannels.first().frequency - 1))
                .describedAs("$wiFiBand")
                .isFalse()
            assertThat(fixture.inRange(expectedWiFiInfo.expectedChannels.last().frequency + 1))
                .describedAs("$wiFiBand")
                .isFalse()
        }
    }

    @Test
    fun wiFiChannelByFrequencyInRange() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            expectedWiFiInfo.expectedChannels.forEach { expected ->
                assertThat(fixture.wiFiChannelByFrequency(expected.frequency))
                    .describedAs("$wiFiBand")
                    .isEqualTo(expected)
            }
        }
    }

    @Test
    fun wiFiChannelByFrequencyOutOfRange() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            assertThat(fixture.wiFiChannelByFrequency(expectedWiFiInfo.expectedChannels.first().frequency - 1))
                .describedAs("$wiFiBand")
                .isEqualTo(WiFiChannel.UNKNOWN)
            assertThat(fixture.wiFiChannelByFrequency(expectedWiFiInfo.expectedChannels.last().frequency + 1))
                .describedAs("$wiFiBand")
                .isEqualTo(WiFiChannel.UNKNOWN)
        }
    }

    @Test
    fun wiFiChannelByChannelInRange() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            expectedWiFiInfo.expectedChannels.forEach { expected ->
                assertThat(fixture.wiFiChannelByChannel(expected.channel))
                    .describedAs("$wiFiBand")
                    .isEqualTo(expected)
            }
        }
    }

    @Test
    fun wiFiChannelByChannelNotInRange() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            assertThat(fixture.wiFiChannelByChannel(expectedWiFiInfo.expectedChannels.first().channel - 1))
                .describedAs("$wiFiBand")
                .isEqualTo(WiFiChannel.UNKNOWN)
            assertThat(fixture.wiFiChannelByChannel(expectedWiFiInfo.expectedChannels.last().channel + 1))
                .describedAs("$wiFiBand")
                .isEqualTo(WiFiChannel.UNKNOWN)
        }
    }

    @Test
    fun channelRange() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            assertThat(fixture.channelRange.first)
                .describedAs("$wiFiBand")
                .isEqualTo(expectedWiFiInfo.expectedChannels.first())
            assertThat(fixture.channelRange.second)
                .describedAs("$wiFiBand")
                .isEqualTo(expectedWiFiInfo.expectedChannels.last())
        }
    }

    @Test
    fun activeChannels() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            fixture.activeChannels.forEach { (wiFiWidth, wiFiWidthChannels) ->
                println("[$wiFiBand $wiFiWidth]")
                val expected = expectedWiFiInfo.expectedActiveChannels
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
    fun wiFiWidthUsingChannelInRange() {
        fixtures.forEach { (wiFiBand, fixture) ->
            fixture.activeChannels.forEach { (wiFiWidth, channels) ->
                if (wiFiBand == WiFiBand.GHZ2 && wiFiWidth == WiFiWidth.MHZ_40) {
                    return@forEach
                }
                channels.forEach { channel ->
                    println("[$wiFiBand $wiFiWidth | Channel: $channel]")
                    assertThat(fixture.wiFiWidthByChannel(channel))
                        .describedAs("$wiFiBand $wiFiWidth | Channel: $channel")
                        .isEqualTo(wiFiWidth)
                }
            }
        }
    }

    @Test
    fun wiFiWidthUsingChannelNotInRange() {
        fixtures.forEach { (wiFiBand, fixture) ->
            val first = wiFiBand.wiFiChannels.channelRange.first.channel
            val last = wiFiBand.wiFiChannels.channelRange.second.channel
            println("[$wiFiBand]")
            assertThat(fixture.wiFiWidthByChannel(first - 1))
                .describedAs("$wiFiBand")
                .isEqualTo(WiFiWidth.MHZ_20)
            assertThat(fixture.wiFiWidthByChannel(last + 1))
                .describedAs("$wiFiBand")
                .isEqualTo(WiFiWidth.MHZ_20)
        }
    }

    @Test
    fun availableChannels() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            assertThat(fixture.availableChannels)
                .describedAs("$wiFiBand")
                .containsExactlyElementsOf(expectedWiFiInfo.expectedAvailableChannels)
        }
    }

    @Test
    fun graphChannels() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            assertThat(fixture.graphChannels.keys)
                .describedAs("$wiFiBand")
                .containsExactlyElementsOf(expectedWiFiInfo.expectedGraphChannels.keys)
            assertThat(fixture.graphChannels.values)
                .describedAs("$wiFiBand")
                .containsExactlyElementsOf(expectedWiFiInfo.expectedGraphChannels.values)
        }
    }

    @Test
    fun availableChannelsUsingWiFiBandAndCountry() {
        Locale.getAvailableLocales().forEach { locale ->
            val locale = findByCountryCode(locale.country)
            fixtures.forEach { (wiFiBand, fixture, _) ->
                println("[$wiFiBand | Country: ${locale.country}]")
                assertThat(fixture.availableChannels(wiFiBand, locale.country).map { it -> it.channel })
                    .describedAs("$wiFiBand | Country: ${locale.country}")
                    .containsExactlyElementsOf(WiFiChannelCountry(locale).channels(wiFiBand))
            }
        }
    }

    @Test
    fun availableChannelsUsingWiFiWidthWiFiBandAndCountry() {
        Locale.getAvailableLocales().forEach { locale ->
            val country = findByCountryCode(locale.country).country
            fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
                WiFiWidth.entries.forEach { wiFiWidth ->
                    println("[$wiFiBand | $wiFiWidth | Country: $country]")
                    assertThat(fixture.availableChannels(wiFiWidth, wiFiBand, country))
                        .describedAs("$wiFiBand | $wiFiWidth | Country: $country")
                        .containsExactlyElementsOf(expectedWiFiInfo.availableChannels(wiFiWidth, wiFiBand, country))
                }
            }
        }
    }

    @Test
    fun ratingChannels() {
        Locale.getAvailableLocales().forEach { locale ->
            val locale = findByCountryCode(locale.country)
            fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
                println("[$wiFiBand Country: ${locale.country}]")
                assertThat(fixture.ratingChannels(wiFiBand, locale.country))
                    .describedAs("$wiFiBand | Country: ${locale.country}")
                    .containsExactlyElementsOf(expectedWiFiInfo.expectedRatingChannels(wiFiBand, locale.country))
            }
        }
    }

    @Test
    fun wiFiChannels() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            assertThat(fixture.wiFiChannels())
                .describedAs("$wiFiBand")
                .containsAll(expectedWiFiInfo.expectedChannels)
        }
    }

    @Test
    fun graphChannelCount() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            assertThat(fixture.graphChannelCount())
                .describedAs("$wiFiBand")
                .isEqualTo(expectedWiFiInfo.expectedChannelsCount)
        }
    }

    @Test
    fun graphChannelByFrequencyInRange() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            expectedWiFiInfo.expectedChannels.forEach { (channel, frequency) ->
                println("[$wiFiBand | Channel: $channel | Frequency: $frequency]")
                assertThat(fixture.graphChannelByFrequency(frequency))
                    .describedAs("$wiFiBand | Channel: $channel | Frequency: $frequency")
                    .isEqualTo(expectedWiFiInfo.expectedGraphChannels[channel] ?: String.EMPTY)
            }
        }
    }

    @Test
    fun graphChannelByFrequencyOutOfRange() {
        fixtures.forEach { (wiFiBand, fixture, expectedWiFiInfo) ->
            println("[$wiFiBand]")
            assertThat(fixture.graphChannelByFrequency(expectedWiFiInfo.expectedChannels.first().frequency - 1))
                .describedAs("$wiFiBand")
                .isEmpty()
            assertThat(fixture.graphChannelByFrequency(expectedWiFiInfo.expectedChannels.last().frequency + 1))
                .describedAs("$wiFiBand")
                .isEmpty()
        }
    }

}
