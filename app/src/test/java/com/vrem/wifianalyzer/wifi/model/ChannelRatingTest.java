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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChannelRatingTest {
    private WiFiDetail wiFiDetail1;
    private WiFiDetail wiFiDetail2;
    private WiFiDetail wiFiDetail3;
    private WiFiDetail wiFiDetail4;
    private ChannelRating fixture;

    @Before
    public void setUp() throws Exception {
        wiFiDetail1 = new WiFiDetail("SSID1", "20:cf:30:ce:1d:71", StringUtils.EMPTY,
            new WiFiSignal(2432, WiFiWidth.MHZ_20, -50), new WiFiAdditional(StringUtils.EMPTY, "192.168.1.1", 11));
        wiFiDetail2 = new WiFiDetail("SSID2", "58:6d:8f:fa:ae:c0", StringUtils.EMPTY,
            new WiFiSignal(2442, WiFiWidth.MHZ_20, -70), WiFiAdditional.EMPTY);
        wiFiDetail3 = new WiFiDetail("SSID3", "84:94:8c:9d:40:68", StringUtils.EMPTY,
            new WiFiSignal(2452, WiFiWidth.MHZ_20, -60), WiFiAdditional.EMPTY);
        wiFiDetail4 = new WiFiDetail("SSID3", "64:A4:8c:90:10:12", StringUtils.EMPTY,
            new WiFiSignal(2452, WiFiWidth.MHZ_20, -80), WiFiAdditional.EMPTY);
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
        fixture.setWiFiDetails(Arrays.asList(wiFiDetail1, wiFiDetail2, wiFiDetail3, wiFiDetail4));
        // execute and validate
        validateCount(2, wiFiDetail1.getWiFiSignal().getWiFiChannel());
        validateCount(4, wiFiDetail2.getWiFiSignal().getWiFiChannel());
        validateCount(3, wiFiDetail3.getWiFiSignal().getWiFiChannel());
    }

    private void validateCount(int expected, @NonNull WiFiChannel wiFiChannel) {
        assertEquals(expected, fixture.getCount(wiFiChannel));
    }

    @Test
    public void testGetStrengthShouldReturnMaximum() throws Exception {
        // setup
        WiFiDetail other = makeCopy(wiFiDetail3);
        fixture.setWiFiDetails(Arrays.asList(other, wiFiDetail3));
        Strength expected = wiFiDetail3.getWiFiSignal().getStrength();
        // execute
        Strength actual = fixture.getStrength(wiFiDetail3.getWiFiSignal().getWiFiChannel());
        // execute and validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStrengthWithConnected() throws Exception {
        // setup
        WiFiDetail other = makeCopy(wiFiDetail1);
        fixture.setWiFiDetails(Arrays.asList(other, wiFiDetail1));
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
        fixture.setWiFiDetails(Arrays.asList(wiFiDetail1, wiFiDetail2, wiFiDetail3, wiFiDetail4));
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

    private void validateChannelAPCount(int expectedChannel, int expectedCount, ChannelRating.ChannelAPCount channelAPCount) {
        assertEquals(expectedChannel, channelAPCount.getWiFiChannel().getChannel());
        assertEquals(expectedCount, channelAPCount.getCount());
    }

    @Test
    public void testSetWiFiChannelsRemovesGuestAccessPoint() throws Exception {
        // setup
        WiFiDetail wiFiDetailGuest = new WiFiDetail("SSID2", "22:cf:30:ce:1d:72", StringUtils.EMPTY,
            new WiFiSignal(2432, WiFiWidth.MHZ_20, -50 + ChannelRating.LEVEL_RANGE_MIN), WiFiAdditional.EMPTY);
        // execute
        fixture.setWiFiDetails(Collections.unmodifiableList(Arrays.asList(wiFiDetail1, wiFiDetailGuest)));
        // validate
        assertEquals(1, fixture.getWiFiDetails().size());
        assertEquals(wiFiDetail1, fixture.getWiFiDetails().get(0));
    }
}