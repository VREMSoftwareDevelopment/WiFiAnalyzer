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

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GroupByTest {
    private WiFiDetail wiFiDetail1;
    private WiFiDetail wiFiDetail2;

    @Before
    public void setUp() throws Exception {
        wiFiDetail1 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
                new WiFiSignal(2462, WiFiWidth.MHZ_20, -35), WiFiAdditional.EMPTY);
        wiFiDetail2 = new WiFiDetail("SSID2", "BSSID2", StringUtils.EMPTY,
                new WiFiSignal(2432, WiFiWidth.MHZ_20, -55), WiFiAdditional.EMPTY);
    }


    @Test
    public void testGroupByNumber() throws Exception {
        assertEquals(3, GroupBy.values().length);
    }

    @Test
    public void testFind() throws Exception {
        Assert.assertEquals(GroupBy.NONE, GroupBy.find(-1));
        assertEquals(GroupBy.NONE, GroupBy.find(3));

        assertEquals(GroupBy.NONE, GroupBy.find(GroupBy.NONE.ordinal()));
        assertEquals(GroupBy.SSID, GroupBy.find(GroupBy.SSID.ordinal()));
        assertEquals(GroupBy.CHANNEL, GroupBy.find(GroupBy.CHANNEL.ordinal()));
    }

    @Test
    public void testGroupBy() throws Exception {
        assertTrue(GroupBy.NONE.groupBy() instanceof GroupBy.None);
        assertTrue(GroupBy.SSID.groupBy() instanceof GroupBy.SSIDGroupBy);
        assertTrue(GroupBy.CHANNEL.groupBy() instanceof GroupBy.ChannelGroupBy);
    }

    @Test
    public void testSortOrder() throws Exception {
        assertTrue(GroupBy.NONE.sortOrder() instanceof GroupBy.None);
        assertTrue(GroupBy.SSID.sortOrder() instanceof GroupBy.SSIDSortOrder);
        assertTrue(GroupBy.CHANNEL.sortOrder() instanceof GroupBy.ChannelSortOrder);
    }

    @Test
    public void testNoneComparator() throws Exception {
        // setup
        GroupBy.None comparator = new GroupBy.None();
        // execute & validate
        assertEquals(0, comparator.compare(wiFiDetail1, wiFiDetail1));
        assertEquals(0, comparator.compare(wiFiDetail2, wiFiDetail2));
        assertEquals(1, comparator.compare(wiFiDetail1, wiFiDetail2));
        assertEquals(1, comparator.compare(wiFiDetail2, wiFiDetail1));
    }

    @Test
    public void testChannelGroupByComparator() throws Exception {
        // setup
        GroupBy.ChannelGroupBy comparator = new GroupBy.ChannelGroupBy();
        // execute & validate
        assertEquals(0, comparator.compare(wiFiDetail1, wiFiDetail1));
        assertEquals(1, comparator.compare(wiFiDetail1, wiFiDetail2));
        assertEquals(-1, comparator.compare(wiFiDetail2, wiFiDetail1));
    }

    @Test
    public void testChannelSortOrder() throws Exception {
        // setup
        GroupBy.ChannelSortOrder comparator = new GroupBy.ChannelSortOrder();
        // execute & validate
        assertEquals(0, comparator.compare(wiFiDetail1, wiFiDetail1));
        assertEquals(1, comparator.compare(wiFiDetail1, wiFiDetail2));
        assertEquals(-1, comparator.compare(wiFiDetail2, wiFiDetail1));
    }

    @Test
    public void testSSIDGroupByComparator() throws Exception {
        // setup
        GroupBy.SSIDGroupBy comparator = new GroupBy.SSIDGroupBy();
        // execute & validate
        assertEquals(0, comparator.compare(wiFiDetail1, wiFiDetail1));
        assertEquals(-1, comparator.compare(wiFiDetail1, wiFiDetail2));
        assertEquals(1, comparator.compare(wiFiDetail2, wiFiDetail1));
    }

    @Test
    public void testSSIDSortOrderComparatorEquals() throws Exception {
        GroupBy.SSIDSortOrder comparator = new GroupBy.SSIDSortOrder();
        // execute & validate
        assertEquals(0, comparator.compare(wiFiDetail1, wiFiDetail1));
        assertEquals(-1, comparator.compare(wiFiDetail1, wiFiDetail2));
        assertEquals(1, comparator.compare(wiFiDetail2, wiFiDetail1));
    }

}