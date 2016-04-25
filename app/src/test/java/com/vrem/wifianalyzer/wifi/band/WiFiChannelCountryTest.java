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

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WiFiChannelCountryTest {

    @Test
    public void testIsChannelAvailableWithTrue() throws Exception {
        assertTrue(WiFiChannelCountry.find(Locale.US.getCountry()).isChannelAvailableGHZ2(1));
        assertTrue(WiFiChannelCountry.find(Locale.US.getCountry()).isChannelAvailableGHZ2(11));

        assertTrue(WiFiChannelCountry.find(Locale.US.getCountry()).isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.find(Locale.US.getCountry()).isChannelAvailableGHZ5(165));

        assertTrue(WiFiChannelCountry.find(Locale.UK.getCountry()).isChannelAvailableGHZ2(1));
        assertTrue(WiFiChannelCountry.find(Locale.UK.getCountry()).isChannelAvailableGHZ2(13));

        assertTrue(WiFiChannelCountry.find(Locale.UK.getCountry()).isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.find(Locale.UK.getCountry()).isChannelAvailableGHZ5(140));
    }

    @Test
    public void testIsChannelAvailableWithGHZ2() throws Exception {
        assertFalse(WiFiChannelCountry.find(Locale.US.getCountry()).isChannelAvailableGHZ2(0));
        assertFalse(WiFiChannelCountry.find(Locale.US.getCountry()).isChannelAvailableGHZ2(12));

        assertFalse(WiFiChannelCountry.find(Locale.UK.getCountry()).isChannelAvailableGHZ2(0));
        assertFalse(WiFiChannelCountry.find(Locale.UK.getCountry()).isChannelAvailableGHZ2(14));
    }

    @Test
    public void testIsChannelAvailableWithGHZ5() throws Exception {
        assertTrue(WiFiChannelCountry.find(Locale.US.getCountry()).isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.find(Locale.US.getCountry()).isChannelAvailableGHZ5(165));

        assertTrue(WiFiChannelCountry.find(Locale.UK.getCountry()).isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.find(Locale.UK.getCountry()).isChannelAvailableGHZ5(140));

        assertTrue(WiFiChannelCountry.find("AE").isChannelAvailableGHZ5(36));
        assertTrue(WiFiChannelCountry.find("AE").isChannelAvailableGHZ5(64));
    }

    @Test
    public void testFind() throws Exception {
        assertEquals("US", WiFiChannelCountry.find(Locale.US.getCountry()).getCountryCode());
    }

    @Test
    public void testFindFailes() throws Exception {
        // setup
        String countryCode = "11";
        // execute
        WiFiChannelCountry actual = WiFiChannelCountry.find(countryCode);
        // validate
        assertEquals(countryCode, actual.getCountryCode());
        assertEquals(WiFiChannelCountry.UNKNOWN, actual.getCountryName());
        assertArrayEquals(WiFiChannelCountry.DEFAULT_CHANNELS_GHZ2.toArray(), actual.getChannelsGHZ2().toArray());
        assertArrayEquals(WiFiChannelCountry.DEFAULT_CHANNELS_GHZ5.toArray(), actual.getChannelsGHZ5().toArray());
    }

}