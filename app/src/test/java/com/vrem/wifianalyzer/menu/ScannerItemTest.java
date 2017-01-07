/*
 * WiFi Analyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.menu;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScannerItemTest {

    @Mock
    private OptionMenu optionMenu;

    private ScannerItem fixture;
    private Scanner scanner;

    @Before
    public void setUp() {
        scanner = MainContextHelper.INSTANCE.getScanner();
        fixture = new ScannerItem();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testPause() throws Exception {
        // setup
        when(scanner.isRunning()).thenReturn(true);
        // execute
        fixture.pause(optionMenu);
        // validate
        verify(scanner).pause();
        verify(scanner).isRunning();
        verify(optionMenu).updateItem(true);
    }

    @Test
    public void testResume() throws Exception {
        // setup
        when(scanner.isRunning()).thenReturn(true);
        // execute
        fixture.resume(optionMenu);
        // validate
        verify(scanner).resume();
        verify(scanner).isRunning();
        verify(optionMenu).updateItem(true);
    }

}