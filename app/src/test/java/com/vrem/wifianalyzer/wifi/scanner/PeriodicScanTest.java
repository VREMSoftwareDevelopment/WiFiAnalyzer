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

import android.os.Handler;

import com.vrem.wifianalyzer.settings.Settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PeriodicScanTest {
    @Mock private Handler handler;
    @Mock private Settings settings;
    @Mock private Scanner scanner;

    private PeriodicScan fixture;

    @Before
    public void setUp() throws Exception {
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