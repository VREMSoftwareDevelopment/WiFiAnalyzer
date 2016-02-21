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
package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.ScanResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CacheTest {
    @Mock
    private ScanResult scanResult1;
    @Mock
    private ScanResult scanResult2;
    @Mock
    private ScanResult scanResult3;

    private Cache fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new Cache();
    }

    @Test
    public void testAddWithNulls() throws Exception {
        // execute
        fixture.add(null);
        // validate
        assertFalse(fixture.getCache().isEmpty());
        assertEquals(1, fixture.getCache().size());
        assertTrue(fixture.getCache().getFirst().isEmpty());
    }

    @Test
    public void testAdd() throws Exception {
        // setup
        List<ScanResult> scanResults = new ArrayList<>();
        // execute
        fixture.add(scanResults);
        // validate
        assertEquals(scanResults, fixture.getCache().getFirst());
    }

    @Test
    public void testAddCompliesToMaxCacheSize() throws Exception {
        // setup
        int size = Cache.MAX_CACHE_SIZE + 2;
        List<List<ScanResult>> expected = new ArrayList<>(size);
        // execute
        for (int i = 0; i < size; i++) {
            List<ScanResult> scanResults = new ArrayList<>();
            expected.add(scanResults);
            fixture.add(scanResults);
        }
        // validate
        assertEquals(size, expected.size());
        assertEquals(Cache.MAX_CACHE_SIZE, fixture.getCache().size());
        assertEquals(expected.get(size - 1), fixture.getCache().getFirst());
        assertEquals(expected.get(size - Cache.MAX_CACHE_SIZE), fixture.getCache().getLast());
    }

    @Test
    public void testGetWiFiData() throws Exception {
        // setup
        withScanResults();
        // execute
        List<ScanResult> actuals = fixture.getScanResults();
        // validate
        assertEquals(1, actuals.size());
        assertEquals(scanResult3, actuals.get(0));
    }

    private void withScanResults() {
        scanResult1.BSSID = scanResult2.BSSID = scanResult3.BSSID = "BBSID";
        scanResult1.level = 1;
        scanResult2.level = 2;
        scanResult3.level = 3;
        fixture.add(Arrays.asList(new ScanResult[]{scanResult1}));
        fixture.add(Arrays.asList(new ScanResult[]{scanResult2}));
        fixture.add(Arrays.asList(new ScanResult[]{scanResult3}));
    }
}