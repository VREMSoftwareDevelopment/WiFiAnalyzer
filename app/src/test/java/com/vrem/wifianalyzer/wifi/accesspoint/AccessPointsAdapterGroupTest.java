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

package com.vrem.wifianalyzer.wifi.accesspoint;

import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccessPointsAdapterGroupTest {
    @Mock
    private ExpandableListView expandableListView;
    @Mock
    private ExpandableListAdapter expandableListAdapter;

    private Settings settings;
    private AccessPointsAdapterGroup fixture;

    @Before
    public void setUp() {
        settings = MainContextHelper.INSTANCE.getSettings();
        fixture = new AccessPointsAdapterGroup();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testBeforeUpdate() {
        assertNull(fixture.getGroupBy());
        assertTrue(fixture.getExpanded().isEmpty());
        assertFalse(fixture.isGroupExpandable());
    }

    @Test
    public void testAfterUpdateWithGroupByChannel() {
        // setup
        List<WiFiDetail> wiFiDetails = withWiFiDetails();
        when(settings.getGroupBy()).thenReturn(GroupBy.CHANNEL);
        when(expandableListView.getExpandableListAdapter()).thenReturn(expandableListAdapter);
        when(expandableListAdapter.getGroupCount()).thenReturn(wiFiDetails.size());
        // execute
        fixture.update(wiFiDetails, expandableListView);
        // validate
        verify(settings).getGroupBy();
        verify(expandableListView).getExpandableListAdapter();
        verify(expandableListAdapter).getGroupCount();

        assertEquals(GroupBy.CHANNEL, fixture.getGroupBy());
    }

    @Test
    public void testUpdateGroupBy() {
        // setup
        when(settings.getGroupBy()).thenReturn(GroupBy.SSID);
        // execute
        fixture.updateGroupBy();
        // validate
        verify(settings).getGroupBy();
        assertEquals(GroupBy.SSID, fixture.getGroupBy());
    }

    @Test
    public void testUpdateGroupByWillClearExpandedWhenGroupByIsChanged() {
        // setup
        fixture.getExpanded().add("TEST");
        when(settings.getGroupBy()).thenReturn(GroupBy.SSID);
        // execute
        fixture.updateGroupBy();
        // validate
        verify(settings).getGroupBy();
        assertEquals(GroupBy.SSID, fixture.getGroupBy());
        assertTrue(fixture.getExpanded().isEmpty());
    }

    @Test
    public void testUpdateGroupByWillNotClearExpandedWhenGroupByIsSame() {
        // setup
        when(settings.getGroupBy()).thenReturn(GroupBy.SSID);
        fixture.updateGroupBy();
        fixture.getExpanded().add("TEST");
        // execute
        fixture.updateGroupBy();
        // validate
        assertFalse(fixture.getExpanded().isEmpty());
    }

    @Test
    public void testIsGroupExpandableWithGroupBySSID() {
        // setup
        when(settings.getGroupBy()).thenReturn(GroupBy.SSID);
        // execute
        fixture.updateGroupBy();
        // validate
        assertTrue(fixture.isGroupExpandable());
    }

    @Test
    public void testIsGroupExpandableWithGroupByChannel() {
        // setup
        when(settings.getGroupBy()).thenReturn(GroupBy.CHANNEL);
        // execute
        fixture.updateGroupBy();
        // validate
        assertTrue(fixture.isGroupExpandable());
    }

    @Test
    public void testIsGroupExpandableWithGroupByNone() {
        // setup
        when(settings.getGroupBy()).thenReturn(GroupBy.NONE);
        // execute
        fixture.updateGroupBy();
        // validate
        assertFalse(fixture.isGroupExpandable());
    }

    @Test
    public void testGetGroupExpandKeyWithGroupBySSID() {
        // setup
        when(settings.getGroupBy()).thenReturn(GroupBy.SSID);
        fixture.updateGroupBy();
        WiFiDetail wiFiDetail = withWiFiDetail();
        // execute
        String actual = fixture.getGroupExpandKey(wiFiDetail);
        // validate
        assertEquals(wiFiDetail.getSSID(), actual);
    }

    @Test
    public void testGetGroupExpandKeyWithGroupByChannel() {
        // setup
        when(settings.getGroupBy()).thenReturn(GroupBy.CHANNEL);
        fixture.updateGroupBy();
        WiFiDetail wiFiDetail = withWiFiDetail();
        // execute
        String actual = fixture.getGroupExpandKey(wiFiDetail);
        // validate
        assertEquals("" + wiFiDetail.getWiFiSignal().getPrimaryWiFiChannel().getChannel(), actual);
    }

    @Test
    public void testGetGroupExpandKeyWithGroupByNone() {
        // setup
        when(settings.getGroupBy()).thenReturn(GroupBy.NONE);
        fixture.updateGroupBy();
        WiFiDetail wiFiDetail = withWiFiDetail();
        // execute
        String actual = fixture.getGroupExpandKey(wiFiDetail);
        // validate
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testOnGroupExpanded() {
        // setup
        when(settings.getGroupBy()).thenReturn(GroupBy.SSID);
        fixture.updateGroupBy();
        List<WiFiDetail> wiFiDetails = withWiFiDetails();
        // execute
        fixture.onGroupExpanded(wiFiDetails, 0);
        // validate
        assertTrue(fixture.getExpanded().contains(wiFiDetails.get(0).getSSID()));
    }

    @Test
    public void testOnGroupCollapsed() {
        // setup
        when(settings.getGroupBy()).thenReturn(GroupBy.SSID);
        fixture.updateGroupBy();
        List<WiFiDetail> wiFiDetails = withWiFiDetails();
        fixture.onGroupExpanded(wiFiDetails, 0);
        // execute
        fixture.onGroupCollapsed(wiFiDetails, 0);
        // validate
        assertTrue(fixture.getExpanded().isEmpty());
    }

    private WiFiDetail withWiFiDetail() {
        WiFiDetail wiFiDetail = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY,
            new WiFiSignal(2255, 2255, WiFiWidth.MHZ_20, -40, true));
        wiFiDetail.addChild(new WiFiDetail("SSID1-1", "BSSID1-1", StringUtils.EMPTY, WiFiSignal.EMPTY));
        wiFiDetail.addChild(new WiFiDetail("SSID1-2", "BSSID1-2", StringUtils.EMPTY, WiFiSignal.EMPTY));
        wiFiDetail.addChild(new WiFiDetail("SSID1-3", "BSSID1-3", StringUtils.EMPTY, WiFiSignal.EMPTY));
        return wiFiDetail;
    }

    private List<WiFiDetail> withWiFiDetails() {
        return Arrays.asList(withWiFiDetail(),
            new WiFiDetail("SSID2", "BSSID2", StringUtils.EMPTY, WiFiSignal.EMPTY),
            new WiFiDetail("SSID3", "BSSID3", StringUtils.EMPTY, WiFiSignal.EMPTY));
    }

}