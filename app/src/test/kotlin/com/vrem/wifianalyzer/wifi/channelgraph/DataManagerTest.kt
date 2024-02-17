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
package com.vrem.wifianalyzer.wifi.channelgraph

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.graphutils.GraphDataPoint
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewWrapper
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y
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
    private val level = -40
    private val fixture = DataManager()

    @Test
    fun newSeries() {
        // setup
        val wiFiChannelPair = WiFiBand.GHZ2.wiFiChannels.wiFiChannelPairs()[0]
        val expected = makeWiFiDetails(wiFiChannelPair.first.frequency)
        // execute
        val actual = fixture.newSeries(expected, wiFiChannelPair)
        // validate
        assertThat(actual).hasSize(expected.size - 1)
        assertThat(actual).contains(expected[0])
        assertThat(actual).doesNotContain(expected[1])
        assertThat(actual).contains(expected[2])
    }

    @Test
    fun graphDataPoints() {
        // setup
        val expected = makeWiFiDetail()
        // execute
        val actual = fixture.graphDataPoints(expected, MAX_Y)
        // validate
        assertThat(actual).hasSize(5)
        assertThat(actual[0].toString()).isEqualTo(GraphDataPoint(2445, -100).toString())
        assertThat(actual[1].toString()).isEqualTo(GraphDataPoint(2447, level).toString())
        assertThat(actual[2].toString()).isEqualTo(GraphDataPoint(2455, level).toString())
        assertThat(actual[3].toString()).isEqualTo(GraphDataPoint(2463, level).toString())
        assertThat(actual[4].toString()).isEqualTo(GraphDataPoint(2465, -100).toString())
    }

    @Test
    fun graphDataPointsExpectLevelToEqualToLevelMax() {
        // setup
        val expectedLevel = level - 10
        val expected = makeWiFiDetail()
        // execute
        val actual = fixture.graphDataPoints(expected, expectedLevel)
        // validate
        assertThat(actual).hasSize(5)
        assertThat(actual[0].toString()).isEqualTo(GraphDataPoint(2445, -100).toString())
        assertThat(actual[1].toString()).isEqualTo(GraphDataPoint(2447, expectedLevel).toString())
        assertThat(actual[2].toString()).isEqualTo(GraphDataPoint(2455, expectedLevel).toString())
        assertThat(actual[3].toString()).isEqualTo(GraphDataPoint(2463, expectedLevel).toString())
        assertThat(actual[4].toString()).isEqualTo(GraphDataPoint(2465, -100).toString())
    }

    @Test
    fun addSeriesDataWithExistingWiFiDetails() {
        // setup
        val graphViewWrapper: GraphViewWrapper = mock()
        val wiFiDetail = makeWiFiDetail()
        val wiFiDetails = setOf(wiFiDetail)
        val dataPoints = fixture.graphDataPoints(wiFiDetail, MAX_Y)
        whenever(graphViewWrapper.newSeries(wiFiDetail)).thenReturn(false)
        // execute
        fixture.addSeriesData(graphViewWrapper, wiFiDetails, MAX_Y)
        // validate
        verify(graphViewWrapper).newSeries(wiFiDetail)
        verify(graphViewWrapper).updateSeries(wiFiDetail, dataPoints, true)
    }

    @Test
    fun addSeriesDataNewWiFiDetails() {
        // setup
        val graphViewWrapper: GraphViewWrapper = mock()
        val wiFiDetail = makeWiFiDetail()
        val wiFiDetails = setOf(wiFiDetail)
        whenever(graphViewWrapper.newSeries(wiFiDetail)).thenReturn(true)
        // execute
        fixture.addSeriesData(graphViewWrapper, wiFiDetails, MAX_Y)
        // validate
        verify(graphViewWrapper).newSeries(wiFiDetail)
        verify(graphViewWrapper).addSeries(eq(wiFiDetail), any(), eq(true))
    }

    @Test
    fun inRangeWithValidFrequency() {
        // setup
        val wiFiChannelPair = WiFiBand.GHZ2.wiFiChannels.wiFiChannelPairs()[0]
        // execute & validate
        assertThat(wiFiChannelPair.inRange(makeWiFiDetail(frequency = wiFiChannelPair.first.frequency))).isTrue()
        assertThat(wiFiChannelPair.inRange(makeWiFiDetail(frequency = wiFiChannelPair.second.frequency))).isTrue()
        assertThat(wiFiChannelPair.inRange(makeWiFiDetail(frequency = (wiFiChannelPair.first.frequency + wiFiChannelPair.second.frequency) / 2))).isTrue()
    }

    @Test
    fun inRangeWithInvalidValidFrequency() {
        // setup
        val wiFiChannelPair = WiFiBand.GHZ2.wiFiChannels.wiFiChannelPairs()[0]
        // execute & validate
        assertThat(wiFiChannelPair.inRange(makeWiFiDetail(frequency = wiFiChannelPair.first.frequency - 1))).isFalse()
        assertThat(wiFiChannelPair.inRange(makeWiFiDetail(frequency = wiFiChannelPair.second.frequency + 1))).isFalse()
    }

    private fun makeWiFiDetail(ssid: String = "SSID", frequency: Int = 2455): WiFiDetail {
        val wiFiSignal = WiFiSignal(frequency, frequency, WiFiWidth.MHZ_20, level)
        val wiFiIdentifier = WiFiIdentifier(ssid, "BSSID")
        return WiFiDetail(wiFiIdentifier, WiFiSecurity.EMPTY, wiFiSignal, WiFiAdditional.EMPTY)
    }

    private fun makeWiFiDetails(frequency: Int): List<WiFiDetail> {
        return listOf(makeWiFiDetail("SSID1", frequency), makeWiFiDetail("SSID2", -frequency), makeWiFiDetail("SSID3", frequency))
    }

}