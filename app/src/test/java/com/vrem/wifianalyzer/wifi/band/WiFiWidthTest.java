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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WiFiWidthTest {

    @Test
    public void testWiFiWidth() {
        assertEquals(5, WiFiWidth.values().length);
    }

    @Test
    public void testGetFrequencyWidth() {
        assertEquals(20, WiFiWidth.MHZ_20.getFrequencyWidth());
        assertEquals(40, WiFiWidth.MHZ_40.getFrequencyWidth());
        assertEquals(80, WiFiWidth.MHZ_80.getFrequencyWidth());
        assertEquals(160, WiFiWidth.MHZ_160.getFrequencyWidth());
        assertEquals(80, WiFiWidth.MHZ_80_PLUS.getFrequencyWidth());
    }

    @Test
    public void testGetFrequencyHalfWidth() {
        assertEquals(10, WiFiWidth.MHZ_20.getFrequencyWidthHalf());
        assertEquals(20, WiFiWidth.MHZ_40.getFrequencyWidthHalf());
        assertEquals(40, WiFiWidth.MHZ_80.getFrequencyWidthHalf());
        assertEquals(80, WiFiWidth.MHZ_160.getFrequencyWidthHalf());
        assertEquals(40, WiFiWidth.MHZ_80_PLUS.getFrequencyWidthHalf());
    }

}