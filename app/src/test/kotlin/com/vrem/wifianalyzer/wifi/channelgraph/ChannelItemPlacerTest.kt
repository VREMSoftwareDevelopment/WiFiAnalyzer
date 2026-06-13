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

import com.patrykandpatrick.vico.views.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.views.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.views.cartesian.data.MutableCartesianChartRanges
import com.patrykandpatrick.vico.views.cartesian.layer.MutableCartesianLayerDimensions
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ChannelItemPlacerTest {
    private val drawingContext: CartesianDrawingContext = mock()
    private val measuringContext: CartesianMeasuringContext = mock()
    private val layerDimensions = MutableCartesianLayerDimensions()
    private val fullXRange = 2400.0..2500.0

    @After
    fun tearDown() {
        verifyNoMoreInteractions(drawingContext, measuringContext)
    }

    @Test
    fun getLabelValuesReturnsFrequenciesInRange() {
        // Arrange
        val fixture = channelItemPlacer(WiFiBand.GHZ2)
        // Act
        val actual = fixture.getLabelValues(drawingContext, 2400.0..2500.0, fullXRange, 10f)
        // Assert
        assertThat(actual).hasSize(13)
        assertThat(actual.first()).isEqualTo(2412.0)
        assertThat(actual.last()).isEqualTo(2472.0)
    }

    @Test
    fun getLabelValuesFiltersOutsideRange() {
        // Arrange
        val fixture = channelItemPlacer(WiFiBand.GHZ2)
        // Act
        val actual = fixture.getLabelValues(drawingContext, 0.0..0.0, 2420.0..2440.0, 10f)
        // Assert
        assertThat(actual).containsExactly(2422.0, 2427.0, 2432.0, 2437.0)
    }

    @Test
    fun getWidthMeasurementLabelValuesReturnsFirstThree() {
        // Arrange
        val fixture = channelItemPlacer(WiFiBand.GHZ2)
        // Act
        val actual = fixture.getWidthMeasurementLabelValues(measuringContext, layerDimensions, fullXRange)
        // Assert
        assertThat(actual).containsExactly(2412.0, 2417.0, 2422.0)
    }

    @Test
    fun getHeightMeasurementLabelValuesReturnsFirstThree() {
        // Arrange
        val fixture = channelItemPlacer(WiFiBand.GHZ5)
        // Act
        val actual = fixture.getHeightMeasurementLabelValues(measuringContext, layerDimensions, fullXRange, 10f)
        // Assert
        assertThat(actual).hasSize(3)
    }

    @Test
    fun getStartLayerMarginIsZero() {
        // Arrange
        val fixture = channelItemPlacer(WiFiBand.GHZ2)
        // Act
        val actual = fixture.getStartLayerMargin(measuringContext, layerDimensions, 1f, 10f)
        // Assert
        assertThat(actual).isEqualTo(0f)
    }

    @Test
    fun getEndLayerMarginIsZero() {
        // Arrange
        val fixture = channelItemPlacer(WiFiBand.GHZ2)
        // Act
        val actual = fixture.getEndLayerMargin(measuringContext, layerDimensions, 1f, 10f)
        // Assert
        assertThat(actual).isEqualTo(0f)
    }

    @Test
    fun getLineValuesForGhz2UsesShortSpacing() {
        // Arrange
        val fixture = channelItemPlacer(WiFiBand.GHZ2)
        stubRanges(minX = 2400.0, maxX = 2500.0, xStep = 1.0)
        // Act
        val actual = fixture.getLineValues(drawingContext, fullXRange, fullXRange, 10f)
        // Assert
        assertThat(actual).hasSize(21)
        assertThat(actual!!.first()).isEqualTo(2400.0)
        assertThat(actual.last()).isEqualTo(2500.0)
        verify(drawingContext).ranges
    }

    @Test
    fun getLineValuesForGhz5UsesLongSpacing() {
        // Arrange
        val fixture = channelItemPlacer(WiFiBand.GHZ5)
        stubRanges(minX = 5000.0, maxX = 5100.0, xStep = 1.0)
        // Act
        val actual = fixture.getLineValues(drawingContext, 5000.0..5100.0, 5000.0..5100.0, 10f)
        // Assert
        assertThat(actual).hasSize(6)
        assertThat(actual!!.first()).isEqualTo(5000.0)
        assertThat(actual.last()).isEqualTo(5100.0)
        verify(drawingContext).ranges
    }

    @Test
    fun getLineValuesClampsSpacingToAtLeastOne() {
        // Arrange
        val fixture = channelItemPlacer(WiFiBand.GHZ2)
        stubRanges(minX = 2400.0, maxX = 2410.0, xStep = 100.0)
        // Act
        val actual = fixture.getLineValues(drawingContext, fullXRange, fullXRange, 10f)
        // Assert
        assertThat(actual).containsExactly(2400.0)
        verify(drawingContext).ranges
    }

    @Test
    fun getLineValuesFiltersOutsideRange() {
        // Arrange
        val fixture = channelItemPlacer(WiFiBand.GHZ2)
        stubRanges(minX = 2400.0, maxX = 2500.0, xStep = 1.0)
        // Act
        val actual = fixture.getLineValues(drawingContext, fullXRange, 2440.0..2460.0, 10f)
        // Assert
        assertThat(actual).containsExactly(2440.0, 2445.0, 2450.0, 2455.0, 2460.0)
        verify(drawingContext).ranges
    }

    @Test
    fun getShiftExtremeLinesIsFalse() {
        // Arrange
        val fixture = channelItemPlacer(WiFiBand.GHZ2)
        // Act
        val actual = fixture.getShiftExtremeLines(drawingContext)
        // Assert
        assertThat(actual).isFalse()
    }

    private fun stubRanges(
        minX: Double,
        maxX: Double,
        xStep: Double,
    ) {
        val ranges =
            MutableCartesianChartRanges().apply {
                tryUpdate(minX, maxX, -100.0, 0.0, null)
                this.xStep = xStep
            }
        doReturn(ranges).`when`(drawingContext).ranges
    }
}
