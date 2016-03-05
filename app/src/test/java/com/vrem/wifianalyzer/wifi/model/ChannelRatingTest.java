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

import static org.junit.Assert.assertEquals;

public class ChannelRatingTest {
    private WiFiDetail wiFiDetail1;
    private WiFiDetail wiFiDetail2;
    private WiFiDetail wiFiDetail3;

    private ChannelRating fixture;

    @Before
    public void setUp() throws Exception {
        wiFiDetail1 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY, new WiFiSignal(2445, -75), WiFiAdditional.EMPTY);
        wiFiDetail2 = new WiFiDetail("SSID2", "BSSID2", StringUtils.EMPTY, new WiFiSignal(2435, -55), WiFiAdditional.EMPTY);
        wiFiDetail3 = new WiFiDetail("SSID3", "BSSID3", StringUtils.EMPTY, new WiFiSignal(2455, -35), WiFiAdditional.EMPTY);
        fixture = new ChannelRating();
        fixture.setWiFiChannels(Arrays.asList(wiFiDetail1, wiFiDetail2, wiFiDetail3));
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
    public void testGetCountChannel1() throws Exception {
        // setup
        int channel = wiFiDetail1.getWiFiSignal().getChannel();
        // execute & validate
        assertEquals(3, fixture.getCount(channel));
    }

    @Test
    public void testGetCountChannel2() throws Exception {
        // setup
        int channel = wiFiDetail2.getWiFiSignal().getChannel();
        // execute & validate
        assertEquals(2, fixture.getCount(channel));
    }

    @Test
    public void testGetCountChannel3() throws Exception {
        // setup
        int channel = wiFiDetail3.getWiFiSignal().getChannel();
        // execute & validate
        assertEquals(2, fixture.getCount(channel));
    }

    @Test
    public void testGetStrengthShouldReturnMaximum() throws Exception {
        // setup
        int channel = wiFiDetail1.getWiFiSignal().getChannel();
        // execute & validate
        assertEquals(wiFiDetail3.getWiFiSignal().getStrength(), fixture.getStrength(channel));
    }

}