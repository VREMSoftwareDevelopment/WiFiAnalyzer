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
import com.patrykandpatrick.vico.views.cartesian.CartesianChartView
import com.patrykandpatrick.vico.views.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.views.common.Point
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.settings.ThemeStyle
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSecurity
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import com.vrem.wifianalyzer.wifi.model.WiFiWidth
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config
import java.util.AbstractMap

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class MarkerInteractionTest {
    private val mainActivity = RobolectricUtil.INSTANCE.activity
    private val chartView: CartesianChartView =
        GraphBuilder(MAX_Y_DEFAULT, ThemeStyle.DARK).build(mainActivity, false)
    private val seriesCache: SeriesCache = mock()
    private val markerHandler: MarkerHandler = mock()
    private val expectedThresholdPx = 20f * chartView.resources.displayMetrics.density
    private val wiFiDetail1 =
        WiFiDetail(
            wiFiIdentifier = WiFiIdentifier("SSID1", "AA:BB:CC:DD:EE:01"),
            wiFiSecurity = WiFiSecurity.EMPTY,
            wiFiSignal = WiFiSignal(2412, 2412, WiFiWidth.MHZ_20, -50),
        )
    private val wiFiDetail2 =
        WiFiDetail(
            wiFiIdentifier = WiFiIdentifier("SSID2", "AA:BB:CC:DD:EE:02"),
            wiFiSecurity = WiFiSecurity.EMPTY,
            wiFiSignal = WiFiSignal(2437, 2437, WiFiWidth.MHZ_20, -60),
        )
    private val fixture = MarkerInteraction(chartView, seriesCache, markerHandler)

    @After
    fun tearDown() {
        verifyNoMoreInteractions(seriesCache, markerHandler)
    }

    @Test
    fun applyToWiresMarkerController() {
        // Arrange
        val existing = chartView.chart!!
        // Act
        val applied = fixture.applyTo(existing)
        // Assert
        assertThat(applied.markerController).isSameAs(fixture.markerController)
    }

    @Test
    fun markerIsDefaultCartesianMarker() {
        // Act & Assert
        assertThat(fixture.marker).usingRecursiveComparison().isEqualTo(createMarker())
    }

    @Test
    fun updatePointMapWithEmptyEntriesPassesEmptyMapToHandler() {
        // Arrange
        val targets = emptyList<CartesianMarker.Target>()
        doReturn(NO_TOUCH).whenever(markerHandler).event(NO_TOUCH, expectedThresholdPx, emptyMap(), targets)
        // Act
        fixture.updatePointMap(emptyList())
        invokeVisibilityListener(targets)
        // Assert
        verify(markerHandler).event(NO_TOUCH, expectedThresholdPx, emptyMap(), targets)
    }

    @Test
    fun updatePointMapMapsDataPointKeyToWiFiDetail() {
        // Arrange
        val dataPoint = DataPoint(10, -50)
        val entries = listOf(withEntry(wiFiDetail1, listOf(dataPoint)))
        val targets = emptyList<CartesianMarker.Target>()
        val expectedMap = mapOf(dataPoint.key to mutableListOf(wiFiDetail1))
        doReturn(NO_TOUCH).whenever(markerHandler).event(NO_TOUCH, expectedThresholdPx, expectedMap, targets)
        // Act
        fixture.updatePointMap(entries)
        invokeVisibilityListener(targets)
        // Assert
        verify(markerHandler).event(NO_TOUCH, expectedThresholdPx, expectedMap, targets)
    }

    @Test
    fun updatePointMapGroupsMultipleDetailsAtSameDataPoint() {
        // Arrange
        val dataPoint = DataPoint(10, -50)
        val entries =
            listOf(
                withEntry(wiFiDetail1, listOf(dataPoint)),
                withEntry(wiFiDetail2, listOf(dataPoint)),
            )
        val targets = emptyList<CartesianMarker.Target>()
        val expectedMap = mapOf(dataPoint.key to mutableListOf(wiFiDetail1, wiFiDetail2))
        doReturn(NO_TOUCH).whenever(markerHandler).event(NO_TOUCH, expectedThresholdPx, expectedMap, targets)
        // Act
        fixture.updatePointMap(entries)
        invokeVisibilityListener(targets)
        // Assert
        verify(markerHandler).event(NO_TOUCH, expectedThresholdPx, expectedMap, targets)
    }

    @Test
    fun updatePointMapCreatesDistinctKeysForDifferentDataPoints() {
        // Arrange
        val dataPoint1 = DataPoint(10, -50)
        val dataPoint2 = DataPoint(20, -60)
        val entries = listOf(withEntry(wiFiDetail1, listOf(dataPoint1, dataPoint2)))
        val targets = emptyList<CartesianMarker.Target>()
        val expectedMap =
            mapOf(
                dataPoint1.key to mutableListOf(wiFiDetail1),
                dataPoint2.key to mutableListOf(wiFiDetail1),
            )
        doReturn(NO_TOUCH).whenever(markerHandler).event(NO_TOUCH, expectedThresholdPx, expectedMap, targets)
        // Act
        fixture.updatePointMap(entries)
        invokeVisibilityListener(targets)
        // Assert
        verify(markerHandler).event(NO_TOUCH, expectedThresholdPx, expectedMap, targets)
    }

    @Test
    fun updatePointMapExcludesPlaceholder() {
        // Arrange
        val placeholderPoint = DataPoint(10, MIN_Y)
        val realPoint = DataPoint(20, -50)
        val entries =
            listOf(
                withEntry(PLACEHOLDER_DETAIL, listOf(placeholderPoint)),
                withEntry(wiFiDetail1, listOf(realPoint)),
            )
        val targets = emptyList<CartesianMarker.Target>()
        val expectedMap = mapOf(realPoint.key to mutableListOf(wiFiDetail1))
        doReturn(NO_TOUCH).whenever(markerHandler).event(NO_TOUCH, expectedThresholdPx, expectedMap, targets)
        // Act
        fixture.updatePointMap(entries)
        invokeVisibilityListener(targets)
        // Assert
        verify(markerHandler).event(NO_TOUCH, expectedThresholdPx, expectedMap, targets)
    }

    @Test
    fun visibilityListenerForwardsReturnedPointAsNextLastTouch() {
        // Arrange
        val secondTouch = Point(5f, 6f)
        val targets = emptyList<CartesianMarker.Target>()
        doReturn(secondTouch).whenever(markerHandler).event(NO_TOUCH, expectedThresholdPx, emptyMap(), targets)
        doReturn(NO_TOUCH).whenever(markerHandler).event(secondTouch, expectedThresholdPx, emptyMap(), targets)
        // Act
        invokeVisibilityListener(targets)
        invokeVisibilityListener(targets)
        // Assert
        verify(markerHandler).event(NO_TOUCH, expectedThresholdPx, emptyMap(), targets)
        verify(markerHandler).event(secondTouch, expectedThresholdPx, emptyMap(), targets)
    }

    private fun invokeVisibilityListener(targets: List<CartesianMarker.Target>) {
        fixture.markerVisibilityListener.onShown(fixture.marker, targets)
    }

    private fun withEntry(
        wiFiDetail: WiFiDetail,
        dataPoints: List<DataPoint>,
    ): SeriesEntry = AbstractMap.SimpleEntry(wiFiDetail, SeriesData(dataPoints))
}
