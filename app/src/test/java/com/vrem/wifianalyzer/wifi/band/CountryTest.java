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
    public void setUp() throws Exception {
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
