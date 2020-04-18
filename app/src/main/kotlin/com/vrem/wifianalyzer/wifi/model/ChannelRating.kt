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

import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import java.util.*
import kotlin.collections.ArrayList

interface IChannelRating {
    fun getCount(wiFiChannel: WiFiChannel): Int

    fun getStrength(wiFiChannel: WiFiChannel): Strength

    fun getWiFiDetails(): List<WiFiDetail>

    fun setWiFiDetails(wiFiDetails: List<WiFiDetail>)

    fun getBestChannels(wiFiChannels: List<WiFiChannel>): List<ChannelAPCount>
}

class ChannelRating : IChannelRating {
    private val wiFiDetails: MutableList<WiFiDetail> = ArrayList()

    override fun getCount(wiFiChannel: WiFiChannel): Int {
        return collectOverlapping(wiFiChannel).size
    }

    override fun getStrength(wiFiChannel: WiFiChannel): Strength =
            enumValues<Strength>()[
                    collectOverlapping(wiFiChannel)
                            .filter { !it.wiFiAdditional.wiFiConnection.isConnected() }
                            .map { it.wiFiSignal.getStrength().ordinal }
                            .maxBy { it } ?: Strength.ZERO.ordinal
            ]

    override fun getWiFiDetails(): List<WiFiDetail> {
        return wiFiDetails.toList()
    }

    override fun setWiFiDetails(wiFiDetails: List<WiFiDetail>) {
        this.wiFiDetails.clear()
        this.wiFiDetails.addAll(removeSame(wiFiDetails))
    }

    override fun getBestChannels(wiFiChannels: List<WiFiChannel>): List<ChannelAPCount> =
            wiFiChannels
                    .filter { bestChannel(it) }
                    .map { ChannelAPCount(it, getCount(it)) }
                    .sortedWith(ChannelAPCountComparator())

    private fun removeSame(wiFiDetails: List<WiFiDetail>): List<WiFiDetail> {
        val (left, right) = wiFiDetails.partition { BSSID_LENGTH == it.BSSID.length }
        return left.distinctBy { it.toKey() }.plus(right).sortedWith(SortBy.STRENGTH.comparator())
    }

    private fun collectOverlapping(wiFiChannel: WiFiChannel): List<WiFiDetail> =
            wiFiDetails
                    .filter { it.wiFiSignal.isInRange(wiFiChannel.frequency) }
                    .toList()

    private fun bestChannel(it: WiFiChannel): Boolean {
        val strength = getStrength(it)
        return Strength.ZERO == strength || Strength.ONE == strength
    }

    private inner class ChannelAPCountComparator : Comparator<ChannelAPCount> {
        override fun compare(lhs: ChannelAPCount, rhs: ChannelAPCount): Int = when {
            lhs.count != rhs.count -> lhs.count.compareTo(rhs.count)
            else -> lhs.wiFiChannel.compareTo(rhs.wiFiChannel)
        }
    }

    private data class Key(val prefix: String, val BSSID: String, val frequency: Int)

    private fun WiFiDetail.toKey(): Key = Key(
            this.BSSID.substring(0, 0).toUpperCase(Locale.getDefault()),
            this.BSSID.substring(2, BSSID_LENGTH - 1).toUpperCase(Locale.getDefault()),
            this.wiFiSignal.primaryFrequency)

    companion object {
        private const val BSSID_LENGTH = 17
    }

}