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
import com.vrem.wifianalyzer.wifi.model.GroupBy.GroupBySSID;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static com.vrem.wifianalyzer.wifi.model.GroupBy.CHANNEL;
import static com.vrem.wifianalyzer.wifi.model.GroupBy.GroupByChannel;
import static com.vrem.wifianalyzer.wifi.model.GroupBy.NONE;
import static com.vrem.wifianalyzer.wifi.model.GroupBy.None;
import static com.vrem.wifianalyzer.wifi.model.GroupBy.SSID;
import static com.vrem.wifianalyzer.wifi.model.GroupBy.values;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GroupByTest {
    private WiFiDetail wiFiDetail1;
    private WiFiDetail wiFiDetail2;

    @Before
    public void setUp() {
        wiFiDetail1 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
            new WiFiSignal(2462, 2462, WiFiWidth.MHZ_20, -35, true),
            WiFiAdditional.EMPTY);
        wiFiDetail2 = new WiFiDetail("SSID2", "BSSID2", StringUtils.EMPTY,
            new WiFiSignal(2432, 2432, WiFiWidth.MHZ_20, -55, true),
            WiFiAdditional.EMPTY);
    }


    @Test
    public void testGroupByNumber() {
        assertEquals(3, values().length);
    }

    @Test
    public void testGroupBy() {
        assertTrue(NONE.groupByComparator() instanceof None);
        assertTrue(SSID.groupByComparator() instanceof GroupBySSID);
        assertTrue(CHANNEL.groupByComparator() instanceof GroupByChannel);
    }

    @Test
    public void testSortBy() {
        assertTrue(NONE.sortByComparator() instanceof None);
        assertTrue(SSID.sortByComparator() instanceof SortBySSID);
        assertTrue(CHANNEL.sortByComparator() instanceof SortByChannel);
    }

    @Test
    public void testNone() {
        // setup
        None fixture = new None();
        // execute & validate
        assertEquals(0, fixture.compare(wiFiDetail1, wiFiDetail1));
        assertEquals(-1, fixture.compare(wiFiDetail1, wiFiDetail2));
        assertEquals(1, fixture.compare(wiFiDetail2, wiFiDetail1));
    }

    @Test
    public void testGroupByChannel() {
        // setup
        GroupByChannel fixture = new GroupByChannel();
        // execute & validate
        assertEquals(0, fixture.compare(wiFiDetail1, wiFiDetail1));
        assertEquals(1, fixture.compare(wiFiDetail1, wiFiDetail2));
        assertEquals(-1, fixture.compare(wiFiDetail2, wiFiDetail1));
    }

    @Test
    public void testGroupBySSID() {
        // setup
        GroupBySSID fixture = new GroupBySSID();
        // execute & validate
        assertEquals(0, fixture.compare(wiFiDetail1, wiFiDetail1));
        assertEquals(-1, fixture.compare(wiFiDetail1, wiFiDetail2));
        assertEquals(1, fixture.compare(wiFiDetail2, wiFiDetail1));
    }


}