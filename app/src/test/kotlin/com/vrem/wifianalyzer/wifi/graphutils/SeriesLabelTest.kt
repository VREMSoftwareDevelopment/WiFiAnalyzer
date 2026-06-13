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

import android.graphics.Canvas
import android.graphics.RectF
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.patrykandpatrick.vico.views.cartesian.CartesianDrawingContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doReturn
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

private const val Y_OFFSET = 8

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class SeriesLabelTest {
    private val context: CartesianDrawingContext = mock()
    private val canvas: Canvas = mock()
    private val calculateLabelPosition: CalculateLabelPosition = mock()
    private val configureLabel: ConfigureLabel = mock()
    private val layerBounds: RectF = RectF(0f, 0f, 200f, 200f)

    private val fixture = SeriesLabel(calculateLabelPosition, configureLabel)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(context, canvas, calculateLabelPosition, configureLabel)
    }

    @Test
    fun paintIsAntiAliased() {
        // Assert
        assertThat(fixture.paint.isAntiAlias).isTrue()
    }

    @Test
    fun drawOverLayersSkipsWhenSeriesEmpty() {
        // Act
        fixture.drawOverLayers(context)
        // Assert
        verify(context, never()).spToPx(any())
        verify(canvas, never()).drawText(any<String>(), any(), any(), any())
    }

    @Test
    fun drawOverLayersSkipsDrawText() {
        // Arrange
        val series = withSeries()
        withContext()
        fixture.seriesSnapshot = series
        series.forEach { seriesData ->
            doReturn(null).whenever(calculateLabelPosition).invoke(context, seriesData)
        }
        // Act
        fixture.drawOverLayers(context)
        // Assert
        verifyContext()
        verify(configureLabel, never()).invoke(any(), any(), any(), any())
        series.forEach { seriesData ->
            verify(calculateLabelPosition).invoke(context, seriesData)
            verify(canvas, never()).drawText(eq(seriesData.title), any(), any(), any())
        }
    }

    @Test
    fun drawOverLayers() {
        // Arrange
        val labelPosition = LabelPosition(30f, 60f)
        val series = withSeries()
        withContext()
        fixture.seriesSnapshot = series
        series.forEach { seriesData ->
            doReturn(labelPosition).whenever(calculateLabelPosition).invoke(context, seriesData)
        }
        // Act
        fixture.drawOverLayers(context)
        // Assert
        verifyContext()
        series.forEach { seriesData ->
            verify(calculateLabelPosition).invoke(context, seriesData)
            verify(configureLabel).invoke(context, labelPosition, seriesData, fixture.paint)
            verify(canvas).drawText(seriesData.title, labelPosition.x, labelPosition.y - Y_OFFSET, fixture.paint)
        }
    }

    private fun withSeries(): List<SeriesData> =
        listOf(
            SeriesData(listOf(DataPoint(1, -50)), title = "SSID1"),
            SeriesData(listOf(DataPoint(2, -60)), title = "SSID2"),
            SeriesData(listOf(DataPoint(3, -70)), title = "SSID3"),
        )

    private fun verifyContext() {
        verify(context).spToPx(4f)
        verify(context).canvas
        verify(context).layerBounds
        verify(canvas).save()
        verify(canvas).clipRect(layerBounds)
        verify(canvas).restoreToCount(0)
    }

    private fun withContext() {
        doReturn(8f).whenever(context).spToPx(4f)
        doReturn(canvas).whenever(context).canvas
        doReturn(layerBounds).whenever(context).layerBounds
    }
}
