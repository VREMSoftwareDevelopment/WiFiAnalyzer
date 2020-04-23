/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi.model

import java.util.*

enum class GroupBy(private val comparator: Comparator<WiFiDetail>, private val groupByKey: GroupByKey) {
    NONE(WiFiDetail.sortByDefault(), GroupBySSID()),
    SSID(WiFiDetail.sortBySSID(), GroupBySSID()),
    CHANNEL(WiFiDetail.sortByChannel(), GroupByChannel());

    fun comparator(): Comparator<WiFiDetail> = comparator

    fun groupByKey(): GroupByKey = groupByKey

    interface GroupByKey {
        fun key(wiFiDetail: WiFiDetail): String
    }

    private class GroupByChannel : GroupByKey {
        override fun key(wiFiDetail: WiFiDetail): String =
                wiFiDetail.wiFiSignal.primaryWiFiChannel().channel.toString()
    }

    private class GroupBySSID : GroupByKey {
        override fun key(wiFiDetail: WiFiDetail): String = wiFiDetail.SSID
    }
}