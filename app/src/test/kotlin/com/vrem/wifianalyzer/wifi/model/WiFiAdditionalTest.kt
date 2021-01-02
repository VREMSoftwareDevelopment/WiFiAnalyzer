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
package com.vrem.wifianalyzer.wifi.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WiFiAdditionalTest {
    private val vendorName = "VendorName"

    @Test
    fun testWiFiAdditionalWithWiFiConnection() {
        // setup
        val wiFiConnection = WiFiConnection(WiFiIdentifier("SSID", "BSSID"), "192.168.1.10", 22)
        // execute
        val fixture = WiFiAdditional(vendorName, wiFiConnection)
        // validate
        assertEquals(vendorName, fixture.vendorName)
        assertEquals(wiFiConnection, fixture.wiFiConnection)
    }

    @Test
    fun testWiFiAdditional() {
        // execute
        val fixture = WiFiAdditional(vendorName, WiFiConnection.EMPTY)
        // validate
        assertEquals(vendorName, fixture.vendorName)
    }

    @Test
    fun testWiFiAdditionalEmpty() {
        // validate
        assertTrue(WiFiAdditional.EMPTY.vendorName.isEmpty())
    }

}