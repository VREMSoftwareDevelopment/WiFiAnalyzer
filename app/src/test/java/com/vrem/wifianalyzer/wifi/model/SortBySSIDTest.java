/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class SortBySSIDTest {
    private Comparator<WiFiDetail> fixture;

    @Before
    public void setUp() {
        fixture = SortByKt.sortBySSID();
    }

    @Test
    public void testSortBySSID() {
        // setup
        WiFiDetail wiFiDetail1 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
            new WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
            WiFiAdditional.EMPTY);
        WiFiDetail wiFiDetail2 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
            new WiFiSignal(2462, 2432, WiFiWidth.MHZ_40, -55, false),
            WiFiAdditional.EMPTY);
        // execute
        int actual = fixture.compare(wiFiDetail1, wiFiDetail2);
        // validate
        assertEquals(0, actual);
    }

    @Test
    public void testSortBySSIDWithDifferentSSID() {
        // setup
        WiFiDetail wiFiDetail1 = new WiFiDetail("ssid1", "BSSID1", StringUtils.EMPTY,
            new WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
            WiFiAdditional.EMPTY);
        WiFiDetail wiFiDetail2 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
            new WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
            WiFiAdditional.EMPTY);
        // execute
        int actual = fixture.compare(wiFiDetail1, wiFiDetail2);
        // validate
        assertEquals(32, actual);
    }

    @Test
    public void testSortBySSIDWithDifferentBSSID() {
        // setup
        WiFiDetail wiFiDetail1 = new WiFiDetail("SSID1", "bssid1", StringUtils.EMPTY,
            new WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
            WiFiAdditional.EMPTY);
        WiFiDetail wiFiDetail2 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
            new WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
            WiFiAdditional.EMPTY);
        // execute
        int actual = fixture.compare(wiFiDetail1, wiFiDetail2);
        // validate
        assertEquals(32, actual);
    }

    @Test
    public void testSortBySSIDWithDifferentStrength() {
        // setup
        WiFiDetail wiFiDetail1 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
            new WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -55, true),
            WiFiAdditional.EMPTY);
        WiFiDetail wiFiDetail2 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
            new WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -35, true),
            WiFiAdditional.EMPTY);
        // execute
        int actual = fixture.compare(wiFiDetail1, wiFiDetail2);
        // validate
        assertEquals(1, actual);
    }

}