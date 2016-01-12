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
import android.net.wifi.WifiManager;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WifiManager.class)
public class DetailsTest {
    private static final String VENDOR_NAME = "VendorName";

    @Mock private ScanResult scanResult;

    private Details fixture;

    @Before
    public void setUp() throws Exception {
        fixture = Details.makeScanResult(scanResult, VENDOR_NAME, false);
    }

    @Test
    public void testGetVendorName() throws Exception {
        // validate
        assertEquals(VENDOR_NAME, fixture.getVendorName());
    }

    @Test
    public void testIsConfiguredNetwork() throws Exception {
        // validate
        assertFalse(fixture.isConfiguredNetwork());
    }

    @Test
    public void testGetFrequency() throws Exception {
        // setup
        scanResult.frequency = 2470;
        // execute
        int actual = fixture.getFrequency();
        // validate
        assertEquals(scanResult.frequency, actual);
    }

    @Test
    public void testGetChannel() throws Exception {
        // setup
        int expected = 5;
        scanResult.frequency = 2435;
        // execute
        int actual = fixture.getChannel();
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSecurity() throws Exception {
        // setup
        Security expected = Security.WPA;
        scanResult.capabilities = "WPA";
        // execute
        Security actual = fixture.getSecurity();
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStrength() throws Exception {
        // setup
        mockStatic(WifiManager.class);
        Strength expected = Strength.TWO;
        scanResult.level = -86;
        when(WifiManager.calculateSignalLevel(scanResult.level, Strength.values().length)).thenReturn(expected.ordinal());
        // execute
        Strength actual = fixture.getStrength();
        // validate
        assertEquals(expected, actual);
        verifyStatic();
    }

    @Test
    public void testGetSSID() throws Exception {
        // setup
        scanResult.SSID = "xyzSSID";
        // execute
        String actual = fixture.getSSID();
        // validate
        assertEquals(scanResult.SSID, actual);
    }

    @Test
    public void testGetBSSID() throws Exception {
        // setup
        scanResult.BSSID = "xyzBSSID";
        // execute
        String actual = fixture.getBSSID();
        // validate
        assertEquals(scanResult.BSSID, actual);
    }

    @Test
    public void testGetLevel() throws Exception {
        // setup
        scanResult.level = -3;
        // execute
        int actual = fixture.getLevel();
        // validate
        assertEquals(Math.abs(scanResult.level), actual);
    }

    @Test
    public void testGetCapabilities() throws Exception {
        // setup
        scanResult.capabilities = "xyzCapabilities";
        // execute
        String actual = fixture.getCapabilities();
        // validate
        assertEquals(scanResult.capabilities, actual);
    }

    @Test
    public void testGetDistance() throws Exception {
        // setup
        scanResult.frequency = 2414;
        scanResult.level = -50;
        double expected = Distance.calculate(scanResult.frequency, scanResult.level);
        // execute
        double actual = fixture.getDistance();
        // validate
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetIPAddressEmpty() throws Exception {
        assertEquals(StringUtils.EMPTY, fixture.getIPAddress());
        assertFalse(fixture.isConnected());
    }

    @Test
    public void testGetIPAddress() throws Exception {
        fixture = Details.makeConnection(scanResult, VENDOR_NAME, "IPAddress");
        assertEquals("IPAddress", fixture.getIPAddress());
        assertTrue(fixture.isConnected());
        assertTrue(fixture.isConfiguredNetwork());
    }

    @Test
    public void testEquals() throws Exception {
        // setup
        scanResult.SSID = "getSSID";
        scanResult.BSSID = "getBSSID";
        Details other = Details.makeScanResult(scanResult, VENDOR_NAME, false);
        // execute & validate
        // validate
        assertEquals(fixture, other);
        assertNotSame(fixture, other);
    }

    @Test
    public void testHashCode() throws Exception {
        // setup
        scanResult.SSID = "getSSID";
        scanResult.BSSID = "getBSSID";
        Details other = Details.makeScanResult(scanResult, VENDOR_NAME, false);
        // execute & validate
        assertEquals(fixture.hashCode(), other.hashCode());
    }

}