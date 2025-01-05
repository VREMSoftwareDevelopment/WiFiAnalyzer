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

import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry.Companion.find
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Locale

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


    @Test
    fun channelAvailableWithTrue() {
        assertThat(find(Locale.US.country).channelAvailableGHZ2(1)).isTrue()
        assertThat(find(Locale.US.country).channelAvailableGHZ2(11)).isTrue()
        assertThat(find(Locale.US.country).channelAvailableGHZ5(36)).isTrue()
        assertThat(find(Locale.US.country).channelAvailableGHZ5(165)).isTrue()
        assertThat(find(Locale.UK.country).channelAvailableGHZ2(1)).isTrue()
        assertThat(find(Locale.UK.country).channelAvailableGHZ2(13)).isTrue()
        assertThat(find(Locale.UK.country).channelAvailableGHZ5(36)).isTrue()
        assertThat(find(Locale.UK.country).channelAvailableGHZ5(140)).isTrue()
    }

    @Test
    fun channelAvailableWithGHZ2() {
        assertThat(find(Locale.US.country).channelAvailableGHZ2(0)).isFalse()
        assertThat(find(Locale.US.country).channelAvailableGHZ2(12)).isFalse()
        assertThat(find(Locale.UK.country).channelAvailableGHZ2(0)).isFalse()
        assertThat(find(Locale.UK.country).channelAvailableGHZ2(14)).isFalse()
    }

    @Test
    fun channelAvailableWithGHZ5() {
        assertThat(find(Locale.US.country).channelAvailableGHZ5(36)).isTrue()
        assertThat(find(Locale.US.country).channelAvailableGHZ5(165)).isTrue()
        assertThat(find(Locale.UK.country).channelAvailableGHZ5(36)).isTrue()
        assertThat(find(Locale.UK.country).channelAvailableGHZ5(140)).isTrue()
        assertThat(find("AE").channelAvailableGHZ5(36)).isTrue()
        assertThat(find("AE").channelAvailableGHZ5(64)).isTrue()
    }

    @Test
    fun channelAvailableWithGHZ6() {
        assertThat(find(Locale.US.country).channelAvailableGHZ6(1)).isTrue()
        assertThat(find(Locale.US.country).channelAvailableGHZ6(93)).isTrue()
        assertThat(find(Locale.UK.country).channelAvailableGHZ6(1)).isTrue()
        assertThat(find(Locale.UK.country).channelAvailableGHZ6(93)).isTrue()
        assertThat(find("AE").channelAvailableGHZ6(1)).isTrue()
        assertThat(find("AE").channelAvailableGHZ6(93)).isTrue()
    }

    @Test
    fun findCorrectlyPopulatesGHZ() {
        // setup
        val expectedCountryCode = Locale.US.country
        val expectedGHZ2: Set<Int> = WiFiChannelCountryGHZ2().findChannels(expectedCountryCode)
        val expectedGHZ5: Set<Int> = WiFiChannelCountryGHZ5().findChannels(expectedCountryCode)
        val expectedGHZ6: Set<Int> = WiFiChannelCountryGHZ6().findChannels()
        // execute
        val actual: WiFiChannelCountry = find(expectedCountryCode)
        // validate
        assertThat(actual.countryCode()).isEqualTo(expectedCountryCode)
        assertThat(actual.channelsGHZ2().toTypedArray()).isEqualTo(expectedGHZ2.toTypedArray())
        assertThat(actual.channelsGHZ5().toTypedArray()).isEqualTo(expectedGHZ5.toTypedArray())
        assertThat(actual.channelsGHZ6().toTypedArray()).isEqualTo(expectedGHZ6.toTypedArray())
    }

    @Test
    fun findCorrectlyPopulatesCountryCodeAndName() {
        // setup
        val expected = Locale.SIMPLIFIED_CHINESE
        val expectedCountryCode = expected.country
        // execute
        val actual: WiFiChannelCountry = find(expectedCountryCode)
        // validate
        assertThat(actual.countryCode()).isEqualTo(expectedCountryCode)
        assertThat(actual.countryName(expected)).isNotEqualTo(expected.displayCountry)
        assertThat(actual.countryName(expected)).isEqualTo(expected.getDisplayCountry(expected))
    }

    @Test
    fun countryName() {
        // setup
        val fixture = WiFiChannelCountry(Locale.US)
        val expected = "United States"
        // execute & validate
        val actual = fixture.countryName(Locale.US)
        // execute & validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun countryNameUnknown() {
        // setup
        val fixture = WiFiChannelCountry(Locale("XYZ"))
        val expected = "-Unknown"
        // execute & validate
        val actual = fixture.countryName(Locale.US)
        // execute & validate
        assertThat(actual).isEqualTo(expected)
    }

}