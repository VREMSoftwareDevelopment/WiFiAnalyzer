/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2023 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import org.junit.Assert.*
import org.junit.Test
import java.util.Locale

class WiFiSignalTest {
    private val primaryFrequency = 2432
    private val primaryChannel = 5
    private val centerFrequency = 2437
    private val centerChannel = 6
    private val level = -65
    private val fixture: WiFiSignal = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.bandwidth40MHz, level, true, WiFiStandard.N)

    @Test
    fun testWiFiFrequency() {
        // validate
        assertEquals(primaryFrequency, fixture.primaryFrequency)
        assertEquals(centerFrequency, fixture.centerFrequency)
        assertEquals(level, fixture.level)
        assertEquals(WiFiBand.band2GHz, fixture.wiFiBand)
        assertEquals(WiFiWidth.bandwidth40MHz, fixture.wiFiWidth)
        assertEquals(WiFiStandard.N, fixture.wiFiStandard)
    }

    @Test
    fun testWiFiFrequencyWithFrequencyAndWiFiWidth() {
        // execute
        val fixture = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.bandwidth80MHz, level, true, WiFiStandard.AC)
        // validate
        assertEquals(primaryFrequency, fixture.primaryFrequency)
        assertEquals(primaryChannel, fixture.primaryWiFiChannel.channel)
        assertEquals(centerFrequency, fixture.centerFrequency)
        assertEquals(centerChannel, fixture.centerWiFiChannel.channel)
        assertEquals(level, fixture.level)
        assertEquals(WiFiBand.band2GHz, fixture.wiFiBand)
        assertEquals(WiFiWidth.bandwidth80MHz, fixture.wiFiWidth)
        assertEquals(WiFiStandard.AC, fixture.wiFiStandard)
    }

    @Test
    fun testCenterFrequency() {
        assertEquals(centerFrequency, fixture.centerFrequency)
        assertEquals(centerFrequency - WiFiWidth.bandwidth40MHz.frequencyWidthHalf, fixture.frequencyStart)
        assertEquals(centerFrequency + WiFiWidth.bandwidth40MHz.frequencyWidthHalf, fixture.frequencyEnd)
    }

    @Test
    fun testInRange() {
        assertTrue(fixture.inRange(centerFrequency))
        assertTrue(fixture.inRange(centerFrequency - WiFiWidth.bandwidth40MHz.frequencyWidthHalf))
        assertTrue(fixture.inRange(centerFrequency + WiFiWidth.bandwidth40MHz.frequencyWidthHalf))
        assertFalse(fixture.inRange(centerFrequency - WiFiWidth.bandwidth40MHz.frequencyWidthHalf - 1))
        assertFalse(fixture.inRange(centerFrequency + WiFiWidth.bandwidth40MHz.frequencyWidthHalf + 1))
    }

    @Test
    fun testPrimaryWiFiChannel() {
        assertEquals(primaryChannel, fixture.primaryWiFiChannel.channel)
    }

    @Test
    fun testCenterWiFiChannel() {
        assertEquals(centerChannel, fixture.centerWiFiChannel.channel)
    }

    @Test
    fun testStrength() {
        assertEquals(Strength.THREE, fixture.strength)
    }

    @Test
    fun testDistance() {
        // setup
        val expected = String.format(Locale.ENGLISH, "~%.1fm", calculateDistance(primaryFrequency, level))
        // execute
        val actual: String = fixture.distance
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testEquals() {
        // setup
        val other = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.bandwidth40MHz, level, true, WiFiStandard.N)
        // execute & validate
        assertEquals(fixture, other)
        assertNotSame(fixture, other)
    }

    @Test
    fun testHashCode() {
        // setup
        val other = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.bandwidth40MHz, level, true, WiFiStandard.N)
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode())
    }

    @Test
    fun testChannelDisplayWhenPrimaryAndCenterSame() {
        // setup
        val fixture = WiFiSignal(primaryFrequency, primaryFrequency, WiFiWidth.bandwidth40MHz, level, true, WiFiStandard.N)
        // execute & validate
        assertEquals("5", fixture.channelDisplay())
    }

    @Test
    fun testChannelDisplayWhenPrimaryAndCenterDifferent() {
        // setup
        val fixture = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.bandwidth40MHz, level, true, WiFiStandard.N)
        // execute & validate
        assertEquals("5(6)", fixture.channelDisplay())
    }

}
