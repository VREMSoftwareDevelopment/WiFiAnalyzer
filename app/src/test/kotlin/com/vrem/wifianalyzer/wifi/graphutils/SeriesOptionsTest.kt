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
package com.vrem.wifianalyzer.wifi.graphutils

import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.TitleLineGraphSeries
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

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
    }

    @Test
    fun highlightConnectedLineGraphSeriesSetsNotConnectedThickness() {
        // execute
        fixture.highlightConnected(lineGraphSeries, false)
        // validate
        verify(lineGraphSeries).thickness = THICKNESS_REGULAR
    }

    @Test
    fun highlightConnectedTitleLineGraphSeriesSetsConnectedThickness() {
        // execute
        fixture.highlightConnected(titleLineGraphSeries, true)
        // validate
        verify(titleLineGraphSeries).thickness = THICKNESS_CONNECTED
        verify(titleLineGraphSeries).setTextBold(true)
    }

    @Test
    fun highlightConnectedTitleLineGraphSeriesSetsNotConnectedThickness() {
        // execute
        fixture.highlightConnected(titleLineGraphSeries, false)
        // validate
        verify(titleLineGraphSeries).thickness = THICKNESS_REGULAR
        verify(titleLineGraphSeries).setTextBold(false)
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
    }

    @Test
    fun drawBackgroundForLineGraphSeries() {
        // execute
        fixture.drawBackground(lineGraphSeries, true)
        // validate
        verify(lineGraphSeries).isDrawBackground = true
    }
}