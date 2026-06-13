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

import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.patrykandpatrick.vico.views.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.views.cartesian.data.MutableCartesianChartRanges
import com.patrykandpatrick.vico.views.cartesian.layer.MutableCartesianLayerDimensions
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
    fun zeroXStepReturnsNull() {
        // Arrange
        val series = withSeriesData()
        stubContext(xStep = 0.0)
        // Act
        val actual = calculateLabelPosition(context, series)
        // Assert
        assertThat(actual).isNull()
        verify(context).ranges
    }

    @Test
    fun usesMiddleDataPoint() {
        // Arrange
        val dataPoints =
            mutableListOf(
                DataPoint(2400, -40),
                DataPoint(2410, -50),
                DataPoint(2420, -60),
                DataPoint(2430, -70),
                DataPoint(2440, -45),
            )
        val series = withSeriesData(dataPoints = dataPoints)
        stubContext()
        // Act
        val actual = calculateLabelPosition(context, series)!!
        // Assert
        // middle point is index 2: DataPoint(2420, -60)
        // canvasX = 0 + 1 * 2 * ((2420 - 2400) / 1) = 40
        assertThat(actual.x).isEqualTo(40f)
        verifyContext()
    }

    @Test
    fun textAlignIsCenter() {
        // Arrange
        val series = withSeriesData()
        stubContext()
        // Act
        val actual = calculateLabelPosition(context, series)!!
        // Assert
        assertThat(actual.textAlign).isEqualTo(Paint.Align.CENTER)
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
    fun singleDataPoint() {
        // Arrange
        val series = withSeriesData(dataPoints = listOf(DataPoint(2420, -50)))
        stubContext()
        // Act
        val actual = calculateLabelPosition(context, series)!!
        // Assert
        // middle index = 0/2 = 0, point = DataPoint(2420, -50)
        // canvasX = 0 + 1 * 2 * ((2420 - 2400) / 1) = 40
        assertThat(actual.x).isEqualTo(40f)
        verifyContext()
    }

    @Test
    fun rtlUsesRightBound() {
        // Arrange
        val series = withSeriesData(dataPoints = listOf(DataPoint(2420, -50)))
        stubContext(isLtr = false)
        // Act
        val actual = calculateLabelPosition(context, series)!!
        // Assert
        // boundsStart = layerBounds.right = 500
        // canvasX = 500 + (-1) * 0 - 0 + (-1) * 2 * ((2420 - 2400) / 1) = 500 - 40 = 460
        assertThat(actual.x).isEqualTo(460f)
        verifyContext()
    }

    private fun verifyContext() {
        verify(context, atLeastOnce()).ranges
        verify(context, atLeastOnce()).layerBounds
        verify(context, atLeastOnce()).layerDimensions
        verify(context, atLeastOnce()).layoutDirectionMultiplier
        verify(context, atLeastOnce()).isLtr
        verify(context, atLeastOnce()).scroll
    }

    private fun stubContext(
        xStep: Double = 1.0,
        isLtr: Boolean = true,
    ) {
        val ranges =
            MutableCartesianChartRanges().apply {
                tryUpdate(2400.0, 2500.0, -100.0, 0.0, null)
                this.xStep = xStep
            }
        val dims = MutableCartesianLayerDimensions(xSpacing = 2f)
        doReturn(ranges).whenever(context).ranges
        doReturn(layerBounds).whenever(context).layerBounds
        doReturn(dims).whenever(context).layerDimensions
        doReturn(isLtr).whenever(context).isLtr
        doReturn(if (isLtr) 1 else -1).whenever(context).layoutDirectionMultiplier
        doReturn(0f).whenever(context).scroll
    }

    private fun withSeriesData(
        title: String = "TestSSID",
        y: Int = -50,
        dataPoints: List<DataPoint> = (1..5).map { DataPoint(2400 + it * 5, y) },
    ): SeriesData =
        SeriesData(
            dataPoints = dataPoints,
            title = title,
            graphColor = GraphColor(0xFF0000, 0x00FF00),
        )
}
