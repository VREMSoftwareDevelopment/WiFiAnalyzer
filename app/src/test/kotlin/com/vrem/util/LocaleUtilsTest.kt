/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class LocaleUtilsTest {
    @Test
    fun testAllCountries() {
        // execute
        val actual = allCountries()
        // validate
        Assert.assertTrue(actual.size >= 2)
        Assert.assertTrue(actual[0].country < actual[actual.size - 1].country)
    }

    @Test
    fun testFindByCountryCode() {
        // setup
        val expected = allCountries()[0]
        // execute
        val actual = findByCountryCode(expected.country)
        // validate
        assertEquals(expected, actual)
        assertEquals(expected.country, actual.country)
        assertEquals(expected.displayCountry, actual.displayCountry)
        Assert.assertNotEquals(expected.country, expected.displayCountry)
        Assert.assertNotEquals(actual.country, actual.displayCountry)
    }

    @Test
    fun testFindByCountryCodeWithUnknownCode() {
        // execute
        val actual = findByCountryCode("WW")
        // validate
        assertEquals(Locale.getDefault(), actual)
    }

    @Test
    fun testToLanguageTag() {
        assertEquals(Locale.US.language + "_" + Locale.US.country, toLanguageTag(Locale.US))
        assertEquals(Locale.ENGLISH.language + "_", toLanguageTag(Locale.ENGLISH))
    }

    @Test
    fun testFindByLanguageTagWithUnknownTag() {
        val defaultLocal = Locale.getDefault()
        assertEquals(defaultLocal, findByLanguageTag(String.EMPTY))
        assertEquals(defaultLocal, findByLanguageTag("WW"))
        assertEquals(defaultLocal, findByLanguageTag("WW_HH_TT"))
    }

    @Test
    fun testFindByLanguageTag() {
        assertEquals(Locale.SIMPLIFIED_CHINESE, findByLanguageTag(toLanguageTag(Locale.SIMPLIFIED_CHINESE)))
        assertEquals(Locale.TRADITIONAL_CHINESE, findByLanguageTag(toLanguageTag(Locale.TRADITIONAL_CHINESE)))
        assertEquals(Locale.ENGLISH, findByLanguageTag(toLanguageTag(Locale.ENGLISH)))
    }

    @Test
    fun testSupportedLanguages() {
        // setup
        val expected: Set<Locale> = setOf(
                BULGARIAN,
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
                UKRAINIAN,
                Locale.getDefault())
        // execute
        val actual = supportedLanguages()
        // validate
        assertEquals(expected.size, actual.size)
        for (locale in expected) {
            Assert.assertTrue(actual.contains(locale))
        }
    }

    @Test
    fun testDefaultCountryCode() {
        assertEquals(Locale.getDefault().country, defaultCountryCode())
    }

    @Test
    fun testDefaultLanguageTag() {
        assertEquals(toLanguageTag(Locale.getDefault()), defaultLanguageTag())
    }
}