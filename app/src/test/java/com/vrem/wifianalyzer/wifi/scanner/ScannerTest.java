/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.os.Handler;

import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.model.WiFiData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScannerTest {

    @Mock
    private Handler handler;
    @Mock
    private Settings settings;
    @Mock
    private WiFiManagerWrapper wiFiManagerWrapper;
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
        scanResults = Collections.emptyList();
        cacheResults = Collections.emptyList();

        fixture = new Scanner(wiFiManagerWrapper, handler, settings);
        fixture.setCache(cache);
        fixture.setTransformer(transformer);

        fixture.register(updateNotifier1);
        fixture.register(updateNotifier2);
        fixture.register(updateNotifier3);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(settings);
        verifyNoMoreInteractions(wiFiManagerWrapper);
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
    public void testUpdate() {
        // setup
        withCache();
        withTransformer();
        withWiFiManagerWrapper();
        // execute
        fixture.update();
        // validate
        assertEquals(wiFiData, fixture.getWiFiData());
        verifyCache();
        verifyTransformer();
        verifyWiFiManagerWrapper();
        verify(updateNotifier1).update(wiFiData);
        verify(updateNotifier2).update(wiFiData);
        verify(updateNotifier3).update(wiFiData);
    }

    @Test
    public void testStopWithIsWiFiOffOnExitTurnsOffWiFi() {
        // setup
        when(settings.wiFiOffOnExit()).thenReturn(true);
        // execute
        fixture.stop();
        // validate
        verify(settings).wiFiOffOnExit();
        verify(wiFiManagerWrapper).disableWiFi();
    }

    @Test
    public void testStopDoesNotTurnsOffWiFi() {
        // setup
        when(settings.wiFiOffOnExit()).thenReturn(false);
        // execute
        fixture.stop();
        // validate
        verify(settings).wiFiOffOnExit();
        verify(wiFiManagerWrapper, never()).enableWiFi();
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

    private void verifyWiFiManagerWrapper() {
        verify(wiFiManagerWrapper).enableWiFi();
        verify(wiFiManagerWrapper).startScan();
        verify(wiFiManagerWrapper).scanResults();
        verify(wiFiManagerWrapper).wiFiInfo();

        verifyWiFiManagerStartScan();
    }

    private void withWiFiManagerWrapper() {
        when(wiFiManagerWrapper.startScan()).thenReturn(true);
        when(wiFiManagerWrapper.scanResults()).thenReturn(scanResults);
        when(wiFiManagerWrapper.wiFiInfo()).thenReturn(wifiInfo);

        withWiFiManagerStartScan();
    }

    private void verifyWiFiManagerStartScan() {
        verify(wiFiManagerWrapper).startScan();
    }

    private void withWiFiManagerStartScan() {
        when(wiFiManagerWrapper.startScan()).thenReturn(true);
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

    @Test
    public void testToggleWhenRunning() {
        // setup
        fixture.setPeriodicScan(periodicScan);
        when(periodicScan.isRunning()).thenReturn(true);
        // execute
        fixture.toggle();
        // validate
        verify(periodicScan).isRunning();
        verify(periodicScan).stop();
    }

    @Test
    public void testToggleWhenNotRunning() {
        // setup
        fixture.setPeriodicScan(periodicScan);
        when(periodicScan.isRunning()).thenReturn(false);
        // execute
        fixture.toggle();
        // validate
        verify(periodicScan).isRunning();
        verify(periodicScan).start();
    }

}