/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import org.junit.After
import org.junit.Assert.*
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
    fun testChannelAvailableWithTrue() {
        assertTrue(find(Locale.US.country).channelAvailable2GHz(1))
        assertTrue(find(Locale.US.country).channelAvailable2GHz(11))
        assertTrue(find(Locale.US.country).channelAvailable5GHz(36))
        assertTrue(find(Locale.US.country).channelAvailable5GHz(165))
        assertTrue(find(Locale.UK.country).channelAvailable2GHz(1))
        assertTrue(find(Locale.UK.country).channelAvailable2GHz(13))
        assertTrue(find(Locale.UK.country).channelAvailable5GHz(36))
        assertTrue(find(Locale.UK.country).channelAvailable5GHz(140))
    }

    @Test
    fun testChannelAvailableWith2GHz() {
        assertFalse(find(Locale.US.country).channelAvailable2GHz(0))
        assertFalse(find(Locale.US.country).channelAvailable2GHz(12))
        assertFalse(find(Locale.UK.country).channelAvailable2GHz(0))
        assertFalse(find(Locale.UK.country).channelAvailable2GHz(14))
    }

    @Test
    fun testChannelAvailableWith5GHz() {
        assertTrue(find(Locale.US.country).channelAvailable5GHz(36))
        assertTrue(find(Locale.US.country).channelAvailable5GHz(165))
        assertTrue(find(Locale.UK.country).channelAvailable5GHz(36))
        assertTrue(find(Locale.UK.country).channelAvailable5GHz(140))
        assertTrue(find("AE").channelAvailable5GHz(36))
        assertTrue(find("AE").channelAvailable5GHz(64))
    }

    @Test
    fun testChannelAvailableWith6GHz() {
        assertTrue(find(Locale.US.country).channelAvailable6GHz(1))
        assertTrue(find(Locale.US.country).channelAvailable6GHz(93))
        assertTrue(find(Locale.UK.country).channelAvailable6GHz(1))
        assertTrue(find(Locale.UK.country).channelAvailable6GHz(93))
        assertTrue(find("AE").channelAvailable6GHz(1))
        assertTrue(find("AE").channelAvailable6GHz(93))
    }

    @Test
    fun testFindCorrectlyPopulatesGHZ() {
        // setup
        val expectedCountryCode = Locale.US.country
        val expected2GHz: Set<Int> = WiFiChannelCountry2GHz().findChannels(expectedCountryCode)
        val expected5GHz: Set<Int> = WiFiChannelCountry5GHz().findChannels(expectedCountryCode)
        val expected6GHz: Set<Int> = WiFiChannelCountry6GHz().findChannels()
        // execute
        val actual: WiFiChannelCountry = find(expectedCountryCode)
        // validate
        assertEquals(expectedCountryCode, actual.countryCode())
        assertArrayEquals(expected2GHz.toTypedArray(), actual.channels2GHz().toTypedArray())
        assertArrayEquals(expected5GHz.toTypedArray(), actual.channels5GHz().toTypedArray())
        assertArrayEquals(expected6GHz.toTypedArray(), actual.channels6GHz().toTypedArray())
    }

    @Test
    fun testFindCorrectlyPopulatesCountryCodeAndName() {
        // setup
        val expected = Locale.SIMPLIFIED_CHINESE
        val expectedCountryCode = expected.country
        // execute
        val actual: WiFiChannelCountry = find(expectedCountryCode)
        // validate
        assertEquals(expectedCountryCode, actual.countryCode())
        assertNotEquals(expected.displayCountry, actual.countryName(expected))
        assertEquals(expected.getDisplayCountry(expected), actual.countryName(expected))
    }

    @Test
    fun testCountryName() {
        // setup
        val fixture = WiFiChannelCountry(Locale.US)
        val expected = "United States"
        // execute & validate
        val actual = fixture.countryName(Locale.US)
        // execute & validate
        assertEquals(expected, actual)
    }

    @Test
    fun testCountryNameUnknown() {
        // setup
        val fixture = WiFiChannelCountry(Locale("XYZ"))
        val expected = "-Unknown"
        // execute & validate
        val actual = fixture.countryName(Locale.US)
        // execute & validate
        assertEquals(expected, actual)
    }

}
