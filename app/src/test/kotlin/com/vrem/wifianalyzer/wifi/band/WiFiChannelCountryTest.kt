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

import com.vrem.util.allCountries
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Locale

class WiFiChannelCountryTest {
    private val currentLocale: Locale = Locale.getDefault()

    private val expectedChannelExcludes = mapOf(
        WiFiBand.GHZ2 to expectedChannelExcludesGHZ2,
        WiFiBand.GHZ5 to expectedChannelExcludesGHZ5,
        WiFiBand.GHZ6 to expectedChannelExcludesGHZ6
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
    fun expectedChannelExcludesCheck() {
        assertThat(expectedChannelExcludes).hasSize(WiFiBand.entries.size)
    }

    @Test
    fun findAll() {
        assertThat(WiFiChannelCountry.findAll()).hasSize(allCountries().size)
    }

    @Test
    fun find() {
        val expected = Locale.US
        val actual = WiFiChannelCountry.find(expected.country)
        assertThat(actual.countryCode).isEqualTo(expected.country)
        assertThat(actual.countryName(expected)).isEqualTo(expected.displayCountry)
    }

    @Test
    fun countryCode() {
        Locale.getAvailableLocales().forEach { locale ->
            assertThat(WiFiChannelCountry(locale).countryCode)
                .describedAs("Code: ${locale.country}")
                .isEqualTo(locale.country)
        }
    }

    @Test
    fun countryName() {
        Locale.getAvailableLocales().forEach { locale ->
            val fixture = WiFiChannelCountry(locale)
            assertThat(fixture.countryName(Locale.US))
                .describedAs("Code: ${locale.country}")
                .isEqualTo(if (locale.displayCountry.isEmpty()) "-Unknown" else locale.displayCountry)
        }
    }

    @Test
    fun channels() {
        Locale.getAvailableLocales().forEach { locale ->
            val fixture = WiFiChannelCountry(locale)
            expectedChannelExcludes.forEach { (wiFiBand, exclude) ->
                assertThat(fixture.channels(wiFiBand))
                    .describedAs("Code: ${locale.country} | ${wiFiBand.name}")
                    .containsExactlyElementsOf(exclude.expectedAvailableChannels(wiFiBand, locale.country))
            }
        }
    }
}