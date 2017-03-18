/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;
import com.vrem.wifianalyzer.wifi.predicate.FilterPredicate;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccessPointsAdapterDataTest {
    @Mock
    private WiFiData wiFiData;

    private Settings settings;
    private AccessPointsAdapterData fixture;

    @Before
    public void setUp() {
        settings = MainContextHelper.INSTANCE.getSettings();
        fixture = new AccessPointsAdapterData();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testBeforeUpdate() throws Exception {
        assertEquals(0, fixture.parentsCount());
        assertEquals(0, fixture.childrenCount(0));
        assertEquals(WiFiDetail.EMPTY, fixture.parent(0));
        assertEquals(WiFiDetail.EMPTY, fixture.parent(-1));
        assertEquals(WiFiDetail.EMPTY, fixture.child(0, 0));
        assertEquals(WiFiDetail.EMPTY, fixture.child(0, -1));
    }

    @Test
    public void testAfterUpdate() throws Exception {
        // setup
        withSettings();
        List<WiFiDetail> wiFiDetails = withWiFiDetails();
        when(wiFiData.getWiFiDetails(any(FilterPredicate.class), eq(SortBy.SSID), eq(GroupBy.CHANNEL))).thenReturn(wiFiDetails);
        // execute
        fixture.update(wiFiData);
        // validate
        verify(wiFiData).getWiFiDetails(any(FilterPredicate.class), eq(SortBy.SSID), eq(GroupBy.CHANNEL));
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

    private List<WiFiDetail> withWiFiDetails() {
        WiFiDetail wiFiDetail1 = new WiFiDetail("SSID1", "BSSID1", StringUtils.EMPTY, WiFiSignal.EMPTY);
        wiFiDetail1.addChild(new WiFiDetail("SSID1-1", "BSSID1-1", StringUtils.EMPTY, WiFiSignal.EMPTY));
        wiFiDetail1.addChild(new WiFiDetail("SSID1-2", "BSSID1-2", StringUtils.EMPTY, WiFiSignal.EMPTY));
        wiFiDetail1.addChild(new WiFiDetail("SSID1-3", "BSSID1-3", StringUtils.EMPTY, WiFiSignal.EMPTY));
        return Arrays.asList(wiFiDetail1,
            new WiFiDetail("SSID2", "BSSID2", StringUtils.EMPTY, WiFiSignal.EMPTY),
            new WiFiDetail("SSID3", "BSSID3", StringUtils.EMPTY, WiFiSignal.EMPTY));
    }

    private void verifySettings() {
        verify(settings).getSortBy();
        verify(settings).getGroupBy();
        verify(settings).getWiFiBands();
        verify(settings).getStrengths();
        verify(settings).getSecurities();
    }

    private void withSettings() {
        when(settings.getSortBy()).thenReturn(SortBy.SSID);
        when(settings.getGroupBy()).thenReturn(GroupBy.CHANNEL);
        when(settings.getWiFiBands()).thenReturn(EnumUtils.values(WiFiBand.class));
        when(settings.getStrengths()).thenReturn(EnumUtils.values(Strength.class));
        when(settings.getSecurities()).thenReturn(EnumUtils.values(Security.class));
    }
}