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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LineStyleTrackerTest {
    private val fixture = LineStyleTracker()

    @Test
    fun changedReturnsTrueOnFirstCall() {
        // Arrange
        val seriesData = listOf(seriesData(primary = 0xFF0000))
        // Act
        val actual = fixture.changed(seriesData)
        // Assert
        assertThat(actual).isTrue
    }

    @Test
    fun changedReturnsFalseWhenStylesIdentical() {
        // Arrange
        val seriesData = listOf(seriesData(primary = 0xFF0000))
        fixture.changed(seriesData)
        // Act
        val actual = fixture.changed(seriesData)
        // Assert
        assertThat(actual).isFalse
    }

    @Test
    fun changedReturnsTrueWhenPrimaryColorChanged() {
        // Arrange
        val seriesData1 = listOf(seriesData(primary = 0xFF0000))
        val seriesData2 = listOf(seriesData(primary = 0x00FF00))
        fixture.changed(seriesData1)
        // Act
        val actual = fixture.changed(seriesData2)
        // Assert
        assertThat(actual).isTrue
    }

    @Test
    fun changedReturnsTrueWhenConnectedChanged() {
        // Arrange
        val seriesData1 = listOf(seriesData(connected = false))
        val seriesData2 = listOf(seriesData(connected = true))
        fixture.changed(seriesData1)
        // Act
        val actual = fixture.changed(seriesData2)
        // Assert
        assertThat(actual).isTrue
    }

    @Test
    fun changedReturnsTrueWhenDrawBackgroundChanged() {
        // Arrange
        val seriesData1 = listOf(seriesData(drawBackground = false))
        val seriesData2 = listOf(seriesData(drawBackground = true))
        fixture.changed(seriesData1)
        // Act
        val actual = fixture.changed(seriesData2)
        // Assert
        assertThat(actual).isTrue
    }

    @Test
    fun changedReturnsTrueWhenSeriesCountChanged() {
        // Arrange
        val seriesData1 = listOf(seriesData())
        val seriesData2 = listOf(seriesData(), seriesData())
        fixture.changed(seriesData1)
        // Act
        val actual = fixture.changed(seriesData2)
        // Assert
        assertThat(actual).isTrue
    }

    @Test
    fun resetForcesChangedToReturnTrue() {
        // Arrange
        val seriesData = listOf(seriesData())
        fixture.changed(seriesData)
        fixture.reset()
        // Act
        val actual = fixture.changed(seriesData)
        // Assert
        assertThat(actual).isTrue
    }

    @Test
    fun changedReturnsFalseWhenEmptyMatchesInitialState() {
        // Act
        val actual = fixture.changed(emptyList())
        // Assert
        assertThat(actual).isFalse
    }

    @Test
    fun changedReturnsFalseOnCallAfterColorChangeSettles() {
        // Arrange
        val seriesData1 = listOf(seriesData(primary = 0xFF0000))
        val seriesData2 = listOf(seriesData(primary = 0x00FF00))
        fixture.changed(seriesData1)
        assertThat(fixture.changed(seriesData2)).isTrue
        // Act
        val actual = fixture.changed(seriesData2)
        // Assert
        assertThat(actual).isFalse
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
}
