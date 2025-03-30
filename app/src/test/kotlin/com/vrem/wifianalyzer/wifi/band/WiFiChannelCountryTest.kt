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

private val countriesETSI = listOf(
    "AT",
    "BE",
    "CH",
    "CY",
    "CZ",
    "DE",
    "DK",
    "EE",
    "ES",
    "FI",
    "FR",
    "GR",
    "HU",
    "IE",
    "IS",
    "IT",
    "LI",
    "LT",
    "LU",
    "LV",
    "MT",
    "NL",
    "NO",
    "PL",
    "PT",
    "RO",
    "SE",
    "SI",
    "SK",
    "IL"
)

private fun excludeChannelsGHZ6(): List<Map<String, List<Int>>> {
    val exclude = (97..223).toList()
    val additional = listOf("JP", "RU", "NZ", "AU", "GL", "AE", "GB", "MX", "SG", "HK", "MO", "PH")
    return (countriesETSI + additional).map { mapOf(it to exclude) }
}

private fun excludeChannelsGHZ5(): List<Map<String, List<Int>>> {
    val exclude1 = listOf(177)
    val exclude2 = (120..128).toList()
    val exclude3 = (169..177).toList()
    val exclude4 = (96..128).toList()
    val exclude5 = (173..177).toList()
    val exclude6 = (149..177).toList()
    val exclude7 = (96..144).toList()
    val exclude8 = listOf(144)
    val exclude9 = (165..177).toList()
    val additional = listOf(
        "AU" to exclude2 + exclude1,
        "CA" to exclude2 + exclude3,
        "UK" to exclude2 + exclude1,
        "RU" to exclude4 + exclude5,
        "JP" to exclude4 + exclude6,
        "IN" to exclude1,
        "SG" to exclude3,
        "CH" to exclude7 + exclude3,
        "IL" to exclude1,
        "KR" to exclude3,
        "TR" to exclude8 + exclude6,
        "ZA" to exclude8 + exclude6,
        "BR" to exclude3,
        "TW" to exclude3,
        "NZ" to exclude3,
        "BH" to exclude7 + exclude3,
        "VN" to exclude3,
        "ID" to exclude7 + exclude9,
        "PH" to exclude5
    )
    return countriesETSI.map { mapOf(it to exclude1) } + additional.map { mapOf(it.first to it.second) }
}

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

    private data class TestData(
        val wiFiBand: WiFiBand,
        val exclude: List<Map<String, List<Int>>> = listOf()
    ) {
        fun find(countryCode: String): List<Int> =
            wiFiBand.wiFiChannels.availableChannels
                .subtract(exclude.flatMap { it[countryCode] ?: emptyList() })
                .toList()
    }

    private val testData = listOf(
        TestData(WiFiBand.GHZ2),
        TestData(WiFiBand.GHZ5, excludeChannelsGHZ5()),
        TestData(WiFiBand.GHZ6, excludeChannelsGHZ6())
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
            testData.forEach { it ->
                assertThat(fixture.channels(it.wiFiBand))
                    .describedAs("Code: ${locale.country} | ${it.wiFiBand.name}")
                    .containsExactlyElementsOf(it.find(locale.country))
            }
        }
    }

}