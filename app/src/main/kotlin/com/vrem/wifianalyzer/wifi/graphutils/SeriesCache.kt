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
package com.vrem.wifianalyzer.wifi.graphutils

import com.jjoe64.graphview.series.BaseSeries
import com.jjoe64.graphview.series.Series
import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.wifi.model.WiFiDetail

@OpenClass
class SeriesCache {
    private val cache: MutableMap<WiFiDetail, BaseSeries<GraphDataPoint>> = mutableMapOf()

    fun difference(series: Set<WiFiDetail>): List<WiFiDetail> = cache.keys.minus(series).toList()

    fun remove(series: List<WiFiDetail>): List<BaseSeries<GraphDataPoint>> =
        series.filter { cache.containsKey(it) }.mapNotNull { cache.remove(it) }

    fun find(series: Series<*>): WiFiDetail = cache.keys.first { series == cache[it] }

    operator fun contains(wiFiDetail: WiFiDetail): Boolean = cache.containsKey(wiFiDetail)

    operator fun get(wiFiDetail: WiFiDetail): BaseSeries<GraphDataPoint> = cache[wiFiDetail]!!

    fun put(wiFiDetail: WiFiDetail, series: BaseSeries<GraphDataPoint>) =
        cache.put(wiFiDetail, series)

}
