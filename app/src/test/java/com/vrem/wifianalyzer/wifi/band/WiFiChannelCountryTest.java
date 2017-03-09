/*
 * WiFiAnalyzer
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

import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WiFiChannelCountryTest {

    @Test
    public void testIsChannelAvailableWithTrue() throws Exception {
        assertTrue(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ2(1));
        assertTrue(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ2(11));

        assertTrue(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ5(165));

        assertTrue(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ2(1));
        assertTrue(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ2(13));

        assertTrue(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ5(140));
    }

    @Test
    public void testIsChannelAvailableWithGHZ2() throws Exception {
        assertFalse(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ2(0));
        assertFalse(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ2(12));

        assertFalse(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ2(0));
        assertFalse(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ2(14));
    }

    @Test
    public void testIsChannelAvailableWithGHZ5() throws Exception {
        assertTrue(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ5(165));

        assertTrue(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ5(140));

        assertTrue(WiFiChannelCountry.get("AE").isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.get("AE").isChannelAvailableGHZ5(64));
    }

    @Test
    public void testFind() throws Exception {
        assertEquals("US", WiFiChannelCountry.get(Locale.US.getCountry()).getCountryCode());
    }

    @Test
    public void testFindFailes() throws Exception {
        // setup
        String countryCode = "11";
        Set<Integer> expectedGHZ2 = new WiFiChannelCountryGHZ2().findChannels(countryCode);
        Set<Integer> expectedGHZ5 = new WiFiChannelCountryGHZ5().findChannels(countryCode);
        // execute
        WiFiChannelCountry actual = WiFiChannelCountry.get(countryCode);
        // validate
        assertEquals(countryCode, actual.getCountryCode());
        assertEquals(countryCode + WiFiChannelCountry.UNKNOWN, actual.getCountryName());
        assertArrayEquals(expectedGHZ2.toArray(), actual.getChannelsGHZ2().toArray());
        assertArrayEquals(expectedGHZ5.toArray(), actual.getChannelsGHZ5().toArray());
    }

}