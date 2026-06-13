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

import com.patrykandpatrick.vico.views.cartesian.CartesianChartView
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.settings.Settings
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class ChannelGraphTest {
    private val settings: Settings = MainContextHelper.INSTANCE.settings
    private val graphWrapper: GraphWrapper = mock()
    private val dataManager: DataManager = mock()
    private val fixture: ChannelGraph = spy(ChannelGraph(WiFiBand.GHZ2, dataManager, graphWrapper))

    @After
    fun tearDown() {
        verifyNoMoreInteractions(graphWrapper)
        verifyNoMoreInteractions(dataManager)
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun update() {
        // Arrange
        val newSeries: Set<WiFiDetail> = setOf()
        val wiFiDetails: List<WiFiDetail> = listOf()
        val wiFiData = WiFiData(wiFiDetails, WiFiConnection.EMPTY)
        val predicate: Predicate = truePredicate
        doReturn(predicate).whenever(fixture).predicate(settings)
        doReturn(true).whenever(fixture).selected()
        doReturn(newSeries).whenever(dataManager).newSeries(wiFiDetails)
        doReturn(MAX_Y).whenever(settings).graphMaximumY()
        doReturn(ThemeStyle.DARK).whenever(settings).themeStyle()
        doReturn(SortBy.CHANNEL).whenever(settings).sortBy()
        // Act
        fixture.update(wiFiData)
        // Assert
        verify(fixture).selected()
        verify(fixture).predicate(settings)
        verify(graphWrapper).reset()
        verify(dataManager).newSeries(wiFiDetails)
        verify(dataManager).addSeriesData(graphWrapper, newSeries, MAX_Y)
        verify(graphWrapper).removeSeries(newSeries)
        verify(graphWrapper).show()
        verify(settings).sortBy()
        verify(settings).graphMaximumY()
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun updateWhenNotSelected() {
        // Arrange
        val wiFiData = WiFiData(listOf(), WiFiConnection.EMPTY)
        doReturn(false).whenever(fixture).selected()
        // Act
        fixture.update(wiFiData)
        // Assert
        verify(fixture).selected()
        verify(graphWrapper).gone()
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
    }

    @Test
    fun destroy() {
        // Act
        fixture.destroy()
        // Assert
        verify(graphWrapper).destroy()
    }

    @Test
    fun selectedWhenBandMatches() {
        // Arrange
        doReturn(WiFiBand.GHZ2).whenever(settings).wiFiBand()
        // Act
        val actual = fixture.selected()
        // Assert
        assertThat(actual).isTrue()
        verify(settings).wiFiBand()
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun selectedWhenBandDoesNotMatch() {
        // Arrange
        doReturn(WiFiBand.GHZ5).whenever(settings).wiFiBand()
        // Act
        val actual = fixture.selected()
        // Assert
        assertThat(actual).isFalse()
        verify(settings).wiFiBand()
        verifyNoMoreInteractions(settings)
    }

    @Test
    fun predicate() {
        // Arrange
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
