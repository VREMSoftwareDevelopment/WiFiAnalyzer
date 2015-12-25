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

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.vrem.wifianalyzer.vendor.VendorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WifiManager.class)
public class WiFiDataTest {
    public static final String VENDOR_NAME = "VendorName+";

    public static final int FREQUENCY1 = 2412;
    public static final int FREQUENCY2 = 2417;
    public static final int FREQUENCY3 = 2422;
    public static final int FREQUENCY4 = 2427;

    public static final int LEVEL0 = 0;
    public static final int LEVEL1 = 1;
    public static final int LEVEL2 = 2;
    public static final int LEVEL3 = 3;
    public static final int LEVEL4 = 4;

    @Mock private WifiInfo wifiInfo;
    @Mock private VendorService vendorService;
    @Mock private ScanResult scanResult1;
    @Mock private ScanResult scanResult2;
    @Mock private ScanResult scanResult3;
    @Mock private ScanResult scanResult4;
    @Mock private ScanResult scanResult_1;
    @Mock private ScanResult scanResult_2;
    @Mock private ScanResult scanResult_3;

    private List<ScanResult> scanResults;
    private WiFiData fixture;

    @Before
    public void setUp() throws Exception {
        mockStatic(WifiManager.class);

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
    }

    private void withSignalLevel() {
        PowerMockito.when(WifiManager.calculateSignalLevel(LEVEL0, Strength.values().length)).thenReturn(LEVEL0);
        PowerMockito.when(WifiManager.calculateSignalLevel(LEVEL1, Strength.values().length)).thenReturn(LEVEL1);
        PowerMockito.when(WifiManager.calculateSignalLevel(LEVEL2, Strength.values().length)).thenReturn(LEVEL2);
        PowerMockito.when(WifiManager.calculateSignalLevel(LEVEL3, Strength.values().length)).thenReturn(LEVEL3);
        PowerMockito.when(WifiManager.calculateSignalLevel(LEVEL4, Strength.values().length)).thenReturn(LEVEL4);
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
            when(vendorService.getVendorName(scanResult.BSSID)).thenReturn(VENDOR_NAME + scanResult.BSSID);
        }
    }

    @Test
    public void testEmptyObject() throws Exception {
        // execute
        fixture = new WiFiData();
        // validate
        assertFalse(fixture.connection().connected());
        assertEquals(0, fixture.parentsCount());
        assertNull(fixture.parent(0));
        assertEquals(0, fixture.childrenCount(0));
        assertNull(fixture.child(0, 0));

        verify(vendorService, never()).getVendorName(anyString());
    }

    @Test
    public void testParentsWithoutConnectionGroupBySSID() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.SSID, false);
        // validate
        assertFalse(fixture.connection().connected());
        assertEquals(4, fixture.parentsCount());
        assertEquals(scanResult3.SSID, fixture.parent(0).SSID());
        assertEquals(scanResult2.SSID, fixture.parent(2).SSID());
        assertEquals(scanResult4.SSID, fixture.parent(3).SSID());
    }

    @Test
    public void testVendorNameGroupBySSID() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.SSID, false);
        // validate
        assertEquals(VENDOR_NAME + scanResult3.BSSID, fixture.parent(0).vendorName());
        assertEquals(VENDOR_NAME + scanResult1.BSSID, fixture.parent(1).vendorName());
        assertEquals(VENDOR_NAME + scanResult2.BSSID, fixture.parent(2).vendorName());
        assertEquals(VENDOR_NAME + scanResult4.BSSID, fixture.parent(3).vendorName());

        verify(vendorService, times(7)).getVendorName(anyString());
    }

    @Test
    public void testChildrenWithoutConnectionGroupBySSID() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.SSID, false);
        // validate
        assertFalse(fixture.connection().connected());
        assertEquals(3, fixture.childrenCount(2));
        assertEquals(scanResult_2.BSSID, fixture.child(2, 0).BSSID());
        assertEquals(scanResult_1.BSSID, fixture.child(2, 2).BSSID());
    }

    @Test
    public void testParentsWithConnectionGroupBySSID() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(scanResult1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult1.BSSID);
        // execute
        fixture = new WiFiData(scanResults, wifiInfo, vendorService, GroupBy.SSID, false);
        // validate
        assertTrue(fixture.connection().connected());
        assertEquals(3, fixture.parentsCount());
        assertEquals(scanResult3.SSID, fixture.parent(0).SSID());
        assertEquals(scanResult4.SSID, fixture.parent(2).SSID());
    }

    @Test
    public void testChildrenWithConnectionGroupBySSID() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(scanResult_1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult_1.BSSID);
        // execute
        fixture = new WiFiData(scanResults, wifiInfo, vendorService, GroupBy.SSID, false);
        // validate
        assertTrue(fixture.connection().connected());
        assertEquals(2, fixture.childrenCount(2));
        assertEquals(scanResult_2.BSSID, fixture.child(2, 0).BSSID());
        assertEquals(scanResult_3.BSSID, fixture.child(2, 1).BSSID());
    }

    @Test
    public void testParentsWithoutConnectionGroupByChannel() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.CHANNEL, false);
        // validate
        assertFalse(fixture.connection().connected());
        assertEquals(4, fixture.parentsCount());
        assertEquals(scanResult3.SSID, fixture.parent(0).SSID());
        assertEquals(scanResult2.SSID, fixture.parent(2).SSID());
        assertEquals(scanResult4.SSID, fixture.parent(3).SSID());
    }

    @Test
    public void testVendorNameGroupByChannel() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.CHANNEL, false);
        // validate
        assertEquals(VENDOR_NAME + scanResult3.BSSID, fixture.parent(0).vendorName());
        assertEquals(VENDOR_NAME + scanResult1.BSSID, fixture.parent(1).vendorName());
        assertEquals(VENDOR_NAME + scanResult2.BSSID, fixture.parent(2).vendorName());
        assertEquals(VENDOR_NAME + scanResult4.BSSID, fixture.parent(3).vendorName());

        verify(vendorService, times(7)).getVendorName(anyString());
    }

    @Test
    public void testChildrenWithoutConnectionGroupByChannel() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.CHANNEL, false);
        // validate
        assertFalse(fixture.connection().connected());
        assertEquals(3, fixture.childrenCount(2));
        assertEquals(scanResult_2.BSSID, fixture.child(2, 0).BSSID());
        assertEquals(scanResult_1.BSSID, fixture.child(2, 2).BSSID());
    }

    @Test
    public void testParentsWithConnectionGroupByChannel() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(scanResult1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult1.BSSID);
        // execute
        fixture = new WiFiData(scanResults, wifiInfo, vendorService, GroupBy.CHANNEL, false);
        // validate
        assertTrue(fixture.connection().connected());
        assertEquals(3, fixture.parentsCount());
        assertEquals(scanResult3.SSID, fixture.parent(0).SSID());
        assertEquals(scanResult4.SSID, fixture.parent(2).SSID());
    }

    @Test
    public void testChildrenWithConnectionGroupByChannel() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(scanResult_1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult_1.BSSID);
        // execute
        fixture = new WiFiData(scanResults, wifiInfo, vendorService, GroupBy.CHANNEL, false);
        // validate
        assertTrue(fixture.connection().connected());
        assertEquals(2, fixture.childrenCount(2));
        assertEquals(scanResult_2.BSSID, fixture.child(2, 0).BSSID());
        assertEquals(scanResult_3.BSSID, fixture.child(2, 1).BSSID());
    }

    @Test
    public void testHideWeakSignal() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(scanResult1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult1.BSSID);
        // execute
        fixture = new WiFiData(scanResults, wifiInfo, vendorService, GroupBy.SSID, true);
        // validate
        assertTrue(fixture.connection().connected());
        assertEquals(2, fixture.parentsCount());
    }

}