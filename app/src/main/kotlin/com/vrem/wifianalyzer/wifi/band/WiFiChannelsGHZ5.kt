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

class WiFiChannelsGHZ5 : WiFiChannels(channelRange, graphChannels) {
    override fun availableChannels(countryCode: String): List<WiFiChannel> =
        availableChannels(WiFiChannelCountry.find(countryCode).channelsGHZ5())

    override fun channelAvailable(countryCode: String, channel: Int): Boolean =
        WiFiChannelCountry.find(countryCode).channelAvailableGHZ5(channel)

    override fun graphChannelCount(): Int {
        return super.graphChannelCount() / 2
    }

    companion object {
        private val channelRange = WiFiChannelPair(WiFiChannel(30, 5150), WiFiChannel(179, 5895))
        private val graphChannels: Set<Int> = setOf(42, 58, 74, 90, 106, 122, 138, 156, 171)

    }
}
