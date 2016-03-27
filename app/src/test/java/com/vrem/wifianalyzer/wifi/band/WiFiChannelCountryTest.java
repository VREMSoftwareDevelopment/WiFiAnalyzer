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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WiFiChannelCountryTest {

    @Test
    public void testIsChannelAvailableWithTrue() throws Exception {
        assertTrue(WiFiChannelCountry.find(Locale.US).isChannelAvailableGHZ_2(1));
        assertTrue(WiFiChannelCountry.find(Locale.US).isChannelAvailableGHZ_2(11));

        assertTrue(WiFiChannelCountry.find(Locale.US).isChannelAvailableGHZ_5(36));
        assertTrue(WiFiChannelCountry.find(Locale.US).isChannelAvailableGHZ_5(165));

        assertTrue(WiFiChannelCountry.find(Locale.UK).isChannelAvailableGHZ_2(1));
        assertTrue(WiFiChannelCountry.find(Locale.UK).isChannelAvailableGHZ_2(13));

        assertTrue(WiFiChannelCountry.find(Locale.UK).isChannelAvailableGHZ_5(36));
        assertTrue(WiFiChannelCountry.find(Locale.UK).isChannelAvailableGHZ_5(140));
    }

    @Test
    public void testIsChannelAvailableWithGHZ_2() throws Exception {
        assertFalse(WiFiChannelCountry.find(Locale.US).isChannelAvailableGHZ_2(0));
        assertFalse(WiFiChannelCountry.find(Locale.US).isChannelAvailableGHZ_2(12));

        assertFalse(WiFiChannelCountry.find(Locale.UK).isChannelAvailableGHZ_2(0));
        assertFalse(WiFiChannelCountry.find(Locale.UK).isChannelAvailableGHZ_2(14));
    }

    @Test
    public void testIsChannelAvailableWithGHZ_5() throws Exception {
        assertTrue(WiFiChannelCountry.find(Locale.US).isChannelAvailableGHZ_5(36));
        assertTrue(WiFiChannelCountry.find(Locale.US).isChannelAvailableGHZ_5(165));

        assertTrue(WiFiChannelCountry.find(Locale.UK).isChannelAvailableGHZ_5(36));
        assertTrue(WiFiChannelCountry.find(Locale.UK).isChannelAvailableGHZ_5(140));

        assertTrue(WiFiChannelCountry.find(new Locale("EN", "AE")).isChannelAvailableGHZ_5(36));
        assertTrue(WiFiChannelCountry.find(new Locale("EN", "AE")).isChannelAvailableGHZ_5(64));
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(WiFiChannelCountry.UNKNOWN, WiFiChannelCountry.find(new Locale("EN", "WW")));
        assertEquals("US", WiFiChannelCountry.find(Locale.US).getCountryCode());
    }

}