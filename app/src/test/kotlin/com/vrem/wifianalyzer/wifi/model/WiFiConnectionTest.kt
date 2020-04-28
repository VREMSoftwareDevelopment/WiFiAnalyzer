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
package com.vrem.wifianalyzer.wifi.model

import com.vrem.util.STRING_EMPTY
import org.junit.Assert.*
import org.junit.Test

class WiFiConnectionTest {
    private val SSID = "SSID-123"
    private val BSSID = "BSSID-123"
    private val ipAddress = "21.205.91.7"
    private val linkSpeed = 21
    private val fixture: WiFiConnection = WiFiConnection(SSID, BSSID, ipAddress, linkSpeed)

    @Test
    fun testWiFiConnectionEmpty() {
        // validate
        assertEquals(STRING_EMPTY, WiFiConnection.EMPTY.SSID)
        assertEquals(STRING_EMPTY, WiFiConnection.EMPTY.BSSID)
        assertEquals(STRING_EMPTY, WiFiConnection.EMPTY.ipAddress)
        assertEquals(WiFiConnection.LINK_SPEED_INVALID, WiFiConnection.EMPTY.linkSpeed)
        assertFalse(WiFiConnection.EMPTY.connected())
    }

    @Test
    fun testWiFiConnection() {
        // validate
        assertEquals(SSID, fixture.SSID)
        assertEquals(BSSID, fixture.BSSID)
        assertEquals(ipAddress, fixture.ipAddress)
        assertEquals(linkSpeed, fixture.linkSpeed)
        assertTrue(fixture.connected())
    }

    @Test
    fun testEquals() {
        // setup
        val other = WiFiConnection(SSID, BSSID, STRING_EMPTY, WiFiConnection.LINK_SPEED_INVALID)
        // execute & validate
        assertEquals(fixture, other)
        assertNotSame(fixture, other)
    }

    @Test
    fun testHashCode() {
        // setup
        val other = WiFiConnection(SSID, BSSID, STRING_EMPTY, WiFiConnection.LINK_SPEED_INVALID)
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode())
    }

}