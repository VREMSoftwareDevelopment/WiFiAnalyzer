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

import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class WiFiChannelCountryTest {

    @Test
    public void testIsChannelAvailableWithTrue() {
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
    public void testIsChannelAvailableWithGHZ2() {
        assertFalse(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ2(0));
        assertFalse(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ2(12));

        assertFalse(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ2(0));
        assertFalse(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ2(14));
    }

    @Test
    public void testIsChannelAvailableWithGHZ5() {
        assertTrue(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.get(Locale.US.getCountry()).isChannelAvailableGHZ5(165));

        assertTrue(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.get(Locale.UK.getCountry()).isChannelAvailableGHZ5(140));

        assertTrue(WiFiChannelCountry.get("AE").isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.get("AE").isChannelAvailableGHZ5(64));
    }

    @Test
    public void testGetCorrectlyPopulatesGHZ() {
        // setup
        String expectedCountryCode = Locale.US.getCountry();
        Set<Integer> expectedGHZ2 = new WiFiChannelCountryGHZ2().findChannels(expectedCountryCode);
        Set<Integer> expectedGHZ5 = new WiFiChannelCountryGHZ5().findChannels(expectedCountryCode);
        // execute
        WiFiChannelCountry actual = WiFiChannelCountry.get(expectedCountryCode);
        // validate
        assertEquals(expectedCountryCode, actual.getCountryCode());
        assertArrayEquals(expectedGHZ2.toArray(), actual.getChannelsGHZ2().toArray());
        assertArrayEquals(expectedGHZ5.toArray(), actual.getChannelsGHZ5().toArray());
    }

    @Test
    public void testGetCorrectlyPopulatesCountryCodeAndName() {
        // setup
        Locale expected = Locale.SIMPLIFIED_CHINESE;
        String expectedCountryCode = expected.getCountry();
        // execute
        WiFiChannelCountry actual = WiFiChannelCountry.get(expectedCountryCode);
        // validate
        assertEquals(expectedCountryCode, actual.getCountryCode());
        assertNotEquals(expected.getDisplayCountry(), actual.getCountryName(expected));
        assertEquals(expected.getDisplayCountry(expected), actual.getCountryName(expected));
    }

}