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

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WiFiChannelCountryGHZ2Test {

    private final static SortedSet<Integer> CHANNELS_SET1 = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
    private final static SortedSet<Integer> CHANNELS_SET2 = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));

    private WiFiChannelCountryGHZ2 fixture;

    @Before
    public void setUp() {
        fixture = new WiFiChannelCountryGHZ2();
    }

    @Test
    public void testChannelsForUSAndSimilar() {
        List<String> countries = Arrays.asList("AS", "CA", "CO", "DO", "FM", "GT", "GU", "MP", "MX", "PA", "PR", "UM", "US", "UZ", "VI");
        IterableUtils.forEach(countries, new ChannelUSClosure());
    }

    @Test
    public void testChannelsForWorld() {
        List<String> countries = Arrays.asList(null, "GB", "XYZ", "AU", "AE");
        IterableUtils.forEach(countries, new ChannelWorldClosure());
    }

    private void validateChannels(SortedSet<Integer> expected, SortedSet<Integer> actual) {
        assertEquals(expected.size(), actual.size());
        assertTrue(actual.containsAll(expected));
    }

    private class ChannelUSClosure implements Closure<String> {
        @Override
        public void execute(String country) {
            validateChannels(CHANNELS_SET1, fixture.findChannels(country));
        }
    }

    private class ChannelWorldClosure implements Closure<String> {
        @Override
        public void execute(String country) {
            validateChannels(CHANNELS_SET2, fixture.findChannels(country));
        }
    }
}