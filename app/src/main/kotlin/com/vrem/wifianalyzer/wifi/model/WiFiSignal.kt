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

import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import com.vrem.wifianalyzer.wifi.band.WiFiWidth

data class WiFiSignal(val primaryFrequency: Int = 0,
                      val centerFrequency: Int = 0,
                      val wiFiWidth: WiFiWidth = WiFiWidth.MHZ_20,
                      val level: Int = 0,
                      val is80211mc: Boolean = false) {

    val wiFiBand: WiFiBand = WiFiBand.find(primaryFrequency)

    fun frequencyStart(): Int = centerFrequency - wiFiWidth.frequencyWidthHalf

    fun frequencyEnd(): Int = centerFrequency + wiFiWidth.frequencyWidthHalf

    fun primaryWiFiChannel(): WiFiChannel = wiFiBand.wiFiChannels.getWiFiChannelByFrequency(primaryFrequency)

    fun centerWiFiChannel(): WiFiChannel = wiFiBand.wiFiChannels.getWiFiChannelByFrequency(centerFrequency)

    fun strength(): Strength = Strength.calculate(level)

    fun distance(): String {
        val distance: Double = WiFiUtils.calculateDistance(primaryFrequency, level)
        return String.format("~%.1fm", distance)
    }

    fun inRange(frequency: Int): Boolean {
        return frequency >= frequencyStart() && frequency <= frequencyEnd()
    }

    fun channelDisplay(): String {
        val primaryChannel: Int = primaryWiFiChannel().channel
        val centerChannel: Int = centerWiFiChannel().channel
        var channel: String = Integer.toString(primaryChannel)
        if (primaryChannel != centerChannel) {
            channel += "($centerChannel)"
        }
        return channel
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WiFiSignal

        if (primaryFrequency != other.primaryFrequency) return false
        if (wiFiWidth != other.wiFiWidth) return false

        return true
    }

    override fun hashCode(): Int = 31 * primaryFrequency + wiFiWidth.hashCode()

    companion object {
        const val FREQUENCY_UNITS = "MHz"

        @JvmField
        val EMPTY = WiFiSignal()
    }

}