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

internal const val FREQUENCY_SPREAD = 5

typealias WiFiRange = Pair<Int, Int>
typealias WiFiChannelPair = Pair<WiFiChannel, WiFiChannel>

private val channelRangeGHZ2 = WiFiChannelPair(WiFiChannel(-1, 2402), WiFiChannel(15, 2482))
private val graphChannelsGHZ2 = (1..13).associate { it to "$it" }

class WiFiChannelsGHZ2 : WiFiChannels(channelRangeGHZ2, graphChannelsGHZ2) {
    override fun availableChannels(countryCode: String): List<WiFiChannel> =
        availableChannels(WiFiChannelCountry.find(countryCode).channelsGHZ2())
}

private val channelRangeGHZ5 = WiFiChannelPair(WiFiChannel(30, 5150), WiFiChannel(179, 5895))
private val graphChannelsGHZ5 = listOf(42, 58, 74, 90, 106, 122, 138, 156, 171).associate {
    it to when (it) {
        156 -> "155"
        else -> "$it"
    }
}

class WiFiChannelsGHZ5 : WiFiChannels(channelRangeGHZ5, graphChannelsGHZ5) {
    override fun availableChannels(countryCode: String): List<WiFiChannel> =
        availableChannels(WiFiChannelCountry.find(countryCode).channelsGHZ5())

    override fun graphChannelCount(): Int = super.graphChannelCount() / 2
}

private val channelRangeGHZ6 = WiFiChannelPair(WiFiChannel(-5, 5925), WiFiChannel(235, 7125))
private val graphChannelsGHZ6 = listOf(15, 47, 79, 110, 142, 174, 208).associate {
    it to when (it) {
        110 -> "111"
        142 -> "143"
        174 -> "175"
        208 -> "207"
        else -> "$it"
    }
}

class WiFiChannelsGHZ6 : WiFiChannels(channelRangeGHZ6, graphChannelsGHZ6) {
    override fun availableChannels(countryCode: String): List<WiFiChannel> =
        availableChannels(WiFiChannelCountry.find(countryCode).channelsGHZ6())

    override fun graphChannelCount(): Int = super.graphChannelCount() / 2
}

abstract class WiFiChannels(val channelRange: WiFiChannelPair, val graphChannels: Map<Int, String>) {
    abstract fun availableChannels(countryCode: String): List<WiFiChannel>

    fun inRange(frequency: Int): Boolean = frequency in channelRange.first.frequency..channelRange.second.frequency

    fun wiFiChannelByFrequency(frequency: Int): WiFiChannel =
        if (inRange(frequency)) wiFiChannel(frequency) else WiFiChannel.UNKNOWN

    fun wiFiChannelByChannel(channel: Int): WiFiChannel =
        if (channel in channelRange.first.channel..channelRange.second.channel) {
            WiFiChannel(channel, channelRange.first.frequency + (channel - channelRange.first.channel) * FREQUENCY_SPREAD)
        } else {
            WiFiChannel.UNKNOWN
        }

    open fun graphChannelCount(): Int = channelRange.second.channel - channelRange.first.channel + 1

    fun graphChannelByFrequency(frequency: Int): String =
        graphChannels[wiFiChannelByFrequency(frequency).channel] ?: String.EMPTY

    fun availableChannels(channels: Set<Int>): List<WiFiChannel> = channels.map { wiFiChannelByChannel(it) }

    fun wiFiChannels(): List<WiFiChannel> = (channelRange.first.channel..channelRange.second.channel).map { wiFiChannelByChannel(it) }

    private fun wiFiChannel(frequency: Int): WiFiChannel {
        val firstChannel = (channelRange.first.channel + if (channelRange.first.channel < 0) -0.5 else 0.5).toInt()
        val channel = ((frequency - channelRange.first.frequency) / FREQUENCY_SPREAD + firstChannel).toInt()
        return WiFiChannel(channel, frequency)
    }
}