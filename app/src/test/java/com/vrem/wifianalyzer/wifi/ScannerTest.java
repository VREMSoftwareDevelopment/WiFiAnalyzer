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

import android.os.Handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScannerTest {
    @Mock private Handler handler;
    @Mock private Updater updater;
    @Mock private WiFi wifi;
    @Mock private Scanner scanner;

    private Scanner fixture;
    private WiFiData wifiData;
    private int scanInterval;

    @Before
    public void setUp() throws Exception {
        wifiData = new WiFiData();

        when(wifi.scan()).thenReturn(wifiData);

        scanInterval = 10;
        fixture = Scanner.performPeriodicScans(wifi, handler, updater, scanInterval);
    }

    @Test
    public void testPerformPeriodicScans() throws Exception {
        // validate
        verify(handler).postDelayed(fixture.getPerformPeriodicScan(), Scanner.DELAY_INITIAL);
        assertEquals(scanInterval, fixture.getPerformPeriodicScan().getScanInterval());
    }

    @Test
    public void testUpdate() throws Exception {
        // execute
        fixture.update();
        // validate
        verify(wifi).enable();
        verify(wifi).scan();
        verify(updater).update(wifiData);
    }

    @Test
    public void testSetScanInterval() throws Exception {
        // setup
        int expected = scanInterval * 10;
        // execute
        fixture.setScanInterval(expected);
        // validate
        assertEquals(expected, fixture.getPerformPeriodicScan().getScanInterval());
    }

    @Test
    public void testPerformPeriodicScanRun() throws Exception {
        // setup
        Scanner.PerformPeriodicScan fixture = new Scanner.PerformPeriodicScan(scanner, handler, scanInterval);
        // execute
        fixture.run();
        // validate
        verify(scanner).update();
        verify(handler).removeCallbacks(fixture);
        verify(handler).postDelayed(fixture, scanInterval * Scanner.DELAY_INTERVAL);
    }

    @Test
    public void testSetGroupBy() throws Exception {
        // execute
        fixture.setGroupBy(GroupBy.CHANNEL);
        // validate
        verify(wifi).setGroupBy(GroupBy.CHANNEL);
    }

}