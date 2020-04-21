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

package com.vrem.wifianalyzer.wifi.model;

import com.vrem.wifianalyzer.wifi.band.WiFiChannel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ChannelAPCountTest {
    private static final int FREQUENCY = 2435;
    private static final int CHANNEL = 10;

    private WiFiChannel wiFiChannel;
    private int count;
    private ChannelAPCount fixture;

    @Before
    public void setUp() {
        count = 111;
        wiFiChannel = new WiFiChannel(CHANNEL, FREQUENCY);
        fixture = new ChannelAPCount(wiFiChannel, count);
    }

    @Test
    public void testEquals() {
        // setup
        WiFiChannel wiFiChannel = new WiFiChannel(CHANNEL, FREQUENCY);
        ChannelAPCount other = new ChannelAPCount(wiFiChannel, count);
        // execute & validate
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() {
        // setup
        WiFiChannel wiFiChannel = new WiFiChannel(CHANNEL, FREQUENCY);
        ChannelAPCount other = new ChannelAPCount(wiFiChannel, count);
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode());
    }

    @Test
    public void testCompareTo() {
        // setup
        WiFiChannel wiFiChannel = new WiFiChannel(CHANNEL, FREQUENCY);
        ChannelAPCount other = new ChannelAPCount(wiFiChannel, count);
        // execute & validate
        assertEquals(0, fixture.compareTo(other));
    }

}