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

import com.patrykandpatrick.vico.views.cartesian.CartesianChart
import com.patrykandpatrick.vico.views.cartesian.CartesianChartView
import com.patrykandpatrick.vico.views.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.views.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.views.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.views.common.Point
import com.vrem.wifianalyzer.wifi.detailview.WiFiDetailPopup
import com.vrem.wifianalyzer.wifi.detailview.WiFiDetailView
import com.vrem.wifianalyzer.wifi.model.WiFiDetail

internal val NO_TOUCH: Point = Point(Float.NaN, Float.NaN)

class MarkerHandler(
    private val chartView: CartesianChartView,
    private val seriesCache: SeriesCache,
    private val wiFiDetailView: WiFiDetailView = WiFiDetailView(),
    private val wiFiDetailPopup: WiFiDetailPopup = WiFiDetailPopup(),
) {
    fun event(
        lastTouch: Point,
        thresholdPx: Float,
        dataPointToDetail: Map<Long, MutableList<WiFiDetail>>,
        targets: List<CartesianMarker.Target>,
    ): Point {
        val lineTarget =
            targets.firstOrNull { it is LineCartesianLayerMarkerTarget } as? LineCartesianLayerMarkerTarget
                ?: return lastTouch
        val points = lineTarget.points
        if (points.isEmpty()) return lastTouch
        val markerPoints = points.map { MarkerPoint(DataPoint(it.entry.x.toInt(), it.entry.y.toInt()), it.canvasY) }
        val wiFiDetails = matchDetails(markerPoints, lineTarget.canvasX, lastTouch, thresholdPx, dataPointToDetail)
        if (wiFiDetails.isNotEmpty()) {
            val views =
                wiFiDetails.map { detail ->
                    val color = seriesCache[detail]?.graphColor?.primary
                    wiFiDetailView.makeViewDetailed(detail, color)
                }
            chartView.post {
                runCatching { wiFiDetailPopup.showSequence(views) }
            }
        }
        return NO_TOUCH
    }
}

class MarkerInteraction(
    chartView: CartesianChartView,
    private val seriesCache: SeriesCache,
    private val markerHandler: MarkerHandler = MarkerHandler(chartView, seriesCache),
) {
    private var lastTouch: Point = NO_TOUCH
    private var dataPointToDetail: Map<Long, MutableList<WiFiDetail>> = emptyMap()
    private val thresholdPx: Float = 20f * chartView.resources.displayMetrics.density

    internal val marker: DefaultCartesianMarker = createMarker()

    internal val markerVisibilityListener: MarkerVisibilityListenerWrapper =
        MarkerVisibilityListenerWrapper { targets ->
            lastTouch = markerHandler.event(lastTouch, thresholdPx, dataPointToDetail, targets)
        }

    internal val markerController: MarkerControllerWrapper =
        MarkerControllerWrapper(thresholdPx) { point -> lastTouch = point }

    fun applyTo(chart: CartesianChart): CartesianChart =
        chart.copy(
            marker = marker,
            markerVisibilityListener = markerVisibilityListener,
            markerController = markerController,
        )

    fun updatePointMap(entries: List<SeriesEntry>) {
        val pointMap = mutableMapOf<Long, MutableList<WiFiDetail>>()
        entries
            .filter { it.key != PLACEHOLDER_DETAIL }
            .forEach { entry ->
                val wiFiDetail = entry.key
                entry.value.dataPoints.forEach { dataPoint ->
                    pointMap.getOrPut(dataPoint.key) { mutableListOf() }.add(wiFiDetail)
                }
            }
        dataPointToDetail = pointMap
    }
}
