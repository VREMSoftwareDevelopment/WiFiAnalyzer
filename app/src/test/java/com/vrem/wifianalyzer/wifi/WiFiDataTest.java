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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WiFiDataTest {
    @Mock
    private WifiInfo wifiInfo;
    @Mock
    private ScanResult scanResult1;
    @Mock
    private ScanResult scanResult2;
    @Mock
    private ScanResult scanResult3;
    @Mock
    private ScanResult scanResult4;
    @Mock
    private ScanResult scanResult_1;
    @Mock
    private ScanResult scanResult_2;
    @Mock
    private ScanResult scanResult_3;

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
        setUpScanResultChildren(scanResult2.SSID, scanResult2.level);
    }

    private void setUpScanResultChildren(String SSID, int level) {
        scanResult_1.SSID = scanResult_2.SSID = scanResult_3.SSID = SSID;

        scanResult_1.BSSID = "B" + SSID + "_1";
        scanResult_1.level = level + 2;

        scanResult_2.BSSID = "B" + SSID + "_2";
        scanResult_2.level = level + 1;

        scanResult_3.BSSID = "B" + SSID + "_3";
        scanResult_3.level = level + 1;
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
    }

    @Test
    public void testParentsWithoutConnection() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null);
        // validate
        assertFalse(fixture.getConnection().isConnected());
        assertEquals(4, fixture.getParentsSize());
        assertEquals(scanResult3.SSID, fixture.getParent(0).getSSID());
        assertEquals(scanResult2.SSID, fixture.getParent(2).getSSID());
        assertEquals(scanResult4.SSID, fixture.getParent(3).getSSID());
    }

    @Test
    public void testChildrenWithoutConnection() throws Exception {
        // execute
        fixture = new WiFiData(scanResults, null);
        // validate
        assertFalse(fixture.getConnection().isConnected());
        assertEquals(3, fixture.getChildrenSize(2));
        assertEquals(scanResult_2.BSSID, fixture.getChild(2, 0).getBSSID());
        assertEquals(scanResult_1.BSSID, fixture.getChild(2, 2).getBSSID());
    }

    @Test
    public void testParentsWithConnection() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(scanResult1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult1.BSSID);
        // execute
        fixture = new WiFiData(scanResults, wifiInfo);
        // validate
        assertTrue(fixture.getConnection().isConnected());
        assertEquals(3, fixture.getParentsSize());
        assertEquals(scanResult3.SSID, fixture.getParent(0).getSSID());
        assertEquals(scanResult4.SSID, fixture.getParent(2).getSSID());
    }

    @Test
    public void testChildrenWithConnection() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(scanResult_1.SSID);
        when(wifiInfo.getBSSID()).thenReturn(scanResult_1.BSSID);
        // execute
        fixture = new WiFiData(scanResults, wifiInfo);
        // validate
        assertTrue(fixture.getConnection().isConnected());
        assertEquals(2, fixture.getChildrenSize(2));
        assertEquals(scanResult_2.BSSID, fixture.getChild(2, 0).getBSSID());
        assertEquals(scanResult_3.BSSID, fixture.getChild(2, 1).getBSSID());
    }

}