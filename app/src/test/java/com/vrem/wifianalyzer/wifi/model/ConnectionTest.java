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
import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionTest {
    private static final String SSID = "SSID-123";
    private static final String BSSID = "BSSID-123";
    private static final String SSID_QUOTED = "\"SSID-123\"";

    @Mock private WifiInfo wifiInfo;
    @Mock private ScanResult scanResult;

    private Connection fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new Connection(wifiInfo);

    }

    @Test
    public void testGetIPAddress() throws Exception {
        // setup
        when(wifiInfo.getIpAddress()).thenReturn(123456789);
        // execute
        String actual = fixture.getIPAddress();
        // validate
        verify(wifiInfo).getIpAddress();
        assertEquals("21.205.91.7", actual);
    }

    @Test
    public void testIpAddressInvalid() throws Exception {
        // setup
        when(wifiInfo.getIpAddress()).thenReturn(123);
        // execute
        String actual = fixture.getIPAddress();
        // validate
        verify(wifiInfo).getIpAddress();
        assertEquals(StringUtils.EMPTY, actual);
    }

    @Test
    public void testMatches() throws Exception {
        // setup
        withConnection(SSID);
        // execute
        boolean actual = fixture.matches(scanResult);
        // validate
        verifyConnection();
        assertTrue(actual);
    }

    @Test
    public void testMatchesWithNull() throws Exception {
        assertFalse(fixture.matches(null));
    }

    @Test
    public void testMatchesWithSSIDQuotes() throws Exception {
        // setup
        withConnection(SSID_QUOTED);
        // execute
        boolean actual = fixture.matches(scanResult);
        // validate
        verifyConnection();
        assertTrue(actual);
    }

    private void withConnection(@NonNull String ssid) {
        when(wifiInfo.getNetworkId()).thenReturn(0);

        when(wifiInfo.getSSID()).thenReturn(ssid);
        when(wifiInfo.getBSSID()).thenReturn(BSSID);

        scanResult.SSID = SSID;
        scanResult.BSSID = BSSID;
    }

    private void verifyConnection() {
        verify(wifiInfo).getNetworkId();

        verify(wifiInfo).getSSID();
        verify(wifiInfo).getBSSID();
    }

}