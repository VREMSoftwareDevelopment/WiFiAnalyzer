/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
    private static final int PRIMARY_FREQUENCY = 2432;
    private static final int PRIMARY_CHANNEL = 5;
    private static final int CENTER_FREQUENCY = 2437;
    private static final int CENTER_CHANNEL = 6;
    private static final int LEVEL = -65;

    private WiFiSignal fixture;

    @Before
    public void setUp() {
        fixture = new WiFiSignal(PRIMARY_FREQUENCY, CENTER_FREQUENCY, WiFiWidth.MHZ_40, LEVEL);
    }

    @Test
    public void testWiFiFrequency() throws Exception {
        // validate
        assertEquals(PRIMARY_FREQUENCY, fixture.getPrimaryFrequency());
        assertEquals(CENTER_FREQUENCY, fixture.getCenterFrequency());
        assertEquals(LEVEL, fixture.getLevel());
        assertEquals(WiFiBand.GHZ2, fixture.getWiFiBand());
        assertEquals(WiFiWidth.MHZ_40, fixture.getWiFiWidth());
    }

    @Test
    public void testWiFiFrequencyWithFrequencyAndWiFiWidth() throws Exception {
        // execute
        fixture = new WiFiSignal(PRIMARY_FREQUENCY, CENTER_FREQUENCY, WiFiWidth.MHZ_80, LEVEL);
        // validate
        assertEquals(PRIMARY_FREQUENCY, fixture.getPrimaryFrequency());
        assertEquals(PRIMARY_CHANNEL, fixture.getPrimaryWiFiChannel().getChannel());
        assertEquals(CENTER_FREQUENCY, fixture.getCenterFrequency());
        assertEquals(CENTER_CHANNEL, fixture.getCenterWiFiChannel().getChannel());
        assertEquals(LEVEL, fixture.getLevel());
        assertEquals(WiFiBand.GHZ2, fixture.getWiFiBand());
        assertEquals(WiFiWidth.MHZ_80, fixture.getWiFiWidth());
    }

    @Test
    public void testGetCenterFrequency() throws Exception {
        assertEquals(CENTER_FREQUENCY, fixture.getCenterFrequency());
        assertEquals(CENTER_FREQUENCY - WiFiWidth.MHZ_40.getFrequencyWidthHalf(), fixture.getFrequencyStart());
        assertEquals(CENTER_FREQUENCY + WiFiWidth.MHZ_40.getFrequencyWidthHalf(), fixture.getFrequencyEnd());
    }

    @Test
    public void testGetInRange() throws Exception {
        assertTrue(fixture.isInRange(CENTER_FREQUENCY));
        assertTrue(fixture.isInRange(CENTER_FREQUENCY - WiFiWidth.MHZ_40.getFrequencyWidthHalf()));
        assertTrue(fixture.isInRange(CENTER_FREQUENCY + WiFiWidth.MHZ_40.getFrequencyWidthHalf()));

        assertFalse(fixture.isInRange(CENTER_FREQUENCY - WiFiWidth.MHZ_40.getFrequencyWidthHalf() - 1));
        assertFalse(fixture.isInRange(CENTER_FREQUENCY + WiFiWidth.MHZ_40.getFrequencyWidthHalf() + 1));
    }

    @Test
    public void testGetPrimaryWiFiChannel() throws Exception {
        assertEquals(PRIMARY_CHANNEL, fixture.getPrimaryWiFiChannel().getChannel());
    }

    @Test
    public void testGetCenterWiFiChannel() throws Exception {
        assertEquals(CENTER_CHANNEL, fixture.getCenterWiFiChannel().getChannel());
    }

    @Test
    public void testGetStrength() throws Exception {
        assertEquals(Strength.THREE, fixture.getStrength());
    }

    @Test
    public void testGetDistance() throws Exception {
        assertEquals(WiFiUtils.calculateDistance(PRIMARY_FREQUENCY, LEVEL), fixture.getDistance(), 0.0);
    }

    @Test
    public void testEquals() throws Exception {
        // setup
        WiFiSignal other = new WiFiSignal(PRIMARY_FREQUENCY, CENTER_FREQUENCY, WiFiWidth.MHZ_40, LEVEL);
        // execute & validate
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() throws Exception {
        // setup
        WiFiSignal other = new WiFiSignal(PRIMARY_FREQUENCY, CENTER_FREQUENCY, WiFiWidth.MHZ_40, LEVEL);
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode());
    }

    @Test
    public void testGetChannelDisplayWhenPrimaryAndCenterSame() throws Exception {
        // setup
        fixture = new WiFiSignal(PRIMARY_FREQUENCY, PRIMARY_FREQUENCY, WiFiWidth.MHZ_40, LEVEL);
        // execute & validate
        assertEquals("5", fixture.getChannelDisplay());
    }

    @Test
    public void testGetChannelDisplayWhenPrimaryAndCenterDifferent() throws Exception {
        // setup
        fixture = new WiFiSignal(PRIMARY_FREQUENCY, CENTER_FREQUENCY, WiFiWidth.MHZ_40, LEVEL);
        // execute & validate
        assertEquals("5(6)", fixture.getChannelDisplay());
    }

}