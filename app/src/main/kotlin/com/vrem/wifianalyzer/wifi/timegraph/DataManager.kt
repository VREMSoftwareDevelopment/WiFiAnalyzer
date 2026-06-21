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
package com.vrem.wifianalyzer.wifi.timegraph

import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.wifi.graphutils.DataPoint
import com.vrem.wifianalyzer.wifi.graphutils.GraphWrapper
import com.vrem.wifianalyzer.wifi.graphutils.MAX_SCAN_COUNT
import com.vrem.wifianalyzer.wifi.model.WiFiDetail

@OpenClass
internal class DataManager(
    private val timeGraphCache: TimeGraphCache = TimeGraphCache(),
) {
    var scanCount: Int = 0
    var xValue = 0

    fun reset(graphWrapper: GraphWrapper) {
        graphWrapper.reset()
        timeGraphCache.reset()
        scanCount = 0
        xValue = 0
    }

    fun addSeriesData(
        graphWrapper: GraphWrapper,
        wiFiDetails: List<WiFiDetail>,
        levelMax: Int,
    ): Set<WiFiDetail> {
        val inOrder: Set<WiFiDetail> = wiFiDetails.toSet()
        inOrder.forEach { addData(graphWrapper, it, levelMax) }
        adjustData(graphWrapper, inOrder)
        graphWrapper.flushData()
        xValue++
        if (scanCount < MAX_SCAN_COUNT) {
            scanCount++
        }
        return newSeries(inOrder)
    }

    fun adjustData(
        graphWrapper: GraphWrapper,
        wiFiDetails: Set<WiFiDetail>,
    ) {
        graphWrapper
            .differenceSeries(wiFiDetails)
            .forEach { timeGraphCache.add(it) }
        timeGraphCache.clear()
    }

    fun newSeries(wiFiDetails: Set<WiFiDetail>): Set<WiFiDetail> = wiFiDetails.plus(timeGraphCache.active())

    fun addData(
        graphWrapper: GraphWrapper,
        wiFiDetail: WiFiDetail,
        levelMax: Int,
    ) {
        val drawBackground = wiFiDetail.wiFiAdditional.wiFiConnection.connected
        val level = wiFiDetail.wiFiSignal.level.coerceAtMost(levelMax)
        val dataPoint = DataPoint(xValue, level)
        if (graphWrapper.newSeries(wiFiDetail)) {
            graphWrapper.addSeries(wiFiDetail, listOf(dataPoint), drawBackground)
        } else {
            graphWrapper.appendToSeries(wiFiDetail, dataPoint, scanCount, drawBackground)
        }
        timeGraphCache.reset(wiFiDetail)
    }
}
