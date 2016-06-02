/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
    public void setUp() throws Exception {
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