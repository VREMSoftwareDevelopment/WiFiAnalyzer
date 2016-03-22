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

public class WiFiChannels2Test {

    private WiFiChannels2 fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new WiFiChannels2();
    }

    @Test
    public void testFindWiFiChannel() throws Exception {
        assertEquals(1, fixture.findWiFiChannel(2410).getChannel());
        assertEquals(1, fixture.findWiFiChannel(2412).getChannel());
        assertEquals(13, fixture.findWiFiChannel(2470).getChannel());
        assertEquals(13, fixture.findWiFiChannel(2472).getChannel());
        assertEquals(13, fixture.findWiFiChannel(2474).getChannel());
        assertEquals(14, fixture.findWiFiChannel(2484).getChannel());
        assertEquals(14, fixture.findWiFiChannel(2486).getChannel());

    }

    @Test
    public void testFindWiFiChannelFail() throws Exception {
        assertEquals(WiFiChannel.UNKNOWN, fixture.findWiFiChannel(2409));
        assertEquals(WiFiChannel.UNKNOWN, fixture.findWiFiChannel(2489));
    }

    @Test
    public void testGetWiFiChannelFirst() throws Exception {
        assertEquals(1, fixture.getWiFiChannelFirst().getChannel());
    }

    @Test
    public void testGetWiFiChannelLast() throws Exception {
        assertEquals(14, fixture.getWiFiChannelLast().getChannel());
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