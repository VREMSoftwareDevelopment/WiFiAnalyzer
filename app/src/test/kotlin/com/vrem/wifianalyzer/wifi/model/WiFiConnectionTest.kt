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

import com.vrem.util.EMPTY
import org.junit.Assert.*
import org.junit.Test

class WiFiConnectionTest {
    private val ipAddress = "21.205.91.7"
    private val linkSpeed = 21
    private val wiFiIdentifier = WiFiIdentifier("SSID-123", "BSSID-123")
    private val fixture: WiFiConnection = WiFiConnection(wiFiIdentifier, ipAddress, linkSpeed)

    @Test
    fun testWiFiConnectionEmpty() {
        // validate
        assertEquals(WiFiIdentifier.EMPTY, WiFiConnection.EMPTY.wiFiIdentifier)
        assertEquals(String.EMPTY, WiFiConnection.EMPTY.ipAddress)
        assertEquals(WiFiConnection.LINK_SPEED_INVALID, WiFiConnection.EMPTY.linkSpeed)
        assertFalse(WiFiConnection.EMPTY.connected)
    }

    @Test
    fun testWiFiConnection() {
        // validate
        assertEquals(wiFiIdentifier, fixture.wiFiIdentifier)
        assertEquals(ipAddress, fixture.ipAddress)
        assertEquals(linkSpeed, fixture.linkSpeed)
        assertTrue(fixture.connected)
    }

    @Test
    fun testEquals() {
        // setup
        val wiFiIdentifier = WiFiIdentifier("SSID-123", "BSSID-123")
        val other = WiFiConnection(wiFiIdentifier, String.EMPTY, WiFiConnection.LINK_SPEED_INVALID)
        // execute & validate
        assertEquals(fixture, other)
        assertNotSame(fixture, other)
    }

    @Test
    fun testHashCode() {
        // setup
        val wiFiIdentifier = WiFiIdentifier("SSID-123", "BSSID-123")
        val other = WiFiConnection(wiFiIdentifier, String.EMPTY, WiFiConnection.LINK_SPEED_INVALID)
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode())
    }

}