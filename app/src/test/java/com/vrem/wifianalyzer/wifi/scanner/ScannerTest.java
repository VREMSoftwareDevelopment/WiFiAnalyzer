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
import android.os.Handler;

import com.vrem.util.BuildUtils;
import com.vrem.wifianalyzer.ActivityUtils;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BuildUtils.class, ActivityUtils.class})
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

    private Scanner fixture;

    @Before
    public void setUp() {
        mockStatic(BuildUtils.class);
        mockStatic(ActivityUtils.class);

        scanResults = Collections.emptyList();
        cacheResults = Collections.emptyList();

        fixture = new Scanner(wifiManager, handler, settings);
        fixture.setCache(cache);
        fixture.setTransformer(transformer);

        fixture.register(updateNotifier1);
        fixture.register(updateNotifier2);
        fixture.register(updateNotifier3);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(BuildUtils.class);
        verifyNoMoreInteractions(ActivityUtils.class);

        verifyNoMoreInteractions(settings);
        verifyNoMoreInteractions(wifiManager);
        verifyNoMoreInteractions(cache);
        verifyNoMoreInteractions(transformer);
        verifyNoMoreInteractions(periodicScan);
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
    public void testUpdateWithWiFiDisabled() {
        // setup
        boolean wifiEnabled = false;
        withCache();
        withTransformer();
        withWiFiManager(wifiEnabled);
        // execute
        fixture.update();
        // validate
        assertEquals(wiFiData, fixture.getWiFiData());
        verifyCache();
        verifyTransformer();
        verifyWiFiManager(wifiEnabled);
        verify(updateNotifier1).update(wiFiData);
        verify(updateNotifier2).update(wiFiData);
        verify(updateNotifier3).update(wiFiData);
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionQ();
    }

    @Test
    public void testUpdateWithWiFiEnabled() {
        // setup
        boolean wifiEnabled = true;
        withCache();
        withTransformer();
        withWiFiManager(wifiEnabled);
        // execute
        fixture.update();
        // validate
        assertEquals(wiFiData, fixture.getWiFiData());
        verifyCache();
        verifyTransformer();
        verifyWiFiManager(wifiEnabled);
        verify(updateNotifier1).update(wiFiData);
        verify(updateNotifier2).update(wiFiData);
        verify(updateNotifier3).update(wiFiData);
    }

    @Test
    public void testUpdateWithWiFiDisabledAndAndroidQ() {
        // setup
        when(BuildUtils.isMinVersionQ()).thenReturn(true);
        withCache();
        withTransformer();
        withWiFiManager(false);
        // execute
        fixture.update();
        // validate
        assertEquals(wiFiData, fixture.getWiFiData());
        verifyCache();
        verifyTransformer();
        verifyWiFiManager(true);
        verify(updateNotifier1).update(wiFiData);
        verify(updateNotifier2).update(wiFiData);
        verify(updateNotifier3).update(wiFiData);
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionQ();
        verifyStatic(ActivityUtils.class);
        ActivityUtils.startWiFiSettings();
    }

    @Test
    public void testStopWithIsWiFiOffOnExitTurnsOffWiFi() {
        // setup
        when(settings.isWiFiOffOnExit()).thenReturn(true);
        // execute
        fixture.stop();
        // validate
        verify(settings).isWiFiOffOnExit();
        verify(wifiManager).setWifiEnabled(false);
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionQ();
    }

    @Test
    public void testStopDoesNotTurnsOffWiFi() {
        // setup
        when(settings.isWiFiOffOnExit()).thenReturn(false);
        // execute
        fixture.stop();
        // validate
        verify(settings).isWiFiOffOnExit();
        verify(wifiManager, never()).setWifiEnabled(anyBoolean());
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionQ();
    }

    @Test
    public void testStopDoesNotTurnsOffWiFiWhenAndroidQ() {
        // setup
        when(BuildUtils.isMinVersionQ()).thenReturn(true);
        // execute
        fixture.stop();
        // validate
        verifyStatic(BuildUtils.class);
        BuildUtils.isMinVersionQ();
    }

    private void withCache() {
        when(cache.getScanResults()).thenReturn(cacheResults);
        when(cache.getWifiInfo()).thenReturn(wifiInfo);
    }

    private void withTransformer() {
        when(transformer.transformToWiFiData(cacheResults, wifiInfo)).thenReturn(wiFiData);
    }

    private void verifyCache() {
        verify(cache).add(scanResults, wifiInfo);
        verify(cache).getScanResults();
        verify(cache).getWifiInfo();
    }

    private void verifyWiFiManager(boolean wifiEnabled) {
        verify(wifiManager).isWifiEnabled();
        if (!wifiEnabled) {
            verify(wifiManager).setWifiEnabled(true);
        }
        verify(wifiManager).startScan();
        verify(wifiManager).getScanResults();
        verify(wifiManager).getConnectionInfo();

        verifyWiFiManagerStartScan();
    }

    private void withWiFiManager(boolean wifiEnabled) {
        when(wifiManager.isWifiEnabled()).thenReturn(wifiEnabled);
        when(wifiManager.startScan()).thenReturn(true);
        when(wifiManager.getScanResults()).thenReturn(scanResults);
        when(wifiManager.getConnectionInfo()).thenReturn(wifiInfo);

        withWiFiManagerStartScan();
    }

    private void verifyWiFiManagerStartScan() {
        verify(wifiManager).startScan();
    }

    private void withWiFiManagerStartScan() {
        when(wifiManager.startScan()).thenReturn(true);
    }

    private void verifyTransformer() {
        verify(transformer).transformToWiFiData(cacheResults, wifiInfo);
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