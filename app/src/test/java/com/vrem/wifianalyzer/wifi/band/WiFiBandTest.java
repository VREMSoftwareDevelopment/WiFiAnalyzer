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

import org.junit.Test;

import java.util.SortedSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WiFiBandTest {
    @Test
    public void testWiFiBand() throws Exception {
        assertEquals(2, WiFiBand.values().length);
    }

    @Test
    public void testGetBand() throws Exception {
        assertEquals("2.4 GHz", WiFiBand.GHZ_2.getBand());
        assertEquals("5 GHz", WiFiBand.GHZ_5.getBand());
    }

    @Test
    public void testFindByBand() throws Exception {
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByBand(null));
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByBand("XYZ"));

        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByBand(WiFiBand.GHZ_2.getBand()));
        assertEquals(WiFiBand.GHZ_5, WiFiBand.findByBand(WiFiBand.GHZ_5.getBand()));
    }

    @Test
    public void testInRange() throws Exception {
        assertTrue(WiFiBand.GHZ_2.inRange(2402));
        assertTrue(WiFiBand.GHZ_2.inRange(2499));
        assertFalse(WiFiBand.GHZ_2.inRange(2401));
        assertFalse(WiFiBand.GHZ_2.inRange(2500));

        assertTrue(WiFiBand.GHZ_5.inRange(4900));
        assertTrue(WiFiBand.GHZ_5.inRange(5900));
        assertFalse(WiFiBand.GHZ_5.inRange(4899));
        assertFalse(WiFiBand.GHZ_5.inRange(5901));
    }

    @Test
    public void testFindChannel() throws Exception {
        assertEquals(1, WiFiBand.GHZ_2.findChannel(2410).getChannel());
        assertEquals(1, WiFiBand.GHZ_2.findChannel(2412).getChannel());
        assertEquals(13, WiFiBand.GHZ_2.findChannel(2470).getChannel());
        assertEquals(13, WiFiBand.GHZ_2.findChannel(2472).getChannel());
        assertEquals(13, WiFiBand.GHZ_2.findChannel(2474).getChannel());
        assertEquals(14, WiFiBand.GHZ_2.findChannel(2484).getChannel());
        assertEquals(14, WiFiBand.GHZ_2.findChannel(2486).getChannel());

        assertEquals(183, WiFiBand.GHZ_5.findChannel(4913).getChannel());
        assertEquals(183, WiFiBand.GHZ_5.findChannel(4915).getChannel());
        assertEquals(36, WiFiBand.GHZ_5.findChannel(5178).getChannel());
        assertEquals(36, WiFiBand.GHZ_5.findChannel(5180).getChannel());
        assertEquals(36, WiFiBand.GHZ_5.findChannel(5182).getChannel());
        assertEquals(165, WiFiBand.GHZ_5.findChannel(5825).getChannel());
        assertEquals(165, WiFiBand.GHZ_5.findChannel(5827).getChannel());
    }

    @Test
    public void testFindChannelFail() throws Exception {
        assertEquals(WiFiChannel.UNKNOWN, WiFiBand.GHZ_2.findChannel(2402));
        assertEquals(WiFiChannel.UNKNOWN, WiFiBand.GHZ_2.findChannel(2409));
        assertEquals(WiFiChannel.UNKNOWN, WiFiBand.GHZ_2.findChannel(2489));
        assertEquals(WiFiChannel.UNKNOWN, WiFiBand.GHZ_2.findChannel(2499));

        assertEquals(WiFiChannel.UNKNOWN, WiFiBand.GHZ_5.findChannel(4900));
        assertEquals(WiFiChannel.UNKNOWN, WiFiBand.GHZ_5.findChannel(4912));
        assertEquals(WiFiChannel.UNKNOWN, WiFiBand.GHZ_5.findChannel(5829));
        assertEquals(WiFiChannel.UNKNOWN, WiFiBand.GHZ_5.findChannel(5900));
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(2400));
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(2500));
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(4899));
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(5901));

        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(2401));
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(2499));
        assertEquals(WiFiBand.GHZ_5, WiFiBand.findByFrequency(4901));
        assertEquals(WiFiBand.GHZ_5, WiFiBand.findByFrequency(5899));
    }

    private void assertSetEquals(SortedSet<Integer> expected, SortedSet<Integer> actual) {
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testToggle() throws Exception {
        assertEquals(WiFiBand.GHZ_5, WiFiBand.GHZ_2.toggle());
        assertEquals(WiFiBand.GHZ_2, WiFiBand.GHZ_5.toggle());
    }

    @Test
    public void testGetWiFiChannels() throws Exception {
        assertEquals(WiFiChannel.GHZ_2, WiFiBand.GHZ_2.getWiFiChannels());
        assertEquals(WiFiChannel.GHZ_5, WiFiBand.GHZ_5.getWiFiChannels());
    }

}