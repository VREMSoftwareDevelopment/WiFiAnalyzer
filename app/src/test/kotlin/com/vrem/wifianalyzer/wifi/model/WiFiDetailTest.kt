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

import com.google.common.collect.Sets
import com.vrem.wifianalyzer.wifi.band.WiFiWidth
import org.apache.commons.lang3.StringUtils
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class WiFiDetailTest {
    private val frequency = 2435
    private val level = -40
    private val vendorName = "VendorName"
    private val capabilities = "WPA-WPA2"
    private val SSID = "xyzSSID"
    private val BSSID = "xyzBSSID"

    private var wiFiSignal: WiFiSignal = WiFiSignal(frequency, frequency, WiFiWidth.MHZ_20, level, true)
    private var wiFiAdditional: WiFiAdditional = WiFiAdditional(vendorName, WiFiConnection.EMPTY)
    private var fixture: WiFiDetail = WiFiDetail(SSID, BSSID, capabilities, wiFiSignal, wiFiAdditional)

    @Test
    fun testWiFiDetail() {
        // setup
        val expectedTitle = "$SSID ($BSSID)"
        // validate
        assertEquals(wiFiSignal, fixture.wiFiSignal)
        assertEquals(wiFiAdditional, fixture.wiFiAdditional)
        assertEquals(SSID, fixture.rawSSID)
        assertEquals(SSID, fixture.SSID)
        assertEquals(BSSID, fixture.BSSID)
        assertEquals(capabilities, fixture.capabilities)
        assertEquals(expectedTitle, fixture.title())
        assertEquals(Security.WPA, fixture.security())
        assertEquals(Sets.newHashSet(Security.WPA, Security.WPA2), fixture.securities())
    }

    @Test
    fun testGetTitleWithEmptySSID() {
        // setup
        val expectedTitle = "*hidden* ($BSSID)"
        fixture = WiFiDetail(StringUtils.EMPTY, BSSID, capabilities, wiFiSignal)
        // validate
        assertEquals(expectedTitle, fixture.title())
    }

    @Test
    fun testEquals() {
        // setup
        val other = WiFiDetail(SSID, BSSID, capabilities, wiFiSignal)
        // execute & validate
        assertEquals(fixture, other)
        Assert.assertNotSame(fixture, other)
    }

    @Test
    fun testHashCode() {
        // setup
        val other = WiFiDetail(SSID, BSSID, capabilities, wiFiSignal)
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode())
    }

    @Test
    fun testCompareTo() {
        // setup
        val other = WiFiDetail(SSID, BSSID, capabilities, wiFiSignal)
        // execute & validate
        assertEquals(0, fixture.compareTo(other))
    }

    @Test
    fun testGetRawSSID() {
        // setup
        fixture = WiFiDetail(StringUtils.EMPTY, BSSID, capabilities, wiFiSignal)
        // execute & validate
        assertEquals(StringUtils.EMPTY, fixture.rawSSID)
        assertEquals(WiFiDetail.SSID_EMPTY, fixture.SSID)
    }

    @Test
    fun testWiFiDetailCopyConstructor() {
        // setup
        val expected = WiFiDetail(StringUtils.EMPTY, BSSID, capabilities, wiFiSignal)
        // execute
        val actual = WiFiDetail(expected, expected.wiFiAdditional)
        // validate
        assertEquals(expected, actual)
        assertEquals(expected.rawSSID, actual.rawSSID)
        assertEquals(expected.SSID, actual.SSID)
        assertEquals(expected.BSSID, actual.BSSID)
        assertEquals(expected.capabilities, actual.capabilities)
        assertEquals(expected.title(), actual.title())
        assertEquals(expected.security(), actual.security())
        assertEquals(expected.securities(), actual.securities())
        assertEquals(expected.wiFiAdditional, actual.wiFiAdditional)
        assertEquals(expected.wiFiSignal, actual.wiFiSignal)
    }

}