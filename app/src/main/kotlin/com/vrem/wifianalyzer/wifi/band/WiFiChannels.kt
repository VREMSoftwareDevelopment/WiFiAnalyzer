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
package com.vrem.wifianalyzer.wifi.band

import com.vrem.util.EMPTY

typealias WiFiRange = Pair<Int, Int>
typealias WiFiChannelPair = Pair<WiFiChannel, WiFiChannel>

abstract class WiFiChannels(val channelRange: WiFiChannelPair, val graphChannels: Set<Int>) {
    abstract fun availableChannels(countryCode: String): List<WiFiChannel>
    abstract fun channelAvailable(countryCode: String, channel: Int): Boolean

    fun inRange(frequency: Int): Boolean = frequency in channelRange.first.frequency..channelRange.second.frequency

    fun wiFiChannelByFrequency(frequency: Int): WiFiChannel =
        if (inRange(frequency)) {
            wiFiChannel(frequency, channelRange)
        } else {
            WiFiChannel.UNKNOWN
        }

    fun wiFiChannelByChannel(channel: Int): WiFiChannel =
        if (channel in channelRange.first.channel..channelRange.second.channel) {
            WiFiChannel(channel, channelRange.first.frequency + (channel - channelRange.first.channel) * FREQUENCY_SPREAD)
        } else {
            WiFiChannel.UNKNOWN
        }

    open fun graphChannelCount(): Int {
        return channelRange.second.channel - channelRange.first.channel + 1
    }

    fun graphChannelByFrequency(frequency: Int): String {
        val wiFiChannel: WiFiChannel = wiFiChannelByFrequency(frequency)
        return if (WiFiChannel.UNKNOWN != wiFiChannel && graphChannels.contains(wiFiChannel.channel)) {
            "${wiFiChannel.channel}"
        } else {
            String.EMPTY
        }
    }

    fun availableChannels(channels: Set<Int>): List<WiFiChannel> = channels.map { this.wiFiChannelByChannel(it) }

    fun wiFiChannels(): List<WiFiChannel> = (channelRange.first.channel..channelRange.second.channel).map { wiFiChannelByChannel(it) }

    private fun wiFiChannel(frequency: Int, wiFiChannelPair: WiFiChannelPair): WiFiChannel {
        val firstChannel: Int = (wiFiChannelPair.first.channel + if (wiFiChannelPair.first.channel < 0) -0.5 else 0.5).toInt()
        val channel: Int = ((frequency - wiFiChannelPair.first.frequency).toDouble() / FREQUENCY_SPREAD + firstChannel).toInt()
        return WiFiChannel(channel, frequency)
    }

    companion object {
        internal const val FREQUENCY_SPREAD = 5
    }

}