/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class WiFiSignalTest {
    private static final int FREQUENCY = 2432;
    private static final int CHANNEL = 5;
    private static final int LEVEL = -65;

    private WiFiSignal fixture;

    @Before
    public void setUp() {
        fixture = new WiFiSignal(FREQUENCY, WiFiWidth.MHZ_20, LEVEL);
    }

    @Test
    public void testWiFiFrequency() throws Exception {
        // validate
        assertEquals(LEVEL, fixture.getLevel());
        assertEquals(WiFiBand.GHZ2, fixture.getWiFiBand());
        assertEquals(WiFiWidth.MHZ_20, fixture.getWiFiWidth());
    }

    @Test
    public void testWiFiFrequencyWithFrequencyAndWiFiWidth() throws Exception {
        // execute
        fixture = new WiFiSignal(FREQUENCY, WiFiWidth.MHZ_80, LEVEL);
        // validate
        assertEquals(FREQUENCY, fixture.getFrequency());
        assertEquals(CHANNEL, fixture.getWiFiChannel().getChannel());
        assertEquals(LEVEL, fixture.getLevel());
        assertEquals(WiFiBand.GHZ2, fixture.getWiFiBand());
        assertEquals(WiFiWidth.MHZ_80, fixture.getWiFiWidth());
    }

    @Test
    public void testGetFrequency() throws Exception {
        assertEquals(FREQUENCY, fixture.getFrequency());
        assertEquals(FREQUENCY - WiFiWidth.MHZ_20.getFrequencyWidthHalf(), fixture.getFrequencyStart());
        assertEquals(FREQUENCY + WiFiWidth.MHZ_20.getFrequencyWidthHalf(), fixture.getFrequencyEnd());
    }

    @Test
    public void testGetInRange() throws Exception {
        assertTrue(fixture.isInRange(FREQUENCY));
        assertTrue(fixture.isInRange(FREQUENCY - WiFiWidth.MHZ_20.getFrequencyWidthHalf()));
        assertTrue(fixture.isInRange(FREQUENCY + WiFiWidth.MHZ_20.getFrequencyWidthHalf()));

        assertFalse(fixture.isInRange(FREQUENCY - WiFiWidth.MHZ_20.getFrequencyWidthHalf() - 1));
        assertFalse(fixture.isInRange(FREQUENCY + WiFiWidth.MHZ_20.getFrequencyWidthHalf() + 1));
    }

    @Test
    public void testGetWiFiChannel() throws Exception {
        assertEquals(CHANNEL, fixture.getWiFiChannel().getChannel());
    }

    @Test
    public void testGetStrength() throws Exception {
        assertEquals(Strength.THREE, fixture.getStrength());
    }

    @Test
    public void testGetDistance() throws Exception {
        assertEquals(WiFiUtils.calculateDistance(FREQUENCY, LEVEL), fixture.getDistance(), 0.0);
    }

    @Test
    public void testEquals() throws Exception {
        // setup
        WiFiSignal other = new WiFiSignal(FREQUENCY, WiFiWidth.MHZ_20, LEVEL);
        // execute & validate
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() throws Exception {
        // setup
        WiFiSignal other = new WiFiSignal(FREQUENCY, WiFiWidth.MHZ_20, LEVEL);
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode());
    }
}