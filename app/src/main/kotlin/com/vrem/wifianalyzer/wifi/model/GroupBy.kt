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

enum class GroupBy(private val sortByComparator: Comparator<WiFiDetail>, private val groupByComparator: Comparator<WiFiDetail>) {
    NONE(None(), None()),
    SSID(SortBySSID(), GroupBySSID()),
    CHANNEL(SortByChannel(), GroupByChannel());

    fun sortByComparator(): Comparator<WiFiDetail> {
        return sortByComparator
    }

    fun groupByComparator(): Comparator<WiFiDetail> {
        return groupByComparator
    }

    internal class None : Comparator<WiFiDetail> {
        override fun compare(lhs: WiFiDetail, rhs: WiFiDetail): Int = lhs.compareTo(rhs)
    }

    internal class GroupBySSID : Comparator<WiFiDetail> {
        override fun compare(lhs: WiFiDetail, rhs: WiFiDetail): Int = lhs.SSID.compareTo(rhs.SSID, true)
    }

    internal class GroupByChannel : Comparator<WiFiDetail> {
        override fun compare(lhs: WiFiDetail, rhs: WiFiDetail): Int =
                lhs.wiFiSignal.getPrimaryWiFiChannel().channel.compareTo(rhs.wiFiSignal.getPrimaryWiFiChannel().channel)
    }

}