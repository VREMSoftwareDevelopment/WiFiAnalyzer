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

import android.net.wifi.ScanResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

@RunWith(MockitoJUnitRunner.class)
public class WiFiSignalTest {
    private static final int FREQUENCY = 2435;
    private static final int CHANNEL = 5;
    private static final int LEVEL = -65;

    @Mock
    private ScanResult scanResult;

    private WiFiSignal fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new WiFiSignal(FREQUENCY, LEVEL);
    }

    @Test
    public void testWiFiFrequencyWithScanResult() throws Exception {
        // setup
        scanResult.frequency = FREQUENCY;
        scanResult.level = LEVEL;
        // execute
        fixture = new WiFiSignal(scanResult);
        // validate
        assertEquals(FREQUENCY, fixture.getFrequency());
        assertEquals(CHANNEL, fixture.getChannel());
        assertEquals(LEVEL, fixture.getLevel());
        assertEquals(WiFiBand.GHZ_2, fixture.getWiFiBand());
        assertEquals(WiFiWidth.MHZ_20, fixture.getWiFiWidth());
    }

    @Test
    public void testWiFiFrequencyWithFrequencyAndWiFiWidth() throws Exception {
        // execute
        fixture = new WiFiSignal(FREQUENCY, WiFiWidth.MHZ_80, LEVEL);
        // validate
        assertEquals(FREQUENCY, fixture.getFrequency());
        assertEquals(CHANNEL, fixture.getChannel());
        assertEquals(LEVEL, fixture.getLevel());
        assertEquals(WiFiBand.GHZ_2, fixture.getWiFiBand());
        assertEquals(WiFiWidth.MHZ_80, fixture.getWiFiWidth());
    }

    @Test
    public void testWiFiFrequency() throws Exception {
        // validate
        assertEquals(LEVEL, fixture.getLevel());
        assertEquals(WiFiBand.GHZ_2, fixture.getWiFiBand());
        assertEquals(WiFiWidth.MHZ_20, fixture.getWiFiWidth());
    }

    @Test
    public void testGetFrequency() throws Exception {
        assertEquals(FREQUENCY, fixture.getFrequency());
        assertEquals(FREQUENCY - WiFiWidth.MHZ_20.getFrequencyWidthHalf(), fixture.getFrequencyStart());
        assertEquals(FREQUENCY + WiFiWidth.MHZ_20.getFrequencyWidthHalf(), fixture.getFrequencyEnd());
    }

    @Test
    public void testGetChannel() throws Exception {
        assertEquals(CHANNEL, fixture.getChannel());
        assertEquals(CHANNEL - WiFiWidth.MHZ_20.getChannelWidthHalf(), fixture.getChannelStart());
        assertEquals(CHANNEL + WiFiWidth.MHZ_20.getChannelWidthHalf(), fixture.getChannelEnd());
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
        WiFiSignal other = new WiFiSignal(FREQUENCY, LEVEL);
        // execute & validate
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() throws Exception {
        // setup
        WiFiSignal other = new WiFiSignal(FREQUENCY, LEVEL);
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode());
    }
}