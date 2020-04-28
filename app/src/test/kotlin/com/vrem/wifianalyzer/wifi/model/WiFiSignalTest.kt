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
package com.vrem.wifianalyzer.wifi.model

import com.vrem.wifianalyzer.wifi.band.WiFiBand
import com.vrem.wifianalyzer.wifi.band.WiFiWidth
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class WiFiSignalTest {
    private val primaryFrequency = 2432
    private val primaryChannel = 5
    private val centerFrequency = 2437
    private val centerChannel = 6
    private val level = -65

    private var fixture: WiFiSignal = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.MHZ_40, level, true)

    @Test
    fun testWiFiFrequency() {
        // validate
        assertEquals(primaryFrequency, fixture.primaryFrequency)
        assertEquals(centerFrequency, fixture.centerFrequency)
        assertEquals(level, fixture.level)
        assertEquals(WiFiBand.GHZ2, fixture.wiFiBand)
        assertEquals(WiFiWidth.MHZ_40, fixture.wiFiWidth)
    }

    @Test
    fun testWiFiFrequencyWithFrequencyAndWiFiWidth() {
        // execute
        fixture = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.MHZ_80, level, true)
        // validate
        assertEquals(primaryFrequency, fixture.primaryFrequency)
        assertEquals(primaryChannel, fixture.primaryWiFiChannel().channel)
        assertEquals(centerFrequency, fixture.centerFrequency)
        assertEquals(centerChannel, fixture.centerWiFiChannel().channel)
        assertEquals(level, fixture.level)
        assertEquals(WiFiBand.GHZ2, fixture.wiFiBand)
        assertEquals(WiFiWidth.MHZ_80, fixture.wiFiWidth)
    }

    @Test
    fun testGetCenterFrequency() {
        assertEquals(centerFrequency, fixture.centerFrequency)
        assertEquals(centerFrequency - WiFiWidth.MHZ_40.frequencyWidthHalf, fixture.frequencyStart())
        assertEquals(centerFrequency + WiFiWidth.MHZ_40.frequencyWidthHalf, fixture.frequencyEnd())
    }

    @Test
    fun testGetInRange() {
        assertTrue(fixture.inRange(centerFrequency))
        assertTrue(fixture.inRange(centerFrequency - WiFiWidth.MHZ_40.frequencyWidthHalf))
        assertTrue(fixture.inRange(centerFrequency + WiFiWidth.MHZ_40.frequencyWidthHalf))
        assertFalse(fixture.inRange(centerFrequency - WiFiWidth.MHZ_40.frequencyWidthHalf - 1))
        assertFalse(fixture.inRange(centerFrequency + WiFiWidth.MHZ_40.frequencyWidthHalf + 1))
    }

    @Test
    fun testGetPrimaryWiFiChannel() {
        assertEquals(primaryChannel, fixture.primaryWiFiChannel().channel)
    }

    @Test
    fun testGetCenterWiFiChannel() {
        assertEquals(centerChannel, fixture.centerWiFiChannel().channel)
    }

    @Test
    fun testGetStrength() {
        assertEquals(Strength.THREE, fixture.strength())
    }

    @Test
    fun testGetDistance() {
        // setup
        val expected = String.format(Locale.ENGLISH, "~%.1fm", calculateDistance(primaryFrequency, level))
        // execute
        val actual: String = fixture.distance()
        // validate
        assertEquals(expected, actual)
    }

    @Test
    fun testEquals() {
        // setup
        val other = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.MHZ_40, level, true)
        // execute & validate
        assertEquals(fixture, other)
        assertNotSame(fixture, other)
    }

    @Test
    fun testHashCode() {
        // setup
        val other = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.MHZ_40, level, true)
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode())
    }

    @Test
    fun testGetChannelDisplayWhenPrimaryAndCenterSame() {
        // setup
        fixture = WiFiSignal(primaryFrequency, primaryFrequency, WiFiWidth.MHZ_40, level, true)
        // execute & validate
        assertEquals("5", fixture.channelDisplay())
    }

    @Test
    fun testGetChannelDisplayWhenPrimaryAndCenterDifferent() {
        // setup
        fixture = WiFiSignal(primaryFrequency, centerFrequency, WiFiWidth.MHZ_40, level, true)
        // execute & validate
        assertEquals("5(6)", fixture.channelDisplay())
    }

}
