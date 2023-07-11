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

package com.vrem.wifianalyzer.wifi.model

import com.vrem.util.EMPTY
import org.junit.Assert.assertEquals
import org.junit.Test

class WiFiVirtualTest {

    @Test
    fun testWiFiVirtualWithVirtualBSSID() {
        // setup
        val wiFiDetail = WiFiDetail(
            WiFiIdentifier("SSID1", "20:cf:30:ce:1d:71"),
            String.EMPTY,
            WiFiSignal(2432, 2432, WiFiWidth.bandwidth20MHz, -50, true),
            WiFiAdditional.EMPTY
        )
        // execute
        val actual = wiFiDetail.wiFiVirtual
        // validate
        assertEquals(":cf:30:ce:1d:7", actual.bssid)
        assertEquals(2432, actual.frequency)
        assertEquals(":cf:30:ce:1d:7-" + 2432, actual.key)
    }

    @Test
    fun testWiFiVirtualWithRegularBSSIDWhenBSSIDShort() {
        // setup
        val wiFiDetail = WiFiDetail(
            WiFiIdentifier("SSID1", "20:cf:30:ce:1d:7"),
            String.EMPTY,
            WiFiSignal(2432, 2432, WiFiWidth.bandwidth20MHz, -50, true),
            WiFiAdditional.EMPTY
        )
        // execute
        val actual = wiFiDetail.wiFiVirtual
        // validate
        assertEquals("20:cf:30:ce:1d:7", actual.bssid)
        assertEquals(2432, actual.frequency)
        assertEquals("20:cf:30:ce:1d:7-" + 2432, actual.key)
    }

    @Test
    fun testWiFiVirtualWithRegularBSSIDWhenBSSIDLong() {
        // setup
        val wiFiDetail = WiFiDetail(
            WiFiIdentifier("SSID1", "20:cf:30:ce:1d:71:"),
            String.EMPTY,
            WiFiSignal(2432, 2432, WiFiWidth.bandwidth20MHz, -50, true),
            WiFiAdditional.EMPTY
        )
        // execute
        val actual = wiFiDetail.wiFiVirtual
        // validate
        assertEquals("20:cf:30:ce:1d:71:", actual.bssid)
        assertEquals(2432, actual.frequency)
        assertEquals("20:cf:30:ce:1d:71:-" + 2432, actual.key)
    }

}
