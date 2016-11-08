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

public class WiFiChannelCountryGHZ2Test {

    @Test
    public void testChannelsForJapan() throws Exception {
        List<Integer> actual = WiFiChannelCountryGHZ2.INSTANCE.findChannels("JP");
        assertEquals(14, actual.size());
    }

    @Test
    public void testChannelsForUSAndSimilar() throws Exception {
        String[] countries = new String[]{"AS", "AU", "CA", "FM", "GU", "MP", "PA", "PR", "TW", "UM", "US", "VI"};
        for (String country : countries) {
            List<Integer> actual = WiFiChannelCountryGHZ2.INSTANCE.findChannels(country);
            assertEquals(11, actual.size());
        }
    }

    @Test
    public void testChannelsForWorld() throws Exception {
        String[] countries = new String[]{null, "GB", "XYZ", "MX", "AE"};
        for (String country : countries) {
            List<Integer> actual = WiFiChannelCountryGHZ2.INSTANCE.findChannels(country);
            assertEquals(13, actual.size());
        }
    }
}