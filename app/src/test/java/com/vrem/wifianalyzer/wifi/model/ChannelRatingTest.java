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
        wiFiDetail1 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY, new WiFiSignal(2445, -75), WiFiAdditional.EMPTY);
        wiFiDetail2 = new WiFiDetail("SSID2", "BSSID2", StringUtils.EMPTY, new WiFiSignal(2435, -55), WiFiAdditional.EMPTY);
        wiFiDetail3 = new WiFiDetail("SSID3", "BSSID3", StringUtils.EMPTY, new WiFiSignal(2455, -35), WiFiAdditional.EMPTY);
        wiFiDetail4 = new WiFiDetail("SSID4", "BSSID4", StringUtils.EMPTY, new WiFiSignal(2435, -35), new WiFiAdditional(StringUtils.EMPTY, "192.168.1.1"));
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
        WiFiSignal wiFiSignal = wiFiDetail1.getWiFiSignal();
        System.out.println(wiFiDetail1.getSSID() + " S:" + wiFiSignal.getChannelStart() + " M:" + wiFiSignal.getChannel() + " E:" + wiFiSignal.getChannelEnd());
        assertEquals(2, fixture.getCount(wiFiSignal.getChannelStart()));
        assertEquals(3, fixture.getCount(wiFiSignal.getChannel()));
        assertEquals(2, fixture.getCount(wiFiSignal.getChannelEnd()));
    }

    @Test
    public void testGetCountChannelWithWiFiDetail2() throws Exception {
        WiFiSignal wiFiSignal = wiFiDetail2.getWiFiSignal();
        System.out.println(wiFiDetail2.getSSID() + " S:" + wiFiSignal.getChannelStart() + " M:" + wiFiSignal.getChannel() + " E:" + wiFiSignal.getChannelEnd());
        assertEquals(1, fixture.getCount(wiFiSignal.getChannelStart()));
        assertEquals(2, fixture.getCount(wiFiSignal.getChannel()));
        assertEquals(3, fixture.getCount(wiFiSignal.getChannelEnd()));
    }

    @Test
    public void testGetCountChannelWithWiFiDetail3() throws Exception {
        WiFiSignal wiFiSignal = wiFiDetail3.getWiFiSignal();
        System.out.println(wiFiDetail3.getSSID() + " S:" + wiFiSignal.getChannelStart() + " M:" + wiFiSignal.getChannel() + " E:" + wiFiSignal.getChannelEnd());
        assertEquals(3, fixture.getCount(wiFiSignal.getChannelStart()));
        assertEquals(2, fixture.getCount(wiFiSignal.getChannel()));
        assertEquals(1, fixture.getCount(wiFiSignal.getChannelEnd()));
    }

    @Test
    public void testGetStrengthShouldReturnMaximum() throws Exception {
        // setup
        int channel = wiFiDetail1.getWiFiSignal().getChannel();
        // execute & validate
        assertEquals(wiFiDetail3.getWiFiSignal().getStrength(), fixture.getStrength(channel));
    }

}