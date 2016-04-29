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

package com.vrem.wifianalyzer.wifi;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.GroupBy;
import com.vrem.wifianalyzer.wifi.model.SortBy;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccessPointsAdapterDataTest {
    @Mock
    private WiFiData wiFiData;

    private Settings settings;
    private AccessPointsAdapterData fixture;

    @Before
    public void setUp() throws Exception {
        settings = MainContextHelper.INSTANCE.getSettings();
        fixture = new AccessPointsAdapterData();
    }

    @After
    public void tearDown() throws Exception {
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
        when(wiFiData.getWiFiDetails(WiFiBand.GHZ5, SortBy.SSID, GroupBy.CHANNEL)).thenReturn(wiFiDetails);
        // execute
        fixture.update(wiFiData);
        // validate
        verify(wiFiData).getWiFiDetails(WiFiBand.GHZ5, SortBy.SSID, GroupBy.CHANNEL);
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
        verify(settings).getWiFiBand();
        verify(settings).getSortBy();
        verify(settings).getGroupBy();
    }

    private void withSettings() {
        when(settings.getWiFiBand()).thenReturn(WiFiBand.GHZ5);
        when(settings.getSortBy()).thenReturn(SortBy.SSID);
        when(settings.getGroupBy()).thenReturn(GroupBy.CHANNEL);
    }
}