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

import org.junit.Test;

import java.util.SortedSet;
import java.util.TreeSet;

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
        assertTrue(WiFiBand.GHZ_2.inRange(2401));
        assertTrue(WiFiBand.GHZ_2.inRange(2499));
        assertFalse(WiFiBand.GHZ_2.inRange(2400));
        assertFalse(WiFiBand.GHZ_2.inRange(2500));

        assertTrue(WiFiBand.GHZ_5.inRange(5001));
        assertTrue(WiFiBand.GHZ_5.inRange(5999));
        assertFalse(WiFiBand.GHZ_5.inRange(5000));
        assertFalse(WiFiBand.GHZ_5.inRange(6000));
    }

    @Test
    public void testChannel() throws Exception {
        assertEquals(0, WiFiBand.GHZ_2.getChannelByFrequency(2400));
        assertEquals(0, WiFiBand.GHZ_2.getChannelByFrequency(2500));

        assertEquals(1, WiFiBand.GHZ_2.getChannelByFrequency(2401));
        assertEquals(1, WiFiBand.GHZ_2.getChannelByFrequency(2416));
        assertEquals(13, WiFiBand.GHZ_2.getChannelByFrequency(2472));
        assertEquals(14, WiFiBand.GHZ_2.getChannelByFrequency(2473));
        assertEquals(14, WiFiBand.GHZ_2.getChannelByFrequency(2484));
        assertEquals(14, WiFiBand.GHZ_2.getChannelByFrequency(2499));

        assertEquals(0, WiFiBand.GHZ_5.getChannelByFrequency(5000));
        assertEquals(0, WiFiBand.GHZ_5.getChannelByFrequency(6000));

        assertEquals(36, WiFiBand.GHZ_5.getChannelByFrequency(5001));
        assertEquals(36, WiFiBand.GHZ_5.getChannelByFrequency(5184));
        assertEquals(165, WiFiBand.GHZ_5.getChannelByFrequency(5825));
        assertEquals(165, WiFiBand.GHZ_5.getChannelByFrequency(5899));
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(2400));
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(2500));
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(5000));
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(6000));

        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(2401));
        assertEquals(WiFiBand.GHZ_2, WiFiBand.findByFrequency(2499));
        assertEquals(WiFiBand.GHZ_5, WiFiBand.findByFrequency(5001));
        assertEquals(WiFiBand.GHZ_5, WiFiBand.findByFrequency(5999));
    }

    @Test
    public void testFindChannels() throws Exception {
        assertSetEquals(expected24GHZChannels(), WiFiBand.GHZ_2.getChannels());
        assertSetEquals(expected5GHZChannels(), WiFiBand.GHZ_5.getChannels());
    }

    @Test
    public void testFindChannel() throws Exception {
        assertEquals(1, WiFiBand.findChannelByFrequency(2412));
    }

    private SortedSet<Integer> expected24GHZChannels() {
        SortedSet<Integer> expected = new TreeSet<>();
        for (int i = 1; i <= 14; i++) {
            expected.add(i);
        }
        return expected;
    }

    private SortedSet<Integer> expected5GHZChannels() {
        SortedSet<Integer> expected = new TreeSet<>();
        for (int i = 36; i <= 165; i++) {
            expected.add(i);
        }
        return expected;
    }

    private void assertSetEquals(SortedSet<Integer> expected, SortedSet<Integer> actual) {
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testToggle() throws Exception {
        assertEquals(WiFiBand.GHZ_5, WiFiBand.GHZ_2.toggle());
        assertEquals(WiFiBand.GHZ_2, WiFiBand.GHZ_5.toggle());
    }
}