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
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WiFiIdentifierTest {
    private val hidden = "*hidden*"
    private val ssid = "xyzSSID"
    private val bssid = "xyzBSSID"
    private val fixture = WiFiIdentifier(ssid, bssid)

    @Test
    fun testWiFiIdentifier() {
        // setup
        val expectedTitle = "$ssid ($bssid)"
        // validate
        assertEquals(ssid, fixture.ssidRaw)
        assertEquals(ssid, fixture.ssid)
        assertEquals(bssid, fixture.bssid)
        assertEquals(expectedTitle, fixture.title)
    }

    @Test
    fun testTitleWithEmptySSID() {
        // setup
        val expectedTitle = "*hidden* ($bssid)"
        val fixture = WiFiIdentifier(String.EMPTY, bssid)
        // validate
        assertEquals(expectedTitle, fixture.title)
    }

    @Test
    fun testEquals() {
        // setup
        val other = WiFiIdentifier(ssid, bssid)
        // execute & validate
        assertEquals(fixture, other)
        Assert.assertNotSame(fixture, other)
    }

    @Test
    fun testHashCode() {
        // setup
        val other = WiFiIdentifier(ssid, bssid)
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode())
    }

    @Test
    fun testEqualsIgnoreCase() {
        // setup
        val other = WiFiIdentifier(ssid.lowercase(), bssid.uppercase())
        // execute & validate
        assertTrue(fixture.equals(other, true))
    }

    @Test
    fun testCompareTo() {
        // setup
        val other = WiFiIdentifier(ssid, bssid)
        // execute & validate
        assertEquals(0, fixture.compareTo(other))
    }

    @Test
    fun testRawSSID() {
        // setup
        val fixture = WiFiIdentifier(String.EMPTY, bssid)
        // execute & validate
        assertEquals(String.EMPTY, fixture.ssidRaw)
        assertEquals(hidden, fixture.ssid)
    }

}