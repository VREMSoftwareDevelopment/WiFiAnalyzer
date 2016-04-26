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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WiFiChannelsGHZ2Test {

    private WiFiChannelsGHZ2 fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new WiFiChannelsGHZ2();
    }

    @Test
    public void testIsInRange() throws Exception {
        assertTrue(fixture.isInRange(2400));
        assertTrue(fixture.isInRange(2499));
    }

    @Test
    public void testIsNotInRange() throws Exception {
        assertFalse(fixture.isInRange(2399));
        assertFalse(fixture.isInRange(2500));
    }

    @Test
    public void testGetWiFiChannelByFrequency() throws Exception {
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
    public void testGetWiFiChannelByFrequencyNotFound() throws Exception {
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2399));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2409));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2481));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2481));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2487));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByFrequency(2500));
    }

    @Test
    public void testGetWiFiChannelByChannel() throws Exception {
        assertEquals(2412, fixture.getWiFiChannelByChannel(1).getFrequency());
        assertEquals(2437, fixture.getWiFiChannelByChannel(6).getFrequency());
        assertEquals(2442, fixture.getWiFiChannelByChannel(7).getFrequency());
        assertEquals(2472, fixture.getWiFiChannelByChannel(13).getFrequency());
        assertEquals(2484, fixture.getWiFiChannelByChannel(14).getFrequency());
    }

    @Test
    public void testGetWiFiChannelByChannelNotFound() throws Exception {
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByChannel(0));
        assertEquals(WiFiChannel.UNKNOWN, fixture.getWiFiChannelByChannel(15));
    }

    @Test
    public void testGetWiFiChannelFirst() throws Exception {
        assertEquals(1, fixture.getWiFiChannelFirst().getChannel());
    }

    @Test
    public void testGetFrequencySpread() throws Exception {
        assertEquals(5, fixture.getFrequencySpread());
    }

    @Test
    public void testGetFrequencyOffset() throws Exception {
        assertEquals(10, fixture.getFrequencyOffset());
    }

    @Test
    public void testGetChannelOffset() throws Exception {
        assertEquals(2, fixture.getChannelOffset());
    }

    @Test
    public void testGetWiFiChannelLast() throws Exception {
        assertEquals(14, fixture.getWiFiChannelLast().getChannel());
    }

    @Test
    public void testGetWiFiChannelPairs() throws Exception {
        List<Pair<WiFiChannel, WiFiChannel>> pair = fixture.getWiFiChannelPairs();
        assertEquals(1, pair.size());
        validatePair(1, 14, pair.get(0));
    }

    @Test
    public void testGetWiFiChannelPair() throws Exception {
        validatePair(1, 14, fixture.getWiFiChannelPairFirst(Locale.US.getCountry()));
        validatePair(1, 14, fixture.getWiFiChannelPairFirst(null));
    }

    private void validatePair(int expectedFirst, int expectedSecond, Pair<WiFiChannel, WiFiChannel> pair) {
        assertEquals(expectedFirst, pair.first.getChannel());
        assertEquals(expectedSecond, pair.second.getChannel());
    }

    @Test
    public void testGetAvailableChannels() throws Exception {
        assertEquals(11, fixture.getAvailableChannels(Locale.US.getCountry()).size());
        assertEquals(13, fixture.getAvailableChannels(Locale.UK.getCountry()).size());
        assertEquals(14, fixture.getAvailableChannels(Locale.JAPAN.getCountry()).size());
    }

    @Test
    public void testGetWiFiChannelByFrequency2GHZ() throws Exception {
        // setup
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairs().get(0);
        // execute
        WiFiChannel actual = fixture.getWiFiChannelByFrequency(2000, wiFiChannelPair);
        // validate
        assertEquals(WiFiChannel.UNKNOWN, actual);
    }

    @Test
    public void testGetWiFiChannelByFrequency2GHZInRange() throws Exception {
        // setup
        Pair<WiFiChannel, WiFiChannel> wiFiChannelPair = fixture.getWiFiChannelPairs().get(0);
        // execute
        WiFiChannel actual = fixture.getWiFiChannelByFrequency(wiFiChannelPair.first.getFrequency(), wiFiChannelPair);
        // validate
        assertEquals(wiFiChannelPair.first, actual);
    }

}