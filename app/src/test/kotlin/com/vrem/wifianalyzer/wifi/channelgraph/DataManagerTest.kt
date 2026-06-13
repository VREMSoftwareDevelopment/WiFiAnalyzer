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
package com.vrem.wifianalyzer.wifi.channelgraph

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.graphutils.DataPoint
import com.vrem.wifianalyzer.wifi.graphutils.GraphWrapper
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSecurity
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class DataManagerTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val level = -40
    private val fixture = DataManager()

    @Test
    fun newSeries() {
        // Arrange
        val expected = makeWiFiDetails()
        // Act
        val actual = fixture.newSeries(expected)
        // Assert
        assertThat(actual).containsAll(expected)
    }

    @Test
    fun graphDataPoints() {
        // Arrange
        val expected = makeWiFiDetail()
        val expectedPoints =
            listOf(DataPoint(2445, -100)) + (2447..2463).map { DataPoint(it, level) } + listOf(DataPoint(2465, -100))
        // Act
        val actual = fixture.graphDataPoints(expected, MAX_Y)
        // Assert
        assertThat(actual).containsExactlyElementsOf(expectedPoints)
    }

    @Test
    fun graphDataPointsExpectLevelToEqualToLevelMax() {
        // Arrange
        val expectedLevel = level - 10
        val expected = makeWiFiDetail()
        val expectedPoints =
            listOf(DataPoint(2445, -100)) + (2447..2463).map { DataPoint(it, expectedLevel) } +
                listOf(DataPoint(2465, -100))
        // Act
        val actual = fixture.graphDataPoints(expected, expectedLevel)
        // Assert
        assertThat(actual).containsExactlyElementsOf(expectedPoints)
    }

    @Test
    fun addSeriesDataWithExistingWiFiDetails() {
        // Arrange
        val graphWrapper: GraphWrapper = mock()
        val wiFiDetail = makeWiFiDetail()
        val wiFiDetails = setOf(wiFiDetail)
        val expectedDataPoints = fixture.graphDataPoints(wiFiDetail, MAX_Y)
        doReturn(false).whenever(graphWrapper).newSeries(wiFiDetail)
        // Act
        fixture.addSeriesData(graphWrapper, wiFiDetails, MAX_Y)
        // Assert
        verify(graphWrapper).newSeries(wiFiDetail)
        verify(graphWrapper).updateSeries(wiFiDetail, expectedDataPoints, true)
        verify(graphWrapper).flushData()
    }

    @Test
    fun addSeriesDataNewWiFiDetails() {
        // Arrange
        val graphWrapper: GraphWrapper = mock()
        val wiFiDetail = makeWiFiDetail()
        val wiFiDetails = setOf(wiFiDetail)
        val expectedDataPoints = fixture.graphDataPoints(wiFiDetail, MAX_Y)
        doReturn(true).whenever(graphWrapper).newSeries(wiFiDetail)
        // Act
        fixture.addSeriesData(graphWrapper, wiFiDetails, MAX_Y)
        // Assert
        verify(graphWrapper).newSeries(wiFiDetail)
        verify(graphWrapper).addSeries(wiFiDetail, expectedDataPoints, true)
        verify(graphWrapper).flushData()
    }

    private fun makeWiFiDetail(
        ssid: String = "SSID",
        frequency: Int = 2455,
    ): WiFiDetail {
        val wiFiSignal = WiFiSignal(frequency, frequency, WiFiWidth.MHZ_20, level)
        val wiFiIdentifier = WiFiIdentifier(ssid, "BSSID")
        return WiFiDetail(wiFiIdentifier, WiFiSecurity.EMPTY, wiFiSignal, WiFiAdditional.EMPTY)
    }

    private fun makeWiFiDetails(): List<WiFiDetail> =
        listOf(makeWiFiDetail("SSID1"), makeWiFiDetail("SSID2"), makeWiFiDetail("SSID3"))
}
