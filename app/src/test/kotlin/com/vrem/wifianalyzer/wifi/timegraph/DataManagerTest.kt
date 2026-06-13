/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.timegraph

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.graphutils.DataPoint
import com.vrem.wifianalyzer.wifi.graphutils.GraphWrapper
import com.vrem.wifianalyzer.wifi.graphutils.MAX_SCAN_COUNT
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSecurity
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import com.vrem.wifianalyzer.wifi.predicate.falsePredicate
import com.vrem.wifianalyzer.wifi.predicate.truePredicate
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class DataManagerTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val bssid = "BSSID"
    private val level = -40
    private val graphWrapper: GraphWrapper = mock()
    private val timeGraphCache: TimeGraphCache = mock()
    private val fixture = DataManager(timeGraphCache)

    @Test
    fun reset() {
        // setup
        fixture.scanCount = 5
        fixture.xValue = 10
        // execute
        fixture.reset(graphWrapper)
        // validate
        assertThat(fixture.scanCount).isEqualTo(0)
        assertThat(fixture.xValue).isEqualTo(0)
        verify(graphWrapper).reset()
        verify(timeGraphCache).reset()
    }

    @Test
    fun addSeriesDataIncreaseXValue() {
        // setup
        assertThat(fixture.xValue).isEqualTo(0)
        // execute
        fixture.addSeriesData(graphWrapper, listOf(), MAX_Y, truePredicate)
        // validate
        assertThat(fixture.xValue).isEqualTo(1)
    }

    @Test
    fun addSeriesDataIncreaseCounts() {
        // setup
        assertThat(fixture.scanCount).isEqualTo(0)
        // execute
        fixture.addSeriesData(graphWrapper, listOf(), MAX_Y, truePredicate)
        // validate
        assertThat(fixture.scanCount).isEqualTo(1)
    }

    @Test
    fun addSeriesDataCallsAddDataAndAdjustData() {
        // setup
        val wiFiDetails = makeWiFiDetails()
        val wiFiDetailsSet = wiFiDetails.toSet()
        // execute
        fixture.addSeriesData(graphWrapper, wiFiDetails, MAX_Y, truePredicate)
        // validate
        wiFiDetailsSet.forEach {
            verify(graphWrapper).newSeries(it)
        }
        verify(graphWrapper).differenceSeries(wiFiDetailsSet)
        verify(graphWrapper).flushData()
    }

    @Test
    fun addSeriesDoesNotIncreasesScanCountWhenLimitIsReached() {
        // setup
        fixture.scanCount = MAX_SCAN_COUNT
        // execute
        fixture.addSeriesData(graphWrapper, listOf(), MAX_Y, truePredicate)
        // validate
        assertThat(fixture.scanCount).isEqualTo(MAX_SCAN_COUNT)
    }

    @Test
    fun adjustDataTracksDisappearedNetworks() {
        // setup
        val wiFiDetails: Set<WiFiDetail> = setOf()
        val difference = makeWiFiDetails()
        whenever(graphWrapper.differenceSeries(wiFiDetails)).thenReturn(difference)
        // execute
        fixture.adjustData(graphWrapper, wiFiDetails, truePredicate)
        // validate
        difference.forEach {
            verify(timeGraphCache).add(it)
        }
        verify(timeGraphCache).clear()
    }

    @Test
    fun adjustDataDoesNotTrackDisappearedNetworksFilteredOutByPredicate() {
        // setup
        val wiFiDetails: Set<WiFiDetail> = setOf()
        val difference = makeWiFiDetails()
        whenever(graphWrapper.differenceSeries(wiFiDetails)).thenReturn(difference)
        // execute
        fixture.adjustData(graphWrapper, wiFiDetails, falsePredicate)
        // validate
        difference.forEach {
            verify(timeGraphCache, never()).add(it)
        }
        verify(timeGraphCache).clear()
    }

    @Test
    fun newSeries() {
        // setup
        val wiFiDetails: Set<WiFiDetail> = makeWiFiDetails().toSet()
        val moreWiFiDetails: Set<WiFiDetail> = makeMoreWiFiDetails().toSet()
        whenever(timeGraphCache.active()).thenReturn(moreWiFiDetails)
        // execute
        val actual = fixture.newSeries(wiFiDetails, truePredicate)
        // validate
        assertThat(actual).containsAll(wiFiDetails)
        assertThat(actual).containsAll(moreWiFiDetails)
        verify(timeGraphCache).active()
    }

    @Test
    fun newSeriesExcludesActiveCacheEntriesFilteredOutByPredicate() {
        // setup
        val wiFiDetails: Set<WiFiDetail> = makeWiFiDetails().toSet()
        val moreWiFiDetails: Set<WiFiDetail> = makeMoreWiFiDetails().toSet()
        whenever(timeGraphCache.active()).thenReturn(moreWiFiDetails)
        // execute
        val actual = fixture.newSeries(wiFiDetails, falsePredicate)
        // validate
        assertThat(actual).containsAll(wiFiDetails)
        assertThat(actual).doesNotContainAnyElementsOf(moreWiFiDetails)
        verify(timeGraphCache).active()
    }

    @Test
    fun addDataToExistingSeries() {
        // setup
        val scanCount = fixture.scanCount
        val xValue = fixture.xValue
        val wiFiDetail = makeWiFiDetail("SSID")
        val dataPoint = DataPoint(xValue, level)
        whenever(graphWrapper.newSeries(wiFiDetail)).thenReturn(false)
        // execute
        fixture.addData(graphWrapper, wiFiDetail, MAX_Y)
        // validate
        verify(graphWrapper).newSeries(wiFiDetail)
        verify(graphWrapper).appendToSeries(
            wiFiDetail,
            dataPoint,
            scanCount,
            wiFiDetail.wiFiAdditional.wiFiConnection.connected,
        )
        verify(timeGraphCache).reset(wiFiDetail)
    }

    @Test
    fun addDataToExistingSeriesExpectLevelToEqualToLevelMax() {
        // setup
        val expectedLevel = level - 10
        val scanCount = fixture.scanCount
        val xValue = fixture.xValue
        val wiFiDetail = makeWiFiDetail("SSID")
        val dataPoint = DataPoint(xValue, expectedLevel)
        whenever(graphWrapper.newSeries(wiFiDetail)).thenReturn(false)
        // execute
        fixture.addData(graphWrapper, wiFiDetail, expectedLevel)
        // validate
        verify(graphWrapper).appendToSeries(
            wiFiDetail,
            dataPoint,
            scanCount,
            wiFiDetail.wiFiAdditional.wiFiConnection.connected,
        )
    }

    @Test
    fun addDataNewSeries() {
        // setup
        val xValue = fixture.xValue
        val wiFiDetail = makeWiFiDetailConnected("SSID")
        val dataPoint = DataPoint(xValue, level)
        whenever(graphWrapper.newSeries(wiFiDetail)).thenReturn(true)
        // execute
        fixture.addData(graphWrapper, wiFiDetail, MAX_Y)
        // validate
        verify(graphWrapper).newSeries(wiFiDetail)
        verify(timeGraphCache).reset(wiFiDetail)
        verify(graphWrapper).addSeries(
            wiFiDetail,
            listOf(dataPoint),
            wiFiDetail.wiFiAdditional.wiFiConnection.connected,
        )
    }

    private fun makeWiFiDetailConnected(ssid: String): WiFiDetail {
        val wiFiIdentifier = WiFiIdentifier(ssid, bssid)
        val wiFiConnection = WiFiConnection(wiFiIdentifier, "IPADDRESS", 11)
        val wiFiAdditional = WiFiAdditional("VendorName", wiFiConnection)
        return WiFiDetail(wiFiIdentifier, WiFiSecurity.EMPTY, makeWiFiSignal(), wiFiAdditional)
    }

    private fun makeWiFiSignal(): WiFiSignal = WiFiSignal(2455, 2455, WiFiWidth.MHZ_20, level)

    private fun makeWiFiDetail(ssid: String): WiFiDetail =
        WiFiDetail(WiFiIdentifier(ssid, bssid), WiFiSecurity.EMPTY, makeWiFiSignal())

    private fun makeWiFiDetails(): List<WiFiDetail> =
        listOf(makeWiFiDetailConnected("SSID1"), makeWiFiDetail("SSID2"), makeWiFiDetail("SSID3"))

    private fun makeMoreWiFiDetails(): List<WiFiDetail> = listOf(makeWiFiDetail("SSID4"), makeWiFiDetail("SSID5"))
}
