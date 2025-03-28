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
import java.util.SortedSet

private val countriesETSI: Set<String> = setOf(
    "AT",      // ETSI Austria
    "BE",      // ETSI Belgium
    "CH",      // ETSI Switzerland
    "CY",      // ETSI Cyprus
    "CZ",      // ETSI Czechia
    "DE",      // ETSI Germany
    "DK",      // ETSI Denmark
    "EE",      // ETSI Estonia
    "ES",      // ETSI Spain
    "FI",      // ETSI Finland
    "FR",      // ETSI France
    "GR",      // ETSI Greece
    "HU",      // ETSI Hungary
    "IE",      // ETSI Ireland
    "IS",      // ETSI Iceland
    "IT",      // ETSI Italy
    "LI",      // ETSI Liechtenstein
    "LT",      // ETSI Lithuania
    "LU",      // ETSI Luxembourg
    "LV",      // ETSI Latvia
    "MT",      // ETSI Malta
    "NL",      // ETSI Netherlands
    "NO",      // ETSI Norway
    "PL",      // ETSI Poland
    "PT",      // ETSI Portugal
    "RO",      // ETSI Romania
    "SE",      // ETSI Sweden
    "SI",      // ETSI Slovenia
    "SK",      // ETSI Slovakia
    "IL"       // ETSI Israel
)

class WiFiChannelCountryTest {
    private val currentLocale: Locale = Locale.getDefault()

    @Before
    fun setUp() {
        Locale.setDefault(Locale.US)
    }

    @After
    fun tearDown() {
        Locale.setDefault(currentLocale)
    }

    data class TestData(
        val wiFiBand: WiFiBand,
        val channels: SortedSet<Int>,
        val channelsCountry: List<Rules> = listOf()
    ) {
        fun find(countryCode: String): SortedSet<Int> =
            channelsCountry
                .filter { it.first.contains(countryCode) }
                .flatMap { it.second }
                .toSortedSet()
                .ifEmpty { channels }
    }

    private val testData = listOf(
        TestData(
            WiFiBand.GHZ2,
            (1..13).toSortedSet()
        ),
        TestData(
            WiFiBand.GHZ5,
            sortedSetOf(42, 50, 58, 74, 82, 90, 106, 114, 122, 138, 155, 163, 171),
            listOf(
                Rules(
                    setOf("JP", "TR", "ZA"),
                    sortedSetOf(42, 50, 58, 74, 82, 90, 106, 114, 122, 138)
                ),
                Rules(
                    setOf("CN", "BH", "ID"),
                    sortedSetOf(42, 50, 58, 74, 82, 90, 155, 163)
                ),
                Rules(
                    setOf("RU"),
                    sortedSetOf(42, 50, 58, 74, 82, 90, 138, 155, 163, 171)
                )
            )
        ),
        TestData(
            WiFiBand.GHZ6,
            sortedSetOf(15, 31, 47, 63, 79, 95, 111, 127, 143, 159, 175, 191, 207),
            listOf(
                Rules(
                    countriesETSI.union(setOf("JP", "RU", "NZ", "AU", "GL", "AE", "GB", "MX", "SG", "HK", "MO", "PH")),
                    sortedSetOf(15, 31, 47, 63, 79)
                )
            )
        )
    )

    @Test
    fun findAll() {
        val expected = allCountries()
        // execute
        val actual = WiFiChannelCountry.findAll()
        // validate
        assertThat(actual).hasSize(expected.size)
    }

    @Test
    fun find() {
        // setup
        val expected = Locale.US
        // execute
        val actual = WiFiChannelCountry.find(expected.country)
        // validate
        assertThat(actual.countryCode()).isEqualTo(expected.country)
        assertThat(actual.countryName(expected)).isEqualTo(expected.displayCountry)
    }

    @Test
    fun countryCode() {
        Locale.getAvailableLocales().forEach { locale ->
            val fixture = WiFiChannelCountry(locale)
            // execute
            val actual = fixture.countryCode()
            // validate
            assertThat(actual).describedAs("Code: ${locale.country}").isEqualTo(locale.country)
        }
    }

    @Test
    fun countryName() {
        Locale.getAvailableLocales().forEach { locale ->
            val fixture = WiFiChannelCountry(locale)
            val expected = if (locale.displayCountry.isEmpty()) "-Unknown" else locale.displayCountry
            // execute
            val actual = fixture.countryName(Locale.US)
            // validate
            assertThat(actual).describedAs("Code: ${locale.country}").isEqualTo(expected)
        }
    }

    @Test
    fun channels() {
        Locale.getAvailableLocales().forEach { locale ->
            val fixture = WiFiChannelCountry(locale)
            testData.forEach { it ->
                val expected = it.find(locale.country)
                // execute
                val actual = fixture.channels(it.wiFiBand)
                // validate
                assertThat(actual).describedAs("Code: ${locale.country} | ${it.wiFiBand.name}").containsExactlyElementsOf(expected)
            }
        }
    }

    @Test
    fun available() {
        Locale.getAvailableLocales().forEach { locale ->
            val fixture = WiFiChannelCountry(locale)
            testData.forEach { (wiFiBand, expectedChannels) ->
                expectedChannels.forEach { channel ->
                    // execute
                    val actual = fixture.available(wiFiBand, channel)
                    // validate
                    assertThat(actual).describedAs("Code: ${locale.country} | ${wiFiBand.name} | Channel: $channel").isTrue()
                }
            }
        }
    }

    @Test
    fun notAvailable() {
        Locale.getAvailableLocales().forEach { locale ->
            val fixture = WiFiChannelCountry(locale)
            testData.forEach { (wiFiBand, channels) ->
                assertThat(fixture.available(wiFiBand, channels.first() - 1)).describedAs("Code: ${locale.country} | ${wiFiBand.name}")
                    .isFalse()
                assertThat(fixture.available(wiFiBand, channels.last() + 1)).describedAs("Code: ${locale.country} | ${wiFiBand.name}")
                    .isFalse()
            }
        }
    }

}