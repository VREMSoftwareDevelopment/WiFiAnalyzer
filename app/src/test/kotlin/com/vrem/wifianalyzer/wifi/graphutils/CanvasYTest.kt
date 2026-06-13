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

import android.graphics.RectF
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.patrykandpatrick.vico.views.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.views.cartesian.data.CartesianChartRanges
import com.patrykandpatrick.vico.views.cartesian.data.MutableCartesianChartRanges
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class CanvasYTest {
    private val context: CartesianDrawingContext = mock()
    private val ranges: CartesianChartRanges = mock()
    private val yRange = MutableCartesianChartRanges.MutableYRange(-100.0, 0.0)
    private val layerBounds = RectF(0f, 0f, 100f, 200f)

    @Before
    fun setUp() {
        doReturn(ranges).whenever(context).ranges
        doReturn(layerBounds).whenever(context).layerBounds
        doReturn(yRange).whenever(ranges).getYRange(null)
    }

    @After
    fun tearDown() {
        verify(context).ranges
        verify(context, times(2)).layerBounds
        verify(ranges).getYRange(null)
        verifyNoMoreInteractions(context, ranges)
    }

    @Test
    fun canvasYCalculation() {
        // Arrange
        val point = DataPoint(5, -50)
        // Act
        val actual = context.canvasY(point)
        // Assert
        // canvasY = bottom - ((y - minY) / length) * height
        // = 200 - ((-50 - -100) / 100) * 200 = 200 - 0.5 * 200 = 100
        assertThat(actual).isEqualTo(100f)
    }

    @Test
    fun canvasYAtMinY() {
        // Arrange
        val dataPoint = DataPoint(5, -100)
        // Act
        val actual = context.canvasY(dataPoint)
        // Assert
        assertThat(actual).isEqualTo(200f)
    }

    @Test
    fun canvasYAtMaxY() {
        // Arrange
        val dataPoint = DataPoint(5, 0)
        // Act
        val actual = context.canvasY(dataPoint)
        // Assert
        assertThat(actual).isEqualTo(0f)
    }
}
