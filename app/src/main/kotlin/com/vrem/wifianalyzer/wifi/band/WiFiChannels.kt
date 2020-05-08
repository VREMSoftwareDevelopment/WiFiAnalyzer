/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import androidx.core.util.Pair

abstract class WiFiChannels(private val wiFiRange: Pair<Int, Int>, private val wiFiChannelPairs: List<Pair<WiFiChannel, WiFiChannel>>) {
    fun inRange(frequency: Int): Boolean = frequency >= wiFiRange.first!! && frequency <= wiFiRange.second!!

    fun wiFiChannelByFrequency(frequency: Int): WiFiChannel =
            if (inRange(frequency)) {
                wiFiChannelPairs.find { WiFiChannel.UNKNOWN != wiFiChannel(frequency, it) }?.let { wiFiChannel(frequency, it) }
                        ?: WiFiChannel.UNKNOWN
            } else {
                WiFiChannel.UNKNOWN
            }

    fun wiFiChannelByChannel(channel: Int): WiFiChannel =
            wiFiChannelPairs.find { channel >= it.first!!.channel && channel <= it.second!!.channel }
                    ?.let { WiFiChannel(channel, it.first!!.frequency + (channel - it.first!!.channel) * FREQUENCY_SPREAD) }
                    ?: WiFiChannel.UNKNOWN

    fun wiFiChannelFirst(): WiFiChannel = wiFiChannelPairs[0].first!!

    fun wiFiChannelLast(): WiFiChannel = wiFiChannelPairs[wiFiChannelPairs.size - 1].second!!

    fun wiFiChannel(frequency: Int, wiFiChannelPair: Pair<WiFiChannel, WiFiChannel>): WiFiChannel {
        val first: WiFiChannel = wiFiChannelPair.first!!
        val last: WiFiChannel = wiFiChannelPair.second!!
        val channel: Int = ((frequency - first.frequency).toDouble() / FREQUENCY_SPREAD + first.channel + 0.5).toInt()
        return if (channel >= first.channel && channel <= last.channel)
            WiFiChannel(channel, frequency)
        else
            WiFiChannel.UNKNOWN
    }

    abstract fun availableChannels(countryCode: String): List<WiFiChannel>
    abstract fun channelAvailable(countryCode: String, channel: Int): Boolean
    abstract fun wiFiChannelPairs(): List<Pair<WiFiChannel, WiFiChannel>>
    abstract fun wiFiChannelPairFirst(countryCode: String): Pair<WiFiChannel, WiFiChannel>
    abstract fun wiFiChannelByFrequency(frequency: Int, wiFiChannelPair: Pair<WiFiChannel, WiFiChannel>): WiFiChannel

    fun availableChannels(channels: Set<Int>): List<WiFiChannel> =
            channels.map { this.wiFiChannelByChannel(it) }.toList()

    fun wiFiChannels(): List<WiFiChannel> = wiFiChannelPairs.flatMap { transform(it) }.toList()

    private fun transform(wiFiChannelPair: Pair<WiFiChannel, WiFiChannel>): List<WiFiChannel> =
            (wiFiChannelPair.first!!.channel..wiFiChannelPair.second!!.channel)
                    .map { wiFiChannelByChannel(it) }
                    .toList()

    companion object {
        @JvmField
        val UNKNOWN = Pair(WiFiChannel.UNKNOWN, WiFiChannel.UNKNOWN)
        const val FREQUENCY_SPREAD = 5
        const val CHANNEL_OFFSET = 2
        const val FREQUENCY_OFFSET = FREQUENCY_SPREAD * CHANNEL_OFFSET
    }

}