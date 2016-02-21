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
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.vrem.wifianalyzer.MainContext;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
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

    public static final int LEVEL0 = -5;
    public static final int LEVEL1 = -4;
    public static final int LEVEL2 = -3;
    public static final int LEVEL3 = -2;
    public static final int LEVEL4 = -1;

    @Mock private WifiInfo wifiInfo;
    @Mock private ScanResult scanResult1;
    @Mock private ScanResult scanResult2;
    @Mock private ScanResult scanResult3;
    @Mock private ScanResult scanResult4;
    @Mock private ScanResult scanResult_1;
    @Mock private ScanResult scanResult_2;
    @Mock private ScanResult scanResult_3;

    @Mock private VendorService vendorService;

    private List<ScanResult> scanResults;
    private List<WifiConfiguration> configuredNetworks;
    private WiFiData fixture;

    @Before
    public void setUp() throws Exception {
        MainContext mainContext = MainContext.INSTANCE;
        mainContext.setVendorService(vendorService);

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
        withConfiguredNetworks();

        fixture = new WiFiData(scanResults, wifiInfo, configuredNetworks);
    }

    private void withConfiguredNetworks() {
        WifiConfiguration wifiConfiguration1 = new WifiConfiguration();
        wifiConfiguration1.SSID = "\"" + scanResult3.SSID + "\"";
        WifiConfiguration wifiConfiguration2 = new WifiConfiguration();
        wifiConfiguration2.SSID = "123-456-789";
        configuredNetworks = Arrays.asList(wifiConfiguration1, wifiConfiguration2);
    }

    private void withWifiInfo() {
        when(wifiInfo.getSSID()).thenReturn(scanResult1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult1.BSSID);
        when(wifiInfo.getIpAddress()).thenReturn(123456789);
    }

    private void withSignalLevel() {
        mockStatic(WifiManager.class);
        when(WifiManager.calculateSignalLevel(LEVEL0, Strength.values().length)).thenReturn(LEVEL0);
        when(WifiManager.calculateSignalLevel(LEVEL1, Strength.values().length)).thenReturn(LEVEL1);
        when(WifiManager.calculateSignalLevel(LEVEL2, Strength.values().length)).thenReturn(LEVEL2);
        when(WifiManager.calculateSignalLevel(LEVEL3, Strength.values().length)).thenReturn(LEVEL3);
        when(WifiManager.calculateSignalLevel(LEVEL4, Strength.values().length)).thenReturn(LEVEL4);
    }

    private void setUpScanResultChildren(ScanResult scanResult) {
        scanResult_1.SSID = scanResult_2.SSID = scanResult_3.SSID = scanResult.SSID;

        scanResult_1.BSSID = "B" + scanResult.SSID + "_1";
        scanResult_1.level = LEVEL2 - 3;
        scanResult_1.frequency = scanResult.frequency;

        scanResult_2.BSSID = "B" + scanResult.SSID + "_2";
        scanResult_2.level = LEVEL2 - 1;
        scanResult_2.frequency = scanResult.frequency;

        scanResult_3.BSSID = "B" + scanResult.SSID + "_3";
        scanResult_3.level = LEVEL2 - 2;
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
        fixture = new WiFiData(null, wifiInfo, null);
        // validate
        assertNull(fixture.getConnection());
    }

    @Test
    public void testGetWiFiListEmptyObject() throws Exception {
        // execute
        fixture = new WiFiData(null, wifiInfo, null);
        // validate
        assertTrue(fixture.getWiFiList(WiFiBand.TWO, GroupBy.SSID, SortBy.STRENGTH).isEmpty());
    }

    @Test
    public void testGetWiFiListEmpty() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null, null);
        // execute
        List<WiFiDetails> actual = fixture.getWiFiList(WiFiBand.FIVE, GroupBy.SSID, SortBy.STRENGTH);
        // validate
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetWiFiListWithSSID() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null, null);
        // execute
        List<WiFiDetails> actual = fixture.getWiFiList(WiFiBand.TWO, GroupBy.SSID, SortBy.STRENGTH);
        // validate
        assertEquals(4, actual.size());
        assertEquals(scanResult2.SSID, actual.get(0).getSSID());
        assertEquals(scanResult4.SSID, actual.get(1).getSSID());
        assertEquals(scanResult1.SSID, actual.get(2).getSSID());
        assertEquals(scanResult3.SSID, actual.get(3).getSSID());
    }

    @Test
    public void testGetWiFiListWithVendorName() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null, null);
        // execute
        List<WiFiDetails> actual = fixture.getWiFiList(WiFiBand.TWO, GroupBy.SSID, SortBy.STRENGTH);
        // validate
        assertEquals(VENDOR_NAME + scanResult2.BSSID, actual.get(0).getVendorName());
        assertEquals(VENDOR_NAME + scanResult4.BSSID, actual.get(1).getVendorName());
        assertEquals(VENDOR_NAME + scanResult1.BSSID, actual.get(2).getVendorName());
        assertEquals(VENDOR_NAME + scanResult3.BSSID, actual.get(3).getVendorName());

        verify(vendorService, times(7)).findVendorName(anyString());
    }

    @Test
    public void testGetWiFiListWithChildren() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null, null);
        // execute
        List<WiFiDetails> actual = fixture.getWiFiList(WiFiBand.TWO, GroupBy.SSID, SortBy.STRENGTH);
        // validate
        WiFiDetails wiFiDetails = actual.get(0);
        List<WiFiDetails> children = wiFiDetails.getChildren();
        assertEquals(3, children.size());
        assertEquals(scanResult_2.BSSID, children.get(0).getBSSID());
        assertEquals(scanResult_3.BSSID, children.get(1).getBSSID());
        assertEquals(scanResult_1.BSSID, children.get(2).getBSSID());
    }

    @Test
    public void testGetConnection() throws Exception {
        // setup
        WiFiDetails expected = Details.makeConnection(scanResult1, VENDOR_NAME + scanResult1.BSSID, "21.205.91.7");
        fixture = new WiFiData(scanResults, wifiInfo, null);
        // execute
        WiFiDetails actual = fixture.getConnection();
        // validate
        assertEquals(expected, actual);
        assertNotSame(expected, actual);
        assertEquals(expected.getSSID(), actual.getSSID());
        assertEquals(expected.getBSSID(), actual.getBSSID());
        assertEquals(expected.getVendorName(), actual.getVendorName());
        assertEquals(expected.getIPAddress(), actual.getIPAddress());
        assertTrue(actual.isConnected());
        assertTrue(actual.isConfiguredNetwork());
    }

    @Test
    public void testIsConfiguredNetwork() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null, configuredNetworks);
        // execute
        List<WiFiDetails> actual = fixture.getWiFiList(WiFiBand.TWO, GroupBy.SSID, SortBy.STRENGTH);
        // validate
        assertEquals(4, actual.size());
        assertFalse(actual.get(0).isConfiguredNetwork());
        assertFalse(actual.get(1).isConfiguredNetwork());
        assertFalse(actual.get(2).isConfiguredNetwork());
        assertTrue(actual.get(3).isConfiguredNetwork());
    }

    @Test
    public void testGetWiFiChannelList() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null, null);
        // execute
        Map<Integer, List<WiFiDetails>> actual = fixture.getWiFiChannels(WiFiBand.TWO);
        // validate
        assertEquals(3, actual.size());
        assertEquals(1, actual.get(WiFiBand.findChannelByFrequency(FREQUENCY1)).size());
        assertEquals(4, actual.get(WiFiBand.findChannelByFrequency(FREQUENCY2)).size());
        assertEquals(2, actual.get(WiFiBand.findChannelByFrequency(FREQUENCY3)).size());
    }

    @Test
    public void testGetWiFiChannelListEmpty() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null, null);
        // execute
        Map<Integer, List<WiFiDetails>> actual = fixture.getWiFiChannels(WiFiBand.FIVE);
        // validate
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetWiFiList() throws Exception {
        // setup
        fixture = new WiFiData(scanResults, null, null);
        // execute
        List<WiFiDetails> actual = fixture.getWiFiList(WiFiBand.TWO);
        // validate
        assertEquals(7, actual.size());
        assertEquals(scanResult1.BSSID, actual.get(0).getBSSID());
        assertEquals(scanResult4.BSSID, actual.get(6).getBSSID());
    }

}