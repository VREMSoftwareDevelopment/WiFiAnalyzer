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

import com.vrem.wifianalyzer.vendor.VendorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.class)
public class WiFiDataTest {
    public static final String VENDOR_NAME = "VendorName+";

    public static final int FREQUENCY1 = 2412;
    public static final int FREQUENCY2 = 2417;
    public static final int FREQUENCY3 = 2422;
    public static final int FREQUENCY4 = 2427;

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
        scanResults = Arrays.asList(scanResult_3,
                scanResult3,
                scanResult_2,
                scanResult1,
                scanResult_1,
                scanResult2,
                scanResult4);

        setUpScanResultGroups();
        setUpScanResultChildren(scanResult2);
        withVendorNames();
    }

    private void setUpScanResultChildren(ScanResult scanResult) {
        scanResult_1.SSID = scanResult_2.SSID = scanResult_3.SSID = scanResult.SSID;

        scanResult_1.BSSID = "B" + scanResult.SSID + "_1";
        scanResult_1.level = scanResult.level + 2;
        scanResult_1.frequency = scanResult.frequency;

        scanResult_2.BSSID = "B" + scanResult.SSID + "_2";
        scanResult_2.level = scanResult.level + 1;
        scanResult_2.frequency = scanResult.frequency;

        scanResult_3.BSSID = "B" + scanResult.SSID + "_3";
        scanResult_3.level = scanResult.level + 1;
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

        scanResult1.level = 1;
        scanResult2.level = 2;
        scanResult3.level = 0;
        scanResult4.level = 2;

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
        assertFalse(fixture.getConnection().isConnected());
        assertEquals(0, fixture.getParentsSize());
        assertNull(fixture.getParent(0));
        assertEquals(0, fixture.getChildrenSize(0));
        assertNull(fixture.getChild(0, 0));

        verify(vendorService, never()).getVendorName(anyString());
    }

    @Test
    public void testParentsWithoutConnectionGroupBySSID() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.SSID);
        // validate
        assertFalse(fixture.getConnection().isConnected());
        assertEquals(4, fixture.getParentsSize());
        assertEquals(scanResult3.SSID, fixture.getParent(0).getSSID());
        assertEquals(scanResult2.SSID, fixture.getParent(2).getSSID());
        assertEquals(scanResult4.SSID, fixture.getParent(3).getSSID());
    }

    @Test
    public void testVendorNameGroupBySSID() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.SSID);
        // validate
        assertEquals(VENDOR_NAME+scanResult3.BSSID, fixture.getParent(0).getVendorName());
        assertEquals(VENDOR_NAME+scanResult1.BSSID, fixture.getParent(1).getVendorName());
        assertEquals(VENDOR_NAME+scanResult2.BSSID, fixture.getParent(2).getVendorName());
        assertEquals(VENDOR_NAME+scanResult4.BSSID, fixture.getParent(3).getVendorName());

        verify(vendorService, times(7)).getVendorName(anyString());
    }

    @Test
    public void testChildrenWithoutConnectionGroupBySSID() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.SSID);
        // validate
        assertFalse(fixture.getConnection().isConnected());
        assertEquals(3, fixture.getChildrenSize(2));
        assertEquals(scanResult_2.BSSID, fixture.getChild(2, 0).getBSSID());
        assertEquals(scanResult_1.BSSID, fixture.getChild(2, 2).getBSSID());
    }

    @Test
    public void testParentsWithConnectionGroupBySSID() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(scanResult1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult1.BSSID);
        // execute
        fixture = new WiFiData(scanResults, wifiInfo, vendorService, GroupBy.SSID);
        // validate
        assertTrue(fixture.getConnection().isConnected());
        assertEquals(3, fixture.getParentsSize());
        assertEquals(scanResult3.SSID, fixture.getParent(0).getSSID());
        assertEquals(scanResult4.SSID, fixture.getParent(2).getSSID());
    }

    @Test
    public void testChildrenWithConnectionGroupBySSID() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(scanResult_1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult_1.BSSID);
        // execute
        fixture = new WiFiData(scanResults, wifiInfo, vendorService, GroupBy.SSID);
        // validate
        assertTrue(fixture.getConnection().isConnected());
        assertEquals(2, fixture.getChildrenSize(2));
        assertEquals(scanResult_2.BSSID, fixture.getChild(2, 0).getBSSID());
        assertEquals(scanResult_3.BSSID, fixture.getChild(2, 1).getBSSID());
    }

    @Test
    public void testParentsWithoutConnectionGroupByChannel() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.CHANNEL);
        // validate
        assertFalse(fixture.getConnection().isConnected());
        assertEquals(4, fixture.getParentsSize());
        assertEquals(scanResult3.SSID, fixture.getParent(0).getSSID());
        assertEquals(scanResult2.SSID, fixture.getParent(2).getSSID());
        assertEquals(scanResult4.SSID, fixture.getParent(3).getSSID());
    }

    @Test
    public void testVendorNameGroupByChannel() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.CHANNEL);
        // validate
        assertEquals(VENDOR_NAME + scanResult3.BSSID, fixture.getParent(0).getVendorName());
        assertEquals(VENDOR_NAME + scanResult1.BSSID, fixture.getParent(1).getVendorName());
        assertEquals(VENDOR_NAME + scanResult2.BSSID, fixture.getParent(2).getVendorName());
        assertEquals(VENDOR_NAME + scanResult4.BSSID, fixture.getParent(3).getVendorName());

        verify(vendorService, times(7)).getVendorName(anyString());
    }

    @Test
    public void testChildrenWithoutConnectionGroupByChannel() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null, vendorService, GroupBy.CHANNEL);
        // validate
        assertFalse(fixture.getConnection().isConnected());
        assertEquals(3, fixture.getChildrenSize(2));
        assertEquals(scanResult_2.BSSID, fixture.getChild(2, 0).getBSSID());
        assertEquals(scanResult_1.BSSID, fixture.getChild(2, 2).getBSSID());
    }

    @Test
    public void testParentsWithConnectionGroupByChannel() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(scanResult1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult1.BSSID);
        // execute
        fixture = new WiFiData(scanResults, wifiInfo, vendorService, GroupBy.CHANNEL);
        // validate
        assertTrue(fixture.getConnection().isConnected());
        assertEquals(3, fixture.getParentsSize());
        assertEquals(scanResult3.SSID, fixture.getParent(0).getSSID());
        assertEquals(scanResult4.SSID, fixture.getParent(2).getSSID());
    }

    @Test
    public void testChildrenWithConnectionGroupByChannel() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(scanResult_1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult_1.BSSID);
        // execute
        fixture = new WiFiData(scanResults, wifiInfo, vendorService, GroupBy.CHANNEL);
        // validate
        assertTrue(fixture.getConnection().isConnected());
        assertEquals(2, fixture.getChildrenSize(2));
        assertEquals(scanResult_2.BSSID, fixture.getChild(2, 0).getBSSID());
        assertEquals(scanResult_3.BSSID, fixture.getChild(2, 1).getBSSID());
    }
}