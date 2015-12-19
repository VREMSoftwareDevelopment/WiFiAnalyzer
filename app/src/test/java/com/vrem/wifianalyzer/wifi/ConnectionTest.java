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

import android.net.wifi.WifiInfo;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class ConnectionTest {
    @Mock private WifiInfo wifiInfo;

    private Connection fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new Connection(wifiInfo);
    }

    @Test
    public void testIsConnected() throws Exception {
        assertTrue(fixture.isConnected());
    }

    @Test
    public void testIsNotConnected() throws Exception {
        fixture = new Connection(null);
        assertFalse(fixture.isConnected());
    }

    @Test
    public void testGetIpAddress() throws Exception {
        // setup
        when(wifiInfo.getIpAddress()).thenReturn(123456789);
        // execute
        String actual = fixture.getIpAddress();
        // validate
        assertEquals("21.205.91.7", actual);
        verify(wifiInfo).getIpAddress();
    }

    @Test
    public void testGetIpAddressInvalid() throws Exception {
        // setup
        mockStatic(Log.class);
        when(wifiInfo.getIpAddress()).thenReturn(123);
        when(Log.e("IPAddress", "java.net.UnknownHostException: addr is of illegal length")).thenReturn(0);
        // execute
        String actual = fixture.getIpAddress();
        // validate
        assertEquals("", actual);
        verify(wifiInfo).getIpAddress();
        verifyStatic();
    }

    @Test
    public void testGetFrequency() throws Exception {
        // setup
        int expected = 2470;
        when(wifiInfo.getFrequency()).thenReturn(expected);
        // execute
        int actual = fixture.getFrequency();
        // validate
        assertEquals(expected, actual);
        verify(wifiInfo).getFrequency();
    }

    @Test
    public void testGetRssi() throws Exception {
        // setup
        int expected = -55;
        when(wifiInfo.getRssi()).thenReturn(expected);
        // execute
        int actual = fixture.getRssi();
        // validate
        assertEquals(Math.abs(expected), actual);
        verify(wifiInfo).getRssi();
    }

    @Test
    public void testGetBSSID() throws Exception {
        // setup
        String expected = "23:34";
        when(wifiInfo.getBSSID()).thenReturn(expected);
        // execute
        String actual = fixture.getBSSID();
        // validate
        assertEquals(expected, actual);
        verify(wifiInfo).getBSSID();
    }

    @Test
    public void testGetSSID() throws Exception {
        // setup
        String expected = "Hello1234";
        when(wifiInfo.getSSID()).thenReturn(expected);
        // execute
        String actual = fixture.getSSID();
        // validate
        assertEquals(expected, actual);
        verify(wifiInfo).getSSID();
    }

    @Test
    public void testGetSSIDWithQuotes() throws Exception {
        // setup
        String input = "\"Hello1234\"";
        String expected = "Hello1234";
        when(wifiInfo.getSSID()).thenReturn(expected);
        // execute
        String actual = fixture.getSSID();
        // validate
        assertEquals(expected, actual);
        verify(wifiInfo).getSSID();
    }

    @Test
    public void testGetDistance() throws Exception {
        // setup
        int frequency = 2414;
        int rssi = -50;
        when(wifiInfo.getFrequency()).thenReturn(frequency);
        when(wifiInfo.getRssi()).thenReturn(rssi);
        double expected = Distance.calculate(frequency, rssi);
        // execute
        double actual = fixture.getDistance();
        // validate
        assertEquals(expected, actual, 0.0);
        verify(wifiInfo).getFrequency();
        verify(wifiInfo).getRssi();
    }

}