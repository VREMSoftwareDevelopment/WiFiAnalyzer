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

package com.vrem.wifianalyzer.wifi.model;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;

public class ChannelRatingTest {
    private WiFiDetail wiFiDetail1;
    private WiFiDetail wiFiDetail2;
    private WiFiDetail wiFiDetail3;
    private WiFiDetail wiFiDetail4;
    private List<WiFiDetail> wiFiDetails;
    private ChannelRating fixture;

    @Before
    public void setUp() throws Exception {
        wiFiDetail1 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
                new WiFiSignal(2445, WiFiWidth.MHZ_20, -70), WiFiAdditional.EMPTY);
        wiFiDetail2 = new WiFiDetail("SSID2", "BSSID2", StringUtils.EMPTY,
                new WiFiSignal(2435, WiFiWidth.MHZ_20, -80), WiFiAdditional.EMPTY);
        wiFiDetail3 = new WiFiDetail("SSID3", "BSSID3", StringUtils.EMPTY,
                new WiFiSignal(2455, WiFiWidth.MHZ_20, -60), WiFiAdditional.EMPTY);
        wiFiDetail4 = new WiFiDetail("SSID4", "BSSID4", StringUtils.EMPTY,
                new WiFiSignal(2435, WiFiWidth.MHZ_20, -50), new WiFiAdditional(StringUtils.EMPTY, "192.168.1.1"));
        wiFiDetails = Arrays.asList(wiFiDetail1, wiFiDetail2, wiFiDetail3, wiFiDetail4);
        fixture = new ChannelRating();
        fixture.setWiFiChannels(wiFiDetails);
    }

    @Test
    public void testChannelRating() throws Exception {
        // setup
        int channel = wiFiDetail1.getWiFiSignal().getChannel();
        fixture = new ChannelRating();
        // execute & validate
        assertEquals(0, fixture.getCount(channel));
        assertEquals(Strength.ZERO, fixture.getStrength(channel));
    }

    @Test
    public void testGetCountChannelWithWiFiDetail1() throws Exception {
        // setup
        WiFiSignal wiFiSignal = wiFiDetail1.getWiFiSignal();
        // execute & validate
        assertEquals(2, fixture.getCount(wiFiSignal.getChannelStart()));
        assertEquals(3, fixture.getCount(wiFiSignal.getChannel()));
        assertEquals(2, fixture.getCount(wiFiSignal.getChannelEnd()));
    }

    @Test
    public void testGetCountChannelWithWiFiDetail2() throws Exception {
        // setup
        WiFiSignal wiFiSignal = wiFiDetail2.getWiFiSignal();
        // execute & validate
        assertEquals(1, fixture.getCount(wiFiSignal.getChannelStart()));
        assertEquals(2, fixture.getCount(wiFiSignal.getChannel()));
        assertEquals(3, fixture.getCount(wiFiSignal.getChannelEnd()));
    }

    @Test
    public void testGetCountChannelWithWiFiDetail3() throws Exception {
        // setup
        WiFiSignal wiFiSignal = wiFiDetail3.getWiFiSignal();
        // execute & validate
        assertEquals(3, fixture.getCount(wiFiSignal.getChannelStart()));
        assertEquals(2, fixture.getCount(wiFiSignal.getChannel()));
        assertEquals(1, fixture.getCount(wiFiSignal.getChannelEnd()));
    }

    @Test
    public void testGetStrengthShouldReturnMaximum() throws Exception {
        assertEquals(wiFiDetail3.getWiFiSignal().getStrength(), fixture.getStrength(wiFiDetail1.getWiFiSignal().getChannel()));
    }

    @Test
    public void testGetBestChannelsSortedInOrderWithMinimumChannels() throws Exception {
        // setup
        SortedSet<Integer> channels = WiFiBand.GHZ_2.getChannels();
        // execute
        List<ChannelRating.ChannelAPCount> actual = fixture.getBestChannels(channels);
        // validate
        assertEquals(7, actual.size());
        validateChannelAPCount(1, 0, actual.get(0));
        validateChannelAPCount(2, 0, actual.get(1));
        validateChannelAPCount(12, 0, actual.get(2));
        validateChannelAPCount(13, 0, actual.get(3));
        validateChannelAPCount(14, 0, actual.get(4));
        validateChannelAPCount(3, 1, actual.get(5));
        validateChannelAPCount(4, 1, actual.get(6));
    }

    private void validateChannelAPCount(int expectedChannel, int expectedAPCount, ChannelRating.ChannelAPCount channelAPCount) {
        assertEquals(expectedChannel, channelAPCount.getChannel());
        assertEquals(expectedAPCount, channelAPCount.getApCount());
    }

}