/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import java.util.List;

import static org.junit.Assert.assertEquals;

public class WiFiChannelCountryGHZ5Test {

    @Test
    public void testChannelsForUSAndCA() throws Exception {
        String[] countries = new String[]{"US", "CA"};
        for (String country : countries) {
            List<Integer> actual = WiFiChannelCountryGHZ5.INSTANCE.findChannels(country);
            assertEquals(25, actual.size());
        }
    }

    @Test
    public void testChannelsForEurope() throws Exception {
        String[] countries = new String[]{"GB", "DE", "SE"};
        for (String country : countries) {
            List<Integer> actual = WiFiChannelCountryGHZ5.INSTANCE.findChannels(country);
            assertEquals(20, actual.size());
        }
    }

    @Test
    public void testChannelsForUnknown() throws Exception {
        List<Integer> actual = WiFiChannelCountryGHZ5.INSTANCE.findChannels("XYZ");
        assertEquals(8, actual.size());
    }
}