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

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import androidx.core.util.Pair;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WiFiChannelsGHZ2Test {

    private WiFiChannelsGHZ2 fixture;

    @Before
    public void setUp() {
        fixture = new WiFiChannelsGHZ2();
    }

    @Test
    public void testIsInRange() {
        assertTrue(fixture.isInRange(2400));
        assertTrue(fixture.isInRange(2499));
    }

    @Test
    public void testIsNotInRange() {
        assertFalse(fixture.isInRange(2399));
        assertFalse(fixture.isInRange(2500));
    }

    @Test
    public void testGetWiFiChannelByFrequency() {
        assertEquals(1, fixture.getWiFiChannelByFrequency(2410).getChannel());
        assertEquals(1, fixture.getWiFiChannelByFrequency(2412).getChannel());
        assertEquals(1, fixture.getWiFiChannelByFrequency(2414).getChannel());

        assertEquals(6, fixture.getWiFiChannelByFrequency(2437).getChannel());
        assertEquals(7, fixture.getWiFiChannelByFrequency(2442).getChannel());

        assertEquals(13, fixture.getWiFiChannelByFrequency(2470).getChannel());
        assertEquals(13, fixture.getWiFiChannelByFrequency(2472).getChannel());
        assertEquals(13, fixture.getWiFiChannelByFrequency(2474).getChannel());

        assertEquals(14, fixture.getWiFiChannelByFrequency(2482).getChannel());
        assertEquals(14, fixture.getWiFiChannelByFrequency(2484).getChannel());
        assertEquals(14, fixture.getWiFiChannelByFrequency(2486).getChannel());
    }

    @Test
    public void testGetWiFiChannelByFrequencyNotFound() {
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2399));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2409));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2481));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2481));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2487));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2500));
    }

    @Test
    public void testGetWiFiChannelByChannel() {
        assertEquals(2412, fixture.getWiFiChannelByChannel(1).getFrequency());
        assertEquals(2437, fixture.getWiFiChannelByChannel(6).getFrequency());
        assertEquals(2442, fixture.getWiFiChannelByChannel(7).getFrequency());
        assertEquals(2472, fixture.getWiFiChannelByChannel(13).getFrequency());
        assertEquals(2484, fixture.getWiFiChannelByChannel(14).getFrequency());
    }

    @Test
    public void testGetWiFiChannelByChannelNotFound() {
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByChannel(0));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByChannel(15));
    }

    @Test
    public void testGetWiFiChannelFirst() {
        assertEquals(1, fixture.getWiFiChannelFirst().getChannel());
    }

    @Test
    public void testGetWiFiChannelLast() {
        assertEquals(14, fixture.getWiFiChannelLast().getChannel());
    }

    @Test
    public void testGetWiFiChannelPairs() {
        List<Pair<WiFiChannel, WiFiChannel>> pair = fixture.getWiFiChannelPairs();
        assertEquals(1, pair.size());
        validatePair(1, 14, pair.get(0));
    }

    @Test
    public void testGetWiFiChannelPair() {
        validatePair(1, 14, fixture.getWiFiChannelPairFirst(Locale.US.getCountry()));
        validatePair(1, 14, fixture.getWiFiChannelPairFirst(null));
    }

    private void validatePair(int expectedFirst, int expectedSecond, Pair<WiFiChannel, WiFiChannel> pair) {
        assertEquals(expectedFirst, pair.first.getChannel());
        assertEquals(expectedSecond, pair.second.getChannel());
    }

    @Test
    public void testGetAvailableChannels() {
        assertEquals(11, fixture.getAvailableChannels(Locale.US.getCountry()).size());
        assertEquals(13, fixture.getAvailableChannels(Locale.UK.getCountry()).size());
    }

    @Test
    public void testGetWiFiChannelByFrequency2GHZ() {
        // setup
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairs().get(0);
        // execute
        WiFiChannel actual = fixture.getWiFiChannelByFrequency(2000, wiFiChannelPair);
        // validate
        assertEquals(WiFiChannel.UNKNOWN, actual);
    }

    @Test
    public void testGetWiFiChannelByFrequency2GHZInRange() {
        // setup
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairs().get(0);
        // execute
        WiFiChannel actual = fixture.getWiFiChannelByFrequency(wiFiChannelPair.first.getFrequency(), wiFiChannelPair);
        // validate
        assertEquals(wiFiChannelPair.first, actual);
    }

}