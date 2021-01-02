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
package com.vrem.wifianalyzer.wifi.model

import com.vrem.wifianalyzer.wifi.band.WiFiChannel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Test

class ChannelAPCountTest {
    private val frequency = 2435
    private val channel = 10
    private val count = 111
    private val wiFiChannel: WiFiChannel = WiFiChannel(channel, frequency)
    private val fixture: ChannelAPCount = ChannelAPCount(wiFiChannel, count)

    @Test
    fun testEquals() {
        // setup
        val wiFiChannel = WiFiChannel(channel, frequency)
        val other = ChannelAPCount(wiFiChannel, count)
        // execute & validate
        assertEquals(fixture, other)
        assertNotSame(fixture, other)
    }

    @Test
    fun testHashCode() {
        // setup
        val wiFiChannel = WiFiChannel(channel, frequency)
        val other = ChannelAPCount(wiFiChannel, count)
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode())
    }

    @Test
    fun testCompareTo() {
        // setup
        val wiFiChannel = WiFiChannel(channel, frequency)
        val other = ChannelAPCount(wiFiChannel, count)
        // execute & validate
        assertEquals(0, fixture.compareTo(other))
    }

}