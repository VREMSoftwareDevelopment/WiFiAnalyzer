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

import com.nhaarman.mockitokotlin2.*
import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.predicate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WiFiDataTest {
    private val ipAddress = "21.205.91.7"
    private val vendorName = "VendorName+"
    private val linkSpeed = 21
    private val ssid1 = "SSID1"
    private val ssid2 = "SSID2"
    private val ssid3 = "SSID3"
    private val ssid4 = "SSID4"
    private val bssid1 = "B$ssid1"
    private val bssid2 = "B$ssid2"
    private val bssid3 = "B$ssid3"
    private val bssid4 = "B$ssid4"
    private val frequency1 = 2412
    private val frequency2 = 2417
    private val frequency3 = 2422
    private val frequency4 = 2422
    private val level0 = -5
    private val level1 = -4
    private val level2 = -3
    private val vendorService = INSTANCE.vendorService
    private val wiFiIdentifier = WiFiIdentifier(ssid1, bssid1)
    private val wiFiConnection = WiFiConnection(wiFiIdentifier, ipAddress, linkSpeed)
    private val wiFiDetails = withWiFiDetails()
    private val fixture = WiFiData(wiFiDetails, wiFiConnection)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(vendorService)
        INSTANCE.restore()
    }

    @Test
    fun testConnection() {
        // setup
        whenever(vendorService.findVendorName(bssid1)).thenReturn(vendorName)
        // execute
        val actual: WiFiDetail = fixture.connection()
        // validate
        assertEquals(wiFiIdentifier, actual.wiFiIdentifier)
        assertEquals(vendorName, actual.wiFiAdditional.vendorName)
        assertEquals(ipAddress, actual.wiFiAdditional.wiFiConnection.ipAddress)
        verify(vendorService).findVendorName(bssid1)
    }

    @Test
    fun testWiFiDetailsWithConfiguredNetwork() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID)
        // validate
        assertEquals(7, actual.size)
        val wiFiDetail: WiFiDetail = actual[0]
        assertEquals(wiFiIdentifier, wiFiDetail.wiFiIdentifier)
        assertEquals(ipAddress, wiFiDetail.wiFiAdditional.wiFiConnection.ipAddress)
        assertTrue(actual[1].wiFiAdditional.wiFiConnection.ipAddress.isEmpty())
        assertTrue(actual[2].wiFiAdditional.wiFiConnection.ipAddress.isEmpty())
        assertTrue(actual[3].wiFiAdditional.wiFiConnection.ipAddress.isEmpty())
        assertTrue(actual[4].wiFiAdditional.wiFiConnection.ipAddress.isEmpty())
        assertTrue(actual[5].wiFiAdditional.wiFiConnection.ipAddress.isEmpty())
        assertTrue(actual[6].wiFiAdditional.wiFiConnection.ipAddress.isEmpty())
        verifyVendorNames()
    }

    @Test
    fun testWiFiDetailsWithVendorName() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.NONE)
        // validate
        assertEquals(7, actual.size)
        assertEquals(vendorName + bssid2, actual[0].wiFiAdditional.vendorName)
        assertEquals(vendorName + bssid4, actual[1].wiFiAdditional.vendorName)
        assertEquals(vendorName + bssid1, actual[2].wiFiAdditional.vendorName)
        assertEquals(vendorName + bssid2 + "_2", actual[3].wiFiAdditional.vendorName)
        assertEquals(vendorName + bssid2 + "_3", actual[4].wiFiAdditional.vendorName)
        assertEquals(vendorName + bssid3, actual[5].wiFiAdditional.vendorName)
        assertEquals(vendorName + bssid2 + "_1", actual[6].wiFiAdditional.vendorName)
        verify(vendorService, times(7)).findVendorName(any())
        verifyVendorNames()
    }

    @Test
    fun testWiFiDetailsSortByStrengthGroupByNone() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH)
        // validate
        assertEquals(7, actual.size)
        assertEquals(bssid2, actual[0].wiFiIdentifier.bssid)
        assertEquals(bssid4, actual[1].wiFiIdentifier.bssid)
        assertEquals(bssid1, actual[2].wiFiIdentifier.bssid)
        assertEquals(bssid2 + "_2", actual[3].wiFiIdentifier.bssid)
        assertEquals(bssid2 + "_3", actual[4].wiFiIdentifier.bssid)
        assertEquals(bssid3, actual[5].wiFiIdentifier.bssid)
        assertEquals(bssid2 + "_1", actual[6].wiFiIdentifier.bssid)
        verifyChildren(actual)
        verifyVendorNames()
    }

    @Test
    fun testWiFiDetailsSortByStrengthGroupBySSID() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.SSID)
        // validate
        assertEquals(4, actual.size)
        assertEquals(ssid2, actual[0].wiFiIdentifier.ssid)
        assertEquals(ssid4, actual[1].wiFiIdentifier.ssid)
        assertEquals(ssid1, actual[2].wiFiIdentifier.ssid)
        assertEquals(ssid3, actual[3].wiFiIdentifier.ssid)
        verifyChildren(actual, 0)
        verifyVendorNames()
    }

    @Test
    fun testWiFiDetailsSortByStrengthGroupByChannel() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.CHANNEL)
        // validate
        assertEquals(3, actual.size)
        assertEquals(ssid2, actual[0].wiFiIdentifier.ssid)
        assertEquals(ssid4, actual[1].wiFiIdentifier.ssid)
        assertEquals(ssid1, actual[2].wiFiIdentifier.ssid)
        verifyChildrenGroupByChannel(actual, 2, 0, 1)
        verifyVendorNames()
    }

    @Test
    fun testWiFiDetailsSortBySSIDGroupByNone() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID)
        // validate
        assertEquals(7, actual.size)
        assertEquals(bssid1, actual[0].wiFiIdentifier.bssid)
        assertEquals(bssid2, actual[1].wiFiIdentifier.bssid)
        assertEquals(bssid2 + "_2", actual[2].wiFiIdentifier.bssid)
        assertEquals(bssid2 + "_3", actual[3].wiFiIdentifier.bssid)
        assertEquals(bssid2 + "_1", actual[4].wiFiIdentifier.bssid)
        assertEquals(bssid3, actual[5].wiFiIdentifier.bssid)
        assertEquals(bssid4, actual[6].wiFiIdentifier.bssid)
        verifyChildren(actual)
        verifyVendorNames()
    }

    @Test
    fun testWiFiDetailsSortBySSIDGroupBySSID() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID, GroupBy.SSID)
        // validate
        assertEquals(4, actual.size)
        assertEquals(ssid1, actual[0].wiFiIdentifier.ssid)
        assertEquals(ssid2, actual[1].wiFiIdentifier.ssid)
        assertEquals(ssid3, actual[2].wiFiIdentifier.ssid)
        assertEquals(ssid4, actual[3].wiFiIdentifier.ssid)
        verifyChildren(actual, 1)
        verifyVendorNames()
    }

    @Test
    fun testWiFiDetailsSortBySSIDGroupByChannel() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID, GroupBy.CHANNEL)
        // validate
        assertEquals(3, actual.size)
        assertEquals(ssid1, actual[0].wiFiIdentifier.ssid)
        assertEquals(ssid2, actual[1].wiFiIdentifier.ssid)
        assertEquals(ssid4, actual[2].wiFiIdentifier.ssid)
        verifyChildrenGroupByChannel(actual, 0, 1, 2)
        verifyVendorNames()
    }

    @Test
    fun testWiFiDetailsSortByChannelGroupByNone() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.CHANNEL)
        // validate
        assertEquals(7, actual.size)
        assertEquals(bssid1, actual[0].wiFiIdentifier.bssid)
        assertEquals(bssid2, actual[1].wiFiIdentifier.bssid)
        assertEquals(bssid2 + "_2", actual[2].wiFiIdentifier.bssid)
        assertEquals(bssid2 + "_3", actual[3].wiFiIdentifier.bssid)
        assertEquals(bssid2 + "_1", actual[4].wiFiIdentifier.bssid)
        assertEquals(bssid4, actual[5].wiFiIdentifier.bssid)
        assertEquals(bssid3, actual[6].wiFiIdentifier.bssid)
        verifyChildren(actual)
        verifyVendorNames()
    }

    @Test
    fun testWiFiDetailsSortByChannelGroupBySSID() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.CHANNEL, GroupBy.SSID)
        // validate
        assertEquals(4, actual.size)
        assertEquals(ssid1, actual[0].wiFiIdentifier.ssid)
        assertEquals(ssid2, actual[1].wiFiIdentifier.ssid)
        assertEquals(ssid4, actual[2].wiFiIdentifier.ssid)
        assertEquals(ssid3, actual[3].wiFiIdentifier.ssid)
        verifyChildren(actual, 1)
        verifyVendorNames()
    }

    @Test
    fun testWiFiDetailsSortByChannelGroupByChannel() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.CHANNEL, GroupBy.CHANNEL)
        // validate
        assertEquals(3, actual.size)
        assertEquals(ssid1, actual[0].wiFiIdentifier.ssid)
        assertEquals(ssid2, actual[1].wiFiIdentifier.ssid)
        assertEquals(ssid4, actual[2].wiFiIdentifier.ssid)
        verifyChildrenGroupByChannel(actual, 0, 1, 2)
        verifyVendorNames()
    }

    private fun withVendorNames() {
        wiFiDetails.forEach {
            whenever(vendorService.findVendorName(it.wiFiIdentifier.bssid)).thenReturn(vendorName + it.wiFiIdentifier.bssid)
        }
    }

    private fun withWiFiDetails(): List<WiFiDetail> {
        val wiFiDetail1 = WiFiDetail(
                WiFiIdentifier(ssid1, bssid1),
                String.EMPTY,
                WiFiSignal(frequency1, frequency1, WiFiWidth.MHZ_20, level1, true))
        val wiFiDetail2 = WiFiDetail(
                WiFiIdentifier(ssid2, bssid2),
                String.EMPTY,
                WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2, true))
        val wiFiDetail3 = WiFiDetail(
                WiFiIdentifier(ssid3, bssid3),
                String.EMPTY,
                WiFiSignal(frequency3, frequency3, WiFiWidth.MHZ_20, level0, true))
        val wiFiDetail4 = WiFiDetail(
                WiFiIdentifier(ssid4, bssid4),
                String.EMPTY,
                WiFiSignal(frequency4, frequency4, WiFiWidth.MHZ_20, level2, true))
        val wiFiDetail21 = WiFiDetail(
                WiFiIdentifier(ssid2, bssid2 + "_1"),
                String.EMPTY,
                WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2 - 3, true))
        val wiFiDetail22 = WiFiDetail(
                WiFiIdentifier(ssid2, bssid2 + "_2"),
                String.EMPTY,
                WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2 - 1, true))
        val wiFiDetail23 = WiFiDetail(
                WiFiIdentifier(ssid2, bssid2 + "_3"),
                String.EMPTY,
                WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2 - 2, true))
        return listOf(wiFiDetail23, wiFiDetail3, wiFiDetail22, wiFiDetail1, wiFiDetail21, wiFiDetail2, wiFiDetail4)
    }

    private fun verifyChildren(actual: List<WiFiDetail>, index: Int) {
        actual.indices.forEach {
            val children: List<WiFiDetail> = actual[index].children
            if (it == index) {
                assertEquals(3, children.size)
                assertEquals(bssid2 + "_2", children[0].wiFiIdentifier.bssid)
                assertEquals(bssid2 + "_3", children[1].wiFiIdentifier.bssid)
                assertEquals(bssid2 + "_1", children[2].wiFiIdentifier.bssid)
            } else {
                assertTrue(actual[it].children.isEmpty())
            }
        }
    }

    private fun verifyChildrenGroupByChannel(actual: List<WiFiDetail>, indexEmpty: Int, indexWith3: Int, indexWith1: Int) {
        assertTrue(actual[indexEmpty].children.isEmpty())
        val children1: List<WiFiDetail> = actual[indexWith3].children
        assertEquals(3, children1.size)
        assertEquals(bssid2 + "_2", children1[0].wiFiIdentifier.bssid)
        assertEquals(bssid2 + "_3", children1[1].wiFiIdentifier.bssid)
        assertEquals(bssid2 + "_1", children1[2].wiFiIdentifier.bssid)
        val children2: List<WiFiDetail> = actual[indexWith1].children
        assertEquals(1, children2.size)
        assertEquals(bssid3, children2[0].wiFiIdentifier.bssid)
    }

    private fun verifyVendorNames() {
        wiFiDetails.forEach { verify(vendorService).findVendorName(it.wiFiIdentifier.bssid) }
    }

    private fun verifyChildren(actual: List<WiFiDetail>) {
        actual.forEach { assertTrue(it.children.isEmpty()) }
    }
}