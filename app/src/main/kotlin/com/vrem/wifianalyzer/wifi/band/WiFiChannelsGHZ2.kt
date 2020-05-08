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

class WiFiChannelsGHZ2 : WiFiChannels(RANGE, SETS) {
    override fun wiFiChannelPairs(): List<Pair<WiFiChannel, WiFiChannel>> = listOf(SET)

    override fun wiFiChannelPairFirst(countryCode: String): Pair<WiFiChannel, WiFiChannel> = SET

    override fun availableChannels(countryCode: String): List<WiFiChannel> =
            availableChannels(WiFiChannelCountry.find(countryCode).channelsGHZ2())

    override fun channelAvailable(countryCode: String, channel: Int): Boolean =
            WiFiChannelCountry.find(countryCode).channelAvailableGHZ2(channel)

    override fun wiFiChannelByFrequency(frequency: Int, wiFiChannelPair: Pair<WiFiChannel, WiFiChannel>): WiFiChannel =
            if (inRange(frequency)) wiFiChannel(frequency, SET) else WiFiChannel.UNKNOWN

    companion object {
        private val RANGE = Pair(2400, 2499)
        private val SETS = listOf(
                Pair(WiFiChannel(1, 2412), WiFiChannel(13, 2472)),
                Pair(WiFiChannel(14, 2484), WiFiChannel(14, 2484)))
        private val SET = Pair(SETS[0].first, SETS[SETS.size - 1].second)
    }
}