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
package com.vrem.wifianalyzer.wifi.band

import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry.Companion.get
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class WiFiChannelCountryTest {
    @Test
    fun testIsChannelAvailableWithTrue() {
        assertTrue(get(Locale.US.country).channelAvailableGHZ2(1))
        assertTrue(get(Locale.US.country).channelAvailableGHZ2(11))
        assertTrue(get(Locale.US.country).channelAvailableGHZ5(36))
        assertTrue(get(Locale.US.country).channelAvailableGHZ5(165))
        assertTrue(get(Locale.UK.country).channelAvailableGHZ2(1))
        assertTrue(get(Locale.UK.country).channelAvailableGHZ2(13))
        assertTrue(get(Locale.UK.country).channelAvailableGHZ5(36))
        assertTrue(get(Locale.UK.country).channelAvailableGHZ5(140))
    }

    @Test
    fun testIsChannelAvailableWithGHZ2() {
        assertFalse(get(Locale.US.country).channelAvailableGHZ2(0))
        assertFalse(get(Locale.US.country).channelAvailableGHZ2(12))
        assertFalse(get(Locale.UK.country).channelAvailableGHZ2(0))
        assertFalse(get(Locale.UK.country).channelAvailableGHZ2(14))
    }

    @Test
    fun testIsChannelAvailableWithGHZ5() {
        assertTrue(get(Locale.US.country).channelAvailableGHZ5(36))
        assertTrue(get(Locale.US.country).channelAvailableGHZ5(165))
        assertTrue(get(Locale.UK.country).channelAvailableGHZ5(36))
        assertTrue(get(Locale.UK.country).channelAvailableGHZ5(140))
        assertTrue(get("AE").channelAvailableGHZ5(36))
        assertTrue(get("AE").channelAvailableGHZ5(64))
    }

    @Test
    fun testGetCorrectlyPopulatesGHZ() {
        // setup
        val expectedCountryCode = Locale.US.country
        val expectedGHZ2: Set<Int> = WiFiChannelCountryGHZ2().findChannels(expectedCountryCode)
        val expectedGHZ5: Set<Int> = WiFiChannelCountryGHZ5().findChannels(expectedCountryCode)
        // execute
        val actual: WiFiChannelCountry = get(expectedCountryCode)
        // validate
        assertEquals(expectedCountryCode, actual.countryCode())
        assertArrayEquals(expectedGHZ2.toTypedArray(), actual.channelsGHZ2().toTypedArray())
        assertArrayEquals(expectedGHZ5.toTypedArray(), actual.channelsGHZ5().toTypedArray())
    }

    @Test
    fun testGetCorrectlyPopulatesCountryCodeAndName() {
        // setup
        val expected = Locale.SIMPLIFIED_CHINESE
        val expectedCountryCode = expected.country
        // execute
        val actual: WiFiChannelCountry = get(expectedCountryCode)
        // validate
        assertEquals(expectedCountryCode, actual.countryCode())
        assertNotEquals(expected.displayCountry, actual.countryName(expected))
        assertEquals(expected.getDisplayCountry(expected), actual.countryName(expected))
    }
}