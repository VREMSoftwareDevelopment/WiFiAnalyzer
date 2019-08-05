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

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.vendor.model.VendorService;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.predicate.WiFiBandPredicate;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WiFiDataTest {
    private static final String IP_ADDRESS = "21.205.91.7";
    private static final String VENDOR_NAME = "VendorName+";
    private static final int LINK_SPEED = 21;
    private static final String SSID_1 = "SSID1";
    private static final String SSID_2 = "SSID2";
    private static final String SSID_3 = "SSID3";
    private static final String SSID_4 = "SSID4";
    private static final String BSSID_1 = "B" + SSID_1;
    private static final String BSSID_2 = "B" + SSID_2;
    private static final String BSSID_3 = "B" + SSID_3;
    private static final String BSSID_4 = "B" + SSID_4;
    private static final int FREQUENCY1 = 2412;
    private static final int FREQUENCY2 = 2417;
    private static final int FREQUENCY3 = 2422;
    private static final int FREQUENCY4 = 2422;
    private static final int LEVEL0 = -5;
    private static final int LEVEL1 = -4;
    private static final int LEVEL2 = -3;

    private VendorService vendorService;
    private WiFiConnection wiFiConnection;
    private List<WiFiDetail> wiFiDetails;
    private WiFiData fixture;

    @Before
    public void setUp() {
        vendorService = MainContextHelper.INSTANCE.getVendorService();

        wiFiDetails = withWiFiDetails();
        wiFiConnection = new WiFiConnection(SSID_1, BSSID_1, IP_ADDRESS, LINK_SPEED);

        withVendorNames();

        fixture = new WiFiData(wiFiDetails, wiFiConnection);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    private List<WiFiDetail> withWiFiDetails() {
        WiFiDetail wiFiDetail1 = new WiFiDetail(SSID_1, BSSID_1, StringUtils.EMPTY,
            new WiFiSignal(FREQUENCY1, FREQUENCY1, WiFiWidth.MHZ_20, LEVEL1, true));
        WiFiDetail wiFiDetail2 = new WiFiDetail(SSID_2, BSSID_2, StringUtils.EMPTY,
            new WiFiSignal(FREQUENCY2, FREQUENCY2, WiFiWidth.MHZ_20, LEVEL2, true));
        WiFiDetail wiFiDetail3 = new WiFiDetail(SSID_3, BSSID_3, StringUtils.EMPTY,
            new WiFiSignal(FREQUENCY3, FREQUENCY3, WiFiWidth.MHZ_20, LEVEL0, true));
        WiFiDetail wiFiDetail4 = new WiFiDetail(SSID_4, BSSID_4, StringUtils.EMPTY,
            new WiFiSignal(FREQUENCY4, FREQUENCY4, WiFiWidth.MHZ_20, LEVEL2, true));

        WiFiDetail wiFiDetail_1 = new WiFiDetail(SSID_2, BSSID_2 + "_1", StringUtils.EMPTY,
            new WiFiSignal(FREQUENCY2, FREQUENCY2, WiFiWidth.MHZ_20, LEVEL2 - 3, true));
        WiFiDetail wiFiDetail_2 = new WiFiDetail(SSID_2, BSSID_2 + "_2", StringUtils.EMPTY,
            new WiFiSignal(FREQUENCY2, FREQUENCY2, WiFiWidth.MHZ_20, LEVEL2 - 1, true));
        WiFiDetail wiFiDetail_3 = new WiFiDetail(SSID_2, BSSID_2 + "_3", StringUtils.EMPTY,
            new WiFiSignal(FREQUENCY2, FREQUENCY2, WiFiWidth.MHZ_20, LEVEL2 - 2, true));

        return Arrays.asList(wiFiDetail_3, wiFiDetail3, wiFiDetail_2, wiFiDetail1, wiFiDetail_1, wiFiDetail2, wiFiDetail4);
    }

    private void withVendorNames() {
        IterableUtils.forEach(wiFiDetails, new VendorNameClosure());
    }

    @Test
    public void testGetWiFiDetailsWithSSID() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        // execute
        List<WiFiDetail> actual = fixture.getWiFiDetails(predicate, SortBy.STRENGTH, GroupBy.SSID);
        // validate
        assertEquals(4, actual.size());
        assertEquals(SSID_2, actual.get(0).getSSID());
        assertEquals(SSID_4, actual.get(1).getSSID());
        assertEquals(SSID_1, actual.get(2).getSSID());
        assertEquals(SSID_3, actual.get(3).getSSID());
    }

    @Test
    public void testGetWiFiDetailsWithVendorName() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        // execute
        List<WiFiDetail> actual = fixture.getWiFiDetails(predicate, SortBy.STRENGTH, GroupBy.SSID);
        // validate
        assertEquals(VENDOR_NAME + BSSID_2, actual.get(0).getWiFiAdditional().getVendorName());
        assertEquals(VENDOR_NAME + BSSID_4, actual.get(1).getWiFiAdditional().getVendorName());
        assertEquals(VENDOR_NAME + BSSID_1, actual.get(2).getWiFiAdditional().getVendorName());
        assertEquals(VENDOR_NAME + BSSID_3, actual.get(3).getWiFiAdditional().getVendorName());

        verify(vendorService, times(7)).findVendorName(anyString());
    }

    @Test
    public void testGetWiFiDetailsWithChildren() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        // execute
        List<WiFiDetail> actual = fixture.getWiFiDetails(predicate, SortBy.STRENGTH, GroupBy.SSID);
        // validate
        WiFiDetail wiFiDetail = actual.get(0);
        List<WiFiDetail> children = wiFiDetail.getChildren();
        assertEquals(3, children.size());
        assertEquals(BSSID_2 + "_2", children.get(0).getBSSID());
        assertEquals(BSSID_2 + "_3", children.get(1).getBSSID());
        assertEquals(BSSID_2 + "_1", children.get(2).getBSSID());
    }

    @Test
    public void testGetConnection() {
        // execute
        WiFiDetail actual = fixture.getConnection();
        // validate
        assertEquals(SSID_1, actual.getSSID());
        assertEquals(BSSID_1, actual.getBSSID());
        assertEquals(IP_ADDRESS, actual.getWiFiAdditional().getWiFiConnection().getIpAddress());
    }

    @Test
    public void testIsConfiguredNetwork() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        // execute
        List<WiFiDetail> actual = fixture.getWiFiDetails(predicate, SortBy.STRENGTH, GroupBy.SSID);
        // validate
        assertEquals(4, actual.size());

        assertEquals(SSID_1, actual.get(2).getSSID());
        assertEquals(SSID_3, actual.get(3).getSSID());
    }

    @Test
    public void testGetWiFiDetails() {
        // setup
        fixture = new WiFiData(wiFiDetails, wiFiConnection) {
            @NonNull
            @Override
            protected List<WiFiDetail> sortAndGroup(@NonNull List<WiFiDetail> wiFiDetails, @NonNull SortBy sortBy, @NonNull GroupBy groupBy) {
                fail("Should not apply grouping");
                return null;
            }
        };
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        // execute
        List<WiFiDetail> actual = fixture.getWiFiDetails(predicate, SortBy.SSID);
        // validate
        assertEquals(7, actual.size());
        assertEquals(BSSID_1, actual.get(0).getBSSID());
        assertEquals(BSSID_4, actual.get(6).getBSSID());
    }

    private class VendorNameClosure implements Closure<WiFiDetail> {
        @Override
        public void execute(WiFiDetail wiFiDetail) {
            when(vendorService.findVendorName(wiFiDetail.getBSSID())).thenReturn(VENDOR_NAME + wiFiDetail.getBSSID());
        }
    }
}