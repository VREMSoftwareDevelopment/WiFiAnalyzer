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

import android.graphics.Color
import android.view.View
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.Viewport
import com.jjoe64.graphview.series.BaseSeries
import com.vrem.wifianalyzer.SIZE_MAX
import com.vrem.wifianalyzer.SIZE_MIN
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class GraphViewWrapperTest {
    private val graphView: GraphView = mock()
    private val viewport: Viewport = mock()
    private val gridLabelRenderer: GridLabelRenderer = mock()
    private val legendRenderer: LegendRenderer = mock()
    private val seriesCache: SeriesCache = mock()
    private val seriesOptions: SeriesOptions = mock()
    private val baseSeries: BaseSeries<GraphDataPoint> = mock()
    private val dataPoint: GraphDataPoint = GraphDataPoint(1, 2)
    private val dataPoints = arrayOf(dataPoint)
    private val wiFiDetail = WiFiDetail.EMPTY
    private val fixture =
        spy(GraphViewWrapper(graphView, GraphLegend.HIDE, ThemeStyle.DARK, seriesCache, seriesOptions))

    @Before
    fun setUp() {
        assertThat(fixture.graphLegend).isEqualTo(GraphLegend.HIDE)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(viewport)
    }

    @Test
    fun removeSeries() {
        // setup
        val newSeries: Set<WiFiDetail> = setOf()
        val difference: List<WiFiDetail> = listOf()
        val removed = listOf(baseSeries)
        whenever(seriesCache.difference(newSeries)).thenReturn(difference)
        whenever(seriesCache.remove(difference)).thenReturn(removed)
        // execute
        fixture.removeSeries(newSeries)
        // validate
        verify(seriesCache).difference(newSeries)
        verify(seriesCache).remove(difference)
        verify(seriesOptions).removeSeriesColor(baseSeries)
        verify(graphView).removeSeries(baseSeries)
    }

    @Test
    fun differenceSeries() {
        // setup
        val newSeries: Set<WiFiDetail> = setOf()
        val expected: List<WiFiDetail> = listOf()
        whenever(seriesCache.difference(newSeries)).thenReturn(expected)
        // execute
        val actual = fixture.differenceSeries(newSeries)
        // validate
        assertThat(actual).isEqualTo(expected)
        verify(seriesCache).difference(newSeries)
    }

    @Test
    fun addSeriesDirectly() {
        // execute
        fixture.addSeries(baseSeries)
        // validate
        verify(graphView).addSeries(baseSeries)
    }

    @Test
    fun addSeriesWhenSeriesExistsDoesNotAddSeries() {
        // setup
        whenever(seriesCache.contains(wiFiDetail)).thenReturn(true)
        // execute
        val actual = fixture.addSeries(wiFiDetail, baseSeries, false)
        // validate
        assertThat(actual).isFalse
        verify(seriesCache).contains(wiFiDetail)
        verify(seriesCache, never()).put(wiFiDetail, baseSeries)
    }

    @Test
    fun addSeriesAddsSeries() {
        // setup
        val expectedTitle = wiFiDetail.wiFiIdentifier.ssid + " " + wiFiDetail.wiFiSignal.channelDisplay()
        val connected = wiFiDetail.wiFiAdditional.wiFiConnection.connected
        whenever(seriesCache.contains(wiFiDetail)).thenReturn(false)
        // execute
        val actual = fixture.addSeries(wiFiDetail, baseSeries, true)
        // validate
        assertThat(actual).isTrue
        verify(seriesCache).contains(wiFiDetail)
        verify(seriesCache).put(wiFiDetail, baseSeries)
        verify(baseSeries).title = expectedTitle
        verify(baseSeries).setOnDataPointTapListener(any())
        verify(seriesOptions).highlightConnected(baseSeries, connected)
        verify(seriesOptions).setSeriesColor(baseSeries)
        verify(seriesOptions).drawBackground(baseSeries, true)
        verify(graphView).addSeries(baseSeries)
    }

    @Test
    fun updateSeriesWhenSeriesDoesNotExistsDoesNotUpdateSeries() {
        // setup
        whenever(seriesCache.contains(wiFiDetail)).thenReturn(false)
        // execute
        val actual = fixture.updateSeries(wiFiDetail, dataPoints, true)
        // validate
        assertThat(actual).isFalse
        verify(seriesCache).contains(wiFiDetail)
        verify(seriesCache, never())[wiFiDetail]
    }

    @Test
    fun updateSeriesWhenSeriesDoesExists() {
        // setup
        val expectedTitle = wiFiDetail.wiFiIdentifier.ssid + " " + wiFiDetail.wiFiSignal.channelDisplay()
        val connected = wiFiDetail.wiFiAdditional.wiFiConnection.connected
        whenever(seriesCache.contains(wiFiDetail)).thenReturn(true)
        whenever(seriesCache[wiFiDetail]).thenReturn(baseSeries)
        // execute
        val actual = fixture.updateSeries(wiFiDetail, dataPoints, true)
        // validate
        assertThat(actual).isTrue
        verify(seriesCache).contains(wiFiDetail)
        verify(seriesCache)[wiFiDetail]
        verify(baseSeries).resetData(dataPoints)
        verify(baseSeries).title = expectedTitle
        verify(seriesOptions).highlightConnected(baseSeries, connected)
        verify(seriesOptions).drawBackground(baseSeries, true)
    }

    @Test
    fun appendSeriesWhenSeriesDoesNotExistsDoesNotUpdateSeries() {
        // setup
        val count = 10
        whenever(seriesCache.contains(wiFiDetail)).thenReturn(false)
        // execute
        val actual = fixture.appendToSeries(wiFiDetail, dataPoint, count, true)
        // validate
        assertThat(actual).isFalse
        verify(seriesCache).contains(wiFiDetail)
        verify(seriesCache, never())[wiFiDetail]
    }

    @Test
    fun appendSeriesWhenSeriesDoesExists() {
        // setup
        val count = 10
        val connected = wiFiDetail.wiFiAdditional.wiFiConnection.connected
        whenever(seriesCache.contains(wiFiDetail)).thenReturn(true)
        whenever(seriesCache[wiFiDetail]).thenReturn(baseSeries)
        // execute
        val actual = fixture.appendToSeries(wiFiDetail, dataPoint, count, true)
        // validate
        assertThat(actual).isTrue
        verify(seriesCache).contains(wiFiDetail)
        verify(seriesCache)[wiFiDetail]
        verify(baseSeries).appendData(dataPoint, true, count + 1)
        verify(seriesOptions).highlightConnected(baseSeries, connected)
        verify(seriesOptions).drawBackground(baseSeries, true)
    }

    @Test
    fun updateLegend() {
        // setup
        val textSize = 10f
        doReturn(legendRenderer).whenever(fixture).newLegendRenderer()
        whenever(graphView.titleTextSize).thenReturn(textSize)
        whenever(graphView.legendRenderer).thenReturn(legendRenderer)
        // execute
        fixture.updateLegend(GraphLegend.RIGHT)
        // validate
        assertThat(fixture.graphLegend).isEqualTo(GraphLegend.RIGHT)
        verify(graphView).titleTextSize
        verify(graphView).legendRenderer
        verify(graphView).legendRenderer = legendRenderer
        verify(legendRenderer).resetStyles()
        verify(legendRenderer).width = 0
        verify(legendRenderer).textSize = textSize
        verify(legendRenderer).textColor = Color.WHITE
        verify(legendRenderer).isVisible = true
        verify(legendRenderer).align = LegendRenderer.LegendAlign.TOP
    }

    @Test
    fun setVisibility() {
        // execute
        fixture.visibility(View.VISIBLE)
        // validate
        verify(graphView).visibility = View.VISIBLE
    }

    @Test
    fun setHorizontalLabelsVisible() {
        // setup
        doReturn(gridLabelRenderer).whenever(graphView).gridLabelRenderer
        // execute
        fixture.setHorizontalLabelsVisible(true)
        // validate
        verify(graphView).gridLabelRenderer
        verify(gridLabelRenderer).isHorizontalLabelsVisible = true
    }

    @Test
    fun calculateGraphType() {
        // execute & validate
        assertThat(fixture.calculateGraphType()).isGreaterThan(0)
    }

    @Test
    fun setViewport() {
        // setup
        whenever(graphView.gridLabelRenderer).thenReturn(gridLabelRenderer)
        whenever(gridLabelRenderer.numHorizontalLabels).thenReturn(10)
        whenever(graphView.viewport).thenReturn(viewport)
        // execute
        fixture.setViewport()
        // validate
        verify(graphView).gridLabelRenderer
        verify(gridLabelRenderer).numHorizontalLabels
        verify(graphView).viewport
        verify(viewport).setMinX(0.0)
        verify(viewport).setMaxX(9.0)
    }

    @Test
    fun getSize() {
        // execute & validate
        assertThat(fixture.size(TYPE1)).isEqualTo(SIZE_MAX)
        assertThat(fixture.size(TYPE2)).isEqualTo(SIZE_MAX)
        assertThat(fixture.size(TYPE3)).isEqualTo(SIZE_MAX)
        assertThat(fixture.size(TYPE4)).isEqualTo(SIZE_MIN)
    }

    @Test
    fun setViewportSetsMinAndMaxX() {
        // setup
        whenever(graphView.viewport).thenReturn(viewport)
        whenever(graphView.gridLabelRenderer).thenReturn(gridLabelRenderer)
        whenever(gridLabelRenderer.numHorizontalLabels).thenReturn(5)
        // execute
        fixture.setViewport()
        // validate
        verify(viewport).setMinX(0.0)
        verify(viewport).setMaxX(4.0)
    }

    @Test
    fun setViewportWithParamsSetsMinAndMaxX() {
        // setup
        whenever(graphView.viewport).thenReturn(viewport)
        // execute
        fixture.setViewport(1, 10)
        // validate
        verify(viewport).setMinX(1.0)
        verify(viewport).setMaxX(10.0)
    }

    @Test
    fun viewportCntXReturnsCorrectValue() {
        // setup
        whenever(graphView.gridLabelRenderer).thenReturn(gridLabelRenderer)
        whenever(gridLabelRenderer.numHorizontalLabels).thenReturn(7)
        // execute
        assertThat(fixture.viewportCntX).isEqualTo(6)
        // validate
        verify(graphView).gridLabelRenderer
        verify(gridLabelRenderer).numHorizontalLabels
    }

    @Test
    fun updateLegendWhenSameLegendDoesNotResetLegendRenderer() {
        whenever(graphView.legendRenderer).thenReturn(legendRenderer)
        whenever(graphView.titleTextSize).thenReturn(12f)
        fixture.updateLegend(GraphLegend.HIDE)
        verify(legendRenderer).resetStyles()
    }

    @Test
    fun setHorizontalLabelsVisibleSetsValue() {
        // setup
        whenever(graphView.gridLabelRenderer).thenReturn(gridLabelRenderer)
        // execute
        fixture.setHorizontalLabelsVisible(true)
        // validate
        verify(gridLabelRenderer).isHorizontalLabelsVisible = true
    }

    @Test
    fun visibilitySetsVisibility() {
        // execute
        fixture.visibility(View.VISIBLE)
        // validate
        verify(graphView).visibility = View.VISIBLE
    }

    @Test
    fun newSeriesReturnsTrueIfNotExists() {
        // setup
        whenever(seriesCache.contains(wiFiDetail)).thenReturn(false)
        // execute & validate
        assertThat(fixture.newSeries(wiFiDetail)).isTrue
    }

    @Test
    fun newSeriesReturnsFalseIfExists() {
        // setup
        whenever(seriesCache.contains(wiFiDetail)).thenReturn(true)
        // execute & validate
        assertThat(fixture.newSeries(wiFiDetail)).isFalse
    }

    @Test
    fun addSeriesTriggersPopupOnTap() {
        // setup
        whenever(seriesCache.contains(wiFiDetail)).thenReturn(false)
        // execute
        fixture.addSeries(wiFiDetail, baseSeries, false)
        // validate
        verify(baseSeries).setOnDataPointTapListener(any())
    }
}
