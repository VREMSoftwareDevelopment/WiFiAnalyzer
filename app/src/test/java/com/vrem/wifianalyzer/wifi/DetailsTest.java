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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WifiManager.class)
public class DetailsTest {
    public static final String MY_IP_ADDRESS = "MyIPAddress";
    @Mock private ScanResult scanResult;

    private Details fixture;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Before
    public void setUp() throws Exception {
        fixture = Details.make(scanResult, MY_IP_ADDRESS);
    }

    @Test
    public void testIsConnected() throws Exception {
        assertTrue(fixture.isConnected());
    }

    @Test
    public void testIsNotConnected() throws Exception {
        fixture = Details.make(scanResult, "");
        assertFalse(fixture.isConnected());
    }

    @Test
    public void testGetIPAddress() throws Exception {
        // execute
        String actual = fixture.getIpAddress();
        // validate
        assertEquals(MY_IP_ADDRESS, actual);
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
        PowerMockito.mockStatic(WifiManager.class);
        Strength expected = Strength.TWO;
        scanResult.level = -86;
        // expected
        when(WifiManager.calculateSignalLevel(scanResult.level, Strength.values().length)).thenReturn(expected.ordinal());
        // execute
        Strength actual = fixture.getStrength();
        // validate
        assertEquals(expected, actual);
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
        testDistance(2437, -36, "0.62");
        testDistance(2437, -42, "1.23");
        testDistance(2432, -88, "246.34");
        testDistance(2412, -91, "350.85");
    }

    private void testDistance(int frequency, int level, String expected) throws Exception {
        // setup
        scanResult.frequency = frequency;
        scanResult.level = level;
        // execute
        double actual = fixture.getDistance();
        // validate
        assertEquals(expected, decimalFormat.format(actual));
    }
}