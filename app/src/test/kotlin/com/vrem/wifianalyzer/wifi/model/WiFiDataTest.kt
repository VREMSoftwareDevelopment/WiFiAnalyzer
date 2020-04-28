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

import com.nhaarman.mockitokotlin2.whenever
import com.vrem.util.STRING_EMPTY
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiWidth
import com.vrem.wifianalyzer.wifi.predicate.WiFiBandPredicate
import org.apache.commons.collections4.Predicate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WiFiDataTest {
    private val ipAddress = "21.205.91.7"
    private val vendorName = "VendorName+"
    private val linkSpeed = 21
    private val SSID_1 = "SSID1"
    private val SSID_2 = "SSID2"
    private val SSID_3 = "SSID3"
    private val SSID_4 = "SSID4"
    private val BSSID_1 = "B$SSID_1"
    private val BSSID_2 = "B$SSID_2"
    private val BSSID_3 = "B$SSID_3"
    private val BSSID_4 = "B$SSID_4"
    private val frequency1 = 2412
    private val frequency2 = 2417
    private val frequency3 = 2422
    private val frequency4 = 2422
    private val level0 = -5
    private val level1 = -4
    private val level2 = -3
    private val vendorService = MainContextHelper.INSTANCE.vendorService
    private val wiFiConnection = WiFiConnection(SSID_1, BSSID_1, ipAddress, linkSpeed)
    private val wiFiDetails = withWiFiDetails()
    private val fixture = WiFiData(wiFiDetails, wiFiConnection)

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions(vendorService)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testGetConnection() {
        // setup
        whenever(vendorService.findVendorName(BSSID_1)).thenReturn(vendorName)
        // execute
        val actual: WiFiDetail = fixture.connection()
        // validate
        assertEquals(SSID_1, actual.SSID)
        assertEquals(BSSID_1, actual.BSSID)
        assertEquals(vendorName, actual.wiFiAdditional.vendorName)
        assertEquals(ipAddress, actual.wiFiAdditional.wiFiConnection.ipAddress)
        verify(vendorService).findVendorName(BSSID_1)
    }

    @Test
    fun testGetWiFiDetailsWithConfiguredNetwork() {
        // setup
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(WiFiBand.GHZ2)
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID)
        // validate
        assertEquals(7, actual.size.toLong())
        val wiFiDetail: WiFiDetail = actual[0]
        assertEquals(SSID_1, wiFiDetail.SSID)
        assertEquals(BSSID_1, wiFiDetail.BSSID)
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
    fun testGetWiFiDetailsWithVendorName() {
        // setup
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(WiFiBand.GHZ2)
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.NONE)
        // validate
        assertEquals(7, actual.size.toLong())
        assertEquals(vendorName + BSSID_2, actual[0].wiFiAdditional.vendorName)
        assertEquals(vendorName + BSSID_4, actual[1].wiFiAdditional.vendorName)
        assertEquals(vendorName + BSSID_1, actual[2].wiFiAdditional.vendorName)
        assertEquals(vendorName + BSSID_2 + "_2", actual[3].wiFiAdditional.vendorName)
        assertEquals(vendorName + BSSID_2 + "_3", actual[4].wiFiAdditional.vendorName)
        assertEquals(vendorName + BSSID_3, actual[5].wiFiAdditional.vendorName)
        assertEquals(vendorName + BSSID_2 + "_1", actual[6].wiFiAdditional.vendorName)
        verify(vendorService, Mockito.times(7)).findVendorName(ArgumentMatchers.anyString())
        verifyVendorNames()
    }

    @Test
    fun testGetWiFiDetailsSortByStrengthGroupByNone() {
        // setup
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(WiFiBand.GHZ2)
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH)
        // validate
        assertEquals(7, actual.size.toLong())
        assertEquals(BSSID_2, actual[0].BSSID)
        assertEquals(BSSID_4, actual[1].BSSID)
        assertEquals(BSSID_1, actual[2].BSSID)
        assertEquals(BSSID_2 + "_2", actual[3].BSSID)
        assertEquals(BSSID_2 + "_3", actual[4].BSSID)
        assertEquals(BSSID_3, actual[5].BSSID)
        assertEquals(BSSID_2 + "_1", actual[6].BSSID)
        verifyChildren(actual)
        verifyVendorNames()
    }

    @Test
    fun testGetWiFiDetailsSortByStrengthGroupBySSID() {
        // setup
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(WiFiBand.GHZ2)
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.SSID)
        // validate
        assertEquals(4, actual.size.toLong())
        assertEquals(SSID_2, actual[0].SSID)
        assertEquals(SSID_4, actual[1].SSID)
        assertEquals(SSID_1, actual[2].SSID)
        assertEquals(SSID_3, actual[3].SSID)
        verifyChildren(actual, 0)
        verifyVendorNames()
    }

    @Test
    fun testGetWiFiDetailsSortByStrengthGroupByChannel() {
        // setup
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(WiFiBand.GHZ2)
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.CHANNEL)
        // validate
        assertEquals(3, actual.size.toLong())
        assertEquals(SSID_2, actual[0].SSID)
        assertEquals(SSID_4, actual[1].SSID)
        assertEquals(SSID_1, actual[2].SSID)
        verifyChildrenGroupByChannel(actual, 2, 0, 1)
        verifyVendorNames()
    }

    @Test
    fun testGetWiFiDetailsSortBySSIDGroupByNone() {
        // setup
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(WiFiBand.GHZ2)
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID)
        // validate
        assertEquals(7, actual.size.toLong())
        assertEquals(BSSID_1, actual[0].BSSID)
        assertEquals(BSSID_2, actual[1].BSSID)
        assertEquals(BSSID_2 + "_2", actual[2].BSSID)
        assertEquals(BSSID_2 + "_3", actual[3].BSSID)
        assertEquals(BSSID_2 + "_1", actual[4].BSSID)
        assertEquals(BSSID_3, actual[5].BSSID)
        assertEquals(BSSID_4, actual[6].BSSID)
        verifyChildren(actual)
        verifyVendorNames()
    }

    @Test
    fun testGetWiFiDetailsSortBySSIDGroupBySSID() {
        // setup
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(WiFiBand.GHZ2)
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID, GroupBy.SSID)
        // validate
        assertEquals(4, actual.size.toLong())
        assertEquals(SSID_1, actual[0].SSID)
        assertEquals(SSID_2, actual[1].SSID)
        assertEquals(SSID_3, actual[2].SSID)
        assertEquals(SSID_4, actual[3].SSID)
        verifyChildren(actual, 1)
        verifyVendorNames()
    }

    @Test
    fun testGetWiFiDetailsSortBySSIDGroupByChannel() {
        // setup
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(WiFiBand.GHZ2)
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID, GroupBy.CHANNEL)
        // validate
        assertEquals(3, actual.size.toLong())
        assertEquals(SSID_1, actual[0].SSID)
        assertEquals(SSID_2, actual[1].SSID)
        assertEquals(SSID_4, actual[2].SSID)
        verifyChildrenGroupByChannel(actual, 0, 1, 2)
        verifyVendorNames()
    }

    @Test
    fun testGetWiFiDetailsSortByChannelGroupByNone() {
        // setup
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(WiFiBand.GHZ2)
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.CHANNEL)
        // validate
        assertEquals(7, actual.size.toLong())
        assertEquals(BSSID_1, actual[0].BSSID)
        assertEquals(BSSID_2, actual[1].BSSID)
        assertEquals(BSSID_2 + "_2", actual[2].BSSID)
        assertEquals(BSSID_2 + "_3", actual[3].BSSID)
        assertEquals(BSSID_2 + "_1", actual[4].BSSID)
        assertEquals(BSSID_4, actual[5].BSSID)
        assertEquals(BSSID_3, actual[6].BSSID)
        verifyChildren(actual)
        verifyVendorNames()
    }

    @Test
    fun testGetWiFiDetailsSortByChannelGroupBySSID() {
        // setup
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(WiFiBand.GHZ2)
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.CHANNEL, GroupBy.SSID)
        // validate
        assertEquals(4, actual.size.toLong())
        assertEquals(SSID_1, actual[0].SSID)
        assertEquals(SSID_2, actual[1].SSID)
        assertEquals(SSID_4, actual[2].SSID)
        assertEquals(SSID_3, actual[3].SSID)
        verifyChildren(actual, 1)
        verifyVendorNames()
    }

    @Test
    fun testGetWiFiDetailsSortByChannelGroupByChannel() {
        // setup
        val predicate: Predicate<WiFiDetail> = WiFiBandPredicate(WiFiBand.GHZ2)
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.CHANNEL, GroupBy.CHANNEL)
        // validate
        assertEquals(3, actual.size.toLong())
        assertEquals(SSID_1, actual[0].SSID)
        assertEquals(SSID_2, actual[1].SSID)
        assertEquals(SSID_4, actual[2].SSID)
        verifyChildrenGroupByChannel(actual, 0, 1, 2)
        verifyVendorNames()
    }

    private fun withVendorNames() {
        wiFiDetails.forEach {
            whenever(vendorService.findVendorName(it.BSSID)).thenReturn(vendorName + it.BSSID)
        }
    }

    private fun withWiFiDetails(): List<WiFiDetail> {
        val wiFiDetail1 = WiFiDetail(SSID_1, BSSID_1, STRING_EMPTY,
                WiFiSignal(frequency1, frequency1, WiFiWidth.MHZ_20, level1, true))
        val wiFiDetail2 = WiFiDetail(SSID_2, BSSID_2, STRING_EMPTY,
                WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2, true))
        val wiFiDetail3 = WiFiDetail(SSID_3, BSSID_3, STRING_EMPTY,
                WiFiSignal(frequency3, frequency3, WiFiWidth.MHZ_20, level0, true))
        val wiFiDetail4 = WiFiDetail(SSID_4, BSSID_4, STRING_EMPTY,
                WiFiSignal(frequency4, frequency4, WiFiWidth.MHZ_20, level2, true))
        val wiFiDetail_1 = WiFiDetail(SSID_2, BSSID_2 + "_1", STRING_EMPTY,
                WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2 - 3, true))
        val wiFiDetail_2 = WiFiDetail(SSID_2, BSSID_2 + "_2", STRING_EMPTY,
                WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2 - 1, true))
        val wiFiDetail_3 = WiFiDetail(SSID_2, BSSID_2 + "_3", STRING_EMPTY,
                WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2 - 2, true))
        return listOf(wiFiDetail_3, wiFiDetail3, wiFiDetail_2, wiFiDetail1, wiFiDetail_1, wiFiDetail2, wiFiDetail4)
    }

    private fun verifyChildren(actual: List<WiFiDetail>, index: Int) {
        for (i in actual.indices) {
            val children: List<WiFiDetail> = actual[index].children
            if (i == index) {
                assertEquals(3, children.size.toLong())
                assertEquals(BSSID_2 + "_2", children[0].BSSID)
                assertEquals(BSSID_2 + "_3", children[1].BSSID)
                assertEquals(BSSID_2 + "_1", children[2].BSSID)
            } else {
                assertTrue(actual[i].children.isEmpty())
            }
        }
    }

    private fun verifyChildrenGroupByChannel(actual: List<WiFiDetail>, indexEmpty: Int, indexWith3: Int, indexWith1: Int) {
        assertTrue(actual[indexEmpty].children.isEmpty())
        val children1: List<WiFiDetail> = actual[indexWith3].children
        assertEquals(3, children1.size.toLong())
        assertEquals(BSSID_2 + "_2", children1[0].BSSID)
        assertEquals(BSSID_2 + "_3", children1[1].BSSID)
        assertEquals(BSSID_2 + "_1", children1[2].BSSID)
        val children2: List<WiFiDetail> = actual[indexWith1].children
        assertEquals(1, children2.size.toLong())
        assertEquals(BSSID_3, children2[0].BSSID)
    }

    private fun verifyVendorNames() {
        wiFiDetails.forEach { verify(vendorService).findVendorName(it.BSSID) }
    }

    private fun verifyChildren(actual: List<WiFiDetail>) {
        actual.forEach { assertTrue(it.children.isEmpty()) }
    }
}