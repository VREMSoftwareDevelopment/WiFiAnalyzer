/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import org.junit.Assert.*
import org.junit.Test

class WiFiChannelTest {
    private val channel = 1
    private val frequency = 200
    private val fixture: WiFiChannel = WiFiChannel(channel, frequency)
    private val other: WiFiChannel = WiFiChannel(channel, frequency)

    @Test
    fun testInRange() {
        assertTrue(fixture.inRange(frequency))
        assertTrue(fixture.inRange(frequency - 2))
        assertTrue(fixture.inRange(frequency + 2))
        assertFalse(fixture.inRange(frequency - 3))
        assertFalse(fixture.inRange(frequency + 3))
    }

    @Test
    fun testEquals() {
        assertEquals(fixture, other)
        assertNotSame(fixture, other)
    }

    @Test
    fun testHashCode() {
        assertEquals(fixture.hashCode(), other.hashCode())
    }

    @Test
    fun testCompareTo() {
        assertEquals(0, fixture.compareTo(other))
    }

}