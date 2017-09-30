/*
 * WiFiAnalyzer
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

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OptionMenuTest {

    @Mock
    private Menu menu;
    @Mock
    private MenuItem menuItem;
    @Mock
    private Activity activity;
    @Mock
    private MenuInflater menuInflater;

    private OptionMenu fixture;
    private Scanner scanner;

    @Before
    public void setUp() {
        scanner = MainContextHelper.INSTANCE.getScanner();
        fixture = new OptionMenu();
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testCreate() throws Exception {
        // setup
        when(activity.getMenuInflater()).thenReturn(menuInflater);
        // execute
        fixture.create(activity, menu);
        // validate
        assertEquals(menu, fixture.getMenu());
        verify(menuInflater).inflate(R.menu.optionmenu, menu);
    }

    @Test
    public void testGetMenu() throws Exception {
        // setup
        when(activity.getMenuInflater()).thenReturn(menuInflater);
        // execute
        fixture.create(activity, menu);
        // validate
        assertEquals(menu, fixture.getMenu());
    }

    @Test
    public void testSelectActionScannerWithPause() throws Exception {
        // setup
        when(menuItem.getItemId()).thenReturn(R.id.action_scanner);
        when(scanner.isRunning()).thenReturn(true);
        // execute
        fixture.select(menuItem);
        // validate
        verify(menuItem).getItemId();
        verify(scanner).isRunning();
        verify(scanner).pause();
    }

    @Test
    public void testSelectActionScannerWithResume() throws Exception {
        // setup
        when(menuItem.getItemId()).thenReturn(R.id.action_scanner);
        when(scanner.isRunning()).thenReturn(false);
        // execute
        fixture.select(menuItem);
        // validate
        verify(menuItem).getItemId();
        verify(scanner).isRunning();
        verify(scanner).resume();
    }

    @Test
    public void testSelectNoActionScanner() throws Exception {
        // execute
        fixture.select(menuItem);
        // validate
        verify(menuItem).getItemId();
        verify(scanner, never()).resume();
        verify(scanner, never()).pause();
        verify(scanner, never()).isRunning();
    }

    @Test
    public void testPause() throws Exception {
        // execute
        fixture.pause();
        // validate
        verify(scanner).pause();
    }

    @Test
    public void testResume() throws Exception {
        // execute
        fixture.resume();
        // validate
        verify(scanner).resume();
    }

}