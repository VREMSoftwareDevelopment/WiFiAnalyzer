/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
    private val fixture = WiFiChannel(channel, frequency)

    @Test
    fun inRange() {
        assertTrue(fixture.inRange(frequency))
        assertTrue(fixture.inRange(frequency - 2))
        assertTrue(fixture.inRange(frequency + 2))
        assertFalse(fixture.inRange(frequency - 3))
        assertFalse(fixture.inRange(frequency + 3))
    }

    @Test
    fun compareToUsingSameChannelAndFrequency() {
        // setup
        val other = WiFiChannel(channel, frequency)
        // execute & validate
        assertEquals(0, fixture.compareTo(other))
    }

    @Test
    fun compareToUsingDifferentChannel() {
        // setup
        val other = WiFiChannel(channel + 1, frequency)
        // execute & validate
        assertEquals(-1, fixture.compareTo(other))
    }

    @Test
    fun compareToUsingDifferentFrequency() {
        // setup
        val other = WiFiChannel(channel, frequency + 1)
        // execute & validate
        assertEquals(-1, fixture.compareTo(other))
    }

}