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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WiFiTest {

    @Mock private WifiManager wifiManager;
    @Mock private ScanResult scanResult;
    @Mock private WifiInfo wifiInfo;
    private WiFi fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new WiFi(wifiManager);
    }

    @Test
    public void testEnableWhenIsWiFiEnabledTrue() throws Exception {
        // setup
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        // execute
        boolean actual = fixture.enable();
        // verify
        assertTrue(actual);
        verify(wifiManager).isWifiEnabled();
    }

    @Test
    public void testEnableWhenIsWiFiEnabledFalse() throws Exception {
        // setup
        when(wifiManager.isWifiEnabled()).thenReturn(false);
        when(wifiManager.setWifiEnabled(true)).thenReturn(true);
        // execute
        boolean actual = fixture.enable();
        // verify
        assertTrue(actual);
        verify(wifiManager).isWifiEnabled();
        verify(wifiManager).setWifiEnabled(true);
    }

    @Test
    public void testScanWithStartScanFalse() throws Exception {
        // setup
        when(wifiManager.startScan()).thenReturn(false);
        // execute
        WiFiInformation actual = fixture.scan();
        // verify
        assertEquals(0, actual.getParentsSize());
        verify(wifiManager).startScan();
        verify(wifiManager, never()).getScanResults();
        verify(wifiManager, never()).getConnectionInfo();
    }

    @Test
    public void testScanWithStartScanTrue() throws Exception {
        // setup
        List<ScanResult> scanResults = new ArrayList<>();
        Mockito.when(wifiManager.startScan()).thenReturn(true);
        Mockito.when(wifiManager.getScanResults()).thenReturn(scanResults);
        Mockito.when(wifiManager.getConnectionInfo()).thenReturn(wifiInfo);
        // execute
        WiFiInformation actual = fixture.scan();
        // verify
        assertEquals(fixture.getWifiInformation(), actual);
        verify(wifiManager).startScan();
        verify(wifiManager).getScanResults();
        verify(wifiManager).getConnectionInfo();
    }
}