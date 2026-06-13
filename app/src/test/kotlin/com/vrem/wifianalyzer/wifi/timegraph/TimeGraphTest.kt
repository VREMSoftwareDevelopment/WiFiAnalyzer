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

import com.patrykandpatrick.vico.views.cartesian.CartesianChartView
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.graphutils.GraphWrapper
import com.vrem.wifianalyzer.wifi.graphutils.MAX_Y
import com.vrem.wifianalyzer.wifi.model.Security
import com.vrem.wifianalyzer.wifi.model.SortBy
import com.vrem.wifianalyzer.wifi.model.Strength
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiData
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.predicate.Predicate
import com.vrem.wifianalyzer.wifi.predicate.truePredicate
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class TimeGraphTest {
    private val dataManager: DataManager = mock()
    private val graphWrapper: GraphWrapper = mock()
    private val fixture: TimeGraph = spy(TimeGraph(WiFiBand.GHZ2, dataManager, graphWrapper))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(dataManager)
        verifyNoMoreInteractions(graphWrapper)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun update() {
        // Arrange
        val settings = MainContextHelper.INSTANCE.settings
        val wiFiDetails: List<WiFiDetail> = listOf()
        val newSeries: Set<WiFiDetail> = setOf()
        val wiFiData = WiFiData(wiFiDetails, WiFiConnection.EMPTY)
        val predicate: Predicate = truePredicate
        doReturn(predicate).whenever(fixture).predicate(settings)
        doReturn(newSeries).whenever(dataManager).addSeriesData(graphWrapper, wiFiDetails, MAX_Y, predicate)
        doReturn(SortBy.SSID).whenever(settings).sortBy()
        doReturn(WiFiBand.GHZ2).whenever(settings).wiFiBand()
        doReturn(MAX_Y).whenever(settings).graphMaximumY()
        doReturn(ThemeStyle.DARK).whenever(settings).themeStyle()
        // Act
        fixture.update(wiFiData)
        // Assert
        verify(fixture).predicate(settings)
        verify(dataManager).reset(graphWrapper)
        verify(dataManager).addSeriesData(graphWrapper, wiFiDetails, MAX_Y, predicate)
        verify(graphWrapper).removeSeries(newSeries)
        verify(graphWrapper).show()
        verify(settings).sortBy()
        verify(settings).graphMaximumY()
        verify(settings).wiFiBand()
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun updateDoesNotResetWhenBandNotChanged() {
        // Arrange
        val settings = MainContextHelper.INSTANCE.settings
        val wiFiDetails: List<WiFiDetail> = listOf()
        val newSeries: Set<WiFiDetail> = setOf()
        val wiFiData = WiFiData(wiFiDetails, WiFiConnection.EMPTY)
        val predicate: Predicate = truePredicate
        doReturn(predicate).whenever(fixture).predicate(settings)
        doReturn(newSeries).whenever(dataManager).addSeriesData(graphWrapper, wiFiDetails, MAX_Y, predicate)
        doReturn(SortBy.SSID).whenever(settings).sortBy()
        doReturn(WiFiBand.GHZ2).whenever(settings).wiFiBand()
        doReturn(MAX_Y).whenever(settings).graphMaximumY()
        doReturn(ThemeStyle.DARK).whenever(settings).themeStyle()
        fixture.update(wiFiData)
        // Act
        fixture.update(wiFiData)
        // Assert
        verify(dataManager).reset(graphWrapper)
        verify(dataManager, times(2)).addSeriesData(graphWrapper, wiFiDetails, MAX_Y, predicate)
        verify(graphWrapper, times(2)).removeSeries(newSeries)
        verify(graphWrapper, times(2)).show()
        verify(fixture, times(2)).predicate(settings)
        verify(settings, times(2)).sortBy()
        verify(settings, times(2)).graphMaximumY()
        verify(settings, times(2)).wiFiBand()
    }

    @Test
    fun updateResetsWhenBandSwitchedBack() {
        // Arrange
        val settings = MainContextHelper.INSTANCE.settings
        val wiFiDetails: List<WiFiDetail> = listOf()
        val newSeries: Set<WiFiDetail> = setOf()
        val wiFiData = WiFiData(wiFiDetails, WiFiConnection.EMPTY)
        val predicate: Predicate = truePredicate
        doReturn(predicate).whenever(fixture).predicate(settings)
        doReturn(newSeries).whenever(dataManager).addSeriesData(graphWrapper, wiFiDetails, MAX_Y, predicate)
        doReturn(SortBy.SSID).whenever(settings).sortBy()
        doReturn(MAX_Y).whenever(settings).graphMaximumY()
        doReturn(ThemeStyle.DARK).whenever(settings).themeStyle()
        // first call - selected
        doReturn(WiFiBand.GHZ2).whenever(settings).wiFiBand()
        fixture.update(wiFiData)
        // second call - not selected
        doReturn(WiFiBand.GHZ5).whenever(settings).wiFiBand()
        fixture.update(wiFiData)
        // third call - selected again, should reset
        doReturn(WiFiBand.GHZ2).whenever(settings).wiFiBand()
        fixture.update(wiFiData)
        // Assert
        verify(dataManager, times(2)).reset(graphWrapper)
        verify(dataManager, times(2)).addSeriesData(graphWrapper, wiFiDetails, MAX_Y, predicate)
        verify(graphWrapper, times(2)).removeSeries(newSeries)
        verify(graphWrapper, times(2)).show()
        verify(graphWrapper).gone()
        verify(fixture, times(2)).predicate(settings)
        verify(settings, times(2)).sortBy()
        verify(settings, times(2)).graphMaximumY()
        verify(settings, times(3)).wiFiBand()
    }

    @Test
    fun graph() {
        // Arrange
        val expected: CartesianChartView = mock()
        doReturn(expected).whenever(graphWrapper).chartView
        // Act
        val actual = fixture.graph()
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(graphWrapper).chartView
        verifyNoMoreInteractions(expected)
    }

    @Test
    fun destroy() {
        // Act
        fixture.destroy()
        // Assert
        verify(graphWrapper).destroy()
    }

    @Test
    fun predicate() {
        // Arrange
        val settings = MainContextHelper.INSTANCE.settings
        doReturn(WiFiBand.GHZ2).whenever(settings).wiFiBand()
        doReturn(setOf<String>()).whenever(settings).findSSIDs()
        doReturn(Strength.entries.toSet()).whenever(settings).findStrengths()
        doReturn(Security.entries.toSet()).whenever(settings).findSecurities()
        // Act
        val actual = fixture.predicate(settings)
        // Assert
        assertThat(actual).isNotNull()
        verify(settings).wiFiBand()
        verify(settings).findSSIDs()
        verify(settings).findStrengths()
        verify(settings).findSecurities()
        verifyNoMoreInteractions(settings)
    }
}
