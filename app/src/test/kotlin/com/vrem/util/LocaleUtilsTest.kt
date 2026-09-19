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
package com.vrem.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Locale

class LocaleUtilsTest {
    private val currentLocale = Locale.getDefault()

    @Before
    fun setUp() {
        Locale.setDefault(Locale.US)
    }

    @After
    fun tearDown() {
        Locale.setDefault(currentLocale)
    }

    @Test
    fun allSupportedCountries() {
        // Act
        val actual = allCountries()
        // Assert
        assertThat(actual.size).isGreaterThanOrEqualTo(2)
        assertThat(actual[0].country).isLessThan(actual[actual.size - 1].country)
    }

    @Test
    fun findByCountryCodeWithKnownCode() {
        // Arrange
        val expected = allCountries()[0]
        // Act
        val actual = findByCountryCode(expected.country)
        // Assert
        assertThat(actual).isEqualTo(expected)
        assertThat(actual.country).isEqualTo(expected.country)
        assertThat(actual.displayCountry).isEqualTo(expected.displayCountry)
        assertThat(expected.displayCountry).isNotEqualTo(expected.country)
        assertThat(actual.displayCountry).isNotEqualTo(actual.country)
    }

    @Test
    fun findByCountryCodeWithUnknownCode() {
        // Act
        val actual = findByCountryCode("WW")
        // Assert
        assertThat(actual).isEqualTo(Locale.ROOT)
    }

    @Test
    fun toLegacyLanguageTagWithSupportedLanguages() {
        // Arrange
        val locales = listOf(Locale.SIMPLIFIED_CHINESE, Locale.ENGLISH, PORTUGUESE_BRAZIL)
        // Act
        val actual = locales.map { toLegacyLanguageTag(it) }
        // Assert
        assertThat(actual).containsExactly("zh_CN", "en_", "pt_BR")
    }

    @Test
    fun findByLegacyLanguageTagWithSupportedTag() {
        // Arrange
        val legacyLanguageTags = listOf("zh_CN", "zh_TW", "en_", "pt_BR", "pt_PT")
        // Act
        val actual = legacyLanguageTags.map { findByLegacyLanguageTag(it) }
        // Assert
        assertThat(actual).containsExactly(
            Locale.SIMPLIFIED_CHINESE,
            Locale.TRADITIONAL_CHINESE,
            Locale.ENGLISH,
            PORTUGUESE_BRAZIL,
            PORTUGUESE_PORTUGAL,
        )
    }

    @Test
    fun findByLegacyLanguageTagWithUnsupportedTag() {
        // Arrange
        val legacyLanguageTags = listOf(String.EMPTY, "en_US", "de_DE", "en", "WW_HH_TT")
        // Act
        val actual = legacyLanguageTags.map { findByLegacyLanguageTag(it) }
        // Assert
        assertThat(actual).containsOnlyNulls()
    }

    @Test
    fun findSupportedLanguageWithSupportedLanguage() {
        // Arrange
        val languageTags =
            listOf("fr-FR", "en-GB", "ja-JP", "de", "pt-PT", "pt-BR", "zh-Hans-CN", "zh-Hans-SG", "zh-Hant-HK")
        // Act
        val actual = languageTags.map { findSupportedLanguage(Locale.forLanguageTag(it)) }
        // Assert
        assertThat(actual).containsExactly(
            Locale.FRENCH,
            Locale.ENGLISH,
            Locale.JAPANESE,
            Locale.GERMAN,
            PORTUGUESE_PORTUGAL,
            PORTUGUESE_BRAZIL,
            Locale.SIMPLIFIED_CHINESE,
            Locale.SIMPLIFIED_CHINESE,
            Locale.TRADITIONAL_CHINESE,
        )
    }

    @Test
    fun findSupportedLanguageWithUnsupportedLanguage() {
        // Arrange
        val languageTags = listOf(String.EMPTY, "ko-KR", "pt-AO", "zh-HK")
        // Act
        val actual = languageTags.map { findSupportedLanguage(Locale.forLanguageTag(it)) }
        // Assert
        assertThat(actual).containsOnlyNulls()
    }

    @Test
    fun allSupportedLanguages() {
        // Arrange
        val expected: Set<Locale> =
            setOf(
                BULGARIAN,
                DUTCH,
                GREEK,
                HUNGARIAN,
                Locale.SIMPLIFIED_CHINESE,
                Locale.TRADITIONAL_CHINESE,
                Locale.ENGLISH,
                Locale.FRENCH,
                Locale.GERMAN,
                Locale.ITALIAN,
                Locale.JAPANESE,
                POLISH,
                PORTUGUESE_BRAZIL,
                PORTUGUESE_PORTUGAL,
                SPANISH,
                RUSSIAN,
                TURKISH,
                UKRAINIAN,
            )
        // Act
        val actual = supportedLanguages()
        // Assert
        assertThat(actual).hasSize(expected.size)
        for (locale in expected) {
            assertThat(actual).contains(locale)
        }
    }
}
