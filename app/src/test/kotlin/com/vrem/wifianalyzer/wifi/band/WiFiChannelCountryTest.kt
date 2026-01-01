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
import org.junit.Test
import java.util.Locale

class WiFiChannelCountryTest {
    @Test
    fun findAll() {
        assertThat(WiFiChannelCountry.findAll()).hasSize(allCountries().size)
    }

    @Test
    fun find() {
        val expected = Locale.US
        val actual = WiFiChannelCountry.find(expected.country)
        assertThat(actual.countryCode).isEqualTo(expected.country)
        assertThat(actual.countryName(expected)).isEqualTo(expected.getDisplayCountry(expected))
    }

    @Test
    fun unknownLocale() {
        val locale = Locale.forLanguageTag("XYZ")
        val wiFiChannelCountry = WiFiChannelCountry(locale)
        assertThat(wiFiChannelCountry.countryCode).isEqualTo("")
        assertThat(wiFiChannelCountry.countryName(Locale.US)).isEqualTo("-Unknown")
    }
}
