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
package com.vrem.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.Locale

class LocaleUtilsTest {
    @Test
    fun allSupportedCountries() {
        // execute
        val actual = allCountries()
        // validate
        assertThat(actual.size).isGreaterThanOrEqualTo(2)
        assertThat(actual[0].country).isLessThan(actual[actual.size - 1].country)
    }

    @Test
    fun findByCountryCodeWithKnownCode() {
        // setup
        val expected = allCountries()[0]
        // execute
        val actual = findByCountryCode(expected.country)
        // validate
        assertThat(actual).isEqualTo(expected)
        assertThat(actual.country).isEqualTo(expected.country)
        assertThat(actual.displayCountry).isEqualTo(expected.displayCountry)
        assertThat(expected.displayCountry).isNotEqualTo(expected.country)
        assertThat(actual.displayCountry).isNotEqualTo(actual.country)
    }

    @Test
    fun findByCountryCodeWithUnknownCode() {
        // execute
        val actual = findByCountryCode("WW")
        // validate
        assertThat(actual).isEqualTo(Locale.getDefault())
    }

    @Test
    fun toLanguageTagWithKnownCode() {
        assertThat(toLanguageTag(Locale.US)).isEqualTo(Locale.US.language + "_" + Locale.US.country)
        assertThat(toLanguageTag(Locale.ENGLISH)).isEqualTo(Locale.ENGLISH.language + "_")
    }

    @Test
    fun findByLanguageTagWithUnknownTag() {
        val defaultLocal = Locale.getDefault()
        assertThat(findByLanguageTag(String.EMPTY)).isEqualTo(defaultLocal)
        assertThat(findByLanguageTag("WW")).isEqualTo(defaultLocal)
        assertThat(findByLanguageTag("WW_HH_TT")).isEqualTo(defaultLocal)
    }

    @Test
    fun findByLanguageTagWithKnownTag() {
        assertThat(findByLanguageTag(toLanguageTag(Locale.SIMPLIFIED_CHINESE))).isEqualTo(Locale.SIMPLIFIED_CHINESE)
        assertThat(findByLanguageTag(toLanguageTag(Locale.TRADITIONAL_CHINESE))).isEqualTo(Locale.TRADITIONAL_CHINESE)
        assertThat(findByLanguageTag(toLanguageTag(Locale.ENGLISH))).isEqualTo(Locale.ENGLISH)
    }

    @Test
    fun allSupportedLanguages() {
        // setup
        val expected: Set<Locale> = setOf(
            BULGARIAN,
            GREEK,
            Locale.SIMPLIFIED_CHINESE,
            Locale.TRADITIONAL_CHINESE,
            Locale.ENGLISH,
            Locale.FRENCH,
            Locale.GERMAN,
            Locale.ITALIAN,
            Locale.JAPANESE,
            POLISH,
            PORTUGUESE,
            SPANISH,
            RUSSIAN,
            TURKISH,
            UKRAINIAN,
            Locale.getDefault()
        )
        // execute
        val actual = supportedLanguages()
        // validate
        assertThat(actual).hasSize(expected.size)
        for (locale in expected) {
            assertThat(actual).contains(locale)
        }
    }

    @Test
    fun currentDefaultCountryCode() {
        assertThat(defaultCountryCode()).isEqualTo(Locale.getDefault().country)
    }

    @Test
    fun currentDefaultLanguageTag() {
        assertThat(defaultLanguageTag()).isEqualTo(toLanguageTag(Locale.getDefault()))
    }
}