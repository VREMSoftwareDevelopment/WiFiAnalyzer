/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jjoe64.graphview.GraphView
import com.vrem.wifianalyzer.Configuration
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.settings.Settings
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import com.vrem.wifianalyzer.wifi.band.WiFiChannelPair
import com.vrem.wifianalyzer.wifi.graphutils.GraphLegend
import com.vrem.wifianalyzer.wifi.graphutils.GraphViewWrapper
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y
import com.vrem.wifianalyzer.wifi.model.SortBy
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.truePredicate
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.UPSIDE_DOWN_CAKE])
class ChannelGraphViewTest {
    private val wiFiChannelPair: WiFiChannelPair = WiFiChannelPair(WiFiChannel.UNKNOWN, WiFiChannel.UNKNOWN)
    private val settings: Settings = MainContextHelper.INSTANCE.settings
    private val configuration: Configuration = MainContextHelper.INSTANCE.configuration
    private val graphViewWrapper: GraphViewWrapper = mock()
    private val dataManager: DataManager = mock()
    private val fixture: ChannelGraphView = spy(ChannelGraphView(WiFiBand.GHZ2, wiFiChannelPair, dataManager, graphViewWrapper))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(graphViewWrapper)
        verifyNoMoreInteractions(dataManager)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun update() {
        // setup
        val newSeries: Set<WiFiDetail> = setOf()
        val wiFiDetails: List<WiFiDetail> = listOf()
        val wiFiData = WiFiData(wiFiDetails, WiFiConnection.EMPTY)
        val predicate: Predicate = truePredicate
        doReturn(predicate).whenever(fixture).predicate(settings)
        doReturn(true).whenever(fixture).selected()
        whenever(dataManager.newSeries(wiFiDetails, wiFiChannelPair)).thenReturn(newSeries)
        whenever(settings.channelGraphLegend()).thenReturn(GraphLegend.RIGHT)
        whenever(settings.graphMaximumY()).thenReturn(MAX_Y)
        whenever(settings.themeStyle()).thenReturn(ThemeStyle.DARK)
        whenever(settings.sortBy()).thenReturn(SortBy.CHANNEL)
        // execute
        fixture.update(wiFiData)
        // validate
        verify(fixture).selected()
        verify(fixture).predicate(settings)
        verify(dataManager).newSeries(wiFiDetails, wiFiChannelPair)
        verify(dataManager).addSeriesData(graphViewWrapper, newSeries, MAX_Y)
        verify(graphViewWrapper).removeSeries(newSeries)
        verify(graphViewWrapper).updateLegend(GraphLegend.RIGHT)
        verify(graphViewWrapper).visibility(View.VISIBLE)
        verify(settings).sortBy()
        verify(settings).channelGraphLegend()
        verify(settings).graphMaximumY()
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun graphView() {
        // setup
        val expected: GraphView = mock()
        whenever(graphViewWrapper.graphView).thenReturn(expected)
        // execute
        val actual = fixture.graphView()
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(graphViewWrapper).graphView
    }

    @Test
    fun wiFiChannelPairNumX() {
        // setup
        val expected = 15
        val wiFiChannelPair: WiFiChannelPair = withWiFiChannelPair()
        // execute
        val actual = wiFiChannelPair.numX()
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun wiFiChannelPairSelected() {
        // setup
        val fixture = withWiFiChannelPair()
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ2)).thenReturn(fixture)
        // execute
        val actual = fixture.selected(WiFiBand.GHZ2)
        // validate
        assertThat(actual).isTrue()
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ2)
    }

    @Test
    fun wiFiChannelPairSelectedWithCurrentWiFiBandGHZ5() {
        // setup
        val fixture = withWiFiChannelPair()
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ5)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ5)).thenReturn(fixture)
        // execute
        val actual = fixture.selected(WiFiBand.GHZ2)
        // validate
        assertThat(actual).isFalse()
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ5)
    }

    @Test
    fun wiFiChannelPairSelectedWithGHZ5() {
        // setup
        val fixture = withWiFiChannelPair()
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ2)).thenReturn(fixture)
        // execute
        val actual = fixture.selected(WiFiBand.GHZ5)
        // validate
        assertThat(actual).isFalse()
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ2)
    }

    @Test
    fun wiFiChannelPairSelectedWithCurrentGHZ5() {
        // setup
        val fixture = withWiFiChannelPair()
        val wiFiChannelPair = withWiFiChannelPair(30)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ5)
        whenever(configuration.wiFiChannelPair(WiFiBand.GHZ5)).thenReturn(wiFiChannelPair)
        // execute
        val actual = fixture.selected(WiFiBand.GHZ5)
        // validate
        assertThat(actual).isFalse()
        verify(settings).wiFiBand()
        verify(configuration).wiFiChannelPair(WiFiBand.GHZ5)
    }

    @Test
    fun makeGraphView() {
        // setup
        RobolectricUtil.INSTANCE.activity
        val wiFiChannelPair = withWiFiChannelPair()
        // execute
        val actual = makeGraphView(MainContext.INSTANCE, 10, ThemeStyle.DARK, WiFiBand.GHZ2, wiFiChannelPair)
        // validate
        assertThat(actual).isNotNull()
    }

    @Test
    fun makeGraphViewWrapper() {
        // setup
        MainContextHelper.INSTANCE.restore()
        RobolectricUtil.INSTANCE.activity
        val wiFiChannelPair = withWiFiChannelPair()
        // execute
        val actual = makeGraphViewWrapper(WiFiBand.GHZ2, wiFiChannelPair)
        // validate
        assertThat(actual).isNotNull()
    }

    @Test
    fun makeDefaultSeries() {
        // setup
        val frequencyEnd = 10
        val minX = 20
        RobolectricUtil.INSTANCE.activity
        // execute
        val actual = makeDefaultSeries(frequencyEnd, minX)
        // validate
        assertThat(actual).isNotNull()
    }

    private fun withWiFiChannelPair(channel: Int = 10): WiFiChannelPair =
        WiFiChannelPair(WiFiChannel(channel, 100), WiFiChannel(20, 200))

}