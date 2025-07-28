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
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel

@OpenClass
class ChannelRating(
    val wiFiDetails: MutableList<WiFiDetail> = mutableListOf(),
) {
    fun count(wiFiChannel: WiFiChannel): Int = collectOverlapping(wiFiChannel).size

    fun strength(wiFiChannel: WiFiChannel): Strength =
        Strength.entries[
            collectOverlapping(wiFiChannel)
                .filter { !it.wiFiAdditional.wiFiConnection.connected }
                .map { it.wiFiSignal.strength.ordinal }
                .maxByOrNull { it } ?: Strength.ZERO.ordinal,
        ]

    fun wiFiDetails(newWiFiDetails: List<WiFiDetail>) {
        wiFiDetails.clear()
        wiFiDetails.addAll(removeSame(newWiFiDetails))
    }

    fun bestChannels(
        wiFiBand: WiFiBand,
        wiFiChannels: List<WiFiChannel>,
    ): List<ChannelAPCount> =
        wiFiChannels
            .filter { bestChannel(it) }
            .map { ChannelAPCount(it, wiFiBand.wiFiChannels.wiFiWidthByChannel(it.channel), count(it)) }
            .sorted()

    private fun removeSame(wiFiDetails: List<WiFiDetail>): List<WiFiDetail> =
        wiFiDetails.distinctBy { it.wiFiVirtual }.sortedWith(SortBy.STRENGTH.sort)

    private fun collectOverlapping(wiFiChannel: WiFiChannel): List<WiFiDetail> =
        wiFiDetails.filter { it.wiFiSignal.inRange(wiFiChannel.frequency) }

    private fun bestChannel(wiFiChannel: WiFiChannel): Boolean {
        val strength: Strength = strength(wiFiChannel)
        return Strength.ZERO == strength || Strength.ONE == strength
    }
}
