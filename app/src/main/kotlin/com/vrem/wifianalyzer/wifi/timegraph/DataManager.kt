/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.timegraph

import com.jjoe64.graphview.series.LineGraphSeries
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.wifi.graphutils.*
import com.vrem.wifianalyzer.wifi.model.WiFiDetail

@OpenClass
internal class DataManager(private val timeGraphCache: TimeGraphCache = TimeGraphCache()) {
    var scanCount: Int = 0
    var xValue = 0

    fun addSeriesData(graphViewWrapper: GraphViewWrapper, wiFiDetails: List<WiFiDetail>, levelMax: Int): Set<WiFiDetail> {
        val inOrder: Set<WiFiDetail> = wiFiDetails.toSet()
        inOrder.forEach { addData(graphViewWrapper, it, levelMax) }
        adjustData(graphViewWrapper, inOrder)
        xValue++
        if (scanCount < MAX_SCAN_COUNT) {
            scanCount++
        }
        if (scanCount == 2) {
            graphViewWrapper.setHorizontalLabelsVisible(true)
        }
        return newSeries(inOrder)
    }

    fun adjustData(graphViewWrapper: GraphViewWrapper, wiFiDetails: Set<WiFiDetail>) {
        graphViewWrapper.differenceSeries(wiFiDetails).forEach {
            val dataPoint = GraphDataPoint(xValue, MIN_Y + MIN_Y_OFFSET)
            val drawBackground = it.wiFiAdditional.wiFiConnection.connected
            graphViewWrapper.appendToSeries(it, dataPoint, scanCount, drawBackground)
            timeGraphCache.add(it)
        }
        timeGraphCache.clear()
    }

    fun newSeries(wiFiDetails: Set<WiFiDetail>): Set<WiFiDetail> = wiFiDetails.plus(timeGraphCache.active())

    fun addData(graphViewWrapper: GraphViewWrapper, wiFiDetail: WiFiDetail, levelMax: Int) {
        val drawBackground = wiFiDetail.wiFiAdditional.wiFiConnection.connected
        val level = wiFiDetail.wiFiSignal.level.coerceAtMost(levelMax)
        if (graphViewWrapper.newSeries(wiFiDetail)) {
            val dataPoint = GraphDataPoint(xValue, (if (scanCount > 0) MIN_Y + MIN_Y_OFFSET else level))
            val series = LineGraphSeries(arrayOf(dataPoint))
            graphViewWrapper.addSeries(wiFiDetail, series, drawBackground)
        } else {
            val dataPoint = GraphDataPoint(xValue, level)
            graphViewWrapper.appendToSeries(wiFiDetail, dataPoint, scanCount, drawBackground)
        }
        timeGraphCache.reset(wiFiDetail)
    }

}