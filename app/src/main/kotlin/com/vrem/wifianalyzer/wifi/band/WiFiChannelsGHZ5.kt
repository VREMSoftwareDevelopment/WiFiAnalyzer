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
package com.vrem.wifianalyzer.wifi.band

import androidx.core.util.Pair

class WiFiChannelsGHZ5 : WiFiChannels(RANGE, SETS) {
    override fun wiFiChannelPairs(): List<Pair<WiFiChannel, WiFiChannel>> = SETS

    override fun wiFiChannelPairFirst(countryCode: String): Pair<WiFiChannel, WiFiChannel> =
            if (countryCode.isBlank())
                SET1
            else
                wiFiChannelPairs().find { channelAvailable(countryCode, it.first!!.channel) }
                        ?: SET1

    override fun availableChannels(countryCode: String): List<WiFiChannel> =
            availableChannels(WiFiChannelCountry.find(countryCode).channelsGHZ5())

    override fun channelAvailable(countryCode: String, channel: Int): Boolean =
            WiFiChannelCountry.find(countryCode).channelAvailableGHZ5(channel)

    override fun wiFiChannelByFrequency(frequency: Int, wiFiChannelPair: Pair<WiFiChannel, WiFiChannel>): WiFiChannel =
            if (inRange(frequency)) wiFiChannel(frequency, wiFiChannelPair) else WiFiChannel.UNKNOWN

    companion object {
        @JvmField
        val SET1 = Pair(WiFiChannel(36, 5180), WiFiChannel(64, 5320))

        @JvmField
        val SET2 = Pair(WiFiChannel(100, 5500), WiFiChannel(144, 5720))

        @JvmField
        val SET3 = Pair(WiFiChannel(149, 5745), WiFiChannel(165, 5825))

        @JvmField
        val SETS = listOf(SET1, SET2, SET3)
        private val RANGE = Pair(4900, 5899)
    }
}