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
import com.vrem.wifianalyzer.wifi.model.WiFiWidth

typealias RatingChannels = (wiFiBand: WiFiBand, countryCode: String) -> List<Int>

class WiFiChannels(
    val channelRange: WiFiChannelPair,
    val offset: Int,
    val activeChannels: Map<WiFiWidth, List<Int>>,
    val graphChannels: Map<Int, String>,
    val availableChannels: List<Int>,
    val ratingChannels: RatingChannels
) {

    fun availableChannels(wiFiBand: WiFiBand, countryCode: String): List<WiFiChannel> =
        WiFiChannelCountry.find(countryCode).channels(wiFiBand).map { wiFiChannelByChannel(it) }

    fun availableChannels(wiFiWidth: WiFiWidth, wiFiBand: WiFiBand, countryCode: String): List<Int> =
        activeChannels[wiFiWidth].orEmpty().filter { it in ratingChannels(wiFiBand, countryCode) }

    fun inRange(frequency: Int): Boolean = frequency in channelRange.first.frequency..channelRange.second.frequency

    fun wiFiChannelByFrequency(frequency: Int): WiFiChannel =
        if (inRange(frequency)) wiFiChannel(frequency) else WiFiChannel.UNKNOWN

    fun wiFiChannelByChannel(channel: Int): WiFiChannel =
        if (channel in channelRange.first.channel..channelRange.second.channel) {
            WiFiChannel(channel, channelRange.first.frequency + (channel - channelRange.first.channel) * FREQUENCY_SPREAD)
        } else {
            WiFiChannel.UNKNOWN
        }

    fun wiFiWidthByChannel(channel: Int): WiFiWidth =
        activeChannels.entries.firstOrNull { it.value.contains(channel) }?.key ?: WiFiWidth.MHZ_20

    fun graphChannelCount(): Int = (channelRange.second.channel - channelRange.first.channel + 1) / offset

    fun graphChannelByFrequency(frequency: Int): String =
        graphChannels[wiFiChannelByFrequency(frequency).channel] ?: String.EMPTY

    fun wiFiChannels(): List<WiFiChannel> =
        (channelRange.first.channel..channelRange.second.channel).map { wiFiChannelByChannel(it) }

    private fun wiFiChannel(frequency: Int): WiFiChannel {
        val firstChannel = (channelRange.first.channel + if (channelRange.first.channel < 0) -0.5 else 0.5).toInt()
        val channel = (frequency - channelRange.first.frequency) / FREQUENCY_SPREAD + firstChannel
        return WiFiChannel(channel, frequency)
    }

}