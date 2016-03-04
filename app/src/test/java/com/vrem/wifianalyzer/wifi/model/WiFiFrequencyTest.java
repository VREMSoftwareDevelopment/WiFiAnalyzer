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

@RunWith(MockitoJUnitRunner.class)
public class WiFiFrequencyTest {
    public static final int FREQUENCY = 2435;
    public static final int CHANNEL = 5;

    @Mock
    private ScanResult scanResult;

    private WiFiFrequency fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new WiFiFrequency(FREQUENCY);
        assertEquals(WiFiBand.GHZ_2, fixture.getWiFiBand());
        assertEquals(WiFiWidth.MHZ_20, fixture.getWiFiWidth());
    }

    @Test
    public void testWiFiFrequencyWithScanResult() throws Exception {
        // setup
        scanResult.frequency = FREQUENCY;
        // execute
        fixture = new WiFiFrequency(scanResult);
        // validate
        assertEquals(scanResult.frequency, fixture.getFrequency());
        assertEquals(CHANNEL, fixture.getChannel());
        assertEquals(WiFiBand.GHZ_2, fixture.getWiFiBand());
        assertEquals(WiFiWidth.MHZ_20, fixture.getWiFiWidth());
    }

    @Test
    public void testWiFiFrequencyWithFrequencyAndWiFiWidth() throws Exception {
        // execute
        fixture = new WiFiFrequency(FREQUENCY, WiFiWidth.MHZ_80);
        // validate
        assertEquals(FREQUENCY, fixture.getFrequency());
        assertEquals(CHANNEL, fixture.getChannel());
        assertEquals(WiFiBand.GHZ_2, fixture.getWiFiBand());
        assertEquals(WiFiWidth.MHZ_80, fixture.getWiFiWidth());
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

}