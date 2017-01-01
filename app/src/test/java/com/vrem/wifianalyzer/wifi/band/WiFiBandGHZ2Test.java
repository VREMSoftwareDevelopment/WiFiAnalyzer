/*
 * WiFi Analyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class WiFiBandGHZ2Test {

    @Test
    public void testGetBand() throws Exception {
        assertEquals("2.4 GHz", WiFiBand.GHZ2.getBand());
    }

    @Test
    public void testFindByBand() throws Exception {
        assertEquals(WiFiBand.GHZ2, WiFiBand.find(-1));
        assertEquals(WiFiBand.GHZ2, WiFiBand.find(WiFiBand.values().length));

        assertEquals(WiFiBand.GHZ2, WiFiBand.find(WiFiBand.GHZ2.ordinal()));
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(2400));
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(2500));
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(4899));
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(5901));
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(2401));
        assertEquals(WiFiBand.GHZ2, WiFiBand.findByFrequency(2499));
    }

    @Test
    public void testToggle() throws Exception {
        assertEquals(WiFiBand.GHZ5, WiFiBand.GHZ2.toggle());
    }

    @Test
    public void testIsGHZ_5() throws Exception {
        assertFalse(WiFiBand.GHZ2.isGHZ5());
    }
}