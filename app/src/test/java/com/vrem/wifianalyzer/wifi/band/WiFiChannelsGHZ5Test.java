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

public class WiFiChannelsGHZ5Test {
    private WiFiChannelsGHZ5 fixture;

    @Before
    public void setUp() {
        fixture = new WiFiChannelsGHZ5();
    }

    @Test
    public void testGetWiFiChannelByFrequency() {
        validateFrequencyToChannel(5180, 5320, 10, 36, 2);
        validateFrequencyToChannel(5500, 5720, 10, 100, 2);
        validateFrequencyToChannel(5745, 5825, 10, 149, 2);
    }

    private void validateFrequencyToChannel(int frequencyStart, int frequencyEnd, int frequencyIncrement, int channelStart, int channelIncrement) {
        int channel = channelStart;
        for (int frequency = frequencyStart; frequency <= frequencyEnd; frequency += frequencyIncrement) {
            assertEquals(channel, fixture.getWiFiChannelByFrequency(frequency).getChannel());
            channel += channelIncrement;
        }
    }

    @Test
    public void testGetWiFiChannelByFrequencyFail() {
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(5167));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(5828));
    }

    @Test
    public void testGetWiFiChannelFirst() {
        assertEquals(36, fixture.getWiFiChannelFirst().getChannel());
    }

    @Test
    public void testGetWiFiChannelLast() {
        assertEquals(165, fixture.getWiFiChannelLast().getChannel());
    }

    @Test
    public void testGetWiFiChannelPair() {
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairFirst(Locale.JAPAN.getCountry());
        validatePair(36, 64, wiFiChannelPair);
    }

    @Test
    public void testGetWiFiChannelPairWithInvalidCountry() {
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairFirst(null);
        validatePair(36, 64, wiFiChannelPair);
    }

    @Test
    public void testGetWiFiChannelPairs() {
        List<Pair<WiFiChannel, WiFiChannel>> wiFiChannelPairs = fixture.getWiFiChannelPairs();
        assertEquals(3, wiFiChannelPairs.size());
        validatePair(36, 64, wiFiChannelPairs.get(0));
        validatePair(100, 144, wiFiChannelPairs.get(1));
        validatePair(149, 165, wiFiChannelPairs.get(2));
    }

    private void validatePair(int expectedFirst, int expectedSecond, Pair<WiFiChannel, WiFiChannel> pair) {
        assertEquals(expectedFirst, pair.first.getChannel());
        assertEquals(expectedSecond, pair.second.getChannel());
    }

    @Test
    public void testGetWiFiChannelByFrequency5GHZ() {
        // setup
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairs().get(1);
        // execute
        WiFiChannel actual = fixture.getWiFiChannelByFrequency(2000, wiFiChannelPair);
        // validate
        assertEquals(WiFiChannel.UNKNOWN, actual);
    }

    @Test
    public void testGetWiFiChannelByFrequency5GHZInRange() {
        // setup
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairs().get(1);
        // execute
        WiFiChannel actual = fixture.getWiFiChannelByFrequency(wiFiChannelPair.first.getFrequency(), wiFiChannelPair);
        // validate
        assertEquals(wiFiChannelPair.first, actual);
    }

}