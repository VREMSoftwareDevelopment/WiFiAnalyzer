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
package com.vrem.wifianalyzer.wifi.model

import com.vrem.util.EMPTY
import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ChannelRatingTest {
    private val wiFiConnection = WiFiConnection(
        WiFiIdentifier("ssid1", "20:CF:30:CE:1D:71"),
        "192.168.1.15",
        11
    )
    private val wiFiDetail1 = WiFiDetail(
        WiFiIdentifier("SSID1", "20:cf:30:ce:1d:71"),
        WiFiSecurity.EMPTY,
        WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -50),
        WiFiAdditional(String.EMPTY, wiFiConnection)
    )
    private val wiFiDetail2 = WiFiDetail(
        WiFiIdentifier("SSID2", "58:6d:8f:fa:ae:c0"),
        WiFiSecurity.EMPTY,
        WiFiSignal(2442, 2442, WiFiWidth.MHZ_20, -70),
        WiFiAdditional.EMPTY
    )
    private val wiFiDetail3 = WiFiDetail(
        WiFiIdentifier("SSID3", "84:94:8c:9d:40:68"),
        WiFiSecurity.EMPTY,
        WiFiSignal(2452, 2452, WiFiWidth.MHZ_20, -60),
        WiFiAdditional.EMPTY
    )
    private val wiFiDetail4 = WiFiDetail(
        WiFiIdentifier("SSID3", "64:A4:8c:90:10:12"),
        WiFiSecurity.EMPTY,
        WiFiSignal(2452, 2452, WiFiWidth.MHZ_20, -80),
        WiFiAdditional.EMPTY
    )

    private val fixture = ChannelRating()

    @Test
    fun channelRating() {
        // setup
        val wiFiChannel: WiFiChannel = wiFiDetail1.wiFiSignal.centerWiFiChannel
        // execute & validate
        assertThat(fixture.count(wiFiChannel)).isEqualTo(0)
        assertThat(fixture.strength(wiFiChannel)).isEqualTo(Strength.ZERO)
    }

    @Test
    fun count() {
        // setup
        fixture.wiFiDetails(listOf(wiFiDetail1, wiFiDetail2, wiFiDetail3, wiFiDetail4))
        // execute and validate
        validateCount(2, wiFiDetail1.wiFiSignal.centerWiFiChannel)
        validateCount(4, wiFiDetail2.wiFiSignal.centerWiFiChannel)
        validateCount(3, wiFiDetail3.wiFiSignal.centerWiFiChannel)
    }

    private fun validateCount(expected: Int, wiFiChannel: WiFiChannel) {
        assertThat(fixture.count(wiFiChannel)).isEqualTo(expected)
    }

    @Test
    fun strengthShouldReturnMaximum() {
        // setup
        val other: WiFiDetail = makeCopy(wiFiDetail3)
        fixture.wiFiDetails(listOf(other, wiFiDetail3))
        val expected: Strength = wiFiDetail3.wiFiSignal.strength
        // execute
        val actual: Strength = fixture.strength(wiFiDetail3.wiFiSignal.centerWiFiChannel)
        // execute and validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun strengthWithConnected() {
        // setup
        val other: WiFiDetail = makeCopy(wiFiDetail1)
        fixture.wiFiDetails(listOf(other, wiFiDetail1))
        val expected: Strength = other.wiFiSignal.strength
        // execute
        val actual: Strength = fixture.strength(wiFiDetail1.wiFiSignal.centerWiFiChannel)
        // execute and validate
        assertThat(actual).isEqualTo(expected)
    }

    private fun makeCopy(wiFiDetail: WiFiDetail): WiFiDetail {
        val wiFiSignal: WiFiSignal = wiFiDetail.wiFiSignal
        return WiFiDetail(
            WiFiIdentifier("SSID2-OTHER", "BSSID-OTHER"),
            WiFiSecurity.EMPTY,
            WiFiSignal(wiFiSignal.primaryFrequency, wiFiSignal.centerFrequency, wiFiSignal.wiFiWidth, -80),
            WiFiAdditional.EMPTY
        )
    }

    @Test
    fun bestChannelsSortedInOrderWithMinimumChannels() {
        // setup
        val channels: List<WiFiChannel> = WiFiBand.GHZ2.wiFiChannels.wiFiChannels()
        fixture.wiFiDetails(listOf(wiFiDetail1, wiFiDetail2, wiFiDetail3, wiFiDetail4))
        // execute
        val actual: List<ChannelAPCount> = fixture.bestChannels(channels)
        // validate
        assertThat(actual).hasSize(7)
        validateChannelAPCount(1, 0, actual[0])
        validateChannelAPCount(2, 0, actual[1])
        validateChannelAPCount(12, 0, actual[2])
        validateChannelAPCount(13, 0, actual[3])
        validateChannelAPCount(14, 0, actual[4])
        validateChannelAPCount(3, 1, actual[5])
        validateChannelAPCount(4, 1, actual[6])
    }

    private fun validateChannelAPCount(expectedChannel: Int, expectedCount: Int, channelAPCount: ChannelAPCount) {
        assertThat(channelAPCount.wiFiChannel.channel).isEqualTo(expectedChannel)
        assertThat(channelAPCount.count).isEqualTo(expectedCount)
    }

    @Test
    fun setWiFiChannelsRemovesDuplicateAccessPoints() {
        // setup
        val wiFiDetail = WiFiDetail(
            WiFiIdentifier("SSID2", "22:cf:30:ce:1d:72"),
            WiFiSecurity.EMPTY,
            WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, wiFiDetail1.wiFiSignal.level - 5),
            WiFiAdditional.EMPTY
        )
        // execute
        fixture.wiFiDetails(listOf(wiFiDetail1, wiFiDetail))
        // validate
        assertThat(fixture.wiFiDetails()).hasSize(1)
        assertThat(fixture.wiFiDetails()[0]).isEqualTo(wiFiDetail1)
    }
}