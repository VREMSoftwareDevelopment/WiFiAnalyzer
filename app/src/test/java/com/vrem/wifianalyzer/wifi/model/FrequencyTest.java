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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FrequencyTest {
    @Test
    public void testFrequency() throws Exception {
        assertEquals(3, Frequency.values().length);
    }

    @Test
    public void testInRange() throws Exception {
        assertTrue(Frequency.UNKNOWN.inRange(0));
        assertFalse(Frequency.UNKNOWN.inRange(1));
        assertFalse(Frequency.UNKNOWN.inRange(-1));

        assertTrue(Frequency.TWO.inRange(2401));
        assertTrue(Frequency.TWO.inRange(2499));
        assertFalse(Frequency.TWO.inRange(2400));
        assertFalse(Frequency.TWO.inRange(2500));

        assertTrue(Frequency.FIVE.inRange(5001));
        assertTrue(Frequency.FIVE.inRange(5999));
        assertFalse(Frequency.FIVE.inRange(5000));
        assertFalse(Frequency.FIVE.inRange(6000));
    }

    @Test
    public void testChannel() throws Exception {
        assertEquals(0, Frequency.TWO.channel(2400));
        assertEquals(0, Frequency.TWO.channel(2500));

        assertEquals(1, Frequency.TWO.channel(2401));
        assertEquals(1, Frequency.TWO.channel(2416));
        assertEquals(11, Frequency.TWO.channel(2462));
        assertEquals(11, Frequency.TWO.channel(2499));

        assertEquals(0, Frequency.FIVE.channel(5000));
        assertEquals(0, Frequency.FIVE.channel(6000));

        assertEquals(36, Frequency.FIVE.channel(5001));
        assertEquals(36, Frequency.FIVE.channel(5184));
        assertEquals(165, Frequency.FIVE.channel(5825));
        assertEquals(165, Frequency.FIVE.channel(5899));
    }

    @Test
    public void testBand() throws Exception {
        assertNull(Frequency.UNKNOWN.wiFiBand());
        assertEquals(WiFiBand.TWO, Frequency.TWO.wiFiBand());
        assertEquals(WiFiBand.FIVE, Frequency.FIVE.wiFiBand());
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(Frequency.UNKNOWN, Frequency.find(2400));
        assertEquals(Frequency.UNKNOWN, Frequency.find(2500));
        assertEquals(Frequency.UNKNOWN, Frequency.find(5000));
        assertEquals(Frequency.UNKNOWN, Frequency.find(6000));

        assertEquals(Frequency.TWO, Frequency.find(2401));
        assertEquals(Frequency.TWO, Frequency.find(2499));
        assertEquals(Frequency.FIVE, Frequency.find(5001));
        assertEquals(Frequency.FIVE, Frequency.find(5999));
    }

    @Test
    public void testFindChannels() throws Exception {
        assertTrue(Frequency.UNKNOWN.channels().isEmpty());
        assertSetEquals(expected24GHZChannels(), Frequency.TWO.channels());
        assertSetEquals(expected5GHZChannels(), Frequency.FIVE.channels());
    }

    @Test
    public void testFindChannel() throws Exception {
        assertEquals(1, Frequency.findChannel(2412));
    }

    private SortedSet<Integer> expected24GHZChannels() {
        SortedSet<Integer> expected = new TreeSet<>();
        for (int i = 1; i <= 11; i++) {
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
}