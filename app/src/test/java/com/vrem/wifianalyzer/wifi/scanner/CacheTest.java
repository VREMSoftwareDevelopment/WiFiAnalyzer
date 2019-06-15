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

import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.settings.Settings;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CacheTest {
    @Mock
    private WifiInfo wifiInfo;
    @Mock
    private ScanResult scanResult1;
    @Mock
    private ScanResult scanResult2;
    @Mock
    private ScanResult scanResult3;
    @Mock
    private ScanResult scanResult4;
    @Mock
    private ScanResult scanResult5;
    @Mock
    private ScanResult scanResult6;

    private Settings settings;
    private Configuration configuration;
    private Cache fixture;

    @Before
    public void setUp() {
        fixture = new Cache();

        settings = MainContextHelper.INSTANCE.getSettings();
        configuration = MainContextHelper.INSTANCE.getConfiguration();

        when(settings.getScanSpeed()).thenReturn(5);
        when(configuration.isSizeAvailable()).thenReturn(true);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testAddWithSizeAvailable() {
        // setup
        List<ScanResult> scanResults = Collections.emptyList();
        // execute
        fixture.add(scanResults, wifiInfo);
        // validate
        assertEquals(scanResults, fixture.getFirst());
    }

    @Test
    public void testAddCompliesToMaxCacheSizeWithSizeAvailable() {
        // setup
        int cacheSize = 2;
        List<List<ScanResult>> expected = new ArrayList<>();
        // execute
        for (int i = 0; i < cacheSize; i++) {
            List<ScanResult> scanResults = Collections.emptyList();
            expected.add(scanResults);
            fixture.add(scanResults, wifiInfo);
        }
        // validate
        assertEquals(cacheSize, expected.size());
        assertEquals(expected.get(cacheSize - 1), fixture.getFirst());
        assertEquals(expected.get(cacheSize - 2), fixture.getLast());
    }

    @Test
    public void testGetWiFiDataWithSizeAvailable() {
        // setup
        withScanResults();
        // execute
        List<CacheResult> actuals = fixture.getScanResults();
        // validate
        assertEquals(3, actuals.size());
        validate(scanResult3, 25, actuals.get(0));
        validate(scanResult5, 40, actuals.get(1));
        validate(scanResult6, 10, actuals.get(2));
    }

    @Test
    public void testGetCacheSizeWithSizeAvailable() {
        // setup
        int[] values = new int[]{
            1, 4,
            2, 3,
            4, 3,
            5, 2,
            9, 2,
            10, 1,
            20, 1
        };
        // execute
        for (int i = 0; i < values.length; i += 2) {
            when(settings.getScanSpeed()).thenReturn(values[i]);
            assertEquals("Scan Speed:" + values[i], values[i + 1], fixture.getCacheSize());
        }
        // validate
        verify(settings, times(values.length / 2)).getScanSpeed();
    }

    @Test
    public void testAdd() {
        // setup
        when(configuration.isSizeAvailable()).thenReturn(false);
        List<ScanResult> scanResults = Collections.emptyList();
        // execute
        fixture.add(scanResults, wifiInfo);
        // validate
        assertEquals(scanResults, fixture.getFirst());
        assertEquals(wifiInfo, fixture.getWifiInfo());
    }

    @Test
    public void testAddCompliesToMaxCacheSize() {
        // setup
        int cacheSize = 2;
        when(configuration.isSizeAvailable()).thenReturn(false);
        List<List<ScanResult>> expected = new ArrayList<>();
        // execute
        for (int i = 0; i < cacheSize; i++) {
            List<ScanResult> scanResults = Collections.emptyList();
            expected.add(scanResults);
            fixture.add(scanResults, wifiInfo);
        }
        // validate
        assertEquals(cacheSize, expected.size());
        assertEquals(expected.get(cacheSize - 1), fixture.getFirst());
        assertEquals(expected.get(cacheSize - 2), fixture.getLast());
    }

    @Test
    public void testGetWiFiData() {
        // setup
        when(configuration.isSizeAvailable()).thenReturn(false);
        withScanResults();
        // execute
        List<CacheResult> actuals = fixture.getScanResults();
        // validate
        assertEquals(2, actuals.size());
        validate(scanResult3, 20, actuals.get(0));
        validate(scanResult6, 0, actuals.get(1));
    }

    @Test
    public void testGetCacheSize() {
        // setup
        int expected = 1;
        when(configuration.isSizeAvailable()).thenReturn(false);
        // execute
        int actual = fixture.getCacheSize();
        // validate
        assertEquals(expected, actual);
        verify(settings, never()).getScanSpeed();
    }

    private void validate(ScanResult expectedScanResult, int expectedLevel, CacheResult actual) {
        assertEquals(expectedScanResult, actual.getScanResult());
        assertEquals(expectedLevel, actual.getLevelAverage());
    }

    private void withScanResults() {
        scanResult1.BSSID = scanResult2.BSSID = scanResult3.BSSID = "BBSID1";
        scanResult1.level = 10;
        scanResult2.level = 20;
        scanResult3.level = 30;

        scanResult4.BSSID = scanResult5.BSSID = "BBSID2";
        scanResult4.level = 60;
        scanResult5.level = 40;

        scanResult6.BSSID = "BBSID3";
        scanResult6.level = 10;

        fixture.add(Arrays.asList(scanResult1, scanResult4), wifiInfo);
        fixture.add(Arrays.asList(scanResult2, scanResult5), wifiInfo);
        fixture.add(Arrays.asList(scanResult3, scanResult6), wifiInfo);
    }

}