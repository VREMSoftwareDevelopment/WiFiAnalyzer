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

import android.support.v4.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class WiFiChannelsGHZ5Test {
    private WiFiChannelsGHZ5 fixture;

    @Before
    public void setUp() {
        fixture = new WiFiChannelsGHZ5();
    }

    @Test
    public void testGetWiFiChannelByFrequency() throws Exception {
        assertEquals(36, fixture.getWiFiChannelByFrequency(5180).getChannel());
        assertEquals(165, fixture.getWiFiChannelByFrequency(5825).getChannel());
    }

    @Test
    public void testGetWiFiChannelByFrequencyFail() throws Exception {
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(5167));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(5828));
    }

    @Test
    public void testGetWiFiChannelFirst() throws Exception {
        assertEquals(8, fixture.getWiFiChannelFirst().getChannel());
    }

    @Test
    public void testGetWiFiChannelLast() throws Exception {
        assertEquals(196, fixture.getWiFiChannelLast().getChannel());
    }

    @Test
    public void testGetFrequencySpread() throws Exception {
        assertEquals(5, fixture.getFrequencySpread());
    }

    @Test
    public void testGetFrequencyOffset() throws Exception {
        assertEquals(20, fixture.getFrequencyOffset());
    }

    @Test
    public void testGetChannelOffset() throws Exception {
        assertEquals(4, fixture.getChannelOffset());
    }

    @Test
    public void testGetWiFiChannelPair() throws Exception {
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairFirst(Locale.JAPAN.getCountry());
        validatePair(8, 16, wiFiChannelPair);
    }

    @Test
    public void testGetWiFiChannelPairWithInvalidCountry() throws Exception {
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairFirst(null);
        validatePair(36, 64, wiFiChannelPair);
    }

    @Test
    public void testGetWiFiChannelPairs() throws Exception {
        List<Pair<WiFiChannel, WiFiChannel>> wiFiChannelPairs = fixture.getWiFiChannelPairs();
        assertEquals(5, wiFiChannelPairs.size());
        validatePair(8, 16, wiFiChannelPairs.get(0));
        validatePair(36, 64, wiFiChannelPairs.get(1));
        validatePair(100, 140, wiFiChannelPairs.get(2));
        validatePair(149, 165, wiFiChannelPairs.get(3));
        validatePair(184, 196, wiFiChannelPairs.get(4));
    }

    private void validatePair(int expectedFirst, int expectedSecond, Pair<WiFiChannel, WiFiChannel> pair) {
        assertEquals(expectedFirst, pair.first.getChannel());
        assertEquals(expectedSecond, pair.second.getChannel());
    }

    @Test
    public void testGetAvailableChannels() throws Exception {
        assertEquals(24, fixture.getAvailableChannels(Locale.US.getCountry()).size());
        assertEquals(19, fixture.getAvailableChannels(Locale.UK.getCountry()).size());
        assertEquals(30, fixture.getAvailableChannels(Locale.JAPAN.getCountry()).size());
    }

    @Test
    public void testGetWiFiChannelByFrequency5GHZ() throws Exception {
        // setup
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairs().get(1);
        // execute
        WiFiChannel actual = fixture.getWiFiChannelByFrequency(2000, wiFiChannelPair);
        // validate
        assertEquals(WiFiChannel.UNKNOWN, actual);
    }

    @Test
    public void testGetWiFiChannelByFrequency5GHZInRange() throws Exception {
        // setup
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairs().get(1);
        // execute
        WiFiChannel actual = fixture.getWiFiChannelByFrequency(wiFiChannelPair.first.getFrequency(), wiFiChannelPair);
        // validate
        assertEquals(wiFiChannelPair.first, actual);
    }

}