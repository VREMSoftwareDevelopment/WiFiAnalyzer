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

package com.vrem.wifianalyzer.wifi.band;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WiFiChannels5Test {
    private WiFiChannels5 fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new WiFiChannels5();
    }

    @Test
    public void testFindWiFiChannel() throws Exception {
        assertEquals(183, fixture.findWiFiChannel(4913).getChannel());
        assertEquals(183, fixture.findWiFiChannel(4915).getChannel());

        assertEquals(36, fixture.findWiFiChannel(5178).getChannel());
        assertEquals(36, fixture.findWiFiChannel(5180).getChannel());
        assertEquals(36, fixture.findWiFiChannel(5182).getChannel());

        assertEquals(165, fixture.findWiFiChannel(5825).getChannel());
        assertEquals(165, fixture.findWiFiChannel(5827).getChannel());
    }

    @Test
    public void testFindWiFiChannelFail() throws Exception {
        assertEquals(WiFiChannel.UNKNOWN, fixture.findWiFiChannel(4912));
        assertEquals(WiFiChannel.UNKNOWN, fixture.findWiFiChannel(5828));
    }

    @Test
    public void testGetWiFiChannelFirst() throws Exception {
        assertEquals(34, fixture.getWiFiChannelFirst().getChannel());
    }

    @Test
    public void testGetWiFiChannelLast() throws Exception {
        assertEquals(140, fixture.getWiFiChannelLast().getChannel());
    }

    @Test
    public void testGetFrequencySpread() throws Exception {
        assertEquals(5, fixture.getFrequencySpread());
    }

    @Test
    public void testGetFrequencyOffset() throws Exception {
        assertEquals(10, fixture.getFrequencyOffset());
    }
}