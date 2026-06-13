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

import android.graphics.Color
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.wifi.model.WiFiDetail
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier
import com.vrem.wifianalyzer.wifi.model.WiFiSecurity
import com.vrem.wifianalyzer.wifi.model.WiFiSignal
import com.vrem.wifianalyzer.wifi.model.WiFiWidth

private val TRANSPARENT_COLOR =
    GraphColor(
        Color.TRANSPARENT,
        Color.TRANSPARENT,
    )

typealias Coordinates = Pair<List<Double>, List<Double>>

typealias SeriesEntry = Map.Entry<WiFiDetail, SeriesData>

val PLACEHOLDER_DETAIL: WiFiDetail =
    WiFiDetail(
        wiFiIdentifier = WiFiIdentifier("__placeholder__", "__placeholder__"),
        wiFiSecurity = WiFiSecurity.EMPTY,
        wiFiSignal = WiFiSignal(0, 0, WiFiWidth.MHZ_20, MIN_Y),
    )

data class DataPoint(
    val x: Int,
    val y: Int,
) {
    val key: Long get() = x.toLong() shl 32 or (y.toLong() and 0xFFFFFFFFL)
}

class SeriesData(
    dataPoints: List<DataPoint> = emptyList(),
    var graphColor: GraphColor = GraphColor(0, 0),
    var title: String = "",
    var connected: Boolean = false,
    var drawBackground: Boolean = false,
) {
    val dataPoints: List<DataPoint>
        field = ArrayDeque(dataPoints)

    fun replaceAll(points: List<DataPoint>) {
        dataPoints.clear()
        dataPoints.addAll(points)
    }

    fun append(
        point: DataPoint,
        maxSize: Int,
    ) {
        dataPoints.addLast(point)
        if (dataPoints.size > maxSize) {
            dataPoints.removeFirst()
        }
    }

    fun toCoordinates(): Coordinates {
        val xValues = ArrayList<Double>(dataPoints.size)
        val yValues = ArrayList<Double>(dataPoints.size)
        dataPoints.forEach { dataPoint ->
            xValues.add(dataPoint.x.toDouble())
            yValues.add(dataPoint.y.toDouble())
        }
        return xValues to yValues
    }
}

fun List<SeriesData>.toCoordinates(): List<Coordinates> = map { it.toCoordinates() }

@OpenClass
class SeriesCache(
    placeholder: List<DataPoint>,
) {
    private val cache: MutableMap<WiFiDetail, SeriesData> =
        linkedMapOf(
            PLACEHOLDER_DETAIL to SeriesData(placeholder, TRANSPARENT_COLOR),
        )

    fun difference(series: Set<WiFiDetail>): List<WiFiDetail> =
        cache.keys.filterNot { it == PLACEHOLDER_DETAIL || it in series }

    fun remove(series: List<WiFiDetail>): List<SeriesData> =
        series.filterNot { it == PLACEHOLDER_DETAIL }.mapNotNull { cache.remove(it) }

    operator fun contains(wiFiDetail: WiFiDetail): Boolean = cache.containsKey(wiFiDetail)

    operator fun get(wiFiDetail: WiFiDetail): SeriesData? = cache[wiFiDetail]

    fun put(
        wiFiDetail: WiFiDetail,
        seriesData: SeriesData,
    ) = cache.put(wiFiDetail, seriesData)

    fun data(): List<SeriesData> = cache.values.toList()

    fun populatedData(): List<SeriesData> = cache.values.filter { it.dataPoints.isNotEmpty() }

    fun entries(): List<SeriesEntry> = cache.entries.toList()

    fun populatedEntries(): List<SeriesEntry> = cache.entries.filter { it.value.dataPoints.isNotEmpty() }
}
