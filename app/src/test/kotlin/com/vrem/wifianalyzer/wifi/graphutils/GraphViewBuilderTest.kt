/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import com.jjoe64.graphview.LabelFormatter
import com.jjoe64.graphview.Viewport
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.settings.ThemeStyle
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class GraphViewBuilderTest {
    private val numHorizontalLabels = 5
    private val gridLabelRenderer: GridLabelRenderer = mock()
    private val graphView: GraphView = mock()
    private val viewport: Viewport = mock()
    private val labelFormatter: LabelFormatter = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(gridLabelRenderer)
        verifyNoMoreInteractions(graphView)
        verifyNoMoreInteractions(viewport)
        verifyNoMoreInteractions(labelFormatter)
    }

    @Test
    fun testGraphViewLayout() {
        // setup
        val fixture = GraphViewBuilder(numHorizontalLabels, MAX_Y_DEFAULT, ThemeStyle.DARK, true)
        val layoutParams = fixture.layoutParams
        // execute
        graphView.layout(layoutParams)
        // validate
        verify(graphView).layoutParams = layoutParams
        verify(graphView).visibility = View.GONE
    }

    @Test
    fun testViewPortInitialize() {
        // setup
        val maximumY = 20
        // execute
        viewport.initialize(maximumY)
        // validate
        verify(viewport).isScrollable = true
        verify(viewport).isYAxisBoundsManual = true
        verify(viewport).setMinY(MIN_Y.toDouble())
        verify(viewport).setMaxY(maximumY.toDouble())
        verify(viewport).isXAxisBoundsManual = true
    }

    @Test
    fun testGridLabelRendererLabelFormat() {
        // execute
        gridLabelRenderer.labelFormat(labelFormatter)
        // validate
        verify(gridLabelRenderer).labelFormatter = labelFormatter
    }

    @Test
    fun testGridLabelRendererLabels() {
        // setup
        val textSize = 11.1f
        val expectedSize = textSize * TEXT_SIZE_ADJUSTMENT
        val numHorizontalLabels = 10
        val numVerticalLabels = 20
        whenever(gridLabelRenderer.textSize).thenReturn(textSize)
        // execute
        gridLabelRenderer.labels(numHorizontalLabels, numVerticalLabels, true)
        // validate
        verify(gridLabelRenderer).setHumanRounding(false)
        verify(gridLabelRenderer).isHighlightZeroLines = false
        verify(gridLabelRenderer).numVerticalLabels = numVerticalLabels
        verify(gridLabelRenderer).numHorizontalLabels = numHorizontalLabels
        verify(gridLabelRenderer).isVerticalLabelsVisible = true
        verify(gridLabelRenderer).isHorizontalLabelsVisible = true
        verify(gridLabelRenderer).textSize = expectedSize
        verify(gridLabelRenderer).textSize
        verify(gridLabelRenderer).reloadStyles()
    }

    @Test
    fun testGridLabelRendererVerticalAxisTitle() {
        // setup
        val verticalTitle = "verticalTitle"
        val textSize = 11.1f
        val expectedSize = textSize * AXIS_TEXT_SIZE_ADJUSTMENT
        whenever(gridLabelRenderer.verticalAxisTitleTextSize).thenReturn(textSize)
        // execute
        gridLabelRenderer.verticalTitle(verticalTitle)
        // validate
        verify(gridLabelRenderer).verticalAxisTitleTextSize = expectedSize
        verify(gridLabelRenderer).verticalAxisTitleTextSize
        verify(gridLabelRenderer).verticalAxisTitle = verticalTitle
    }

    @Test
    fun testGridLabelRendererVerticalTitleEmpty() {
        // execute
        gridLabelRenderer.verticalTitle("")
        // validate
        verify(gridLabelRenderer, never()).verticalAxisTitle = any()
        verify(gridLabelRenderer, never()).verticalAxisTitleTextSize = any()
    }

    @Test
    fun testGridLabelRendererHorizontalAxisTitle() {
        // setup
        val horizontalTitle = "horizontalTitle"
        val textSize = 11f
        val expectedSize = textSize * AXIS_TEXT_SIZE_ADJUSTMENT
        whenever(gridLabelRenderer.horizontalAxisTitleTextSize).thenReturn(textSize)
        // execute
        gridLabelRenderer.horizontalTitle(horizontalTitle)
        // validate
        verify(gridLabelRenderer).horizontalAxisTitleTextSize = expectedSize
        verify(gridLabelRenderer).horizontalAxisTitleTextSize
        verify(gridLabelRenderer).horizontalAxisTitle = horizontalTitle
    }

    @Test
    fun testGridLabelRendererHorizontalAxisTitleEmpty() {
        // execute
        gridLabelRenderer.horizontalTitle("")
        // validate
        verify(gridLabelRenderer, never()).horizontalAxisTitle = any()
        verify(gridLabelRenderer, never()).horizontalAxisTitleTextSize = any()
    }

    @Test
    fun testGetNumVerticalLabels() {
        // setup
        val expected = 9
        val fixture = GraphViewBuilder(numHorizontalLabels, MAX_Y_DEFAULT, ThemeStyle.DARK, true)
        // execute
        val actual = fixture.numVerticalLabels
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testGetMaximumYLimits() {
        validateMaximumY(1, MAX_Y_DEFAULT)
        validateMaximumY(0, 0)
        validateMaximumY(-50, -50)
        validateMaximumY(-51, MAX_Y_DEFAULT)
    }

    private fun validateMaximumY(maximumY: Int, expected: Int) {
        val fixture = GraphViewBuilder(numHorizontalLabels, maximumY, ThemeStyle.DARK, true)
        assertEquals(expected, fixture.maximumPortY)
    }

    @Test
    fun testGridLabelRenderColorsDarkTheme() {
        // execute
        gridLabelRenderer.colors(ThemeStyle.DARK)
        // validate
        verify(gridLabelRenderer).gridColor = Color.GRAY
        verify(gridLabelRenderer).verticalLabelsColor = Color.WHITE
        verify(gridLabelRenderer).verticalAxisTitleColor = Color.WHITE
        verify(gridLabelRenderer).horizontalLabelsColor = Color.WHITE
        verify(gridLabelRenderer).horizontalAxisTitleColor = Color.WHITE
    }

    @Test
    fun testGridLabelRenderColorsLightTheme() {
        // execute
        gridLabelRenderer.colors(ThemeStyle.LIGHT)
        // validate
        verify(gridLabelRenderer).gridColor = Color.GRAY
        verify(gridLabelRenderer).verticalLabelsColor = Color.BLACK
        verify(gridLabelRenderer).verticalAxisTitleColor = Color.BLACK
        verify(gridLabelRenderer).horizontalLabelsColor = Color.BLACK
        verify(gridLabelRenderer).horizontalAxisTitleColor = Color.BLACK
    }

}