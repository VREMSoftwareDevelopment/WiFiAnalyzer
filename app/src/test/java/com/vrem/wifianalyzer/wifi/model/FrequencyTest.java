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

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FrequencyTest {
    @Test
    public void testFrequency() throws Exception {
        assertEquals(4, Frequency.values().length);
    }

    @Test
    public void testInRange() throws Exception {
        assertTrue(Frequency.UNKNOWN.inRange(0));
        assertFalse(Frequency.UNKNOWN.inRange(1));
        assertFalse(Frequency.UNKNOWN.inRange(-1));

        assertTrue(Frequency.TWO_POINT_FOUR.inRange(2412));
        assertTrue(Frequency.TWO_POINT_FOUR.inRange(2472));
        assertFalse(Frequency.TWO_POINT_FOUR.inRange(2411));
        assertFalse(Frequency.TWO_POINT_FOUR.inRange(2473));

        assertTrue(Frequency.TWO_POINT_FOUR_CH_14.inRange(2484));
        assertFalse(Frequency.TWO_POINT_FOUR_CH_14.inRange(2485));
        assertFalse(Frequency.TWO_POINT_FOUR_CH_14.inRange(2483));

        assertTrue(Frequency.FIVE.inRange(5170));
        assertTrue(Frequency.FIVE.inRange(5825));
        assertFalse(Frequency.FIVE.inRange(5169));
        assertFalse(Frequency.FIVE.inRange(5826));
    }

    @Test
    public void testChannel() throws Exception {
        assertEquals(0, Frequency.TWO_POINT_FOUR.channel(1000));

        assertEquals(1, Frequency.TWO_POINT_FOUR.channel(2412));
        assertEquals(13, Frequency.TWO_POINT_FOUR.channel(2472));

        assertEquals(14, Frequency.TWO_POINT_FOUR_CH_14.channel(2484));

        assertEquals(34, Frequency.FIVE.channel(5170));
        assertEquals(165, Frequency.FIVE.channel(5825));
    }

    @Test
    public void testBand() throws Exception {
        assertEquals(StringUtils.EMPTY, Frequency.UNKNOWN.band());
        assertEquals("2.4GHz", Frequency.TWO_POINT_FOUR.band());
        assertEquals("2.4GHz", Frequency.TWO_POINT_FOUR_CH_14.band());
        assertEquals("5GHz", Frequency.FIVE.band());
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(Frequency.UNKNOWN, Frequency.find(1000));

        assertEquals(Frequency.UNKNOWN, Frequency.find(0));
        assertEquals(Frequency.TWO_POINT_FOUR, Frequency.find(2412));
        assertEquals(Frequency.TWO_POINT_FOUR_CH_14, Frequency.find(2484));
        assertEquals(Frequency.FIVE, Frequency.find(5825));
    }

    @Test
    public void testFindChannel() throws Exception {
        assertEquals(1, Frequency.findChannel(2412));
    }
}