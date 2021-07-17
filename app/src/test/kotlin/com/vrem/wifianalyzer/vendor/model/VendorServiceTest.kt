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
package com.vrem.wifianalyzer.vendor.model

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class VendorServiceTest {
    private val vendorName = "CISCO SYSTEMS INC"
    private val macAddress = "00:23:AB:8C:DF:10"
    private val vendorNameInvalid = "XXXXX"
    private val macAddressInvalid = "XX:XX:XX"
    private val vendorSize = 18124
    private val macsSize = 30293
    private val macsCiscoSize = 987
    private val filterVendor = "1394 "
    private val filterMac = "00:A0:2"
    private val expectedVendorName1 = "1394 TRADE ASSOCIATION"
    private val expectedVendorName2 = "TRANSITIONS RESEARCH CORP"
    private val expectedVendorName3 = "1394 PRINTER WORKING GROUP"
    private val expectedMac1 = "00:00:0C"
    private val expectedMac2 = "FC:FB:FB"
    private val expectedMac3 = "04:76:B0"

    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val fixture = VendorService(mainActivity.resources)

    @Test
    fun testFindVendorNameUsingLowerCase() {
        // execute
        val actual = fixture.findVendorName(macAddress.lowercase())
        // validate
        assertEquals(vendorName, actual)
    }

    @Test
    fun testFindVendorNameWithInvalidMac() {
        // execute
        val actual = fixture.findVendorName(macAddressInvalid)
        // validate
        assertTrue(actual.isEmpty())
    }

    @Test
    fun testFindVendorNameUsingDefault() {
        // execute
        val actual = fixture.findVendorName()
        // validate
        assertTrue(actual.isEmpty())
    }

    @Test
    fun testFindMacAddresses() {
        // setup
        // execute
        val actual = fixture.findMacAddresses(vendorName)
        // validate
        assertEquals(macsCiscoSize, actual.size)
        assertEquals(expectedMac1, actual[0])
        assertEquals(expectedMac2, actual[macsCiscoSize - 1])
        assertEquals(expectedMac3, actual[macsCiscoSize / 2])
    }

    @Test
    fun testFindMacAddressesUsingLowerCase() {
        // setup
        // execute
        val actual = fixture.findMacAddresses(vendorName.lowercase())
        // validate
        assertEquals(macsCiscoSize, actual.size)
        assertEquals(expectedMac1, actual[0])
        assertEquals(expectedMac2, actual[macsCiscoSize - 1])
        assertEquals(expectedMac3, actual[macsCiscoSize / 2])
    }

    @Test
    fun testFindMacAddressesWithInvalidName() {
        // execute
        val actual = fixture.findMacAddresses(vendorNameInvalid)
        // validate
        assertTrue(actual.isEmpty())
    }

    @Test
    fun testFindMacAddressesWithDefault() {
        // setup
        // execute
        val actual = fixture.findMacAddresses()
        // validate
        assertTrue(actual.isEmpty())
    }

    @Test
    fun testFindVendors() {
        // execute
        val actual = fixture.findVendors()
        // validate
        assertEquals(vendorSize, actual.size)
    }

    @Test
    fun testFindVendorsWithVendorFilter() {
        // execute
        val actual = fixture.findVendors(filterVendor)
        // validate
        assertEquals(2, actual.size)
        assertEquals(expectedVendorName3, actual[0])
        assertEquals(expectedVendorName1, actual[1])
    }

    @Test
    fun testFindVendorsWithVendorFilterUsingLowerCase() {
        // execute
        val actual = fixture.findVendors(filterVendor.lowercase())
        // validate
        assertEquals(2, actual.size)
        assertEquals(expectedVendorName3, actual[0])
        assertEquals(expectedVendorName1, actual[1])
    }

    @Test
    fun testFindVendorsWithMacFilter() {
        // execute
        val actual = fixture.findVendors(filterMac)
        // validate
        assertEquals(16, actual.size)
        assertEquals(expectedVendorName1, actual[0])
        assertEquals(expectedVendorName2, actual[15])
    }

    @Test
    fun testFindVendorsWithMacFilterUsingLowerCase() {
        // execute
        val actual = fixture.findVendors(filterMac.lowercase())
        // validate
        assertEquals(16, actual.size)
        assertEquals(expectedVendorName1, actual[0])
        assertEquals(expectedVendorName2, actual[15])
    }

    @Test
    fun testFindMacs() {
        // execute
        val actual = fixture.findMacs()
        // validate
        assertEquals(macsSize, actual.size)
    }

}