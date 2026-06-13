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

class SeriesDataTest {
    @Test
    fun replaceAll() {
        // Arrange
        val fixture = SeriesData(listOf(DataPoint(1, -10), DataPoint(2, -20)))
        val newPoints = listOf(DataPoint(3, -30), DataPoint(4, -40), DataPoint(5, -50))
        // Act
        fixture.replaceAll(newPoints)
        // Assert
        assertThat(fixture.dataPoints).containsExactlyElementsOf(newPoints)
    }

    @Test
    fun appendAddsDataPoint() {
        // Arrange
        val fixture = SeriesData(listOf(DataPoint(1, -10)))
        // Act
        fixture.append(DataPoint(2, -20), 3)
        // Assert
        assertThat(fixture.dataPoints).containsExactly(DataPoint(1, -10), DataPoint(2, -20))
    }

    @Test
    fun appendRemovesOldestWhenExceedingMaxSize() {
        // Arrange
        val fixture = SeriesData(listOf(DataPoint(1, -10), DataPoint(2, -20)))
        // Act
        fixture.append(DataPoint(3, -30), 2)
        // Assert
        assertThat(fixture.dataPoints).containsExactly(DataPoint(2, -20), DataPoint(3, -30))
    }

    @Test
    fun toCoordinatesReturnsXValues() {
        // Arrange
        val seriesData = SeriesData(listOf(DataPoint(10, -50), DataPoint(20, -60)))
        // Act
        val actual = seriesData.toCoordinates()
        // Assert
        assertThat(actual.first).containsExactly(10.0, 20.0)
    }

    @Test
    fun toCoordinatesReturnsYValues() {
        // Arrange
        val seriesData = SeriesData(listOf(DataPoint(10, -50), DataPoint(20, -60)))
        // Act
        val actual = seriesData.toCoordinates()
        // Assert
        assertThat(actual.second).containsExactly(-50.0, -60.0)
    }

    @Test
    fun toCoordinatesHandlesEmptyDataPoints() {
        // Arrange
        val seriesData = SeriesData(emptyList())
        // Act
        val actual = seriesData.toCoordinates()
        // Assert
        assertThat(actual.first).isEmpty()
        assertThat(actual.second).isEmpty()
    }

    @Test
    fun listToCoordinatesMapsEachElement() {
        // Arrange
        val seriesData = withSeriesData()
        val expected = withExpectedCoordinates()
        // Act
        val actual = seriesData.toCoordinates()
        // Assert
        assertThat(actual).containsExactlyElementsOf(expected)
    }

    @Test
    fun listToCoordinatesReturnsEmptyForEmptyList() {
        // Act
        val actual = emptyList<SeriesData>().toCoordinates()
        // Assert
        assertThat(actual).isEmpty()
    }

    private fun withSeriesData(): List<SeriesData> =
        listOf(
            SeriesData(listOf(DataPoint(1, -10))),
            SeriesData(listOf(DataPoint(2, -20), DataPoint(3, -30))),
        )

    private fun withExpectedCoordinates(): List<Coordinates> =
        listOf(
            listOf(1.0) to listOf(-10.0),
            listOf(2.0, 3.0) to listOf(-20.0, -30.0),
        )
}
