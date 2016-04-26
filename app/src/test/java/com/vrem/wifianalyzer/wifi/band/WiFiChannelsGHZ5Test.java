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

import android.support.v4.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class WiFiChannelsGHZ5Test {
    private WiFiChannelsGHZ5 fixture;

    @Before
    public void setUp() throws Exception {
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