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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.patrykandpatrick.vico.views.cartesian.CartesianChart
import com.patrykandpatrick.vico.views.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.views.cartesian.layer.LineCartesianLayer
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import java.util.AbstractMap.SimpleEntry

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class ChartUpdaterTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val chartView = GraphBuilder(MAX_Y_DEFAULT, ThemeStyle.DARK).build(mainActivity, false)
    private val seriesLabel = SeriesLabel(calculateLabelPosition = { _, _ -> null })
    private val seriesCache = SeriesCache(emptyList())
    private val lineStyleTracker = LineStyleTracker()
    private val lineLayerFactory = LineLayerFactory()
    private val markerInteraction: MarkerInteraction = mock()
    private val rangeProvider: CartesianLayerRangeProvider =
        CartesianLayerRangeProvider.fixed(
            minY = MIN_Y.toDouble(),
            maxY = MAX_Y_DEFAULT.toDouble(),
        )

    private val fixture =
        ChartUpdater(
            chartView,
            seriesLabel,
            seriesCache,
            lineStyleTracker,
            lineLayerFactory,
            markerInteraction,
        )

    @Before
    fun setUp() {
        doAnswer { it.arguments[0] as CartesianChart }.whenever(markerInteraction).applyTo(any<CartesianChart>())
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(markerInteraction)
    }

    @Test
    fun syncSwapsChartOnFirstCall() {
        // Arrange
        val expected = populatedData()
        val entries = withEntries(expected)
        val existingChart = chartView.chart!!
        // Act
        val actual = fixture.sync(entries, existingChart, rangeProvider)
        // Assert
        assertThat(actual).isEqualTo(expected)
        val swapped = chartView.chart!!
        assertThat(swapped).isNotSameAs(existingChart)
        assertThat(swapped.layers).hasSize(1).hasOnlyElementsOfType(LineCartesianLayer::class.java)
        verify(markerInteraction).applyTo(swapped)
        verify(markerInteraction).updatePointMap(entries)
    }

    @Test
    fun syncUpdatesSeriesSnapshot() {
        // Arrange
        val expected = populatedData()
        val entries = withEntries(expected)
        val existingChart = chartView.chart!!
        // Act
        val actual = fixture.sync(entries, existingChart, rangeProvider)
        // Assert
        assertThat(actual).isEqualTo(expected)
        assertThat(seriesLabel.seriesSnapshot).isEqualTo(expected)
        verify(markerInteraction).applyTo(chartView.chart!!)
        verify(markerInteraction).updatePointMap(entries)
    }

    @Test
    fun syncForwardsEntriesToMarkerInteraction() {
        // Arrange
        val expected = populatedData()
        val entries = withEntries(expected)
        val existingChart = chartView.chart!!
        // Act
        val actual = fixture.sync(entries, existingChart, rangeProvider)
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(markerInteraction).applyTo(chartView.chart!!)
        verify(markerInteraction).updatePointMap(entries)
    }

    @Test
    fun syncSkipsSwapWhenStylesUnchanged() {
        // Arrange
        val firstEntries = withEntries(populatedData())
        val seriesData = seriesData(dataPoints = listOf(DataPoint(99, -60)))
        val expected = populatedData(seriesData)
        val secondEntries = listOf(SimpleEntry(WiFiDetail.EMPTY, seriesData))
        fixture.sync(firstEntries, chartView.chart!!, rangeProvider)
        val chartAfterFirst = chartView.chart
        // Act
        val actual = fixture.sync(secondEntries, chartAfterFirst!!, rangeProvider)
        // Assert
        assertThat(actual).isEqualTo(expected)
        assertThat(chartView.chart).isSameAs(chartAfterFirst)
        assertThat(seriesLabel.seriesSnapshot).isEqualTo(expected)
        verify(markerInteraction).applyTo(chartAfterFirst)
        verify(markerInteraction).updatePointMap(firstEntries)
        verify(markerInteraction).updatePointMap(secondEntries)
    }

    @Test
    fun syncPointMapForwardsToMarkerInteraction() {
        // Arrange
        val entries = withEntries(populatedData())
        // Act
        fixture.syncPointMap(entries)
        // Assert
        verify(markerInteraction).updatePointMap(entries)
    }

    @Test
    fun resetStylesForcesNextSyncToSwap() {
        // Arrange
        val expected = populatedData()
        val entries = withEntries(expected)
        fixture.sync(entries, chartView.chart!!, rangeProvider)
        val chartAfterFirst = chartView.chart
        // Act
        fixture.resetStyles()
        val actual = fixture.sync(entries, chartAfterFirst!!, rangeProvider)
        // Assert
        assertThat(actual).isEqualTo(expected)
        assertThat(chartView.chart).isNotSameAs(chartAfterFirst)
        verify(markerInteraction).applyTo(chartAfterFirst)
        verify(markerInteraction).applyTo(chartView.chart!!)
        verify(markerInteraction, times(2)).updatePointMap(entries)
    }

    private fun seriesData(dataPoints: List<DataPoint> = listOf(DataPoint(1, -50))): SeriesData =
        SeriesData(
            dataPoints = dataPoints,
            graphColor = GraphColor(0xFF0000, 0x00FF00),
        )

    private fun populatedData(seriesData: SeriesData = seriesData()) = listOf(seriesData)

    private fun withEntries(populatedData: List<SeriesData>): List<SeriesEntry> =
        populatedData.map { SimpleEntry(WiFiDetail.EMPTY, it) }
}
