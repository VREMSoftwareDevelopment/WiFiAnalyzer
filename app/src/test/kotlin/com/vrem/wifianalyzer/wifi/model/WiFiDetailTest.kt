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

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class WiFiDetailTest {
    private val frequency = 2435
    private val level = -40
    private val vendorName = "VendorName"
    private val capabilities = "WPA-WPA2"
    private val ssid = "xyzSSID"
    private val bssid = "xyzBSSID"
    private val wiFiSignal = WiFiSignal(frequency, frequency, WiFiWidth.MHZ_20, level, true)
    private val wiFiAdditional = WiFiAdditional(vendorName, WiFiConnection.EMPTY)
    private val wiFiIdentifier = WiFiIdentifier(ssid, bssid)
    private val fixture = WiFiDetail(wiFiIdentifier, capabilities, wiFiSignal, wiFiAdditional)

    @Test
    fun testWiFiDetail() {
        // validate
        assertEquals(wiFiSignal, fixture.wiFiSignal)
        assertEquals(wiFiAdditional, fixture.wiFiAdditional)
        assertEquals(wiFiIdentifier, fixture.wiFiIdentifier)
        assertEquals(capabilities, fixture.capabilities)
        assertEquals(Security.WPA, fixture.security)
        assertEquals(setOf(Security.WPA, Security.WPA2), fixture.securities)
    }

    @Test
    fun testEquals() {
        // setup
        val other = WiFiDetail(wiFiIdentifier, capabilities, wiFiSignal)
        // execute & validate
        assertEquals(fixture, other)
        Assert.assertNotSame(fixture, other)
    }

    @Test
    fun testHashCode() {
        // setup
        val other = WiFiDetail(wiFiIdentifier, capabilities, wiFiSignal)
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode())
    }

    @Test
    fun testCompareTo() {
        // setup
        val other = WiFiDetail(wiFiIdentifier, capabilities, wiFiSignal)
        // execute & validate
        assertEquals(0, fixture.compareTo(other))
    }

    @Test
    fun testWiFiDetailCopyConstructor() {
        // setup
        val expected = WiFiDetail(wiFiIdentifier, capabilities, wiFiSignal)
        // execute
        val actual = WiFiDetail(expected, expected.wiFiAdditional)
        // validate
        assertEquals(expected, actual)
        assertEquals(expected.wiFiIdentifier, actual.wiFiIdentifier)
        assertEquals(expected.capabilities, actual.capabilities)
        assertEquals(expected.security, actual.security)
        assertEquals(expected.securities, actual.securities)
        assertEquals(expected.wiFiAdditional, actual.wiFiAdditional)
        assertEquals(expected.wiFiSignal, actual.wiFiSignal)
    }

}