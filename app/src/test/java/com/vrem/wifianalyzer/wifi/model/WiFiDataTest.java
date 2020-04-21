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

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.vendor.model.VendorService;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.predicate.WiFiBandPredicate;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

        fixture = new WiFiData(wiFiDetails, wiFiConnection);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(vendorService);
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testGetConnection() {
        // setup
        when(vendorService.findVendorName(BSSID_1)).thenReturn(VENDOR_NAME);
        // execute
        WiFiDetail actual = fixture.connection();
        // validate
        assertEquals(SSID_1, actual.getSSID());
        assertEquals(BSSID_1, actual.getBSSID());
        assertEquals(VENDOR_NAME, actual.getWiFiAdditional().getVendorName());
        assertEquals(IP_ADDRESS, actual.getWiFiAdditional().getWiFiConnection().getIpAddress());
        verify(vendorService).findVendorName(BSSID_1);
    }

    @Test
    public void testGetWiFiDetailsWithConfiguredNetwork() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        withVendorNames();
        // execute
        List<WiFiDetail> actual = fixture.wiFiDetails(predicate, SortBy.SSID);
        // validate
        assertEquals(7, actual.size());
        WiFiDetail wiFiDetail = actual.get(0);
        assertEquals(SSID_1, wiFiDetail.getSSID());
        assertEquals(BSSID_1, wiFiDetail.getBSSID());
        assertEquals(IP_ADDRESS, wiFiDetail.getWiFiAdditional().getWiFiConnection().getIpAddress());

        assertTrue(actual.get(1).getWiFiAdditional().getWiFiConnection().getIpAddress().isEmpty());
        assertTrue(actual.get(2).getWiFiAdditional().getWiFiConnection().getIpAddress().isEmpty());
        assertTrue(actual.get(3).getWiFiAdditional().getWiFiConnection().getIpAddress().isEmpty());
        assertTrue(actual.get(4).getWiFiAdditional().getWiFiConnection().getIpAddress().isEmpty());
        assertTrue(actual.get(5).getWiFiAdditional().getWiFiConnection().getIpAddress().isEmpty());
        assertTrue(actual.get(6).getWiFiAdditional().getWiFiConnection().getIpAddress().isEmpty());
        verifyVendorNames();
    }

    @Test
    public void testGetWiFiDetailsWithVendorName() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        withVendorNames();
        // execute
        List<WiFiDetail> actual = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.NONE);
        // validate
        assertEquals(7, actual.size());
        assertEquals(VENDOR_NAME + BSSID_2, actual.get(0).getWiFiAdditional().getVendorName());
        assertEquals(VENDOR_NAME + BSSID_4, actual.get(1).getWiFiAdditional().getVendorName());
        assertEquals(VENDOR_NAME + BSSID_1, actual.get(2).getWiFiAdditional().getVendorName());
        assertEquals(VENDOR_NAME + BSSID_2 + "_2", actual.get(3).getWiFiAdditional().getVendorName());
        assertEquals(VENDOR_NAME + BSSID_2 + "_3", actual.get(4).getWiFiAdditional().getVendorName());
        assertEquals(VENDOR_NAME + BSSID_3, actual.get(5).getWiFiAdditional().getVendorName());
        assertEquals(VENDOR_NAME + BSSID_2 + "_1", actual.get(6).getWiFiAdditional().getVendorName());
        verify(vendorService, times(7)).findVendorName(anyString());
        verifyVendorNames();
    }

    @Test
    public void testGetWiFiDetailsSortByStrengthGroupByNone() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        withVendorNames();
        // execute
        List<WiFiDetail> actual = fixture.wiFiDetails(predicate, SortBy.STRENGTH);
        // validate
        assertEquals(7, actual.size());
        assertEquals(BSSID_2, actual.get(0).getBSSID());
        assertEquals(BSSID_4, actual.get(1).getBSSID());
        assertEquals(BSSID_1, actual.get(2).getBSSID());
        assertEquals(BSSID_2 + "_2", actual.get(3).getBSSID());
        assertEquals(BSSID_2 + "_3", actual.get(4).getBSSID());
        assertEquals(BSSID_3, actual.get(5).getBSSID());
        assertEquals(BSSID_2 + "_1", actual.get(6).getBSSID());
        verifyChildren(actual);
        verifyVendorNames();
    }

    @Test
    public void testGetWiFiDetailsSortByStrengthGroupBySSID() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        withVendorNames();
        // execute
        List<WiFiDetail> actual = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.SSID);
        // validate
        assertEquals(4, actual.size());
        assertEquals(SSID_2, actual.get(0).getSSID());
        assertEquals(SSID_4, actual.get(1).getSSID());
        assertEquals(SSID_1, actual.get(2).getSSID());
        assertEquals(SSID_3, actual.get(3).getSSID());
        verifyChildren(actual, 0);
        verifyVendorNames();
    }

    @Test
    public void testGetWiFiDetailsSortByStrengthGroupByChannel() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        withVendorNames();
        // execute
        List<WiFiDetail> actual = fixture.wiFiDetails(predicate, SortBy.STRENGTH, GroupBy.CHANNEL);
        // validate
        assertEquals(3, actual.size());
        assertEquals(SSID_2, actual.get(0).getSSID());
        assertEquals(SSID_4, actual.get(1).getSSID());
        assertEquals(SSID_1, actual.get(2).getSSID());
        verifyChildrenGroupByChannel(actual, 2, 0, 1);
        verifyVendorNames();
    }

    @Test
    public void testGetWiFiDetailsSortBySSIDGroupByNone() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        withVendorNames();
        // execute
        List<WiFiDetail> actual = fixture.wiFiDetails(predicate, SortBy.SSID);
        // validate
        assertEquals(7, actual.size());
        assertEquals(BSSID_1, actual.get(0).getBSSID());
        assertEquals(BSSID_2, actual.get(1).getBSSID());
        assertEquals(BSSID_2 + "_2", actual.get(2).getBSSID());
        assertEquals(BSSID_2 + "_3", actual.get(3).getBSSID());
        assertEquals(BSSID_2 + "_1", actual.get(4).getBSSID());
        assertEquals(BSSID_3, actual.get(5).getBSSID());
        assertEquals(BSSID_4, actual.get(6).getBSSID());
        verifyChildren(actual);
        verifyVendorNames();
    }

    @Test
    public void testGetWiFiDetailsSortBySSIDGroupBySSID() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        withVendorNames();
        // execute
        List<WiFiDetail> actual = fixture.wiFiDetails(predicate, SortBy.SSID, GroupBy.SSID);
        // validate
        assertEquals(4, actual.size());
        assertEquals(SSID_1, actual.get(0).getSSID());
        assertEquals(SSID_2, actual.get(1).getSSID());
        assertEquals(SSID_3, actual.get(2).getSSID());
        assertEquals(SSID_4, actual.get(3).getSSID());
        verifyChildren(actual, 1);
        verifyVendorNames();
    }

    @Test
    public void testGetWiFiDetailsSortBySSIDGroupByChannel() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        withVendorNames();
        // execute
        List<WiFiDetail> actual = fixture.wiFiDetails(predicate, SortBy.SSID, GroupBy.CHANNEL);
        // validate
        assertEquals(3, actual.size());
        assertEquals(SSID_1, actual.get(0).getSSID());
        assertEquals(SSID_2, actual.get(1).getSSID());
        assertEquals(SSID_4, actual.get(2).getSSID());
        verifyChildrenGroupByChannel(actual, 0, 1, 2);
        verifyVendorNames();
    }

    @Test
    public void testGetWiFiDetailsSortByChannelGroupByNone() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        withVendorNames();
        // execute
        List<WiFiDetail> actual = fixture.wiFiDetails(predicate, SortBy.CHANNEL);
        // validate
        assertEquals(7, actual.size());
        assertEquals(BSSID_1, actual.get(0).getBSSID());
        assertEquals(BSSID_2, actual.get(1).getBSSID());
        assertEquals(BSSID_2 + "_2", actual.get(2).getBSSID());
        assertEquals(BSSID_2 + "_3", actual.get(3).getBSSID());
        assertEquals(BSSID_2 + "_1", actual.get(4).getBSSID());
        assertEquals(BSSID_4, actual.get(5).getBSSID());
        assertEquals(BSSID_3, actual.get(6).getBSSID());
        verifyChildren(actual);
        verifyVendorNames();
    }

    @Test
    public void testGetWiFiDetailsSortByChannelGroupBySSID() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        withVendorNames();
        // execute
        List<WiFiDetail> actual = fixture.wiFiDetails(predicate, SortBy.CHANNEL, GroupBy.SSID);
        // validate
        assertEquals(4, actual.size());
        assertEquals(SSID_1, actual.get(0).getSSID());
        assertEquals(SSID_2, actual.get(1).getSSID());
        assertEquals(SSID_4, actual.get(2).getSSID());
        assertEquals(SSID_3, actual.get(3).getSSID());
        verifyChildren(actual, 1);
        verifyVendorNames();
    }

    @Test
    public void testGetWiFiDetailsSortByChannelGroupByChannel() {
        // setup
        Predicate<WiFiDetail> predicate = new WiFiBandPredicate(WiFiBand.GHZ2);
        withVendorNames();
        // execute
        List<WiFiDetail> actual = fixture.wiFiDetails(predicate, SortBy.CHANNEL, GroupBy.CHANNEL);
        // validate
        assertEquals(3, actual.size());
        assertEquals(SSID_1, actual.get(0).getSSID());
        assertEquals(SSID_2, actual.get(1).getSSID());
        assertEquals(SSID_4, actual.get(2).getSSID());
        verifyChildrenGroupByChannel(actual, 0, 1, 2);
        verifyVendorNames();
    }

    private void withVendorNames() {
        wiFiDetails.forEach(wiFiDetail ->
            when(vendorService.findVendorName(wiFiDetail.getBSSID())).thenReturn(VENDOR_NAME + wiFiDetail.getBSSID()));
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

    private void verifyChildren(List<WiFiDetail> actual, int index) {
        for (int i = 0; i < actual.size(); i++) {
            WiFiDetail wiFiDetail = actual.get(index);
            if (i == index) {
                List<WiFiDetail> children = wiFiDetail.getChildren();
                assertEquals(3, children.size());
                assertEquals(BSSID_2 + "_2", children.get(0).getBSSID());
                assertEquals(BSSID_2 + "_3", children.get(1).getBSSID());
                assertEquals(BSSID_2 + "_1", children.get(2).getBSSID());
            } else {
                assertTrue(actual.get(i).getChildren().isEmpty());
            }
        }
    }

    private void verifyChildrenGroupByChannel(List<WiFiDetail> actual, int indexEmpty, int indexWith3, int indexWith1) {
        assertTrue(actual.get(indexEmpty).getChildren().isEmpty());

        List<WiFiDetail> children1 = actual.get(indexWith3).getChildren();
        assertEquals(3, children1.size());
        assertEquals(BSSID_2 + "_2", children1.get(0).getBSSID());
        assertEquals(BSSID_2 + "_3", children1.get(1).getBSSID());
        assertEquals(BSSID_2 + "_1", children1.get(2).getBSSID());

        List<WiFiDetail> children2 = actual.get(indexWith1).getChildren();
        assertEquals(1, children2.size());
        assertEquals(BSSID_3, children2.get(0).getBSSID());
    }

    private void verifyVendorNames() {
        wiFiDetails.forEach(wiFiDetail -> verify(vendorService).findVendorName(wiFiDetail.getBSSID()));
    }

    private void verifyChildren(List<WiFiDetail> actual) {
        actual.forEach(wiFiDetail -> assertTrue(wiFiDetail.getChildren().isEmpty()));
    }

}