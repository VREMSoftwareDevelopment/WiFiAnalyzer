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
package com.vrem.wifianalyzer.wifi.band

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.band.WiFiBand.Companion.find
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

class WiFiBandTest {
    private val wiFiManagerWrapper = MainContextHelper.INSTANCE.wiFiManagerWrapper

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
        verifyNoMoreInteractions(wiFiManagerWrapper)
    }


    @Test
    fun testWiFiBand() {
        assertEquals(3, WiFiBand.values().size)
    }

    @Test
    fun testAvailable() {
        assertTrue(WiFiBand.GHZ2.available.javaClass.isInstance(availableGHZ2))
        assertTrue(WiFiBand.GHZ5.available.javaClass.isInstance(availableGHZ5))
        assertTrue(WiFiBand.GHZ6.available.javaClass.isInstance(availableGHZ6))
    }

    @Test
    fun testTextResource() {
        assertEquals(R.string.wifi_band_2ghz, WiFiBand.GHZ2.textResource)
        assertEquals(R.string.wifi_band_5ghz, WiFiBand.GHZ5.textResource)
        assertEquals(R.string.wifi_band_6ghz, WiFiBand.GHZ6.textResource)
    }

    @Test
    fun testToggle() {
        assertEquals(WiFiBand.GHZ5, WiFiBand.GHZ2.toggle())
        assertEquals(WiFiBand.GHZ2, WiFiBand.GHZ5.toggle())
    }

    @Test
    fun testGhz5() {
        assertFalse(WiFiBand.GHZ2.ghz5())
        assertTrue(WiFiBand.GHZ5.ghz5())
        assertFalse(WiFiBand.GHZ6.ghz5())
    }

    @Test
    fun testWiFiBandFind() {
        assertEquals(WiFiBand.GHZ2, find(2399))
        assertEquals(WiFiBand.GHZ2, find(2400))
        assertEquals(WiFiBand.GHZ2, find(2499))
        assertEquals(WiFiBand.GHZ2, find(2500))

        assertEquals(WiFiBand.GHZ2, find(4899))
        assertEquals(WiFiBand.GHZ5, find(4900))
        assertEquals(WiFiBand.GHZ5, find(5899))
        assertEquals(WiFiBand.GHZ2, find(5900))

        assertEquals(WiFiBand.GHZ2, find(5924))
        assertEquals(WiFiBand.GHZ6, find(5925))
        assertEquals(WiFiBand.GHZ6, find(7125))
        assertEquals(WiFiBand.GHZ2, find(7126))
    }

    @Test
    fun testAvailableGHZ2() {
        // execute
        val actual = WiFiBand.GHZ2.available()
        // validate
        assertTrue(actual)
    }

    @Test
    fun testAvailableGHZ5() {
        // setup
        whenever(wiFiManagerWrapper.is5GHzBandSupported()).thenReturn(true)
        // execute
        val actual = WiFiBand.GHZ5.available()
        // validate
        assertTrue(actual)
        verify(wiFiManagerWrapper).is5GHzBandSupported()
    }

    @Test
    fun testAvailableGHZ6() {
        // setup
        whenever(wiFiManagerWrapper.is6GHzBandSupported()).thenReturn(true)
        // execute
        val actual = WiFiBand.GHZ6.available()
        // validate
        assertTrue(actual)
        verify(wiFiManagerWrapper).is6GHzBandSupported()
    }

}