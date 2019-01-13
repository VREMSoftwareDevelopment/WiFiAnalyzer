/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.band;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class WiFiChannelTest {
    private static final int CHANNEL = 1;
    private static final int FREQUENCY = 200;

    private WiFiChannel fixture;
    private WiFiChannel other;

    @Before
    public void setUp() {
        fixture = new WiFiChannel(CHANNEL, FREQUENCY);
        other = new WiFiChannel(CHANNEL, FREQUENCY);
    }

    @Test
    public void testIsInRange() {
        assertTrue(fixture.isInRange(FREQUENCY));
        assertTrue(fixture.isInRange(FREQUENCY - 2));
        assertTrue(fixture.isInRange(FREQUENCY + 2));

        assertFalse(fixture.isInRange(FREQUENCY - 3));
        assertFalse(fixture.isInRange(FREQUENCY + 3));
    }

    @Test
    public void testEquals() {
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() {
        assertEquals(fixture.hashCode(), other.hashCode());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, fixture.compareTo(other));
    }
}