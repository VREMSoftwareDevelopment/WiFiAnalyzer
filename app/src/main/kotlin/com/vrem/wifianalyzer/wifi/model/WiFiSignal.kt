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

import com.vrem.util.EnumUtils
import com.vrem.wifianalyzer.wifi.band.FrequencyPredicate
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import com.vrem.wifianalyzer.wifi.band.WiFiWidth
import java.util.*

data class WiFiSignal(val primaryFrequency: Int, val wiFiWidth: WiFiWidth) {
    constructor(primaryFrequency: Int = 0, centerFrequency: Int = 0, wiFiWidth: WiFiWidth = WiFiWidth.MHZ_20, level: Int = 0, is80211mc: Boolean = false) :
            this(primaryFrequency, wiFiWidth) {
        this.centerFrequency = centerFrequency
        this.level = level
        this.is80211mc = is80211mc
    }

    val wiFiBand: WiFiBand

    var centerFrequency: Int = 0
    var level: Int = 0
    var is80211mc: Boolean = false

    fun getFrequencyStart(): Int = centerFrequency - wiFiWidth.frequencyWidthHalf

    fun getFrequencyEnd(): Int = centerFrequency + wiFiWidth.frequencyWidthHalf

    fun getPrimaryWiFiChannel(): WiFiChannel = wiFiBand.wiFiChannels.getWiFiChannelByFrequency(primaryFrequency)

    fun getCenterWiFiChannel(): WiFiChannel = wiFiBand.wiFiChannels.getWiFiChannelByFrequency(centerFrequency)

    fun getStrength(): Strength = Strength.calculate(level)

    fun getDistance(): String {
        val distance = WiFiUtils.calculateDistance(primaryFrequency, level)
        return String.format(Locale.ENGLISH, "~%.1fm", distance)
    }

    fun isInRange(frequency: Int): Boolean {
        return frequency >= getFrequencyStart() && frequency <= getFrequencyEnd()
    }

    fun getChannelDisplay(): String {
        val primaryChannel = getPrimaryWiFiChannel().channel
        val centerChannel = getCenterWiFiChannel().channel
        var channel = Integer.toString(primaryChannel)
        if (primaryChannel != centerChannel) {
            channel += "($centerChannel)"
        }
        return channel
    }

    companion object {
        val EMPTY = WiFiSignal()
        const val FREQUENCY_UNITS = "MHz"
    }

    init {
        wiFiBand = EnumUtils.find(WiFiBand::class.java, FrequencyPredicate(primaryFrequency), WiFiBand.GHZ2)
    }
}