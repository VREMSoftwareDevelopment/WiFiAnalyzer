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
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.patrykandpatrick.vico.views.cartesian.CartesianChartView
import com.patrykandpatrick.vico.views.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.views.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.views.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.views.common.Point
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.wifi.detailview.WiFiDetailPopup
import com.vrem.wifianalyzer.wifi.detailview.WiFiDetailView
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doReturn
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

private const val THRESHOLD_PX = 50f
private const val CANVAS_X = 100f
private const val CANVAS_Y = 200f

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class MarkerHandlerTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val chartView: CartesianChartView = CartesianChartView(mainActivity)
    private val seriesCache: SeriesCache = mock()
    private val wiFiDetailView: WiFiDetailView = mock()
    private val wiFiDetailPopup: WiFiDetailPopup = mock()
    private val lineCartesianLayerMarkerTarget: LineCartesianLayerMarkerTarget = mock()
    private val view: View = mock()
    private val lastTouch = Point(CANVAS_X, CANVAS_Y)

    private val fixture = MarkerHandler(chartView, seriesCache, wiFiDetailView, wiFiDetailPopup)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(seriesCache, wiFiDetailView, wiFiDetailPopup, lineCartesianLayerMarkerTarget, view)
    }

    @Test
    fun eventReturnsLastTouchWhenNoLineTarget() {
        // Arrange
        val targets: List<CartesianMarker.Target> = emptyList()
        // Act
        val actual = fixture.event(lastTouch, THRESHOLD_PX, emptyMap(), targets)
        // Assert
        assertThat(actual).isEqualTo(lastTouch)
        verify(wiFiDetailView, never()).makeViewDetailed(any<WiFiDetail>(), any<Int>())
        verify(wiFiDetailPopup, never()).showSequence(any<List<View>>())
    }

    @Test
    fun eventReturnsLastTouchWhenEmptyPoints() {
        // Arrange
        val points = emptyList<LineCartesianLayerMarkerTarget.Point>()
        doReturn(CANVAS_X).whenever(lineCartesianLayerMarkerTarget).canvasX
        doReturn(points).whenever(lineCartesianLayerMarkerTarget).points
        // Act
        val actual = fixture.event(lastTouch, THRESHOLD_PX, emptyMap(), listOf(lineCartesianLayerMarkerTarget))
        // Assert
        assertThat(actual).isEqualTo(lastTouch)
        verify(lineCartesianLayerMarkerTarget).points
        verifyNoMoreInteractions(lineCartesianLayerMarkerTarget)
        verify(wiFiDetailView, never()).makeViewDetailed(any<WiFiDetail>(), any<Int>())
        verify(wiFiDetailPopup, never()).showSequence(any<List<View>>())
    }

    @Test
    fun eventReturnsNewPointWhenPointsPresent() {
        // Arrange
        val expected = NO_TOUCH
        val targetPoints = withTargetPoints()
        doReturn(CANVAS_X).whenever(lineCartesianLayerMarkerTarget).canvasX
        doReturn(targetPoints).whenever(lineCartesianLayerMarkerTarget).points
        // Act
        val actual = fixture.event(lastTouch, THRESHOLD_PX, emptyMap(), listOf(lineCartesianLayerMarkerTarget))
        // Assert
        assertThat(actual).isEqualTo(expected)
        verify(this.lineCartesianLayerMarkerTarget).canvasX
        verify(this.lineCartesianLayerMarkerTarget).points
    }

    @Test
    fun eventShowsPopupWhenDetailsMatch() {
        // Arrange
        val wiFiDetails = withWiFiDetails()
        val targetPoints = withTargetPoints()
        val pointMap = withPointMap(targetPoints, wiFiDetails)
        val seriesData = SeriesData(graphColor = GraphColor(0xFF0000, 0x00FF00))
        doReturn(CANVAS_X).whenever(lineCartesianLayerMarkerTarget).canvasX
        doReturn(targetPoints).whenever(lineCartesianLayerMarkerTarget).points
        wiFiDetails.forEach { wiFiDetail ->
            doReturn(seriesData).whenever(seriesCache)[wiFiDetail]
            doReturn(view).whenever(wiFiDetailView).makeViewDetailed(wiFiDetail, 0xFF0000)
        }
        // Act
        val actual = fixture.event(lastTouch, THRESHOLD_PX, pointMap, listOf(lineCartesianLayerMarkerTarget))
        // Assert
        assertThat(actual).isEqualTo(NO_TOUCH)
        verify(this.lineCartesianLayerMarkerTarget).canvasX
        verify(this.lineCartesianLayerMarkerTarget).points
        wiFiDetails.forEach { wiFiDetail ->
            verify(seriesCache)[wiFiDetail]
            verify(wiFiDetailView).makeViewDetailed(wiFiDetail, 0xFF0000)
        }
    }

    @Test
    fun eventShowsPopupWithNullColorWhenNotInCache() {
        // Arrange
        val wiFiDetails = withWiFiDetails()
        val targetPoints = withTargetPoints()
        val pointMap = withPointMap(targetPoints, wiFiDetails)
        doReturn(CANVAS_X).whenever(lineCartesianLayerMarkerTarget).canvasX
        doReturn(targetPoints).whenever(lineCartesianLayerMarkerTarget).points
        wiFiDetails.forEach { wiFiDetail ->
            doReturn(null).whenever(seriesCache)[wiFiDetail]
            doReturn(view).whenever(wiFiDetailView).makeViewDetailed(wiFiDetail, null)
        }
        // Act
        val actual = fixture.event(lastTouch, THRESHOLD_PX, pointMap, listOf(lineCartesianLayerMarkerTarget))
        // Assert
        assertThat(actual).isEqualTo(NO_TOUCH)
        verify(lineCartesianLayerMarkerTarget).canvasX
        verify(lineCartesianLayerMarkerTarget).points
        wiFiDetails.forEach { wiFiDetail ->
            verify(seriesCache)[wiFiDetail]
            verify(wiFiDetailView).makeViewDetailed(wiFiDetail, null)
        }
    }

    @Test
    fun eventDoesNotShowPopupWhenNoMatchingDetails() {
        // Arrange
        val targetPoints = withTargetPoints()
        doReturn(CANVAS_X).whenever(lineCartesianLayerMarkerTarget).canvasX
        doReturn(targetPoints).whenever(lineCartesianLayerMarkerTarget).points
        // Act
        val actual = fixture.event(lastTouch, THRESHOLD_PX, emptyMap(), listOf(lineCartesianLayerMarkerTarget))
        // Assert
        assertThat(actual).isEqualTo(NO_TOUCH)
        verify(lineCartesianLayerMarkerTarget).canvasX
        verify(lineCartesianLayerMarkerTarget).points
        verify(wiFiDetailView, never()).makeViewDetailed(any<WiFiDetail>(), any<Int>())
        verify(wiFiDetailPopup, never()).showSequence(any<List<View>>())
    }

    @Test
    fun eventDoesNotShowPopupWhenTouchOutsideYThreshold() {
        // Arrange - touch is far in Y from the target points
        val touchFarInY = Point(CANVAS_X, CANVAS_Y + THRESHOLD_PX + 1f)
        val wiFiDetails = withWiFiDetails()
        val targetPoints = withTargetPoints()
        val pointMap = withPointMap(targetPoints, wiFiDetails)
        doReturn(CANVAS_X).whenever(lineCartesianLayerMarkerTarget).canvasX
        doReturn(targetPoints).whenever(lineCartesianLayerMarkerTarget).points
        // Act
        val actual = fixture.event(touchFarInY, THRESHOLD_PX, pointMap, listOf(lineCartesianLayerMarkerTarget))
        // Assert
        assertThat(actual).isEqualTo(NO_TOUCH)
        verify(lineCartesianLayerMarkerTarget).canvasX
        verify(lineCartesianLayerMarkerTarget).points
        verify(wiFiDetailView, never()).makeViewDetailed(any<WiFiDetail>(), any<Int>())
        verify(wiFiDetailPopup, never()).showSequence(any<List<View>>())
    }

    private fun withWiFiDetails(): List<WiFiDetail> =
        listOf(
            WiFiDetail(wiFiIdentifier = WiFiIdentifier("SSID1", "AA:BB:CC:DD:EE:01")),
            WiFiDetail(wiFiIdentifier = WiFiIdentifier("SSID2", "AA:BB:CC:DD:EE:02")),
            WiFiDetail(wiFiIdentifier = WiFiIdentifier("SSID3", "AA:BB:CC:DD:EE:03")),
        )

    private fun withTargetPoints(): List<LineCartesianLayerMarkerTarget.Point> =
        listOf(
            withTargetPoint(1, -50),
            withTargetPoint(2, -60),
            withTargetPoint(3, -70),
        )

    private fun withPointMap(
        targetPoints: List<LineCartesianLayerMarkerTarget.Point>,
        wiFiDetails: List<WiFiDetail>,
    ): Map<Long, MutableList<WiFiDetail>> =
        targetPoints.zip(wiFiDetails).associate { (targetPoint, wiFiDetail) ->
            DataPoint(targetPoint.entry.x.toInt(), targetPoint.entry.y.toInt()).key to mutableListOf(wiFiDetail)
        }

    private fun withTargetPoint(
        x: Int,
        y: Int,
    ): LineCartesianLayerMarkerTarget.Point {
        val entry = LineCartesianLayerModel.Entry(x, y)
        return LineCartesianLayerMarkerTarget.Point(entry, CANVAS_Y, 0)
    }
}
