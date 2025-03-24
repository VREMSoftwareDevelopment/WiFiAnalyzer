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

class WiFiChannelsGHZ5 : WiFiChannels(RANGE, SETS) {
    override fun wiFiChannelPairs(): List<WiFiChannelPair> = SETS

    override fun availableChannels(countryCode: String): List<WiFiChannel> =
        availableChannels(WiFiChannelCountry.find(countryCode).channelsGHZ5())

    override fun channelAvailable(countryCode: String, channel: Int): Boolean =
        WiFiChannelCountry.find(countryCode).channelAvailableGHZ5(channel)

    companion object {
        private val SET1 = WiFiChannelPair(WiFiChannel(36, 5180), WiFiChannel(64, 5320))
        private val SET2 = WiFiChannelPair(WiFiChannel(100, 5500), WiFiChannel(144, 5720))
        private val SET3 = WiFiChannelPair(WiFiChannel(149, 5745), WiFiChannel(177, 5885))
        private val SETS = listOf(SET1, SET2, SET3)
        private val RANGE = WiFiRange(4900, 5899)
    }
}
