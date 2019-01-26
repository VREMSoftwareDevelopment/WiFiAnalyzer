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
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScannerTest {
    @Mock
    private Handler handler;
    @Mock
    private Settings settings;
    @Mock
    private WifiManager wifiManager;
    @Mock
    private UpdateNotifier updateNotifier1;
    @Mock
    private UpdateNotifier updateNotifier2;
    @Mock
    private UpdateNotifier updateNotifier3;
    @Mock
    private WifiInfo wifiInfo;
    @Mock
    private Cache cache;
    @Mock
    private Transformer transformer;
    @Mock
    private WiFiData wiFiData;
    @Mock
    private PeriodicScan periodicScan;

    private List<ScanResult> scanResults;
    private List<CacheResult> cacheResults;
    private List<WifiConfiguration> configuredNetworks;

    private Scanner fixture;

    @Before
    public void setUp() {
        scanResults = Collections.emptyList();
        cacheResults = Collections.emptyList();
        configuredNetworks = Collections.emptyList();

        fixture = new Scanner(wifiManager, handler, settings);
        fixture.setCache(cache);
        fixture.setTransformer(transformer);

        fixture.register(updateNotifier1);
        fixture.register(updateNotifier2);
        fixture.register(updateNotifier3);
    }

    @Test
    public void testPeriodicScanIsSet() {
        assertNotNull(fixture.getPeriodicScan());
    }

    @Test
    public void testRegister() {
        // setup
        assertEquals(3, fixture.getUpdateNotifiers().size());
        // execute
        fixture.register(updateNotifier2);
        // validate
        assertEquals(4, fixture.getUpdateNotifiers().size());
    }

    @Test
    public void testUnregister() {
        // setup
        assertEquals(3, fixture.getUpdateNotifiers().size());
        // execute
        fixture.unregister(updateNotifier2);
        // validate
        assertEquals(2, fixture.getUpdateNotifiers().size());
    }

    @Test
    public void testUpdateWithWiFiData() {
        // setup
        withCache();
        withTransformer();
        withWiFiManager();
        // execute
        fixture.update();
        // validate
        assertEquals(wiFiData, fixture.getWiFiData());
        verifyCache();
        verifyTransfomer();
        verifyWiFiManager();
        verify(updateNotifier1).update(wiFiData);
        verify(updateNotifier2).update(wiFiData);
        verify(updateNotifier3).update(wiFiData);
    }

    @Test
    public void testUpdateWithWiFiManager() {
        // setup
        withCache();
        withWiFiManager();
        // execute
        fixture.update();
        // validate
        verifyWiFiManager();
    }

    @Test
    public void testUpdateWithCache() {
        // setup
        withCache();
        withWiFiManager();
        // execute
        fixture.update();
        // validate
        verifyCache();
    }

    @Test
    public void testSetWiFiOnExitOff() {
        // setup
        when(settings.isWiFiOffOnExit()).thenReturn(true);
        // execute
        fixture.setWiFiOnExit();
        // validate
        verify(settings).isWiFiOffOnExit();
        verify(wifiManager).setWifiEnabled(false);
    }

    @Test
    public void testSetWiFiOnExitDoNothing() {
        // setup
        when(settings.isWiFiOffOnExit()).thenReturn(false);
        // execute
        fixture.setWiFiOnExit();
        // validate
        verify(settings).isWiFiOffOnExit();
        verify(wifiManager, never()).setWifiEnabled(anyBoolean());
    }

    private void withCache() {
        when(cache.getScanResults()).thenReturn(cacheResults);
    }

    private void withTransformer() {
        when(transformer.transformToWiFiData(cacheResults, wifiInfo, configuredNetworks)).thenReturn(wiFiData);
    }

    private void verifyCache() {
        verify(cache).add(scanResults);
        verify(cache).getScanResults();
    }

    private void verifyWiFiManager() {
        verify(wifiManager).isWifiEnabled();
        verify(wifiManager).setWifiEnabled(true);
        verify(wifiManager).startScan();
        verify(wifiManager).getScanResults();
        verify(wifiManager).getConnectionInfo();
        verify(wifiManager).getConfiguredNetworks();

        verifyWiFiManagerStartScan();
    }

    private void withWiFiManager() {
        when(wifiManager.isWifiEnabled()).thenReturn(false);
        when(wifiManager.startScan()).thenReturn(true);
        when(wifiManager.getScanResults()).thenReturn(scanResults);
        when(wifiManager.getConnectionInfo()).thenReturn(wifiInfo);
        when(wifiManager.getConfiguredNetworks()).thenReturn(configuredNetworks);

        withWiFiManagerStartScan();
    }

    @SuppressWarnings("deprecation")
    private void verifyWiFiManagerStartScan() {
        verify(wifiManager).startScan();
    }

    @SuppressWarnings("deprecation")
    private void withWiFiManagerStartScan() {
        when(wifiManager.startScan()).thenReturn(true);
    }

    private void verifyTransfomer() {
        verify(transformer).transformToWiFiData(cacheResults, wifiInfo, configuredNetworks);
    }

    @Test
    public void testPause() {
        // setup
        fixture.setPeriodicScan(periodicScan);
        // execute
        fixture.pause();
        // validate
        verify(periodicScan).stop();
    }

    @Test
    public void testResume() {
        // setup
        fixture.setPeriodicScan(periodicScan);
        // execute
        fixture.resume();
        // validate
        verify(periodicScan).start();
    }

}