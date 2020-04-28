/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
    fun testGetAllCountries() {
        // execute
        val actual = allCountries()
        // validate
        Assert.assertTrue(actual.size >= 2)
        Assert.assertTrue(actual[0].country.compareTo(actual[actual.size - 1].country) < 0)
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
        assertEquals(defaultLocal, findByLanguageTag(STRING_EMPTY))
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
    fun testGetSupportedLanguages() {
        // setup
        val expected: Set<Locale> = HashSet(Arrays.asList(
                Locale.GERMAN,
                Locale.ENGLISH,
                Locale.FRENCH,
                Locale.ITALIAN,
                Locale.SIMPLIFIED_CHINESE,
                Locale.TRADITIONAL_CHINESE,
                SPANISH,
                PORTUGUESE,
                RUSSIAN,
                Locale.getDefault()))
        // execute
        val actual = supportedLanguages()
        // validate
        assertEquals(expected.size.toLong(), actual.size.toLong())
        for (locale in expected) {
            Assert.assertTrue(actual.contains(locale))
        }
    }

    @Test
    fun testGetDefaultCountryCode() {
        assertEquals(Locale.getDefault().country, defaultCountryCode())
    }

    @Test
    fun testGetDefaultLanguageTag() {
        assertEquals(toLanguageTag(Locale.getDefault()), defaultLanguageTag())
    }
}