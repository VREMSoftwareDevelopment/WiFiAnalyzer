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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WiFiTest {

    @Mock private WifiManager wifiManager;
    private WiFi fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new WiFi(wifiManager);
    }

    @Test
    public void testEnableWhenIsWiFiEnabledTrue() throws Exception {
        // expected
        Mockito.when(wifiManager.isWifiEnabled()).thenReturn(true);
        // execute
        boolean actual = fixture.enable();
        // verify
        assertTrue(actual);
        Mockito.verify(wifiManager).isWifiEnabled();
    }

    @Test
    public void testEnableWhenIsWiFiEnabledFalse() throws Exception {
        // expected
        Mockito.when(wifiManager.isWifiEnabled()).thenReturn(false);
        Mockito.when(wifiManager.setWifiEnabled(true)).thenReturn(true);
        // execute
        boolean actual = fixture.enable();
        // verify
        assertTrue(actual);
        Mockito.verify(wifiManager).isWifiEnabled();
        Mockito.verify(wifiManager).setWifiEnabled(true);
    }

    @Test
    public void testScanWithStartScanFalse() throws Exception {
        // expected
        Mockito.when(wifiManager.startScan()).thenReturn(false);
        // execute
        List<ScanResult> actual = fixture.scan();
        // verify
        assertTrue(actual.isEmpty());
        Mockito.verify(wifiManager).startScan();
    }

    @Test
    public void testScanWithStartScanTrue() throws Exception {
        // setup
        List<ScanResult> expected = new ArrayList<>();
        // expected
        Mockito.when(wifiManager.startScan()).thenReturn(true);
        Mockito.when(wifiManager.getScanResults()).thenReturn(expected);
        // execute
        List<ScanResult> actual = fixture.scan();
        // verify
        assertEquals(expected, actual);
        assertSame(expected, actual);
        Mockito.verify(wifiManager).startScan();
        Mockito.verify(wifiManager).getScanResults();
    }
}