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

import com.patrykandpatrick.vico.views.common.Point
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.Test

private const val THRESHOLD = 50f
private const val CANVAS_X = 100f
private const val CANVAS_Y = 200f

class DistanceToTest {
    @Test
    fun exactlyOnTarget() {
        // Arrange
        val touch = Point(CANVAS_X, CANVAS_Y)
        // Act
        val actual = touch.distanceTo(CANVAS_X, CANVAS_Y)
        // Assert
        assertThat(actual).isEqualTo(0f)
    }

    @Test
    fun horizontalOffset() {
        // Arrange
        val touch = Point(CANVAS_X + 30f, CANVAS_Y)
        // Act
        val actual = touch.distanceTo(CANVAS_X, CANVAS_Y)
        // Assert
        assertThat(actual).isEqualTo(30f)
    }

    @Test
    fun verticalOffset() {
        // Arrange
        val touch = Point(CANVAS_X, CANVAS_Y + 40f)
        // Act
        val actual = touch.distanceTo(CANVAS_X, CANVAS_Y)
        // Assert
        assertThat(actual).isEqualTo(40f)
    }

    @Test
    fun diagonalOffset() {
        // Arrange - classic 3-4-5 triangle
        val touch = Point(CANVAS_X + 30f, CANVAS_Y + 40f)
        // Act
        val actual = touch.distanceTo(CANVAS_X, CANVAS_Y)
        // Assert
        // sqrt(30^2 + 40^2) = sqrt(900 + 1600) = sqrt(2500) = 50
        assertThat(actual).isCloseTo(50f, Offset.offset(0.01f))
    }

    @Test
    fun nanTouchX() {
        // Arrange
        val touch = Point(Float.NaN, CANVAS_Y + 40f)
        // Act
        val actual = touch.distanceTo(CANVAS_X, CANVAS_Y)
        // Assert
        assertThat(actual).isEqualTo(40f)
    }

    @Test
    fun nanTouchY() {
        // Arrange
        val touch = Point(CANVAS_X + 30f, Float.NaN)
        // Act
        val actual = touch.distanceTo(CANVAS_X, CANVAS_Y)
        // Assert
        assertThat(actual).isEqualTo(30f)
    }

    @Test
    fun nanBoth() {
        // Arrange
        val touch = Point(Float.NaN, Float.NaN)
        // Act
        val actual = touch.distanceTo(CANVAS_X, CANVAS_Y)
        // Assert
        assertThat(actual).isEqualTo(0f)
    }
}

class WithinProximityTest {
    @Test
    fun withinThreshold() {
        // Arrange
        val touch = Point(CANVAS_X + THRESHOLD / 2, CANVAS_Y + THRESHOLD / 2)
        // Act
        val actual = touch.withinProximity(CANVAS_X, CANVAS_Y, THRESHOLD)
        // Assert
        assertThat(actual).isTrue()
    }

    @Test
    fun exactlyOnTarget() {
        // Arrange
        val touch = Point(CANVAS_X, CANVAS_Y)
        // Act
        val actual = touch.withinProximity(CANVAS_X, CANVAS_Y, THRESHOLD)
        // Assert
        assertThat(actual).isTrue()
    }

    @Test
    fun outsideThresholdX() {
        // Arrange
        val touch = Point(CANVAS_X + THRESHOLD + 1f, CANVAS_Y)
        // Act
        val actual = touch.withinProximity(CANVAS_X, CANVAS_Y, THRESHOLD)
        // Assert
        assertThat(actual).isFalse()
    }

    @Test
    fun outsideThresholdY() {
        // Arrange
        val touch = Point(CANVAS_X, CANVAS_Y + THRESHOLD + 1f)
        // Act
        val actual = touch.withinProximity(CANVAS_X, CANVAS_Y, THRESHOLD)
        // Assert
        assertThat(actual).isFalse()
    }

    @Test
    fun outsideThresholdDiagonal() {
        // Arrange - each axis within threshold but Euclidean distance exceeds it
        val offset = THRESHOLD * 0.8f
        val touch = Point(CANVAS_X + offset, CANVAS_Y + offset)
        // Act
        val actual = touch.withinProximity(CANVAS_X, CANVAS_Y, THRESHOLD)
        // Assert
        assertThat(actual).isFalse()
    }

    @Test
    fun nanTouchX() {
        // Arrange
        val touch = Point(Float.NaN, CANVAS_Y)
        // Act
        val actual = touch.withinProximity(CANVAS_X, CANVAS_Y, THRESHOLD)
        // Assert
        assertThat(actual).isTrue()
    }

    @Test
    fun nanTouchY() {
        // Arrange
        val touch = Point(CANVAS_X, Float.NaN)
        // Act
        val actual = touch.withinProximity(CANVAS_X, CANVAS_Y, THRESHOLD)
        // Assert
        assertThat(actual).isTrue()
    }

    @Test
    fun nanBoth() {
        // Arrange
        val touch = Point(Float.NaN, Float.NaN)
        // Act
        val actual = touch.withinProximity(CANVAS_X, CANVAS_Y, THRESHOLD)
        // Assert
        assertThat(actual).isTrue()
    }
}

class MatchDetailsTest {
    private val wiFiDetails =
        listOf(
            WiFiDetail(wiFiIdentifier = WiFiIdentifier("SSID1", "AA:BB:CC:DD:EE:01")),
            WiFiDetail(wiFiIdentifier = WiFiIdentifier("SSID2", "AA:BB:CC:DD:EE:02")),
            WiFiDetail(wiFiIdentifier = WiFiIdentifier("SSID3", "AA:BB:CC:DD:EE:03")),
        )
    private val points =
        listOf(
            MarkerPoint(DataPoint(1, -50), CANVAS_Y),
            MarkerPoint(DataPoint(2, -60), CANVAS_Y),
            MarkerPoint(DataPoint(3, -70), CANVAS_Y),
        )
    private val pointMap =
        points.zip(wiFiDetails).associate { (point, detail) ->
            point.entry.key to listOf(detail)
        }
    private val touch = Point(CANVAS_X, CANVAS_Y)

    @Test
    fun emptyPoints() {
        // Act
        val actual = matchDetails(emptyList(), CANVAS_X, touch, THRESHOLD, pointMap)
        // Assert
        assertThat(actual).isEmpty()
    }

    @Test
    fun noMatchingKey() {
        // Arrange
        val points =
            listOf(
                MarkerPoint(DataPoint(91, 91), CANVAS_Y),
                MarkerPoint(DataPoint(92, 92), CANVAS_Y),
                MarkerPoint(DataPoint(93, 93), CANVAS_Y),
            )
        // Act
        val actual = matchDetails(points, CANVAS_X, touch, THRESHOLD, pointMap)
        // Assert
        assertThat(actual).isEmpty()
    }

    @Test
    fun withinThreshold() {
        // Act
        val actual = matchDetails(points, CANVAS_X, touch, THRESHOLD, pointMap)
        // Assert
        assertThat(actual).containsExactly(wiFiDetails[0], wiFiDetails[1], wiFiDetails[2])
    }

    @Test
    fun outsideThresholdY() {
        // Arrange
        val touch = Point(CANVAS_X, CANVAS_Y + THRESHOLD + 1f)
        // Act
        val actual = matchDetails(points, CANVAS_X, touch, THRESHOLD, pointMap)
        // Assert
        assertThat(actual).isEmpty()
    }

    @Test
    fun nanTouchCoordinatesMatch() {
        // Arrange
        val touch = Point(Float.NaN, Float.NaN)
        // Act
        val actual = matchDetails(points, CANVAS_X, touch, THRESHOLD, pointMap)
        // Assert
        assertThat(actual).containsExactly(wiFiDetails[0], wiFiDetails[1], wiFiDetails[2])
    }

    @Test
    fun sortedByDistance() {
        // Arrange
        val nearPoint = MarkerPoint(DataPoint(1, -50), CANVAS_Y)
        val farPoint = MarkerPoint(DataPoint(2, -60), CANVAS_Y + THRESHOLD / 2)
        val farthestPoint = MarkerPoint(DataPoint(3, -70), CANVAS_Y + THRESHOLD / 2 + 1f)
        // Act
        val actual = matchDetails(listOf(farthestPoint, nearPoint, farPoint), CANVAS_X, touch, THRESHOLD, pointMap)
        // Assert
        assertThat(actual).containsExactly(wiFiDetails[0], wiFiDetails[1], wiFiDetails[2])
    }

    @Test
    fun deduplicates() {
        // Arrange
        val points =
            listOf(
                MarkerPoint(DataPoint(1, -50), CANVAS_Y),
                MarkerPoint(DataPoint(1, -50), CANVAS_Y + 5f),
                MarkerPoint(DataPoint(1, -50), CANVAS_Y + 10f),
            )
        // Act
        val actual = matchDetails(points, CANVAS_X, touch, THRESHOLD, pointMap)
        // Assert
        assertThat(actual).containsExactly(wiFiDetails[0])
    }
}
