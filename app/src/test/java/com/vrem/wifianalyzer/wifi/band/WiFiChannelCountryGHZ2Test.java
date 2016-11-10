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

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WiFiChannelCountryGHZ2Test {

    private SortedSet<Integer> channelsSet1 = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
    private SortedSet<Integer> channelsSet2 = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
    private SortedSet<Integer> channelsSet3 = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14));

    @Test
    public void testChannelsForJapan() throws Exception {
        SortedSet<Integer> channels = WiFiChannelCountryGHZ2.INSTANCE.findChannels("JP");
        validateChannels(channelsSet3, channels);
    }

    @Test
    public void testChannelsForUSAndSimilar() throws Exception {
        String[] countries = new String[]{"AS", "AU", "CA", "FM", "GU", "MP", "PA", "PR", "TW", "UM", "US", "VI"};
        for (String country : countries) {
            SortedSet<Integer> channels = WiFiChannelCountryGHZ2.INSTANCE.findChannels(country);
            validateChannels(channelsSet1, channels);
        }
    }

    @Test
    public void testChannelsForWorld() throws Exception {
        String[] countries = new String[]{null, "GB", "XYZ", "MX", "AE"};
        for (String country : countries) {
            SortedSet<Integer> channels = WiFiChannelCountryGHZ2.INSTANCE.findChannels(country);
            validateChannels(channelsSet2, channels);
        }
    }

    private void validateChannels(SortedSet<Integer> expected, SortedSet<Integer> actual) {
        assertEquals(expected.size(), actual.size());
        assertTrue(actual.containsAll(expected));
    }



}