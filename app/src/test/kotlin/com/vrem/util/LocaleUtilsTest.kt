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
        assertThat(toLanguageTag(Locale.US)).isEqualTo("en-US")
        assertThat(toLanguageTag(ENGLISH)).isEqualTo("en")
        assertThat(toLanguageTag(CHINESE_SIMPLIFIED)).isEqualTo("zh-Hans")
        assertThat(toLanguageTag(CHINESE_TRADITIONAL)).isEqualTo("zh-Hant")
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
        // BCP-47 format (new)
        assertThat(findByLanguageTag("en")).isEqualTo(ENGLISH)
        assertThat(findByLanguageTag("en-US")).isEqualTo(ENGLISH)
        assertThat(findByLanguageTag("zh")).isEqualTo(CHINESE)
        assertThat(findByLanguageTag("zh-CN")).isEqualTo(CHINESE_SIMPLIFIED)
        assertThat(findByLanguageTag("zh-Hans")).isEqualTo(CHINESE_SIMPLIFIED)
        assertThat(findByLanguageTag("zh-Hant")).isEqualTo(CHINESE_TRADITIONAL)
        assertThat(findByLanguageTag("zh-TW")).isEqualTo(CHINESE_TRADITIONAL)
        assertThat(findByLanguageTag("zh-XX")).isEqualTo(CHINESE)

        // Backward compatibility: underscore format (old)
        assertThat(findByLanguageTag("en_US")).isEqualTo(ENGLISH)
        assertThat(findByLanguageTag("zh_CN")).isEqualTo(CHINESE_SIMPLIFIED)
        assertThat(findByLanguageTag("zh_Hans")).isEqualTo(CHINESE_SIMPLIFIED)
        assertThat(findByLanguageTag("zh_Hant")).isEqualTo(CHINESE_TRADITIONAL)
        assertThat(findByLanguageTag("zh_TW")).isEqualTo(CHINESE_TRADITIONAL)
        assertThat(findByLanguageTag("zh_XX")).isEqualTo(CHINESE)
    }

    @Test
    fun findSupportedLocaleTest() {
        val defaultLocale = Locale.getDefault()

        // Empty language
        assertThat(findSupportedLocale(Locale.forLanguageTag(""))).isEqualTo(defaultLocale)

        // Chinese logic
        assertThat(findSupportedLocale(Locale.forLanguageTag("zh"))).isEqualTo(CHINESE)
        assertThat(findSupportedLocale(Locale.forLanguageTag("zh-CN"))).isEqualTo(CHINESE_SIMPLIFIED)
        assertThat(findSupportedLocale(Locale.forLanguageTag("zh-SG"))).isEqualTo(CHINESE_SIMPLIFIED)
        assertThat(findSupportedLocale(Locale.forLanguageTag("zh-TW"))).isEqualTo(CHINESE_TRADITIONAL)
        assertThat(findSupportedLocale(Locale.forLanguageTag("zh-HK"))).isEqualTo(CHINESE_TRADITIONAL)
        assertThat(findSupportedLocale(Locale.forLanguageTag("zh-MO"))).isEqualTo(CHINESE_TRADITIONAL)
        assertThat(findSupportedLocale(Locale.forLanguageTag("zh-XX"))).isEqualTo(CHINESE)

        // Exact match
        assertThat(findSupportedLocale(JAPANESE)).isEqualTo(JAPANESE)
        assertThat(findSupportedLocale(PORTUGUESE_BRAZIL)).isEqualTo(PORTUGUESE_BRAZIL)
        assertThat(findSupportedLocale(PORTUGUESE_PORTUGAL)).isEqualTo(PORTUGUESE_PORTUGAL)

        // Language + Script match
        assertThat(findSupportedLocale(Locale.forLanguageTag("zh-Hans-US"))).isEqualTo(CHINESE_SIMPLIFIED)
        assertThat(findSupportedLocale(Locale.forLanguageTag("zh-Hant-US"))).isEqualTo(CHINESE_TRADITIONAL)

        // Language + Country match (fails script match but matches country)
        assertThat(findSupportedLocale(Locale.forLanguageTag("pt-Latn-BR"))).isEqualTo(PORTUGUESE_BRAZIL)
        assertThat(findSupportedLocale(Locale.forLanguageTag("pt-Latn-PT"))).isEqualTo(PORTUGUESE_PORTUGAL)

        // Language only match
        assertThat(findSupportedLocale(Locale.forLanguageTag("en-US"))).isEqualTo(ENGLISH)
        assertThat(findSupportedLocale(Locale.forLanguageTag("en-GB"))).isEqualTo(ENGLISH)
        assertThat(findSupportedLocale(Locale.forLanguageTag("fr-CA"))).isEqualTo(FRENCH)
        // pt-Latn-AO (Angola) matches PORTUGUESE_BRAZIL because it is the first 'pt' in the list
        assertThat(findSupportedLocale(Locale.forLanguageTag("pt-Latn-AO"))).isEqualTo(PORTUGUESE_BRAZIL)

        // No match fallback to default
        assertThat(findSupportedLocale(Locale.forLanguageTag("xx"))).isEqualTo(defaultLocale)
    }

    @Test
    fun supportedLanguageTagsTest() {
        val actual = supportedLanguageTags()
        assertThat(actual).contains("")
        assertThat(actual).contains("en")
        assertThat(actual).contains("zh-Hans")
        assertThat(actual).contains("zh-Hant")
        assertThat(actual).hasSize(baseSupportedLocales.size + 1)
    }

    @Test
    fun toSupportedLocaleTagTest() {
        assertThat(Locale.forLanguageTag("en-US").toSupportedLocaleTag()).isEqualTo("en")
        assertThat(Locale.forLanguageTag("zh-CN").toSupportedLocaleTag()).isEqualTo("zh-Hans")
    }

    @Test
    fun allSupportedLanguages() {
        // setup
        val expected: Set<Locale> =
            setOf(
                BULGARIAN,
                CHINESE_SIMPLIFIED,
                CHINESE_TRADITIONAL,
                DUTCH,
                ENGLISH,
                FRENCH,
                GERMAN,
                GREEK,
                HUNGARIAN,
                ITALIAN,
                JAPANESE,
                POLISH,
                PORTUGUESE_BRAZIL,
                PORTUGUESE_PORTUGAL,
                RUSSIAN,
                SPANISH,
                TURKISH,
                UKRAINIAN,
                Locale.getDefault(),
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
    fun currentCountryCodeReturnsDefault() {
        assertThat(currentCountryCode()).isEqualTo(Locale.getDefault().country)
    }

    @Test
    fun currentLanguageTagReturnsDefault() {
        assertThat(currentLanguageTag()).isEqualTo(toLanguageTag(Locale.getDefault()))
    }
}
