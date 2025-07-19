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
package com.vrem.wifianalyzer.wifi.graphutils

import com.jjoe64.graphview.series.BaseSeries
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.TitleLineGraphSeries
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.*

class SeriesOptionsTest {
    private val graphColors: GraphColors = mock()
    private val lineGraphSeries: LineGraphSeries<GraphDataPoint> = mock()
    private val titleLineGraphSeries: TitleLineGraphSeries<GraphDataPoint> = mock()
    private val graphColor = GraphColor(22, 11)
    private val fixture = SeriesOptions(graphColors)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(lineGraphSeries)
        verifyNoMoreInteractions(titleLineGraphSeries)
        verifyNoMoreInteractions(graphColors)
    }

    @Test
    fun removeSeries() {
        // setup
        val color = 10
        whenever(lineGraphSeries.color).thenReturn(color)
        // execute
        fixture.removeSeriesColor(lineGraphSeries)
        // validate
        verify(lineGraphSeries).color
        verify(graphColors).addColor(color.toLong())
    }

    @Test
    fun highlightConnectedLineGraphSeriesSetsConnectedThickness() {
        // execute
        fixture.highlightConnected(lineGraphSeries, true)
        // validate
        verify(lineGraphSeries).thickness = THICKNESS_CONNECTED
        verify(titleLineGraphSeries, never()).thickness
        verify(titleLineGraphSeries, never()).setTextBold(any())
    }

    @Test
    fun highlightConnectedLineGraphSeriesSetsNotConnectedThickness() {
        // execute
        fixture.highlightConnected(lineGraphSeries, false)
        // validate
        verify(lineGraphSeries).thickness = THICKNESS_REGULAR
        verify(titleLineGraphSeries, never()).thickness
        verify(titleLineGraphSeries, never()).setTextBold(any())
    }

    @Test
    fun highlightConnectedTitleLineGraphSeriesSetsConnectedThickness() {
        // execute
        fixture.highlightConnected(titleLineGraphSeries, true)
        // validate
        verify(titleLineGraphSeries).thickness = THICKNESS_CONNECTED
        verify(titleLineGraphSeries).setTextBold(true)
        verify(lineGraphSeries, never()).thickness
    }

    @Test
    fun highlightConnectedTitleLineGraphSeriesSetsNotConnectedThickness() {
        // execute
        fixture.highlightConnected(titleLineGraphSeries, false)
        // validate
        verify(titleLineGraphSeries).thickness = THICKNESS_REGULAR
        verify(titleLineGraphSeries).setTextBold(false)
        verify(lineGraphSeries, never()).thickness
    }

    @Test
    fun highlightConnectedThrowsException() {
        // setup
        val baseSeries: BaseSeries<GraphDataPoint> = mock()
        // execute & validate
        assertThatThrownBy { fixture.highlightConnected(baseSeries, true) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Unsupported series type")
    }

    @Test
    fun setSeriesColorForLineGraphSeries() {
        // setup
        whenever(graphColors.graphColor()).thenReturn(graphColor)
        // execute
        fixture.setSeriesColor(lineGraphSeries)
        // validate
        verify(graphColors).graphColor()
        verify(lineGraphSeries).color = graphColor.primary.toInt()
        verify(lineGraphSeries).backgroundColor = graphColor.background.toInt()
        verify(titleLineGraphSeries, never()).color
        verify(titleLineGraphSeries, never()).backgroundColor
    }

    @Test
    fun setSeriesColorForTitleLineGraphSeries() {
        // setup
        whenever(graphColors.graphColor()).thenReturn(graphColor)
        // execute
        fixture.setSeriesColor(titleLineGraphSeries)
        // validate
        verify(graphColors).graphColor()
        verify(titleLineGraphSeries).color = graphColor.primary.toInt()
        verify(titleLineGraphSeries).backgroundColor = graphColor.background.toInt()
        verify(lineGraphSeries, never()).color
        verify(lineGraphSeries, never()).backgroundColor
    }

    @Test
    fun setSeriesColorThrowsException() {
        // setup
        val baseSeries: BaseSeries<GraphDataPoint> = mock()
        whenever(graphColors.graphColor()).thenReturn(graphColor)
        // execute & validate
        assertThatThrownBy { fixture.setSeriesColor(baseSeries) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Unsupported series type")
        // validate
        verify(graphColors).graphColor()
    }

    @Test
    fun drawBackgroundForLineGraphSeries() {
        // execute
        fixture.drawBackground(lineGraphSeries, true)
        // validate
        verify(lineGraphSeries).isDrawBackground = true
    }

    @Test
    fun drawBackgroundForTitleLineGraphSeries() {
        // execute
        fixture.drawBackground(titleLineGraphSeries, true)
        // validate
        verify(titleLineGraphSeries).isDrawBackground = true
    }

    @Test
    fun drawBackgroundThrowsException() {
        // setup
        val baseSeries: BaseSeries<GraphDataPoint> = mock()
        // execute & validate
        assertThatThrownBy { fixture.drawBackground(baseSeries, true) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Unsupported series type")
    }

}