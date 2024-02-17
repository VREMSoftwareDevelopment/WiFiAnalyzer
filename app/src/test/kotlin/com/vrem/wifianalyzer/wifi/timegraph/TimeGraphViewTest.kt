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
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jjoe64.graphview.GraphView
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.band.WiFiBand
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
class TimeGraphViewTest {
    private val dataManager: DataManager = mock()
    private val graphViewWrapper: GraphViewWrapper = mock()
    private val fixture: TimeGraphView = spy(TimeGraphView(WiFiBand.GHZ2, dataManager, graphViewWrapper))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(dataManager)
        verifyNoMoreInteractions(graphViewWrapper)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun update() {
        // setup
        val settings = MainContextHelper.INSTANCE.settings
        val wiFiDetails: List<WiFiDetail> = listOf()
        val newSeries: Set<WiFiDetail> = setOf()
        val wiFiData = WiFiData(wiFiDetails, WiFiConnection.EMPTY)
        val predicate: Predicate = truePredicate
        doReturn(predicate).whenever(fixture).predicate(settings)
        whenever(dataManager.addSeriesData(graphViewWrapper, wiFiDetails, MAX_Y)).thenReturn(newSeries)
        whenever(settings.sortBy()).thenReturn(SortBy.SSID)
        whenever(settings.timeGraphLegend()).thenReturn(GraphLegend.LEFT)
        whenever(settings.wiFiBand()).thenReturn(WiFiBand.GHZ2)
        whenever(settings.graphMaximumY()).thenReturn(MAX_Y)
        whenever(settings.themeStyle()).thenReturn(ThemeStyle.DARK)
        // execute
        fixture.update(wiFiData)
        // validate
        verify(fixture).predicate(settings)
        verify(dataManager).addSeriesData(graphViewWrapper, wiFiDetails, MAX_Y)
        verify(graphViewWrapper).removeSeries(newSeries)
        verify(graphViewWrapper).updateLegend(GraphLegend.LEFT)
        verify(graphViewWrapper).visibility(View.VISIBLE)
        verify(settings).sortBy()
        verify(settings).timeGraphLegend()
        verify(settings).graphMaximumY()
        verify(settings).wiFiBand()
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
        verifyNoMoreInteractions(expected)
    }

    @Test
    fun makeGraphViewShouldNotBeNull() {
        // setup
        RobolectricUtil.INSTANCE.activity
        // execute
        val actual = makeGraphView(MainContext.INSTANCE, 10, ThemeStyle.DARK)
        // validate
        assertThat(actual).isNotNull()
    }

    @Test
    fun makeGraphViewWrapperShouldNotBeNull() {
        // setup
        RobolectricUtil.INSTANCE.activity
        // execute
        val actual = makeGraphViewWrapper()
        // validate
        assertThat(actual).isNotNull()
    }
}