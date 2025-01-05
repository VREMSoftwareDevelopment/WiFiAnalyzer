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

import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.wifi.graphutils.MAX_NOT_SEEN_COUNT
import com.vrem.wifianalyzer.wifi.model.WiFiDetail

@OpenClass
internal class TimeGraphCache {
    private val notSeen: MutableMap<WiFiDetail, Int> = mutableMapOf()

    fun active(): Set<WiFiDetail> =
        notSeen.filterValues { it <= MAX_NOT_SEEN_COUNT }
            .keys
            .toSet()

    fun clear() =
        notSeen.filterValues { it > MAX_NOT_SEEN_COUNT }
            .keys
            .forEach { notSeen.remove(it) }

    fun add(wiFiDetail: WiFiDetail) {
        notSeen[wiFiDetail] = (notSeen[wiFiDetail] ?: 0) + 1
    }

    fun reset(wiFiDetail: WiFiDetail) {
        if (notSeen[wiFiDetail] != null) notSeen[wiFiDetail] = 0
    }

    val wiFiDetails: Set<WiFiDetail>
        get() = notSeen.keys

}