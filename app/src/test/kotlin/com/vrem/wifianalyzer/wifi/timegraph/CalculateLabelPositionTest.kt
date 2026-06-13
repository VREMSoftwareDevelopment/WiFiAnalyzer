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

import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.patrykandpatrick.vico.views.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.views.cartesian.data.MutableCartesianChartRanges
import com.vrem.wifianalyzer.wifi.graphutils.DataPoint
import com.vrem.wifianalyzer.wifi.graphutils.GraphColor
import com.vrem.wifianalyzer.wifi.graphutils.MIN_Y
import com.vrem.wifianalyzer.wifi.graphutils.SeriesData
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class CalculateLabelPositionTest {
    private val context: CartesianDrawingContext = mock()
    private val layerBounds = RectF(0f, 0f, 500f, 200f)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(context)
    }

    @Test
    fun emptyDataPointsReturnsNull() {
        // Arrange
        val series = withSeriesData(dataPoints = emptyList())
        // Act
        val actual = calculateLabelPosition(context, series)
        // Assert
        assertThat(actual).isNull()
    }

    @Test
    fun emptyTitleReturnsNull() {
        // Arrange
        val series = withSeriesData(title = "")
        // Act
        val actual = calculateLabelPosition(context, series)
        // Assert
        assertThat(actual).isNull()
    }

    @Test
    fun minYReturnsNull() {
        // Arrange
        val series = withSeriesData(y = MIN_Y)
        // Act
        val actual = calculateLabelPosition(context, series)
        // Assert
        assertThat(actual).isNull()
    }

    @Test
    fun canvasXIsCalculated() {
        // Arrange
        val series = withSeriesData()
        stubContext()
        // Act
        val actual = calculateLabelPosition(context, series)!!
        // Assert
        // canvasX = layerBounds.right - spToPx(2f) = 500 - 4 = 496
        assertThat(actual.x).isEqualTo(496f)
        verifyContext()
    }

    @Test
    fun textAlignIsRight() {
        // Arrange
        val series = withSeriesData()
        stubContext()
        // Act
        val actual = calculateLabelPosition(context, series)!!
        // Assert
        assertThat(actual.textAlign).isEqualTo(Paint.Align.RIGHT)
        verifyContext()
    }

    @Test
    fun canvasYIsCalculated() {
        // Arrange
        val series = withSeriesData()
        stubContext()
        // Act
        val actual = calculateLabelPosition(context, series)!!
        // Assert
        // canvasY = bottom - ((y - minY) / length) * height
        // = 200 - ((-50 - -100) / 100) * 200 = 100
        assertThat(actual.y).isEqualTo(100f)
        verifyContext()
    }

    @Test
    fun usesLastDataPoint() {
        // Arrange
        val dataPoints = listOf(DataPoint(1, -40), DataPoint(2, -50), DataPoint(3, MIN_Y))
        val series = withSeriesData(dataPoints = dataPoints)
        // Act
        val actual = calculateLabelPosition(context, series)
        // Assert
        // last point has y == MIN_Y, so should return null
        assertThat(actual).isNull()
    }

    @Test
    fun usesLastDataPointWhenValid() {
        // Arrange
        val dataPoints = listOf(DataPoint(1, MIN_Y), DataPoint(2, MIN_Y), DataPoint(3, -50))
        val series = withSeriesData(dataPoints = dataPoints)
        stubContext()
        // Act
        val actual = calculateLabelPosition(context, series)
        // Assert
        // last point has y == -50 which is valid
        assertThat(actual).isNotNull()
        verifyContext()
    }

    private fun verifyContext() {
        verify(context, atLeastOnce()).ranges
        verify(context, atLeastOnce()).layerBounds
        verify(context, atLeastOnce()).spToPx(2f)
    }

    private fun stubContext() {
        val ranges =
            MutableCartesianChartRanges().apply {
                tryUpdate(0.0, 100.0, -100.0, 0.0, null)
            }
        doReturn(ranges).whenever(context).ranges
        doReturn(layerBounds).whenever(context).layerBounds
        doReturn(4f).whenever(context).spToPx(2f)
    }

    private fun withSeriesData(
        title: String = "TestSSID",
        y: Int = -50,
        dataPoints: List<DataPoint> = (1..5).map { DataPoint(it, y) },
    ): SeriesData =
        SeriesData(
            dataPoints = dataPoints,
            title = title,
            graphColor = GraphColor(0xFF0000, 0x00FF00),
        )
}
