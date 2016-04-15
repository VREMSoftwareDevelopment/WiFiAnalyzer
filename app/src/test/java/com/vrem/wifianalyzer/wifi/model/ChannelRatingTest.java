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

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;

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
    private ChannelRating fixture;

    @Before
    public void setUp() throws Exception {
        wiFiDetail1 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
                new WiFiSignal(2432, WiFiWidth.MHZ_20, -50), WiFiAdditional.EMPTY);
        wiFiDetail2 = new WiFiDetail("SSID2", "BSSID2", StringUtils.EMPTY,
                new WiFiSignal(2442, WiFiWidth.MHZ_20, -70), WiFiAdditional.EMPTY);
        wiFiDetail3 = new WiFiDetail("SSID3", "BSSID3", StringUtils.EMPTY,
                new WiFiSignal(2452, WiFiWidth.MHZ_20, -60), WiFiAdditional.EMPTY);
        fixture = new ChannelRating();
    }

    @Test
    public void testChannelRating() throws Exception {
        // setup
        WiFiChannel wiFiChannel = wiFiDetail1.getWiFiSignal().getWiFiChannel();
        // execute & validate
        assertEquals(0, fixture.getCount(wiFiChannel));
        assertEquals(Strength.ZERO, fixture.getStrength(wiFiChannel));
    }

    @Test
    public void testGetCount() throws Exception {
        // setup
        fixture.setWiFiChannels(Arrays.asList(wiFiDetail1, wiFiDetail2, wiFiDetail3));
        // execute and validate
        validateCount(2, wiFiDetail1.getWiFiSignal().getWiFiChannel());
        validateCount(3, wiFiDetail2.getWiFiSignal().getWiFiChannel());
        validateCount(2, wiFiDetail3.getWiFiSignal().getWiFiChannel());
    }

    private void validateCount(int expected, @NonNull WiFiChannel wiFiChannel) {
        assertEquals(expected, fixture.getCount(wiFiChannel));
    }

    @Test
    public void testGetStrengthShouldReturnMaximum() throws Exception {
        // setup
        WiFiDetail other = makeCopy(wiFiDetail1);
        fixture.setWiFiChannels(Arrays.asList(other, wiFiDetail1));
        Strength expected = wiFiDetail1.getWiFiSignal().getStrength();
        // execute
        Strength actual = fixture.getStrength(wiFiDetail1.getWiFiSignal().getWiFiChannel());
        // execute and validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStrengthWithConnected() throws Exception {
        // setup
        wiFiDetail1 = new WiFiDetail(wiFiDetail1, new WiFiAdditional(StringUtils.EMPTY, "192.168.1.1", 11));
        WiFiDetail other = makeCopy(wiFiDetail1);
        fixture.setWiFiChannels(Arrays.asList(other, wiFiDetail1));
        Strength expected = other.getWiFiSignal().getStrength();
        // execute
        Strength actual = fixture.getStrength(wiFiDetail1.getWiFiSignal().getWiFiChannel());
        // execute and validate
        assertEquals(expected, actual);
    }

    private WiFiDetail makeCopy(WiFiDetail wiFiDetail) {
        return new WiFiDetail("SSID2-OTHER", "BSSID-OTHER", StringUtils.EMPTY,
                new WiFiSignal(wiFiDetail.getWiFiSignal().getFrequency(), wiFiDetail.getWiFiSignal().getWiFiWidth(), -80),
                WiFiAdditional.EMPTY);
    }

    @Test
    public void testGetBestChannelsSortedInOrderWithMinimumChannels() throws Exception {
        // setup
        List<WiFiChannel> channels = WiFiBand.GHZ2.getWiFiChannels().getWiFiChannels();
        fixture.setWiFiChannels(Arrays.asList(wiFiDetail1, wiFiDetail2, wiFiDetail3));
        // execute
        List<ChannelRating.ChannelAPCount> actual = fixture.getBestChannels(channels);
        // validate
        assertEquals(5, actual.size());
        validateChannelAPCount(1, actual.get(0));
        validateChannelAPCount(2, actual.get(1));
        validateChannelAPCount(12, actual.get(2));
        validateChannelAPCount(13, actual.get(3));
        validateChannelAPCount(14, actual.get(4));
    }

    private void validateChannelAPCount(int expectedChannel, ChannelRating.ChannelAPCount channelAPCount) {
        assertEquals(expectedChannel, channelAPCount.getWiFiChannel().getChannel());
        assertEquals(0, channelAPCount.getCount());
    }

}