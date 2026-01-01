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
package com.vrem.wifianalyzer.wifi.band

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter
import org.junit.runners.Parameterized.Parameters
import java.util.Locale

@RunWith(Parameterized::class)
class WiFiChannelCountryParameterizedTest {
    private val expectedWiFiBands =
        listOf(
            WiFiBand.GHZ2 to expectedWiFiInfoGHZ2,
            WiFiBand.GHZ5 to expectedWiFiInfoGHZ5,
            WiFiBand.GHZ6 to expectedWiFiInfoGHZ6,
        )

    @Parameter(0)
    lateinit var locale: Locale

    @Parameter(1)
    lateinit var countryCode: String

    @Parameter(2)
    lateinit var countryName: String

    lateinit var fixture: WiFiChannelCountry

    @Before
    fun setUp() {
        fixture = WiFiChannelCountry(locale)
    }

    @Test
    fun countryCode() {
        assertThat(fixture.countryCode).isEqualTo(countryCode)
    }

    @Test
    fun countryName() {
        assertThat(fixture.countryName(Locale.US)).isEqualTo(countryName)
    }

    @Test
    fun channels() {
        expectedWiFiBands.forEach { (wiFiBand, expectedWiFiInfo) ->
            assertThat(fixture.channels(wiFiBand))
                .describedAs("$wiFiBand")
                .containsExactlyElementsOf(expectedWiFiInfo.expectedRatingChannels(wiFiBand, countryCode))
        }
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{index}: {1} - {2}")
        fun data() =
            Locale
                .getAvailableLocales()
                .filter { !it.country.isEmpty() && !it.displayName.isEmpty() }
                .map { locale -> arrayOf(locale, locale.country, locale.getDisplayCountry(Locale.US)) }
    }
}
