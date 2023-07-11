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
        assertTrue(WiFiBand.band2GHz.available.javaClass.isInstance(available2GHz))
        assertTrue(WiFiBand.band5GHz.available.javaClass.isInstance(available5GHz))
        assertTrue(WiFiBand.band6GHz.available.javaClass.isInstance(available6GHz))
    }

    @Test
    fun testTextResource() {
        assertEquals(R.string.wifi_band_2ghz, WiFiBand.band2GHz.textResource)
        assertEquals(R.string.wifi_band_5ghz, WiFiBand.band5GHz.textResource)
        assertEquals(R.string.wifi_band_6ghz, WiFiBand.band6GHz.textResource)
    }

    @Test
    fun test5GHz() {
        assertFalse(WiFiBand.band2GHz.is5GHz)
        assertTrue(WiFiBand.band5GHz.is5GHz)
        assertFalse(WiFiBand.band6GHz.is5GHz)
    }

    @Test
    fun test2GHz() {
        assertTrue(WiFiBand.band2GHz.is2GHz)
        assertFalse(WiFiBand.band5GHz.is2GHz)
        assertFalse(WiFiBand.band6GHz.is2GHz)
    }

    @Test
    fun test6GHz() {
        assertFalse(WiFiBand.band2GHz.is6GHz)
        assertFalse(WiFiBand.band5GHz.is6GHz)
        assertTrue(WiFiBand.band6GHz.is6GHz)
    }

    @Test
    fun testWiFiBandFind() {
        assertEquals(WiFiBand.band2GHz, find(2399))
        assertEquals(WiFiBand.band2GHz, find(2400))
        assertEquals(WiFiBand.band2GHz, find(2499))
        assertEquals(WiFiBand.band2GHz, find(2500))

        assertEquals(WiFiBand.band2GHz, find(4899))
        assertEquals(WiFiBand.band5GHz, find(4900))
        assertEquals(WiFiBand.band5GHz, find(5899))
        assertEquals(WiFiBand.band2GHz, find(5900))

        assertEquals(WiFiBand.band2GHz, find(5924))
        assertEquals(WiFiBand.band6GHz, find(5925))
        assertEquals(WiFiBand.band6GHz, find(7125))
        assertEquals(WiFiBand.band2GHz, find(7126))
    }

    @Test
    fun testAvailable2GHz() {
        // execute
        val actual = WiFiBand.band2GHz.available()
        // validate
        assertTrue(actual)
    }

    @Test
    fun testAvailable5GHz() {
        // setup
        whenever(wiFiManagerWrapper.is5GHzBandSupported()).thenReturn(true)
        // execute
        val actual = WiFiBand.band5GHz.available()
        // validate
        assertTrue(actual)
        verify(wiFiManagerWrapper).is5GHzBandSupported()
    }

    @Test
    fun testAvailable6GHz() {
        // setup
        whenever(wiFiManagerWrapper.is6GHzBandSupported()).thenReturn(true)
        // execute
        val actual = WiFiBand.band6GHz.available()
        // validate
        assertTrue(actual)
        verify(wiFiManagerWrapper).is6GHzBandSupported()
    }

}
