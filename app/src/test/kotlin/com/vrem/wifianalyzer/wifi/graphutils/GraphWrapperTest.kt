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
package com.vrem.wifianalyzer.wifi.graphutils

import android.os.Build
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.patrykandpatrick.vico.views.cartesian.CartesianChart
import com.patrykandpatrick.vico.views.cartesian.CartesianChartView
import com.patrykandpatrick.vico.views.cartesian.ScrollHandler
import com.patrykandpatrick.vico.views.cartesian.ZoomHandler
import com.patrykandpatrick.vico.views.cartesian.data.CartesianLayerRangeProvider
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.SIZE_MAX
import com.vrem.wifianalyzer.SIZE_MIN
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional
import com.vrem.wifianalyzer.wifi.model.WiFiConnection
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import kotlinx.coroutines.isActive
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import java.util.AbstractMap.SimpleEntry

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class GraphWrapperTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val chartView: CartesianChartView = mock()
    private val seriesCache: SeriesCache = mock()
    private val seriesLabel: SeriesLabel = mock()
    private val chartUpdater: ChartUpdater = mock()
    private val chart: CartesianChart = mock()
    private val graphColors: GraphColors = GraphColors()
    private val seriesData: SeriesData = SeriesData()
    private val dataPoint: DataPoint = DataPoint(1, 2)
    private val wiFiDetail = WiFiDetail.EMPTY
    private val rangeProvider = CartesianLayerRangeProvider.fixed()
    private val graphViewport =
        GraphViewport(
            rangeProvider = rangeProvider,
            scrollHandler = ScrollHandler(),
            placeholderDataPoints = emptyList(),
        )
    private val fixture =
        GraphWrapper(
            graphViewport,
            chartView,
            seriesLabel,
            seriesCache,
            graphColors,
            chartUpdater,
        )

    @After
    fun tearDown() {
        verify(chartView).modelProducer = fixture.modelProducer
        verify(chartView).scrollHandler = graphViewport.scrollHandler
        verify(chartView).zoomHandler = any<ZoomHandler>()
        verifyNoMoreInteractions(
            chartView,
            seriesCache,
            seriesLabel,
            chartUpdater,
            chart,
        )
    }

    @Test
    fun removeSeries() {
        // Arrange
        val newSeries: Set<WiFiDetail> = setOf()
        val difference: List<WiFiDetail> = listOf()
        val removed = listOf(seriesData)
        val populatedEntries: List<SeriesEntry> = listOf(SimpleEntry(wiFiDetail, seriesData))
        doReturn(difference).whenever(seriesCache).difference(newSeries)
        doReturn(removed).whenever(seriesCache).remove(difference)
        doReturn(populatedEntries).whenever(seriesCache).populatedEntries()
        // Act
        fixture.removeSeries(newSeries)
        // Assert
        verify(seriesCache).difference(newSeries)
        verify(seriesCache).remove(difference)
        verify(seriesCache).populatedEntries()
        verify(chartUpdater).resetStyles()
        verify(chartUpdater).syncPointMap(populatedEntries)
    }

    @Test
    fun differenceSeries() {
        // Arrange
        val newSeries: Set<WiFiDetail> = setOf()
        val expected: List<WiFiDetail> = listOf()
        doReturn(expected).whenever(seriesCache).difference(newSeries)
        // Act
        val actual = fixture.differenceSeries(newSeries)
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(seriesCache).difference(newSeries)
    }

    @Test
    fun addSeriesWhenSeriesExistsDoesNotAddSeries() {
        // Arrange
        doReturn(true).whenever(seriesCache).contains(wiFiDetail)
        // Act
        val actual = fixture.addSeries(wiFiDetail, listOf(dataPoint), false)
        // Assert
        assertThat(actual).isFalse
        verify(seriesCache).contains(wiFiDetail)
        verify(seriesCache, never()).put(wiFiDetail, seriesData)
    }

    @Test
    fun addSeries() {
        // Arrange
        val seriesDataCaptor = argumentCaptor<SeriesData>()
        doReturn(false).whenever(seriesCache).contains(wiFiDetail)
        doReturn(seriesData).whenever(seriesCache).put(eq(wiFiDetail), seriesDataCaptor.capture())
        // Act
        val actual = fixture.addSeries(wiFiDetail, listOf(dataPoint), true)
        // Assert
        assertThat(actual).isTrue
        val actualSeriesData = seriesDataCaptor.firstValue
        verify(seriesCache).contains(wiFiDetail)
        verify(seriesCache).put(wiFiDetail, actualSeriesData)
        assertThat(actualSeriesData.dataPoints).containsExactly(dataPoint)
        assertThat(actualSeriesData.drawBackground).isTrue
    }

    @Test
    fun updateSeriesWhenSeriesDoesNotExistsDoesNotUpdateSeries() {
        // Arrange
        doReturn(null).whenever(seriesCache)[wiFiDetail]
        // Act
        val actual = fixture.updateSeries(wiFiDetail, listOf(dataPoint), true)
        // Assert
        assertThat(actual).isFalse
        verify(seriesCache)[wiFiDetail]
    }

    @Test
    fun updateSeriesWhenSeriesDoesExists() {
        // Arrange
        val connected = wiFiDetail.wiFiAdditional.wiFiConnection.connected
        doReturn(seriesData).whenever(seriesCache)[wiFiDetail]
        // Act
        val actual = fixture.updateSeries(wiFiDetail, listOf(dataPoint), true)
        // Assert
        assertThat(actual).isTrue
        assertThat(seriesData.connected).isEqualTo(connected)
        assertThat(seriesData.drawBackground).isTrue
        verify(seriesCache)[wiFiDetail]
    }

    @Test
    fun appendToSeriesWhenSeriesDoesNotExistsDoesNotUpdateSeries() {
        // Arrange
        val count = 10
        doReturn(null).whenever(seriesCache)[wiFiDetail]
        // Act
        val actual = fixture.appendToSeries(wiFiDetail, dataPoint, count, true)
        // Assert
        assertThat(actual).isFalse
        verify(seriesCache)[wiFiDetail]
    }

    @Test
    fun appendToSeriesWhenSeriesDoesExists() {
        // Arrange
        val count = 10
        val connected = wiFiDetail.wiFiAdditional.wiFiConnection.connected
        doReturn(seriesData).whenever(seriesCache)[wiFiDetail]
        // Act
        val actual = fixture.appendToSeries(wiFiDetail, dataPoint, count, true)
        // Assert
        assertThat(actual).isTrue
        assertThat(seriesData.connected).isEqualTo(connected)
        assertThat(seriesData.drawBackground).isTrue
        verify(seriesCache)[wiFiDetail]
    }

    @Test
    fun show() {
        // Act
        fixture.show()
        // Assert
        verify(chartView).visibility = View.VISIBLE
    }

    @Test
    fun gone() {
        // Act
        fixture.gone()
        // Assert
        verify(chartView).visibility = View.GONE
    }

    @Test
    fun calculateGraphType() {
        // Act
        val actual = fixture.calculateGraphType()
        // Assert
        assertThat(actual).isGreaterThan(0)
    }

    @Test
    fun getSize() {
        // Act & assert
        assertThat(fixture.size(TYPE1)).isEqualTo(SIZE_MAX)
        assertThat(fixture.size(TYPE2)).isEqualTo(SIZE_MAX)
        assertThat(fixture.size(TYPE3)).isEqualTo(SIZE_MAX)
        assertThat(fixture.size(TYPE4)).isEqualTo(SIZE_MIN)
    }

    @Test
    fun newSeriesReturnsTrueIfNotExists() {
        // Arrange
        doReturn(false).whenever(seriesCache).contains(wiFiDetail)
        // Act
        val actual = fixture.newSeries(wiFiDetail)
        // Assert
        assertThat(actual).isTrue
        verify(seriesCache).contains(wiFiDetail)
    }

    @Test
    fun newSeriesReturnsFalseIfExists() {
        // Arrange
        doReturn(true).whenever(seriesCache).contains(wiFiDetail)
        // Act
        val actual = fixture.newSeries(wiFiDetail)
        // Assert
        assertThat(actual).isFalse
        verify(seriesCache).contains(wiFiDetail)
    }

    @Test
    fun reset() {
        // Arrange
        val difference: List<WiFiDetail> = listOf()
        val removed = listOf(seriesData)
        val populatedEntries = emptyList<SeriesEntry>()
        doReturn(difference).whenever(seriesCache).difference(emptySet())
        doReturn(removed).whenever(seriesCache).remove(difference)
        doReturn(populatedEntries).whenever(seriesCache).populatedEntries()
        // Act
        fixture.reset()
        // Assert
        verify(seriesCache).difference(emptySet())
        verify(seriesCache).remove(difference)
        verify(seriesCache, times(2)).populatedEntries()
        verify(chartUpdater).resetStyles()
        verify(chartUpdater).syncPointMap(populatedEntries)
    }

    @Test
    fun resetScalable() {
        // Arrange
        val chartView = GraphBuilder(MAX_Y_DEFAULT, ThemeStyle.DARK).build(mainActivity, true)
        val viewport =
            GraphViewport(
                rangeProvider = rangeProvider,
                scrollHandler = ScrollHandler(true),
                placeholderDataPoints = listOf(),
                scalable = true,
            )
        val fixture = GraphWrapper(viewport, chartView, seriesLabel)
        val zoomHandlerAfterInit = chartView.zoomHandler
        val externallyAssignedZoomHandler = ZoomHandler()
        chartView.scrollHandler = ScrollHandler()
        chartView.zoomHandler = externallyAssignedZoomHandler
        // Act
        fixture.reset()
        // Assert
        assertThat(chartView.scrollHandler).isEqualTo(viewport.scrollHandler)
        assertThat(chartView.zoomHandler).isNotEqualTo(externallyAssignedZoomHandler)
        assertThat(chartView.zoomHandler).isNotEqualTo(zoomHandlerAfterInit)
    }

    @Test
    fun resetScalableBuildsFreshZoomHandlerEachInvocation() {
        // Arrange
        val chartView = GraphBuilder(MAX_Y_DEFAULT, ThemeStyle.DARK).build(mainActivity, true)
        val viewport =
            GraphViewport(
                rangeProvider = rangeProvider,
                scrollHandler = ScrollHandler(true),
                placeholderDataPoints = listOf(),
                scalable = true,
            )
        val fixture = GraphWrapper(viewport, chartView, seriesLabel)
        val afterInit = chartView.zoomHandler
        // Act
        fixture.reset()
        val afterFirstReset = chartView.zoomHandler
        fixture.reset()
        val afterSecondReset = chartView.zoomHandler
        // Assert
        assertThat(afterInit).isNotEqualTo(afterFirstReset)
        assertThat(afterFirstReset).isNotEqualTo(afterSecondReset)
    }

    @Test
    fun resetNonScalableDoesNotReassignZoomHandler() {
        // Arrange
        val chartView = GraphBuilder(MAX_Y_DEFAULT, ThemeStyle.DARK).build(mainActivity, false)
        val viewport =
            GraphViewport(
                rangeProvider = rangeProvider,
                scrollHandler = ScrollHandler(),
                placeholderDataPoints = listOf(),
            )
        val fixture = GraphWrapper(viewport, chartView, seriesLabel)
        val zoomHandlerAfterInit = chartView.zoomHandler
        // Act
        fixture.reset()
        // Assert
        assertThat(chartView.zoomHandler).isEqualTo(zoomHandlerAfterInit)
    }

    @Test
    fun flushDataWithEmptySeries() {
        // Arrange
        doReturn(emptyList<SeriesEntry>()).whenever(seriesCache).populatedEntries()
        // Act
        fixture.flushData()
        // Assert
        verify(seriesCache).populatedEntries()
        verify(chartView, never()).chart
        verify(chartUpdater, never()).sync(any(), any(), any())
    }

    @Test
    fun flushDataWithNoChart() {
        // Arrange
        val seriesData = SeriesData(listOf(DataPoint(1, -50)), GraphColor(0xFF0000, 0x00FF00))
        val entry = SimpleEntry(wiFiDetail, seriesData)
        doReturn(listOf(entry)).whenever(seriesCache).populatedEntries()
        doReturn(null).whenever(chartView).chart
        // Act
        fixture.flushData()
        // Assert
        verify(seriesCache).populatedEntries()
        verify(chartView).chart
        verify(chartUpdater, never()).sync(any(), any(), any())
    }

    @Test
    fun flushDataWithValidDataSyncsChart() {
        // Arrange
        val seriesData = SeriesData(listOf(DataPoint(1, -50)), GraphColor(0xFF0000, 0x00FF00))
        val entry = SimpleEntry(wiFiDetail, seriesData)
        val rangeProviderCaptor = argumentCaptor<CartesianLayerRangeProvider>()
        val entries = listOf(entry)
        val series = listOf(seriesData)
        doReturn(entries).whenever(seriesCache).populatedEntries()
        doReturn(chart).whenever(chartView).chart
        doReturn(series).whenever(chartUpdater).sync(eq(entries), eq(chart), rangeProviderCaptor.capture())
        // Act
        fixture.flushData()
        // Assert
        val rangeProvider = rangeProviderCaptor.firstValue
        assertThat(rangeProvider).isNotNull
        verify(seriesCache).populatedEntries()
        verify(chartView).chart
        verify(chartUpdater).sync(entries, chart, rangeProvider)
    }

    @Test
    fun removeSeriesWithEmptyRemovedDoesNotResetStyles() {
        // Arrange
        val newSeries: Set<WiFiDetail> = setOf()
        val difference: List<WiFiDetail> = listOf()
        val removed = emptyList<SeriesData>()
        doReturn(difference).whenever(seriesCache).difference(newSeries)
        doReturn(removed).whenever(seriesCache).remove(difference)
        // Act
        fixture.removeSeries(newSeries)
        // Assert
        verify(seriesCache).difference(newSeries)
        verify(seriesCache).remove(difference)
        verify(chartUpdater, never()).resetStyles()
        verify(chartUpdater, never()).syncPointMap(any())
    }

    @Test
    fun updateSeriesWhenDisconnectedBecomesConnected() {
        // Arrange
        val wiFiConnection = WiFiConnection(WiFiIdentifier("ssid", "bssid"), "192.168.1.1", 72)
        val connectedDetail =
            WiFiDetail(
                wiFiIdentifier = WiFiIdentifier("ssid", "bssid"),
                wiFiAdditional = WiFiAdditional(wiFiConnection = wiFiConnection),
            )
        val originalColor = GraphColor(0xFF0000, 0x00FF00)
        val existingData = SeriesData(listOf(dataPoint), originalColor, connected = false)
        doReturn(existingData).whenever(seriesCache)[connectedDetail]
        // Act
        val actual = fixture.updateSeries(connectedDetail, listOf(dataPoint), true)
        // Assert
        assertThat(actual).isTrue
        assertThat(existingData.connected).isTrue
        assertThat(existingData.graphColor).isNotEqualTo(originalColor)
        verify(seriesCache)[connectedDetail]
    }

    @Test
    fun updateSeriesWhenConnectedBecomesDisconnected() {
        // Arrange
        val disconnectedDetail =
            WiFiDetail(
                wiFiIdentifier = WiFiIdentifier("ssid", "bssid"),
            )
        val connectedColor = GraphColor(0x0000FF, 0xFF00FF)
        val existingData = SeriesData(listOf(dataPoint), connectedColor, connected = true)
        doReturn(existingData).whenever(seriesCache)[disconnectedDetail]
        // Act
        val actual = fixture.updateSeries(disconnectedDetail, listOf(dataPoint), true)
        // Assert
        assertThat(actual).isTrue
        assertThat(existingData.connected).isFalse
        assertThat(existingData.graphColor).isNotEqualTo(connectedColor)
        verify(seriesCache)[disconnectedDetail]
    }

    @Test
    fun destroyCancelsCoroutineScope() {
        // Act
        fixture.destroy()
        // Assert
        assertThat(fixture.coroutineScope.isActive).isFalse
    }

    @Test
    fun appendToSeriesRemovesOldDataPoints() {
        // Arrange
        val count = 2
        val existingData = SeriesData(listOf(DataPoint(0, -40), DataPoint(1, -50), DataPoint(2, -60)))
        doReturn(existingData).whenever(seriesCache)[wiFiDetail]
        // Act
        val actual = fixture.appendToSeries(wiFiDetail, DataPoint(3, -70), count, true)
        // Assert
        assertThat(actual).isTrue
        assertThat(existingData.dataPoints).hasSize(count + 1)
        verify(seriesCache)[wiFiDetail]
    }
}
