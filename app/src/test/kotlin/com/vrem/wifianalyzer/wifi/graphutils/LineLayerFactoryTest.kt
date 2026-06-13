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
import com.patrykandpatrick.vico.views.cartesian.data.CartesianLayerRangeProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class LineLayerFactoryTest {
    private val rangeProvider: CartesianLayerRangeProvider =
        CartesianLayerRangeProvider.fixed(
            minY = MIN_Y.toDouble(),
            maxY = MAX_Y_DEFAULT.toDouble(),
        )

    private val fixture = LineLayerFactory()

    @Test
    fun linesMapsSeriesDataToLineProperties() {
        // Arrange
        val seriesData = withSeriesData()
        val expectedThickness = listOf(THICKNESS_CONNECTED_DP, THICKNESS_REGULAR_DP, THICKNESS_CONNECTED_DP)
        val expectedColors = listOf(0xFF0000, 0x00FF00, 0x0000FF)
        // Act
        val actual = fixture.lines(seriesData)
        // Assert
        assertThat(actual).hasSize(3)
        assertThat(actual.map { it.stroke.thicknessDp }).containsExactlyElementsOf(expectedThickness)
        assertThat(actual.map { it.fillColor }).containsExactlyElementsOf(expectedColors)
    }

    @Test
    fun linesReturnsEmptyForEmptySeries() {
        // Act
        val actual = fixture.lines(emptyList())
        // Assert
        assertThat(actual).isEmpty()
    }

    @Test
    fun createReturnsLayer() {
        // Arrange
        val seriesData = listOf(seriesData(), seriesData(), seriesData())
        // Act
        val actual = fixture.create(seriesData, rangeProvider)
        // Assert
        assertThat(actual).isNotNull
    }

    private fun seriesData(
        primary: Int = 0xFF0000,
        background: Int = 0x00FF00,
        connected: Boolean = false,
        drawBackground: Boolean = false,
    ): SeriesData =
        SeriesData(
            dataPoints = listOf(DataPoint(1, -50)),
            graphColor = GraphColor(primary, background),
            connected = connected,
            drawBackground = drawBackground,
        )

    private fun withSeriesData(): List<SeriesData> =
        listOf(
            seriesData(primary = 0xFF0000, connected = true),
            seriesData(primary = 0x00FF00, connected = false),
            seriesData(primary = 0x0000FF, connected = true, drawBackground = true),
        )
}
