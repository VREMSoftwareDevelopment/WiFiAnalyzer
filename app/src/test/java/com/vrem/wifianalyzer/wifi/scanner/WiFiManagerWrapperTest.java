/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WiFiManagerWrapperTest {
    @Mock
    private WifiManager wifiManager;
    @Mock
    private WiFiSwitch wiFiSwitch;
    @Mock
    private WifiInfo wifiInfo;

    private WiFiManagerWrapper fixture;

    @Before
    public void setUp() {
        fixture = new WiFiManagerWrapper(wifiManager);
        fixture.setWiFiSwitch(wiFiSwitch);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(wifiManager);
        verifyNoMoreInteractions(wiFiSwitch);
    }

    @Test
    public void testIsWifiEnabled() {
        // setup
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        // execute
        boolean actual = fixture.isWifiEnabled();
        // validate
        assertTrue(actual);
        verify(wifiManager).isWifiEnabled();
    }

    @Test
    public void testIsWifiEnabledWithException() {
        // setup
        when(wifiManager.isWifiEnabled()).thenThrow(new RuntimeException());
        // execute
        boolean actual = fixture.isWifiEnabled();
        // validate
        assertFalse(actual);
        verify(wifiManager).isWifiEnabled();
    }

    @Test
    public void testEnableWiFi() {
        // setup
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        // execute
        boolean actual = fixture.enableWiFi();
        // validate
        assertTrue(actual);
        verify(wifiManager).isWifiEnabled();
        verify(wifiManager, never()).setWifiEnabled(anyBoolean());
    }

    @Test
    public void testEnableWiFiWhenDisabled() {
        // setup
        when(wifiManager.isWifiEnabled()).thenReturn(false);
        when(wiFiSwitch.setEnabled(true)).thenReturn(true);
        // execute
        boolean actual = fixture.enableWiFi();
        // validate
        assertTrue(actual);
        verify(wifiManager).isWifiEnabled();
        verify(wiFiSwitch).setEnabled(true);
    }

    @Test
    public void testEnableWiFiWithException() {
        // setup
        when(wifiManager.isWifiEnabled()).thenReturn(false);
        when(wiFiSwitch.setEnabled(true)).thenThrow(new RuntimeException());
        // execute
        boolean actual = fixture.enableWiFi();
        // validate
        assertFalse(actual);
        verify(wifiManager).isWifiEnabled();
        verify(wiFiSwitch).setEnabled(true);
    }

    @Test
    public void testDisableWiFi() {
        // setup
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        when(wiFiSwitch.setEnabled(false)).thenReturn(true);
        // execute
        boolean actual = fixture.disableWiFi();
        // validate
        assertTrue(actual);
        verify(wifiManager).isWifiEnabled();
        verify(wiFiSwitch).setEnabled(false);
    }

    @Test
    public void testDisableWiFiWhenDisabled() {
        // setup
        when(wifiManager.isWifiEnabled()).thenReturn(false);
        // execute
        boolean actual = fixture.disableWiFi();
        // validate
        assertTrue(actual);
        verify(wifiManager).isWifiEnabled();
        verify(wiFiSwitch, never()).setEnabled(anyBoolean());
    }

    @Test
    public void testDisableWiFiWithException() {
        // setup
        when(wifiManager.isWifiEnabled()).thenReturn(true);
        when(wiFiSwitch.setEnabled(false)).thenThrow(new RuntimeException());
        // execute
        boolean actual = fixture.disableWiFi();
        // validate
        assertFalse(actual);
        verify(wifiManager).isWifiEnabled();
        verify(wiFiSwitch).setEnabled(false);
    }

    @Test
    public void testStartScan() {
        // setup
        when(wifiManager.startScan()).thenReturn(true);
        // execute
        boolean actual = fixture.startScan();
        // validate
        assertTrue(actual);
        verify(wifiManager).startScan();
    }

    @Test
    public void testStartScanWithException() {
        // setup
        when(wifiManager.startScan()).thenThrow(new RuntimeException());
        // execute
        boolean actual = fixture.startScan();
        // validate
        assertFalse(actual);
        verify(wifiManager).startScan();
    }

    @Test
    public void testScanResults() {
        // setup
        List<ScanResult> expected = Collections.emptyList();
        when(wifiManager.getScanResults()).thenReturn(expected);
        // execute
        List<ScanResult> actual = fixture.scanResults();
        // validate
        assertSame(expected, actual);
        verify(wifiManager).getScanResults();
    }

    @Test
    public void testScanResultsWhenWiFiManagerReturnsNullScanResults() {
        // setup
        when(wifiManager.getScanResults()).thenReturn(null);
        // execute
        List<ScanResult> actual = fixture.scanResults();
        // validate
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
        verify(wifiManager).getScanResults();
    }

    @Test
    public void testScanResultsWithException() {
        // setup
        when(wifiManager.getScanResults()).thenThrow(new RuntimeException());
        // execute
        List<ScanResult> actual = fixture.scanResults();
        // validate
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
        verify(wifiManager).getScanResults();
    }

    @Test
    public void testWiFiInfo() {
        // setup
        when(wifiManager.getConnectionInfo()).thenReturn(wifiInfo);
        // execute
        WifiInfo actual = fixture.wiFiInfo();
        // validate
        assertSame(wifiInfo, actual);
        verify(wifiManager).getConnectionInfo();
    }

    @Test
    public void testWiFiInfoWithException() {
        // setup
        when(wifiManager.getConnectionInfo()).thenThrow(new RuntimeException());
        // execute
        WifiInfo actual = fixture.wiFiInfo();
        // validate
        assertNull(actual);
        verify(wifiManager).getConnectionInfo();
    }

}
