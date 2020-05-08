/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.widget.ExpandableListView;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiIdentifier;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;
import com.vrem.wifianalyzer.wifi.predicate.FilterPredicate;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccessPointsAdapterDataTest {
    @Mock
    private WiFiData wiFiData;
    @Mock
    private AccessPointsAdapterGroup accessPointsAdapterGroup;
    @Mock
    private ExpandableListView expandableListView;

    private Settings settings;
    private AccessPointsAdapterData fixture;

    @Before
    public void setUp() {
        settings = MainContextHelper.INSTANCE.getSettings();

        fixture = new AccessPointsAdapterData();
        fixture.setAccessPointsAdapterGroup(accessPointsAdapterGroup);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testBeforeUpdate() {
        assertEquals(0, fixture.parentsCount());
        assertEquals(0, fixture.childrenCount(0));
        assertEquals(WiFiDetail.EMPTY, fixture.parent(0));
        assertEquals(WiFiDetail.EMPTY, fixture.parent(-1));
        assertEquals(WiFiDetail.EMPTY, fixture.child(0, 0));
        assertEquals(WiFiDetail.EMPTY, fixture.child(0, -1));
    }

    @Test
    public void testAfterUpdateWithGroupByChannel() {
        // setup
        List<WiFiDetail> wiFiDetails = withWiFiDetails();
        withSettings();
        when(wiFiData.wiFiDetails(any(FilterPredicate.class), eq(SortBy.SSID), eq(GroupBy.CHANNEL))).thenReturn(wiFiDetails);
        // execute
        fixture.update(wiFiData, expandableListView);
        // validate
        verify(wiFiData).wiFiDetails(any(FilterPredicate.class), eq(SortBy.SSID), eq(GroupBy.CHANNEL));
        verify(accessPointsAdapterGroup).update(wiFiDetails, expandableListView);
        verifySettings();

        assertEquals(wiFiDetails.size(), fixture.parentsCount());
        assertEquals(wiFiDetails.get(0), fixture.parent(0));
        assertEquals(wiFiDetails.get(0).getChildren().size(), fixture.childrenCount(0));
        assertEquals(wiFiDetails.get(0).getChildren().get(0), fixture.child(0, 0));

        assertEquals(WiFiDetail.EMPTY, fixture.parent(-1));
        assertEquals(WiFiDetail.EMPTY, fixture.parent(wiFiDetails.size()));
        assertEquals(WiFiDetail.EMPTY, fixture.child(0, -1));
        assertEquals(WiFiDetail.EMPTY, fixture.child(0, wiFiDetails.get(0).getChildren().size()));
    }

    @Test
    public void testOnGroupCollapsed() {
        // setup
        int index = 11;
        List<WiFiDetail> wiFiDetails = fixture.getWiFiDetails();
        // execute
        fixture.onGroupCollapsed(index);
        // validate
        verify(accessPointsAdapterGroup).onGroupCollapsed(wiFiDetails, index);
    }

    @Test
    public void testOnGroupExpanded() {
        // setup
        int index = 22;
        List<WiFiDetail> wiFiDetails = fixture.getWiFiDetails();
        // execute
        fixture.onGroupExpanded(index);
        // validate
        verify(accessPointsAdapterGroup).onGroupExpanded(wiFiDetails, index);
    }

    private WiFiDetail withWiFiDetail() {
        List<WiFiDetail> children = Arrays.asList(
            new WiFiDetail(new WiFiIdentifier("SSID1-1", "BSSID1-1"), StringUtils.EMPTY, WiFiSignal.EMPTY),
            new WiFiDetail(new WiFiIdentifier("SSID1-2", "BSSID1-2"), StringUtils.EMPTY, WiFiSignal.EMPTY),
            new WiFiDetail(new WiFiIdentifier("SSID1-3", "BSSID1-3"), StringUtils.EMPTY, WiFiSignal.EMPTY));
        WiFiSignal wiFiSignal = new WiFiSignal(2255, 2255, WiFiWidth.MHZ_20, -40, true);
        return new WiFiDetail(new WiFiIdentifier("SSID1", "BSSID1"), StringUtils.EMPTY, wiFiSignal, WiFiAdditional.EMPTY, children);
    }

    private List<WiFiDetail> withWiFiDetails() {
        return Arrays.asList(withWiFiDetail(),
            new WiFiDetail(new WiFiIdentifier("SSID2", "BSSID2"), StringUtils.EMPTY, WiFiSignal.EMPTY),
            new WiFiDetail(new WiFiIdentifier("SSID3", "BSSID3"), StringUtils.EMPTY, WiFiSignal.EMPTY));
    }

    private void verifySettings() {
        verify(settings).sortBy();
        verify(settings).groupBy();
        verify(settings).findWiFiBands();
        verify(settings).findStrengths();
        verify(settings).findSecurities();
    }

    private void withSettings() {
        when(settings.sortBy()).thenReturn(SortBy.SSID);
        when(settings.groupBy()).thenReturn(GroupBy.CHANNEL);
        when(settings.findWiFiBands()).thenReturn(new HashSet<>(Arrays.asList(WiFiBand.values())));
        when(settings.findStrengths()).thenReturn(new HashSet<>(Arrays.asList(Strength.values())));
        when(settings.findSecurities()).thenReturn(new HashSet<>(Arrays.asList(Security.values())));
    }
}