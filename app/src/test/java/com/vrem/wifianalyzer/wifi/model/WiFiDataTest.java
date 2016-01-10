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

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.vendor.model.VendorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WifiManager.class)
public class WiFiDataTest {
    public static final String VENDOR_NAME = "VendorName+";

    public static final int FREQUENCY1 = 2412;
    public static final int FREQUENCY2 = 2417;
    public static final int FREQUENCY3 = 2422;
    public static final int FREQUENCY4 = 2422;

    public static final int LEVEL0 = 0;
    public static final int LEVEL1 = 1;
    public static final int LEVEL2 = 2;
    public static final int LEVEL3 = 3;
    public static final int LEVEL4 = 4;

    @Mock private WifiInfo wifiInfo;
    @Mock private ScanResult scanResult1;
    @Mock private ScanResult scanResult2;
    @Mock private ScanResult scanResult3;
    @Mock private ScanResult scanResult4;
    @Mock private ScanResult scanResult_1;
    @Mock private ScanResult scanResult_2;
    @Mock private ScanResult scanResult_3;

    @Mock private VendorService vendorService;
    @Mock private Settings settings;

    private List<ScanResult> scanResults;
    private WiFiData fixture;

    @Before
    public void setUp() throws Exception {
        mockStatic(WifiManager.class);

        MainContext mainContext = MainContext.INSTANCE;
        mainContext.setVendorService(vendorService);
        mainContext.setSettings(settings);

        when(settings.getGroupBy()).thenReturn(GroupBy.SSID);
        when(settings.getWiFiBand()).thenReturn(WiFiBand.TWO_POINT_FOUR);

        scanResults = Arrays.asList(scanResult_3,
                scanResult3,
                scanResult_2,
                scanResult1,
                scanResult_1,
                scanResult2,
                scanResult4);

        setUpScanResultGroups();
        setUpScanResultChildren(scanResult2);
        withSignalLevel();
        withVendorNames();
        withWifiInfo();

        fixture = new WiFiData(scanResults, wifiInfo);
    }

    private void withWifiInfo() {
        when(wifiInfo.getSSID()).thenReturn(scanResult1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult1.BSSID);
        when(wifiInfo.getIpAddress()).thenReturn(123456789);
    }

    private void withSignalLevel() {
        when(WifiManager.calculateSignalLevel(LEVEL0, Strength.values().length)).thenReturn(LEVEL0);
        when(WifiManager.calculateSignalLevel(LEVEL1, Strength.values().length)).thenReturn(LEVEL1);
        when(WifiManager.calculateSignalLevel(LEVEL2, Strength.values().length)).thenReturn(LEVEL2);
        when(WifiManager.calculateSignalLevel(LEVEL3, Strength.values().length)).thenReturn(LEVEL3);
        when(WifiManager.calculateSignalLevel(LEVEL4, Strength.values().length)).thenReturn(LEVEL4);
    }

    private void setUpScanResultChildren(ScanResult scanResult) {
        scanResult_1.SSID = scanResult_2.SSID = scanResult_3.SSID = scanResult.SSID;

        scanResult_1.BSSID = "B" + scanResult.SSID + "_1";
        scanResult_1.level = LEVEL4;
        scanResult_1.frequency = scanResult.frequency;

        scanResult_2.BSSID = "B" + scanResult.SSID + "_2";
        scanResult_2.level = LEVEL3;
        scanResult_2.frequency = scanResult.frequency;

        scanResult_3.BSSID = "B" + scanResult.SSID + "_3";
        scanResult_3.level = LEVEL3;
        scanResult_3.frequency = scanResult.frequency;

    }

    private void setUpScanResultGroups() {
        scanResult1.SSID = "SSID1";
        scanResult2.SSID = "SSID2";
        scanResult3.SSID = "SSID3";
        scanResult4.SSID = "SSID4";

        scanResult1.BSSID = "BSSID1";
        scanResult2.BSSID = "BSSID2";
        scanResult3.BSSID = "BSSID3";
        scanResult4.BSSID = "BSSID4";

        scanResult1.level = LEVEL1;
        scanResult2.level = LEVEL2;
        scanResult3.level = LEVEL0;
        scanResult4.level = LEVEL2;

        scanResult1.frequency = FREQUENCY1;
        scanResult2.frequency = FREQUENCY2;
        scanResult3.frequency = FREQUENCY3;
        scanResult4.frequency = FREQUENCY4;
    }

    private void withVendorNames() {
        for (ScanResult scanResult : scanResults) {
            when(vendorService.findVendorName(scanResult.BSSID)).thenReturn(VENDOR_NAME + scanResult.BSSID);
        }
    }

    @Test
    public void testGetConnectionEmptyObject() throws Exception {
        // execute
        fixture = new WiFiData(null, wifiInfo);
        // validate
        assertNull(fixture.getConnection());
    }

    @Test
    public void testGetWiFiListEmptyObject() throws Exception {
        // execute
        fixture = new WiFiData(null, wifiInfo);
        // validate
        assertTrue(fixture.getWiFiList().isEmpty());
    }

    @Test
    public void testGetWiFiListEmpty() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null);
        when(settings.getWiFiBand()).thenReturn(WiFiBand.FIVE);
        // execute
        List<DetailsInfo> actual = fixture.getWiFiList();
        // validate
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetWiFiListWithSSID() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null);
        // execute
        List<DetailsInfo> actual = fixture.getWiFiList();
        // validate
        assertEquals(4, actual.size());
        assertEquals(scanResult3.SSID, actual.get(0).getSSID());
        assertEquals(scanResult2.SSID, actual.get(2).getSSID());
        assertEquals(scanResult4.SSID, actual.get(3).getSSID());

        verify(settings).getWiFiBand();
        verify(settings).getGroupBy();
    }

    @Test
    public void testGetWiFiListWithVendorName() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null);
        // execute
        List<DetailsInfo> actual = fixture.getWiFiList();
        // validate
        assertEquals(VENDOR_NAME + scanResult3.BSSID, actual.get(0).getVendorName());
        assertEquals(VENDOR_NAME + scanResult1.BSSID, actual.get(1).getVendorName());
        assertEquals(VENDOR_NAME + scanResult2.BSSID, actual.get(2).getVendorName());
        assertEquals(VENDOR_NAME + scanResult4.BSSID, actual.get(3).getVendorName());

        verify(vendorService, times(7)).findVendorName(anyString());
    }

    @Test
    public void testGetWiFiListWithChildren() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null);
        // execute
        List<DetailsInfo> actual = fixture.getWiFiList();
        // validate
        List<DetailsInfo> children = actual.get(2).getChildren();
        assertEquals(3, children.size());
        assertEquals(scanResult_2.BSSID, children.get(0).getBSSID());
        assertEquals(scanResult_1.BSSID, children.get(2).getBSSID());
    }

    @Test
    public void testGetConnection() throws Exception {
        // setup
        DetailsInfo expected = new Details(scanResult1, VENDOR_NAME + scanResult1.BSSID, "21.205.91.7");
        fixture = new WiFiData(scanResults, wifiInfo);
        // execute
        DetailsInfo actual = fixture.getConnection();
        // validate
        assertEquals(expected, actual);
        assertNotSame(expected, actual);
        assertEquals(expected.getSSID(), actual.getSSID());
        assertEquals(expected.getBSSID(), actual.getBSSID());
        assertEquals(expected.getVendorName(), actual.getVendorName());
        assertEquals(expected.getIPAddress(), actual.getIPAddress());
    }

    @Test
    public void testGetWiFiChannelList() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null);
        // execute
        Map<Integer, List<DetailsInfo>> actual = fixture.getWiFiChannels();
        // validate
        assertEquals(3, actual.size());
        assertEquals(1, actual.get(Frequency.findChannel(FREQUENCY1)).size());
        assertEquals(4, actual.get(Frequency.findChannel(FREQUENCY2)).size());
        assertEquals(2, actual.get(Frequency.findChannel(FREQUENCY3)).size());

        verify(settings).getWiFiBand();
        verify(settings, never()).getGroupBy();
        verify(settings, never()).hideWeakSignal();
    }

    @Test
    public void testGetWiFiChannelListEmpty() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null);
        when(settings.getWiFiBand()).thenReturn(WiFiBand.FIVE);
        // execute
        Map<Integer, List<DetailsInfo>> actual = fixture.getWiFiChannels();
        // validate
        assertTrue(actual.isEmpty());
    }
}