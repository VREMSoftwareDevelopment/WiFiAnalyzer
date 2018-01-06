/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.os.Handler;

import com.vrem.wifianalyzer.settings.Settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PeriodicScanTest {
    @Mock
    private Handler handler;
    @Mock
    private Settings settings;
    @Mock
    private ScannerService scanner;

    private PeriodicScan fixture;

    @Before
    public void setUp() {
        fixture = new PeriodicScan(scanner, handler, settings);
    }

    @Test
    public void testScanInitial() throws Exception {
        // validate
        verify(handler).removeCallbacks(fixture);
        verify(handler).postDelayed(fixture, PeriodicScan.DELAY_INITIAL);
    }

    @Test
    public void testRun() throws Exception {
        // setup
        int scanInterval = 15;
        when(settings.getScanInterval()).thenReturn(scanInterval);
        // execute
        fixture.run();
        // validate
        verify(scanner).update();
        verify(handler, times(2)).removeCallbacks(fixture);
        verify(handler).postDelayed(fixture, scanInterval * PeriodicScan.DELAY_INTERVAL);
    }

    @Test
    public void testStop() throws Exception {
        // execute
        fixture.stop();
        // validate
        verify(handler, times(2)).removeCallbacks(fixture);
    }

    @Test
    public void testStart() throws Exception {
        // execute
        fixture.start();
        // validate
        verify(handler, times(2)).removeCallbacks(fixture);
        verify(handler, times(2)).postDelayed(fixture, PeriodicScan.DELAY_INITIAL);
    }

}