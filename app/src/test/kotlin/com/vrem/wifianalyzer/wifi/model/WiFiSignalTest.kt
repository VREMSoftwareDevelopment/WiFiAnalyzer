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

import com.vrem.wifianalyzer.wifi.band.WiFiBand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.Locale

class WiFiSignalTest {
    private val primaryFrequency = 2432
    private val primaryChannel = 5
    private val centerFrequency = 2437
    private val centerChannel = 6
    private val level = -65
    private val other = WiFiSignal(primaryFrequency, centerFrequency + 10, WiFiWidth.MHZ_40, level + 10)
    private val fixture = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.MHZ_40, level)

    @Test
    fun wiFiSignal() {
        // validate
        assertThat(fixture.primaryFrequency).isEqualTo(primaryFrequency)
        assertThat(fixture.centerFrequency).isEqualTo(centerFrequency)
        assertThat(fixture.level).isEqualTo(level)
        assertThat(fixture.wiFiBand).isEqualTo(WiFiBand.GHZ2)
        assertThat(fixture.wiFiWidth).isEqualTo(WiFiWidth.MHZ_40)
        assertThat(fixture.extra).isEqualTo(WiFiSignalExtra.EMPTY)
    }

    @Test
    fun wiFiSignalWithFrequencyAndWiFiWidth() {
        // execute
        val fixture = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.MHZ_80, level)
        // validate
        assertThat(fixture.primaryFrequency).isEqualTo(primaryFrequency)
        assertThat(fixture.primaryWiFiChannel.channel).isEqualTo(primaryChannel)
        assertThat(fixture.centerFrequency).isEqualTo(centerFrequency)
        assertThat(fixture.centerWiFiChannel.channel).isEqualTo(centerChannel)
        assertThat(fixture.level).isEqualTo(level)
        assertThat(fixture.wiFiBand).isEqualTo(WiFiBand.GHZ2)
        assertThat(fixture.wiFiWidth).isEqualTo(WiFiWidth.MHZ_80)
    }

    @Test
    fun centerFrequency() {
        assertThat(fixture.centerFrequency).isEqualTo(centerFrequency)
        assertThat(fixture.frequencyStart).isEqualTo(centerFrequency - WiFiWidth.MHZ_40.frequencyWidthHalf)
        assertThat(fixture.frequencyEnd).isEqualTo(centerFrequency + WiFiWidth.MHZ_40.frequencyWidthHalf)
    }

    @Test
    fun inRange() {
        assertThat(fixture.inRange(centerFrequency)).isTrue()
        assertThat(fixture.inRange(centerFrequency - WiFiWidth.MHZ_40.frequencyWidthHalf)).isTrue()
        assertThat(fixture.inRange(centerFrequency + WiFiWidth.MHZ_40.frequencyWidthHalf)).isTrue()
        assertThat(fixture.inRange(centerFrequency - WiFiWidth.MHZ_40.frequencyWidthHalf - 1)).isFalse()
        assertThat(fixture.inRange(centerFrequency + WiFiWidth.MHZ_40.frequencyWidthHalf + 1)).isFalse()
    }

    @Test
    fun primaryWiFiChannel() {
        assertThat(fixture.primaryWiFiChannel.channel).isEqualTo(primaryChannel)
    }

    @Test
    fun centerWiFiChannel() {
        assertThat(fixture.centerWiFiChannel.channel).isEqualTo(centerChannel)
    }

    @Test
    fun strength() {
        assertThat(fixture.strength).isEqualTo(Strength.THREE)
    }

    @Test
    fun distance() {
        // setup
        val expected = String.format(Locale.ENGLISH, "~%.1fm", calculateDistance(primaryFrequency, level))
        // execute
        val actual: String = fixture.distance
        // validate
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun equalsUsingPrimaryFrequencyAndWidth() {
        // execute & validate
        assertThat(other).isEqualTo(fixture)
        assertThat(other).isNotSameAs(fixture)
    }

    @Test
    fun hashCodeUsingPrimaryFrequencyAndWidth() {
        // execute & validate
        assertThat(other.hashCode()).isEqualTo(fixture.hashCode())
    }

    @Test
    fun channelDisplayWhenPrimaryAndCenterSame() {
        // setup
        val fixture = WiFiSignal(primaryFrequency, primaryFrequency, WiFiWidth.MHZ_40, level)
        // execute & validate
        assertThat(fixture.channelDisplay()).isEqualTo("5")
    }

    @Test
    fun channelDisplayWhenPrimaryAndCenterDifferent() {
        // setup
        val fixture = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.MHZ_40, level)
        // execute & validate
        assertThat(fixture.channelDisplay()).isEqualTo("5(6)")
    }

}
