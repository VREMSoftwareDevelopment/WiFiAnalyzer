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
package com.vrem.wifianalyzer.wifi.timegraph

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.graphutils.*
import com.vrem.wifianalyzer.wifi.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class DataManagerTest {
    @Suppress("unused")
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val bssid = "BSSID"
    private val level = -40
    private val graphViewWrapper: GraphViewWrapper = mock()
    private val timeGraphCache: TimeGraphCache = mock()
    private val fixture = DataManager(timeGraphCache)

    @Test
    fun addSeriesDataIncreaseXValue() {
        // setup
        assertThat(fixture.xValue).isEqualTo(0)
        // execute
        fixture.addSeriesData(graphViewWrapper, listOf(), MAX_Y)
        // validate
        assertThat(fixture.xValue).isEqualTo(1)
    }

    @Test
    fun addSeriesDataIncreaseCounts() {
        // setup
        assertThat(fixture.scanCount).isEqualTo(0)
        // execute
        fixture.addSeriesData(graphViewWrapper, listOf(), MAX_Y)
        // validate
        assertThat(fixture.scanCount).isEqualTo(1)
    }

    @Test
    fun addSeriesDoesNotIncreasesScanCountWhenLimitIsReached() {
        // setup
        fixture.scanCount = MAX_SCAN_COUNT
        // execute
        fixture.addSeriesData(graphViewWrapper, listOf(), MAX_Y)
        // validate
        assertThat(fixture.scanCount).isEqualTo(MAX_SCAN_COUNT)
    }

    @Test
    fun addSeriesSetHorizontalLabelsVisible() {
        // setup
        fixture.scanCount = 1
        // execute
        fixture.addSeriesData(graphViewWrapper, listOf(), MAX_Y)
        // validate
        assertThat(fixture.scanCount).isEqualTo(2)
        verify(graphViewWrapper).setHorizontalLabelsVisible(true)
    }

    @Test
    fun addSeriesDoesNotSetHorizontalLabelsVisible() {
        // execute
        fixture.addSeriesData(graphViewWrapper, listOf(), MAX_Y)
        // validate
        verify(graphViewWrapper, never()).setHorizontalLabelsVisible(true)
    }

    @Test
    fun adjustDataAppendsData() {
        // setup
        val wiFiDetails: Set<WiFiDetail> = setOf()
        val difference = makeWiFiDetails()
        val xValue = fixture.xValue
        val scanCount = fixture.scanCount
        val dataPoint = GraphDataPoint(xValue, MIN_Y + MIN_Y_OFFSET)
        whenever(graphViewWrapper.differenceSeries(wiFiDetails)).thenReturn(difference)
        // execute
        fixture.adjustData(graphViewWrapper, wiFiDetails)
        // validate
        difference.forEach {
            verify(graphViewWrapper).appendToSeries(
                it,
                dataPoint,
                scanCount,
                it.wiFiAdditional.wiFiConnection.connected
            )
            verify(timeGraphCache).add(it)
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
        val actual = fixture.newSeries(wiFiDetails)
        // validate
        assertThat(actual).containsAll(wiFiDetails)
        assertThat(actual).containsAll(moreWiFiDetails)
        verify(timeGraphCache).active()
    }

    @Test
    fun addDataToExistingSeries() {
        // setup
        val scanCount = fixture.scanCount
        val xValue = fixture.xValue
        val wiFiDetail = makeWiFiDetail("SSID")
        val dataPoint = GraphDataPoint(xValue, level)
        whenever(graphViewWrapper.newSeries(wiFiDetail)).thenReturn(false)
        // execute
        fixture.addData(graphViewWrapper, wiFiDetail, MAX_Y)
        // validate
        verify(graphViewWrapper).newSeries(wiFiDetail)
        verify(graphViewWrapper).appendToSeries(
            wiFiDetail,
            dataPoint,
            scanCount,
            wiFiDetail.wiFiAdditional.wiFiConnection.connected
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
        val dataPoint = GraphDataPoint(xValue, expectedLevel)
        whenever(graphViewWrapper.newSeries(wiFiDetail)).thenReturn(false)
        // execute
        fixture.addData(graphViewWrapper, wiFiDetail, expectedLevel)
        // validate
        verify(graphViewWrapper).appendToSeries(
            wiFiDetail,
            dataPoint,
            scanCount,
            wiFiDetail.wiFiAdditional.wiFiConnection.connected
        )
    }

    @Test
    fun addDataNewSeries() {
        // setup
        val wiFiDetail = makeWiFiDetailConnected("SSID")
        whenever(graphViewWrapper.newSeries(wiFiDetail)).thenReturn(true)
        // execute
        fixture.addData(graphViewWrapper, wiFiDetail, MAX_Y)
        // validate
        verify(graphViewWrapper).newSeries(wiFiDetail)
        verify(timeGraphCache).reset(wiFiDetail)
        verify(graphViewWrapper).addSeries(
            eq(wiFiDetail),
            any(),
            eq(wiFiDetail.wiFiAdditional.wiFiConnection.connected)
        )
    }

    private fun makeWiFiDetailConnected(ssid: String): WiFiDetail {
        val wiFiIdentifier = WiFiIdentifier(ssid, bssid)
        val wiFiConnection = WiFiConnection(wiFiIdentifier, "IPADDRESS", 11)
        val wiFiAdditional = WiFiAdditional("VendorName", wiFiConnection)
        return WiFiDetail(wiFiIdentifier, WiFiSecurity.EMPTY, makeWiFiSignal(), wiFiAdditional)
    }

    private fun makeWiFiSignal(): WiFiSignal =
        WiFiSignal(2455, 2455, WiFiWidth.MHZ_20, level)

    private fun makeWiFiDetail(ssid: String): WiFiDetail =
        WiFiDetail(WiFiIdentifier(ssid, bssid), WiFiSecurity.EMPTY, makeWiFiSignal())

    private fun makeWiFiDetails(): List<WiFiDetail> =
        listOf(makeWiFiDetailConnected("SSID1"), makeWiFiDetail("SSID2"), makeWiFiDetail("SSID3"))

    private fun makeMoreWiFiDetails(): List<WiFiDetail> =
        listOf(makeWiFiDetail("SSID4"), makeWiFiDetail("SSID5"))

}
