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

package com.vrem.wifianalyzer.wifi.model;

import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiChannel;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

import static org.junit.Assert.assertEquals;

public class ChannelRatingTest {
    private WiFiDetail wiFiDetail1;
    private WiFiDetail wiFiDetail2;
    private WiFiDetail wiFiDetail3;
    private WiFiDetail wiFiDetail4;
    private ChannelRating fixture;

    @Before
    public void setUp() {
        WiFiConnection wiFiConnection = new WiFiConnection("SSID1", "20:cf:30:ce:1d:71", "192.168.1.15", 11);
        wiFiDetail1 = new WiFiDetail("SSID1", "20:cf:30:ce:1d:71", StringUtils.EMPTY,
            new WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -50, true),
            new WiFiAdditional(StringUtils.EMPTY, wiFiConnection));
        wiFiDetail2 = new WiFiDetail("SSID2", "58:6d:8f:fa:ae:c0", StringUtils.EMPTY,
            new WiFiSignal(2442, 2442, WiFiWidth.MHZ_20, -70, true),
            WiFiAdditional.EMPTY);
        wiFiDetail3 = new WiFiDetail("SSID3", "84:94:8c:9d:40:68", StringUtils.EMPTY,
            new WiFiSignal(2452, 2452, WiFiWidth.MHZ_20, -60, true),
            WiFiAdditional.EMPTY);
        wiFiDetail4 = new WiFiDetail("SSID3", "64:A4:8c:90:10:12", StringUtils.EMPTY,
            new WiFiSignal(2452, 2452, WiFiWidth.MHZ_20, -80, true),
            WiFiAdditional.EMPTY);
        fixture = new ChannelRating();
    }

    @Test
    public void testChannelRating() {
        // setup
        WiFiChannel wiFiChannel = wiFiDetail1.getWiFiSignal().getCenterWiFiChannel();
        // execute & validate
        assertEquals(0, fixture.getCount(wiFiChannel));
        assertEquals(Strength.ZERO, fixture.getStrength(wiFiChannel));
    }

    @Test
    public void testGetCount() {
        // setup
        fixture.setWiFiDetails(Arrays.asList(wiFiDetail1, wiFiDetail2, wiFiDetail3, wiFiDetail4));
        // execute and validate
        validateCount(2, wiFiDetail1.getWiFiSignal().getCenterWiFiChannel());
        validateCount(4, wiFiDetail2.getWiFiSignal().getCenterWiFiChannel());
        validateCount(3, wiFiDetail3.getWiFiSignal().getCenterWiFiChannel());
    }

    private void validateCount(int expected, @NonNull WiFiChannel wiFiChannel) {
        assertEquals(expected, fixture.getCount(wiFiChannel));
    }

    @Test
    public void testGetStrengthShouldReturnMaximum() {
        // setup
        WiFiDetail other = makeCopy(wiFiDetail3);
        fixture.setWiFiDetails(Arrays.asList(other, wiFiDetail3));
        Strength expected = wiFiDetail3.getWiFiSignal().getStrength();
        // execute
        Strength actual = fixture.getStrength(wiFiDetail3.getWiFiSignal().getCenterWiFiChannel());
        // execute and validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStrengthWithConnected() {
        // setup
        WiFiDetail other = makeCopy(wiFiDetail1);
        fixture.setWiFiDetails(Arrays.asList(other, wiFiDetail1));
        Strength expected = other.getWiFiSignal().getStrength();
        // execute
        Strength actual = fixture.getStrength(wiFiDetail1.getWiFiSignal().getCenterWiFiChannel());
        // execute and validate
        assertEquals(expected, actual);
    }

    private WiFiDetail makeCopy(WiFiDetail wiFiDetail) {
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        return new WiFiDetail("SSID2-OTHER", "BSSID-OTHER", StringUtils.EMPTY,
            new WiFiSignal(wiFiSignal.getPrimaryFrequency(), wiFiSignal.getCenterFrequency(), wiFiSignal.getWiFiWidth(), -80, true),
            WiFiAdditional.EMPTY);
    }

    @Test
    public void testGetBestChannelsSortedInOrderWithMinimumChannels() {
        // setup
        List<WiFiChannel> channels = WiFiBand.GHZ2.getWiFiChannels().getWiFiChannels();
        fixture.setWiFiDetails(Arrays.asList(wiFiDetail1, wiFiDetail2, wiFiDetail3, wiFiDetail4));
        // execute
        List<ChannelAPCount> actual = fixture.getBestChannels(channels);
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

    private void validateChannelAPCount(int expectedChannel, int expectedCount, ChannelAPCount channelAPCount) {
        assertEquals(expectedChannel, channelAPCount.getWiFiChannel().getChannel());
        assertEquals(expectedCount, channelAPCount.getCount());
    }

    @Test
    public void testSetWiFiChannelsRemovesGuestAccessPoint() {
        // setup
        WiFiDetail wiFiDetailGuest = new WiFiDetail("SSID2", "22:cf:30:ce:1d:72", StringUtils.EMPTY,
            new WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -50 + ChannelRating.LEVEL_RANGE_MIN, true),
            WiFiAdditional.EMPTY);
        // execute
        fixture.setWiFiDetails(Collections.unmodifiableList(Arrays.asList(wiFiDetail1, wiFiDetailGuest)));
        // validate
        assertEquals(1, fixture.getWiFiDetails().size());
        assertEquals(wiFiDetail1, fixture.getWiFiDetails().get(0));
    }
}