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

package com.vrem.wifianalyzer.navigation.options;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.wifi.scanner.ScannerService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScannerActionTest {
    private ScannerService scannerService;

    private ScannerAction fixture;

    @Before
    public void setUp() {
        scannerService = MainContextHelper.INSTANCE.getScannerService();

        fixture = new ScannerAction();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testExecuteWithPause() {
        // setup
        when(scannerService.isRunning()).thenReturn(true);
        // execute
        fixture.execute();
        // validate
        verify(scannerService).isRunning();
        verify(scannerService).pause();
        verify(scannerService, never()).resume();
    }

    @Test
    public void testExecuteActionWithResume() {
        // setup
        when(scannerService.isRunning()).thenReturn(false);
        // execute
        fixture.execute();
        // validate
        verify(scannerService).isRunning();
        verify(scannerService, never()).pause();
        verify(scannerService).resume();
    }

}