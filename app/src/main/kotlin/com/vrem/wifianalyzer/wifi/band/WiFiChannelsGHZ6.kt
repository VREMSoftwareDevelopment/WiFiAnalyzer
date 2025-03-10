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

class WiFiChannelsGHZ6 : WiFiChannels(RANGE, SETS) {
    override fun wiFiChannelPairs(): List<WiFiChannelPair> = SETS

    override fun wiFiChannelPairFirst(countryCode: String): WiFiChannelPair =
        if (countryCode.isBlank())
            SET1
        else
            wiFiChannelPairs().firstOrNull { channelAvailable(countryCode, it.first.channel) } ?: SET1

    override fun availableChannels(countryCode: String): List<WiFiChannel> =
        availableChannels(WiFiChannelCountry.find(countryCode).channelsGHZ6())

    override fun channelAvailable(countryCode: String, channel: Int): Boolean =
        WiFiChannelCountry.find(countryCode).channelAvailableGHZ6(channel)

    override fun wiFiChannelByFrequency(frequency: Int, wiFiChannelPair: WiFiChannelPair): WiFiChannel =
        if (inRange(frequency)) wiFiChannel(frequency, wiFiChannelPair) else WiFiChannel.UNKNOWN

    companion object {
        val SET1 = WiFiChannelPair(WiFiChannel(1, 5955), WiFiChannel(65, 6275))
        val SET2 = WiFiChannelPair(WiFiChannel(33, 6115), WiFiChannel(97, 6435))
        val SET3 = WiFiChannelPair(WiFiChannel(65, 6275), WiFiChannel(129, 6595))
        val SET4 = WiFiChannelPair(WiFiChannel(97, 6435), WiFiChannel(161, 6755))
        val SET5 = WiFiChannelPair(WiFiChannel(129, 6595), WiFiChannel(189, 6895))
        val SET6 = WiFiChannelPair(WiFiChannel(161, 6755), WiFiChannel(233, 7115))
        val SETS = listOf(SET1, SET2, SET3, SET4, SET5, SET6)
        private val RANGE = WiFiRange(5925, 7125)
    }
}
