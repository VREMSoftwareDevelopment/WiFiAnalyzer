/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.wifianalyzer.MainContextHelper.INSTANCE
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.predicate
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.*

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
    fun connection() {
        // setup
        whenever(vendorService.findVendorName(bssid1)).thenReturn(vendorName)
        // execute
        val actual: WiFiDetail = fixture.connection()
        // validate
        assertThat(actual.wiFiIdentifier).isEqualTo(wiFiIdentifier)
        assertThat(actual.wiFiAdditional.vendorName).isEqualTo(vendorName)
        assertThat(actual.wiFiAdditional.wiFiConnection.ipAddress).isEqualTo(ipAddress)
        verify(vendorService).findVendorName(bssid1)
    }

    @Test
    fun wiFiDetailsWithConfiguredNetwork() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID)
        // validate
        assertThat(actual).hasSize(7)
        val wiFiDetail: WiFiDetail = actual[0]
        assertThat(wiFiDetail.wiFiIdentifier).isEqualTo(wiFiIdentifier)
        assertThat(wiFiDetail.wiFiAdditional.wiFiConnection.ipAddress).isEqualTo(ipAddress)
        assertThat(actual[1].wiFiAdditional.wiFiConnection.ipAddress).isEmpty()
        assertThat(actual[2].wiFiAdditional.wiFiConnection.ipAddress).isEmpty()
        assertThat(actual[3].wiFiAdditional.wiFiConnection.ipAddress).isEmpty()
        assertThat(actual[4].wiFiAdditional.wiFiConnection.ipAddress).isEmpty()
        assertThat(actual[5].wiFiAdditional.wiFiConnection.ipAddress).isEmpty()
        assertThat(actual[6].wiFiAdditional.wiFiConnection.ipAddress).isEmpty()
        verifyVendorNames()
    }

    @Test
    fun wiFiDetailsWithVendorName() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.NONE)
        // validate
        assertThat(actual).hasSize(7)
        assertThat(actual[0].wiFiAdditional.vendorName).isEqualTo(vendorName + bssid2)
        assertThat(actual[1].wiFiAdditional.vendorName).isEqualTo(vendorName + bssid4)
        assertThat(actual[2].wiFiAdditional.vendorName).isEqualTo(vendorName + bssid1)
        assertThat(actual[3].wiFiAdditional.vendorName).isEqualTo(vendorName + bssid2 + "_2")
        assertThat(actual[4].wiFiAdditional.vendorName).isEqualTo(vendorName + bssid2 + "_3")
        assertThat(actual[5].wiFiAdditional.vendorName).isEqualTo(vendorName + bssid3)
        assertThat(actual[6].wiFiAdditional.vendorName).isEqualTo(vendorName + bssid2 + "_1")
        verify(vendorService, times(7)).findVendorName(any())
        verifyVendorNames()
    }

    @Test
    fun wiFiDetailsSortByStrengthGroupByNone() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH)
        // validate
        assertThat(actual).hasSize(7)
        assertThat(actual[0].wiFiIdentifier.bssid).isEqualTo(bssid2)
        assertThat(actual[1].wiFiIdentifier.bssid).isEqualTo(bssid4)
        assertThat(actual[2].wiFiIdentifier.bssid).isEqualTo(bssid1)
        assertThat(actual[3].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_2")
        assertThat(actual[4].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_3")
        assertThat(actual[5].wiFiIdentifier.bssid).isEqualTo(bssid3)
        assertThat(actual[6].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_1")
        verifyChildren(actual)
        verifyVendorNames()
    }

    @Test
    fun wiFiDetailsSortByStrengthGroupBySSID() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.SSID)
        // validate
        assertThat(actual).hasSize(4)
        assertThat(actual[0].wiFiIdentifier.ssid).isEqualTo(ssid2)
        assertThat(actual[1].wiFiIdentifier.ssid).isEqualTo(ssid4)
        assertThat(actual[2].wiFiIdentifier.ssid).isEqualTo(ssid1)
        assertThat(actual[3].wiFiIdentifier.ssid).isEqualTo(ssid3)
        verifyChildren(actual, 0)
        verifyVendorNames()
    }

    @Test
    fun wiFiDetailsSortByStrengthGroupByChannel() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.CHANNEL)
        // validate
        assertThat(actual).hasSize(3)
        assertThat(actual[0].wiFiIdentifier.ssid).isEqualTo(ssid2)
        assertThat(actual[1].wiFiIdentifier.ssid).isEqualTo(ssid4)
        assertThat(actual[2].wiFiIdentifier.ssid).isEqualTo(ssid1)
        verifyChildrenGroupByChannel(actual, 2, 0, 1)
        verifyVendorNames()
    }

    @Test
    fun wiFiDetailsSortBySSIDGroupByNone() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID)
        // validate
        assertThat(actual).hasSize(7)
        assertThat(actual[0].wiFiIdentifier.bssid).isEqualTo(bssid1)
        assertThat(actual[1].wiFiIdentifier.bssid).isEqualTo(bssid2)
        assertThat(actual[2].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_2")
        assertThat(actual[3].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_3")
        assertThat(actual[4].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_1")
        assertThat(actual[5].wiFiIdentifier.bssid).isEqualTo(bssid3)
        assertThat(actual[6].wiFiIdentifier.bssid).isEqualTo(bssid4)
        verifyChildren(actual)
        verifyVendorNames()
    }

    @Test
    fun wiFiDetailsSortBySSIDGroupBySSID() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID, GroupBy.SSID)
        // validate
        assertThat(actual).hasSize(4)
        assertThat(actual[0].wiFiIdentifier.ssid).isEqualTo(ssid1)
        assertThat(actual[1].wiFiIdentifier.ssid).isEqualTo(ssid2)
        assertThat(actual[2].wiFiIdentifier.ssid).isEqualTo(ssid3)
        assertThat(actual[3].wiFiIdentifier.ssid).isEqualTo(ssid4)
        verifyChildren(actual, 1)
        verifyVendorNames()
    }

    @Test
    fun wiFiDetailsSortBySSIDGroupByChannel() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.SSID, GroupBy.CHANNEL)
        // validate
        assertThat(actual).hasSize(3)
        assertThat(actual[0].wiFiIdentifier.ssid).isEqualTo(ssid1)
        assertThat(actual[1].wiFiIdentifier.ssid).isEqualTo(ssid2)
        assertThat(actual[2].wiFiIdentifier.ssid).isEqualTo(ssid4)
        verifyChildrenGroupByChannel(actual, 0, 1, 2)
        verifyVendorNames()
    }

    @Test
    fun wiFiDetailsSortByChannelGroupByNone() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.CHANNEL)
        // validate
        assertThat(actual).hasSize(7)
        assertThat(actual[0].wiFiIdentifier.bssid).isEqualTo(bssid1)
        assertThat(actual[1].wiFiIdentifier.bssid).isEqualTo(bssid2)
        assertThat(actual[2].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_2")
        assertThat(actual[3].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_3")
        assertThat(actual[4].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_1")
        assertThat(actual[5].wiFiIdentifier.bssid).isEqualTo(bssid4)
        assertThat(actual[6].wiFiIdentifier.bssid).isEqualTo(bssid3)
        verifyChildren(actual)
        verifyVendorNames()
    }

    @Test
    fun wiFiDetailsSortByChannelGroupBySSID() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.CHANNEL, GroupBy.SSID)
        // validate
        assertThat(actual).hasSize(4)
        assertThat(actual[0].wiFiIdentifier.ssid).isEqualTo(ssid1)
        assertThat(actual[1].wiFiIdentifier.ssid).isEqualTo(ssid2)
        assertThat(actual[2].wiFiIdentifier.ssid).isEqualTo(ssid4)
        assertThat(actual[3].wiFiIdentifier.ssid).isEqualTo(ssid3)
        verifyChildren(actual, 1)
        verifyVendorNames()
    }

    @Test
    fun wiFiDetailsSortByChannelGroupByChannel() {
        // setup
        val predicate: Predicate = WiFiBand.GHZ2.predicate()
        withVendorNames()
        // execute
        val actual: List<WiFiDetail> = fixture.wiFiDetails(predicate, SortBy.CHANNEL, GroupBy.CHANNEL)
        // validate
        assertThat(actual).hasSize(3)
        assertThat(actual[0].wiFiIdentifier.ssid).isEqualTo(ssid1)
        assertThat(actual[1].wiFiIdentifier.ssid).isEqualTo(ssid2)
        assertThat(actual[2].wiFiIdentifier.ssid).isEqualTo(ssid4)
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
            WiFiSecurity.EMPTY,
            WiFiSignal(frequency1, frequency1, WiFiWidth.MHZ_20, level1)
        )
        val wiFiDetail2 = WiFiDetail(
            WiFiIdentifier(ssid2, bssid2),
            WiFiSecurity.EMPTY,
            WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2)
        )
        val wiFiDetail3 = WiFiDetail(
            WiFiIdentifier(ssid3, bssid3),
            WiFiSecurity.EMPTY,
            WiFiSignal(frequency3, frequency3, WiFiWidth.MHZ_20, level0)
        )
        val wiFiDetail4 = WiFiDetail(
            WiFiIdentifier(ssid4, bssid4),
            WiFiSecurity.EMPTY,
            WiFiSignal(frequency4, frequency4, WiFiWidth.MHZ_20, level2)
        )
        val wiFiDetail21 = WiFiDetail(
            WiFiIdentifier(ssid2, bssid2 + "_1"),
            WiFiSecurity.EMPTY,
            WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2 - 3)
        )
        val wiFiDetail22 = WiFiDetail(
            WiFiIdentifier(ssid2, bssid2 + "_2"),
            WiFiSecurity.EMPTY,
            WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2 - 1)
        )
        val wiFiDetail23 = WiFiDetail(
            WiFiIdentifier(ssid2, bssid2 + "_3"),
            WiFiSecurity.EMPTY,
            WiFiSignal(frequency2, frequency2, WiFiWidth.MHZ_20, level2 - 2)
        )
        return listOf(wiFiDetail23, wiFiDetail3, wiFiDetail22, wiFiDetail1, wiFiDetail21, wiFiDetail2, wiFiDetail4)
    }

    private fun verifyChildren(actual: List<WiFiDetail>, index: Int) {
        actual.indices.forEach {
            val children: List<WiFiDetail> = actual[index].children
            if (it == index) {
                assertThat(children).hasSize(3)
                assertThat(children[0].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_2")
                assertThat(children[1].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_3")
                assertThat(children[2].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_1")
            } else {
                assertThat(actual[it].children).isEmpty()
            }
        }
    }

    private fun verifyChildrenGroupByChannel(actual: List<WiFiDetail>, indexEmpty: Int, indexWith3: Int, indexWith1: Int) {
        assertThat(actual[indexEmpty].children).isEmpty()
        val children1: List<WiFiDetail> = actual[indexWith3].children
        assertThat(children1).hasSize(3)
        assertThat(children1[0].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_2")
        assertThat(children1[1].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_3")
        assertThat(children1[2].wiFiIdentifier.bssid).isEqualTo(bssid2 + "_1")
        val children2: List<WiFiDetail> = actual[indexWith1].children
        assertThat(children2).hasSize(1)
        assertThat(children2[0].wiFiIdentifier.bssid).isEqualTo(bssid3)
    }

    private fun verifyVendorNames() {
        wiFiDetails.forEach { verify(vendorService).findVendorName(it.wiFiIdentifier.bssid) }
    }

    private fun verifyChildren(actual: List<WiFiDetail>) {
        actual.forEach { assertThat(it.children).isEmpty() }
    }
}