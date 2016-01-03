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
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WifiManager.class, Log.class})
public class ConnectionTest {
/*
    @Mock private WifiInfo wifiInfo;
    @Mock private ScanResult scanResult;

    private DetailsInfo detailsInfo;
    private Connection fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new Connection(wifiInfo);
        detailsInfo = makeDetailsInfo();
    }

    @Test
    public void testConnected() throws Exception {
        // setup
        when(wifiInfo.getNetworkId()).thenReturn(0);
        // execute and validate
        assertTrue(fixture.connected());
        verify(wifiInfo).getNetworkId();
    }

    @Test
    public void testNotConnectedWithInvalidNetworkId() throws Exception {
        // setup
        when(wifiInfo.getNetworkId()).thenReturn(-1);
        // execute and validate
        assertFalse(fixture.connected());
        verify(wifiInfo).getNetworkId();
    }

    @Test
    public void testNotConnectedWithNullWiFiInfo() throws Exception {
        // setup
        fixture = new Connection(null);
        // execute and validate
        assertFalse(fixture.connected());
    }

    @Test
    public void testIpAddress() throws Exception {
        // setup
        when(wifiInfo.getIpAddress()).thenReturn(123456789);
        // execute
        String actual = fixture.ipAddress();
        // validate
        assertEquals("21.205.91.7", actual);
        verify(wifiInfo).getIpAddress();
    }

    @Test
    public void testIpAddressInvalid() throws Exception {
        // setup
        mockStatic(Log.class);
        when(wifiInfo.getIpAddress()).thenReturn(123);
        when(Log.e("IPAddress", "java.net.UnknownHostException: addr is of illegal length")).thenReturn(0);
        // execute
        String actual = fixture.ipAddress();
        // validate
        assertEquals(StringUtils.EMPTY, actual);
        verify(wifiInfo).getIpAddress();
        verifyStatic();
    }

    @Test
    public void testBSSID() throws Exception {
        // setup
        when(wifiInfo.getBSSID()).thenReturn(detailsInfo.getBSSID());
        // execute
        String actual = fixture.BSSID();
        // validate
        assertEquals(detailsInfo.getBSSID(), actual);
        verify(wifiInfo).getBSSID();
    }

    @Test
    public void testSSID() throws Exception {
        // setup
        when(wifiInfo.getSSID()).thenReturn(detailsInfo.getSSID());
        // execute
        String actual = fixture.SSID();
        // validate
        assertEquals(detailsInfo.getSSID(), actual);
        verify(wifiInfo).getSSID();
    }

    @Test
    public void testSSIDWithQuotes() throws Exception {
        // setup
        String expected = "\"Hello1234\"";
        when(wifiInfo.getSSID()).thenReturn(expected);
        // execute
        String actual = fixture.SSID();
        // validate
        assertEquals(expected.replace("\"", ""), actual);
        verify(wifiInfo).getSSID();
    }

    @Test
    public void testAddDetails() throws Exception {
        // setup
        withSSIDAndBSSID();
        // execute
        boolean actual = fixture.detailsInfo(detailsInfo);
        // validate
        assertTrue(actual);
        assertEquals(detailsInfo, fixture.detailsInfo());
        assertSame(detailsInfo, fixture.detailsInfo());
        validateSSIDAndBSSID();
    }

    @Test
    public void testAddDetailsFails() throws Exception {
        // setup
        fixture = new Connection(null);
        // execute
        boolean actual = fixture.detailsInfo(detailsInfo);
        // execute
        assertFalse(actual);
        verify(wifiInfo, never()).getSSID();
        verify(wifiInfo, never()).getBSSID();
    }

    @Test
    public void testHasDetails() throws Exception {
        // setup
        withSSIDAndBSSID();
        fixture.detailsInfo(detailsInfo);
        // execute and validate
        assertTrue(fixture.hasDetails());
        validateSSIDAndBSSID();
    }

    private void validateSSIDAndBSSID() {
        verify(wifiInfo).getSSID();
        verify(wifiInfo).getBSSID();
    }

    private void withSSIDAndBSSID() {
        when(wifiInfo.getSSID()).thenReturn(detailsInfo.getSSID());
        when(wifiInfo.getBSSID()).thenReturn(detailsInfo.getBSSID());
    }

    @Test
    public void testDoesNotHaveDetails() throws Exception {
        // execute and validate
        assertFalse(fixture.hasDetails());
    }

    private DetailsInfo makeDetailsInfo() {
        return new DummyDetails(scanResult, "getSSID-123", "getBSSID-123", 0);
    }
*/
}