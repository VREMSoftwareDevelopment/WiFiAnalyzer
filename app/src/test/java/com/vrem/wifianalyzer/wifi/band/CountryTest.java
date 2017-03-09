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

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class CountryTest {
    private Country fixture;

    @Before
    public void setUp() {
        fixture = new Country();
    }

    @Test
    public void testGetCountries() throws Exception {
        // execute
        List<Locale> actual = fixture.getCountries();
        // validate
        assertTrue(actual.size() >= 2);
        assertTrue(actual.get(0).getCountry().compareTo(actual.get(actual.size() - 1).getCountry()) < 0);
    }

    @Test
    public void testGetCountry() throws Exception {
        // setup
        Locale expected = fixture.getCountries().get(0);
        // execute
        Locale actual = fixture.getCountry(expected.getCountry());
        // validate
        assertEquals(expected, actual);
        assertEquals(expected.getCountry(), actual.getCountry());
        assertEquals(expected.getDisplayCountry(), actual.getDisplayCountry());

        assertNotEquals(expected.getCountry(), expected.getDisplayCountry());
        assertNotEquals(actual.getCountry(), actual.getDisplayCountry());
    }

    @Test
    public void testGetCountryWithUnknownCode() throws Exception {
        // setup
        String countryCode = "WW";
        // execute
        Locale actual = fixture.getCountry(countryCode);
        // validate
        assertEquals(countryCode, actual.getCountry());
        assertEquals(countryCode, actual.getDisplayCountry());
    }

}
