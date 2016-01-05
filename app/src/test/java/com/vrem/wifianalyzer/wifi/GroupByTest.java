/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.wifi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GroupByTest {
    @Mock
    private DetailsInfo lhs;
    @Mock
    private DetailsInfo rhs;

    @Test
    public void testFind() throws Exception {
        assertEquals(GroupBy.NONE, GroupBy.find(null));
        assertEquals(GroupBy.NONE, GroupBy.find(""));
        assertEquals(GroupBy.NONE, GroupBy.find("xYz"));

        assertEquals(GroupBy.NONE, GroupBy.find(GroupBy.NONE.name()));
        assertEquals(GroupBy.NONE, GroupBy.find(GroupBy.NONE.name().toLowerCase()));

        assertEquals(GroupBy.SSID, GroupBy.find(GroupBy.SSID.name()));
        assertEquals(GroupBy.SSID, GroupBy.find(GroupBy.SSID.name().toLowerCase()));

        assertEquals(GroupBy.CHANNEL, GroupBy.find(GroupBy.CHANNEL.name()));
        assertEquals(GroupBy.CHANNEL, GroupBy.find(GroupBy.CHANNEL.name().toLowerCase()));
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
        GroupBy.None comparator = new GroupBy.None();

        assertEquals(0, comparator.compare(lhs, lhs));
        assertEquals(0, comparator.compare(rhs, rhs));
        assertEquals(1, comparator.compare(lhs, rhs));
        assertEquals(1, comparator.compare(rhs, lhs));
    }

    @Test
    public void testChannelGroupByComparator() throws Exception {
        validateChannelGroupBy(0, 0, 0);
        validateChannelGroupBy(-1, 0, 1);
        validateChannelGroupBy(1, 1, 0);

        verify(lhs, times(3)).getChannel();
        verify(rhs, times(3)).getChannel();
    }

    private void validateChannelGroupBy(int expected, int lhsValue, int rhsValue) {
        // setup
        GroupBy.ChannelGroupBy comparator = new GroupBy.ChannelGroupBy();
        when(lhs.getChannel()).thenReturn(lhsValue);
        when(rhs.getChannel()).thenReturn(rhsValue);
        // execute & validate
        assertEquals(expected, comparator.compare(lhs, rhs));
    }

    @Test
    public void testChannelSortOrderComparatorEquals() throws Exception {
        GroupBy.ChannelSortOrder comparator = new GroupBy.ChannelSortOrder();
        withDetailsInfo(lhs, "BSSID");
        withDetailsInfo(rhs, "BSSID");
        // execute & validate
        assertEquals(0, comparator.compare(lhs, rhs));
        verifyDetailsInfo(lhs);
        verifyDetailsInfo(rhs);
        verify(lhs).getChannel();
        verify(rhs).getChannel();
    }

    @Test
    public void testChannelSortOrderComparatorLess() throws Exception {
        GroupBy.ChannelSortOrder comparator = new GroupBy.ChannelSortOrder();
        withDetailsInfo(lhs, "BSSID");
        withDetailsInfo(rhs, "CSSID");
        // execute & validate
        assertEquals(-1, comparator.compare(lhs, rhs));
        verifyDetailsInfo(lhs);
        verifyDetailsInfo(rhs);
        verify(lhs).getChannel();
        verify(rhs).getChannel();
    }

    @Test
    public void testChannelSortOrderComparatorMore() throws Exception {
        GroupBy.ChannelSortOrder comparator = new GroupBy.ChannelSortOrder();
        withDetailsInfo(lhs, "BSSID");
        withDetailsInfo(rhs, "ASSID");
        // execute & validate
        assertEquals(1, comparator.compare(lhs, rhs));
        verifyDetailsInfo(lhs);
        verifyDetailsInfo(rhs);
        verify(lhs).getChannel();
        verify(rhs).getChannel();
    }

    private void withDetailsInfo(DetailsInfo detailsInfo, String BSSID) {
        when(detailsInfo.getChannel()).thenReturn(0);
        when(detailsInfo.getLevel()).thenReturn(0);
        when(detailsInfo.getSSID()).thenReturn("SSID");
        when(detailsInfo.getBSSID()).thenReturn(BSSID);
    }

    private void verifyDetailsInfo(DetailsInfo detailsInfo) {
        verify(detailsInfo).getLevel();
        verify(detailsInfo).getSSID();
        verify(detailsInfo).getBSSID();
    }

    @Test
    public void testSSIDGroupByComparator() throws Exception {
        validateSSIDGroupBy(0, "B-SSID", "B-SSID");
        validateSSIDGroupBy(-1, "B-SSID", "C-SSID");
        validateSSIDGroupBy(1, "B-SSID", "A-SSID");

        verify(lhs, times(3)).getSSID();
        verify(rhs, times(3)).getSSID();
    }

    private void validateSSIDGroupBy(int expected, String lhsValue, String rhsValue) {
        // setup
        GroupBy.SSIDGroupBy comparator = new GroupBy.SSIDGroupBy();
        when(lhs.getSSID()).thenReturn(lhsValue);
        when(rhs.getSSID()).thenReturn(rhsValue);
        // execute & validate
        assertEquals(expected, comparator.compare(lhs, rhs));
    }

    @Test
    public void testSSIDSortOrderComparatorEquals() throws Exception {
        GroupBy.SSIDSortOrder comparator = new GroupBy.SSIDSortOrder();
        withDetailsInfo(lhs, "BSSID");
        withDetailsInfo(rhs, "BSSID");
        // execute & validate
        assertEquals(0, comparator.compare(lhs, rhs));
        verifyDetailsInfo(lhs);
        verifyDetailsInfo(rhs);
    }

    @Test
    public void testSSIDSortOrderComparatorLess() throws Exception {
        GroupBy.SSIDSortOrder comparator = new GroupBy.SSIDSortOrder();
        withDetailsInfo(lhs, "BSSID");
        withDetailsInfo(rhs, "CSSID");
        // execute & validate
        assertEquals(-1, comparator.compare(lhs, rhs));
        verifyDetailsInfo(lhs);
        verifyDetailsInfo(rhs);
    }

    @Test
    public void testSSIDSortOrderComparatorMore() throws Exception {
        GroupBy.SSIDSortOrder comparator = new GroupBy.SSIDSortOrder();
        withDetailsInfo(lhs, "BSSID");
        withDetailsInfo(rhs, "ASSID");
        // execute & validate
        assertEquals(1, comparator.compare(lhs, rhs));
        verifyDetailsInfo(lhs);
        verifyDetailsInfo(rhs);
    }
}