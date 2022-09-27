/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2022 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import java.util.*

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
        assertTrue(find(Locale.US.country).channelAvailableGHZ2(1))
        assertTrue(find(Locale.US.country).channelAvailableGHZ2(11))
        assertTrue(find(Locale.US.country).channelAvailableGHZ5(36))
        assertTrue(find(Locale.US.country).channelAvailableGHZ5(165))
        assertTrue(find(Locale.UK.country).channelAvailableGHZ2(1))
        assertTrue(find(Locale.UK.country).channelAvailableGHZ2(13))
        assertTrue(find(Locale.UK.country).channelAvailableGHZ5(36))
        assertTrue(find(Locale.UK.country).channelAvailableGHZ5(140))
    }

    @Test
    fun testChannelAvailableWithGHZ2() {
        assertFalse(find(Locale.US.country).channelAvailableGHZ2(0))
        assertFalse(find(Locale.US.country).channelAvailableGHZ2(12))
        assertFalse(find(Locale.UK.country).channelAvailableGHZ2(0))
        assertFalse(find(Locale.UK.country).channelAvailableGHZ2(14))
    }

    @Test
    fun testChannelAvailableWithGHZ5() {
        assertTrue(find(Locale.US.country).channelAvailableGHZ5(36))
        assertTrue(find(Locale.US.country).channelAvailableGHZ5(165))
        assertTrue(find(Locale.UK.country).channelAvailableGHZ5(36))
        assertTrue(find(Locale.UK.country).channelAvailableGHZ5(140))
        assertTrue(find("AE").channelAvailableGHZ5(36))
        assertTrue(find("AE").channelAvailableGHZ5(64))
    }

    @Test
    fun testChannelAvailableWithGHZ6() {
        assertTrue(find(Locale.US.country).channelAvailableGHZ6(1))
        assertTrue(find(Locale.US.country).channelAvailableGHZ6(93))
        assertTrue(find(Locale.UK.country).channelAvailableGHZ6(1))
        assertTrue(find(Locale.UK.country).channelAvailableGHZ6(93))
        assertTrue(find("AE").channelAvailableGHZ6(1))
        assertTrue(find("AE").channelAvailableGHZ6(93))
    }

    @Test
    fun testFindCorrectlyPopulatesGHZ() {
        // setup
        val expectedCountryCode = Locale.US.country
        val expectedGHZ2: Set<Int> = WiFiChannelCountryGHZ2().findChannels(expectedCountryCode)
        val expectedGHZ5: Set<Int> = WiFiChannelCountryGHZ5().findChannels(expectedCountryCode)
        val expectedGHZ6: Set<Int> = WiFiChannelCountryGHZ6().findChannels()
        // execute
        val actual: WiFiChannelCountry = find(expectedCountryCode)
        // validate
        assertEquals(expectedCountryCode, actual.countryCode())
        assertArrayEquals(expectedGHZ2.toTypedArray(), actual.channelsGHZ2().toTypedArray())
        assertArrayEquals(expectedGHZ5.toTypedArray(), actual.channelsGHZ5().toTypedArray())
        assertArrayEquals(expectedGHZ6.toTypedArray(), actual.channelsGHZ6().toTypedArray())
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