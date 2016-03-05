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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

@RunWith(MockitoJUnitRunner.class)
public class WiFiDetailTest {
    private static final int FREQUENCY = 2435;
    private static final int LEVEL = -40;
    private static final String VENDOR_NAME = "VendorName";
    private static final String WPA = "WPA";
    private static final String SSID = "xyzSSID";
    private static final String BSSID = "xyzBSSID";

    @Mock
    private ScanResult scanResult;

    private WiFiSignal wiFiSignal;
    private WiFiAdditional wiFiAdditional;
    private WiFiDetail fixture;

    @Before
    public void setUp() throws Exception {
        wiFiAdditional = new WiFiAdditional(VENDOR_NAME, false);
        wiFiSignal = new WiFiSignal(FREQUENCY, LEVEL);
    }

    @Test
    public void testWiFiDetailWithScanResult() throws Exception {
        // setup
        scanResult.SSID = SSID;
        scanResult.BSSID = BSSID;
        scanResult.capabilities = WPA;
        scanResult.frequency = FREQUENCY;
        scanResult.level = LEVEL;
        // execute
        fixture = new WiFiDetail(scanResult, wiFiAdditional);
        // validate
        assertEquals(wiFiSignal, fixture.getWiFiSignal());
        assertNotSame(wiFiSignal, fixture.getWiFiSignal());
        assertEquals(wiFiAdditional, fixture.getWiFiAdditional());
        assertEquals(SSID, fixture.getSSID());
        assertEquals(BSSID, fixture.getBSSID());
        assertEquals(WPA, fixture.getCapabilities());
    }

    @Test
    public void testWiFiDetail() throws Exception {
        // execute
        fixture = new WiFiDetail(SSID, BSSID, WPA, wiFiSignal, wiFiAdditional);
        // validate
        assertEquals(wiFiSignal, fixture.getWiFiSignal());
        assertEquals(wiFiAdditional, fixture.getWiFiAdditional());
        assertEquals(SSID, fixture.getSSID());
        assertEquals(BSSID, fixture.getBSSID());
        assertEquals(WPA, fixture.getCapabilities());
        assertEquals(Security.WPA, fixture.getSecurity());
    }
}