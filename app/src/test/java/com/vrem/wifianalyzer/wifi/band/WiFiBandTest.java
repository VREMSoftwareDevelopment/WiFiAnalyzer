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

import com.vrem.wifianalyzer.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WiFiBandTest {
    @Test
    public void testWiFiBand() {
        assertEquals(2, WiFiBand.values().length);
    }

    @Test
    public void testGetTextResource() {
        assertEquals(R.string.wifi_band_2ghz, WiFiBand.GHZ2.getTextResource());
        assertEquals(R.string.wifi_band_5ghz, WiFiBand.GHZ5.getTextResource());
    }

    @Test
    public void testToggle() {
        assertEquals(WiFiBand.GHZ5, WiFiBand.GHZ2.toggle());
        assertEquals(WiFiBand.GHZ2, WiFiBand.GHZ5.toggle());
    }

    @Test
    public void testIsGHZ_5() {
        assertFalse(WiFiBand.GHZ2.isGHZ5());
        assertTrue(WiFiBand.GHZ5.isGHZ5());
    }

}