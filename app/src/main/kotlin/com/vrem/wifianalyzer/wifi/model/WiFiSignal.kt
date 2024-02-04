/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel

data class WiFiSignal(
    val primaryFrequency: Int = 0,
    val centerFrequency: Int = 0,
    val wiFiWidth: WiFiWidth = WiFiWidth.MHZ_20,
    val level: Int = 0,
    val is80211mc: Boolean = false,
    val wiFiStandard: WiFiStandard = WiFiStandard.UNKNOWN,
    val timestamp: Long = 0,
    val fastRoaming: List<FastRoaming> = listOf()
) {

    val wiFiBand: WiFiBand = WiFiBand.find(primaryFrequency)

    val frequencyStart: Int
        get() = centerFrequency - wiFiWidth.frequencyWidthHalf

    val frequencyEnd: Int
        get() = centerFrequency + wiFiWidth.frequencyWidthHalf

    val primaryWiFiChannel: WiFiChannel
        get() = wiFiBand.wiFiChannels.wiFiChannelByFrequency(primaryFrequency)

    val centerWiFiChannel: WiFiChannel
        get() = wiFiBand.wiFiChannels.wiFiChannelByFrequency(centerFrequency)

    val strength: Strength
        get() = Strength.calculate(level)

    val distance: String
        get() = String.format("~%.1fm", calculateDistance(primaryFrequency, level))

    fun inRange(frequency: Int): Boolean =
        frequency in frequencyStart..frequencyEnd

    fun channelDisplay(): String {
        val primaryChannel: Int = primaryWiFiChannel.channel
        val centerChannel: Int = centerWiFiChannel.channel
        val channel: String = primaryChannel.toString()
        return if (primaryChannel != centerChannel) "$channel($centerChannel)" else channel
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as WiFiSignal
        if (primaryFrequency != other.primaryFrequency) return false
        return wiFiWidth == other.wiFiWidth
    }

    override fun hashCode(): Int = 31 * primaryFrequency + wiFiWidth.hashCode()

    companion object {
        const val FREQUENCY_UNITS = "MHz"

        const val RM_ENABLED_CAPABILITIES_IE = 70
        const val NEIGHBOR_REPORT_IDX = 0
        const val NEIGHBOR_REPORT_BIT = 1

        const val EXTENDED_CAPABILITIES_IE = 127
        const val BSS_TRANSITION_IDX = 2
        const val BSS_TRANSITION_BIT = 3

        const val MOBILE_DOMAIN_IE = 54
        val EMPTY = WiFiSignal()
    }

}
