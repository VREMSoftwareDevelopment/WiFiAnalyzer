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
package com.vrem.wifianalyzer.wifi.model

import com.vrem.annotation.OpenClass
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.wifi.predicate.Predicate

@OpenClass
class WiFiData(val wiFiDetails: List<WiFiDetail>, val wiFiConnection: WiFiConnection) {

    fun connection(): WiFiDetail =
        wiFiDetails.firstOrNull { connected(it) }?.let { copy(it) } ?: WiFiDetail.EMPTY

    fun wiFiDetails(predicate: Predicate, sortBy: SortBy): List<WiFiDetail> =
        wiFiDetails(predicate, sortBy, GroupBy.NONE)

    fun wiFiDetails(predicate: Predicate, sortBy: SortBy, groupBy: GroupBy): List<WiFiDetail> {
        val connection: WiFiDetail = connection()
        return wiFiDetails
            .filter { predicate(it) }
            .map { transform(it, connection) }
            .sortAndGroup(sortBy, groupBy)
            .sortedWith(sortBy.sort)
    }

    private fun List<WiFiDetail>.sortAndGroup(sortBy: SortBy, groupBy: GroupBy): List<WiFiDetail> =
        if (groupBy.none) {
            this
        } else {
            this.groupBy { groupBy.group(it) }
                .values
                .map(map(sortBy, groupBy))
                .sortedWith(sortBy.sort)
        }

    private fun map(sortBy: SortBy, groupBy: GroupBy): (List<WiFiDetail>) -> WiFiDetail = {
        val sortedWith: List<WiFiDetail> = it.sortedWith(groupBy.sort)
        when (sortedWith.size) {
            1 -> sortedWith.first()
            else ->
                WiFiDetail(
                    sortedWith.first(),
                    sortedWith.subList(1, sortedWith.size).sortedWith(sortBy.sort)
                )
        }
    }

    private fun transform(wiFiDetail: WiFiDetail, connection: WiFiDetail): WiFiDetail =
        when (wiFiDetail) {
            connection -> connection
            else -> {
                val vendorName: String =
                    MainContext.INSTANCE.vendorService.findVendorName(wiFiDetail.wiFiIdentifier.bssid)
                val wiFiAdditional = WiFiAdditional(vendorName, WiFiConnection.EMPTY)
                WiFiDetail(wiFiDetail, wiFiAdditional)
            }
        }

    private fun connected(it: WiFiDetail): Boolean =
        wiFiConnection.wiFiIdentifier.equals(it.wiFiIdentifier, true)

    private fun copy(wiFiDetail: WiFiDetail): WiFiDetail {
        val vendorName: String = MainContext.INSTANCE.vendorService.findVendorName(wiFiDetail.wiFiIdentifier.bssid)
        val wiFiAdditional = WiFiAdditional(vendorName, wiFiConnection)
        return WiFiDetail(wiFiDetail, wiFiAdditional)
    }

    companion object {
        val EMPTY = WiFiData(listOf(), WiFiConnection.EMPTY)
    }

}