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

package com.vrem.wifianalyzer;

import android.content.res.Configuration;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.navigation.NavigationMenuController;
import com.vrem.wifianalyzer.navigation.options.OptionMenu;
import com.vrem.wifianalyzer.permission.PermissionService;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.scanner.ScannerService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class MainActivityTest {

    private MainActivity fixture;

    @Before
    public void setUp() {
        fixture = Robolectric.setupActivity(MainActivity.class);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testMainActivity() {
        assertTrue(MainContext.INSTANCE.getScannerService().isRunning());
    }

    @Test
    public void testOnPauseWillPauseScanner() {
        // setup
        ScannerService scannerService = MainContextHelper.INSTANCE.getScannerService();
        // execute
        fixture.onPause();
        // validate
        verify(scannerService).pause();
    }

    @Test
    public void testOnResumeCallsOptionMenuResume() {
        // setup
        PermissionService permissionService = mock(PermissionService.class);
        fixture.setPermissionService(permissionService);
        ScannerService scannerService = MainContextHelper.INSTANCE.getScannerService();
        when(permissionService.isPermissionGranted()).thenReturn(true);
        // execute
        fixture.onResume();
        // validate
        verify(permissionService).isPermissionGranted();
        verify(scannerService).resume();
    }

    @Test
    public void testOnCreateOptionsMenu() {
        // setup
        Menu menu = mock(Menu.class);
        OptionMenu optionMenu = mock(OptionMenu.class);
        fixture.setOptionMenu(optionMenu);
        // execute
        boolean actual = fixture.onCreateOptionsMenu(menu);
        // validate
        assertTrue(actual);
        verify(optionMenu).create(fixture, menu);
    }

    @Test
    public void testOnOptionsItemSelected() {
        // setup
        MenuItem menuItem = mock(MenuItem.class);
        OptionMenu optionMenu = mock(OptionMenu.class);
        fixture.setOptionMenu(optionMenu);
        // execute
        boolean actual = fixture.onOptionsItemSelected(menuItem);
        // validate
        assertTrue(actual);
        verify(optionMenu).select(menuItem);
    }

    @Test
    public void testOnConfigurationChanged() {
        // setup
        Configuration configuration = fixture.getResources().getConfiguration();
        DrawerNavigation drawerNavigation = mock(DrawerNavigation.class);
        fixture.setDrawerNavigation(drawerNavigation);
        // execute
        fixture.onConfigurationChanged(configuration);
        // validate
        verify(drawerNavigation).onConfigurationChanged(configuration);
    }

    @Test
    public void testOnPostCreate() {
        // setup
        DrawerNavigation drawerNavigation = mock(DrawerNavigation.class);
        fixture.setDrawerNavigation(drawerNavigation);
        // execute
        fixture.onPostCreate(null);
        // validate
        verify(drawerNavigation).syncState();
    }

    @Test
    public void testOnStop() {
        // setup
        ScannerService scanner = MainContextHelper.INSTANCE.getScannerService();
        // execute
        fixture.onStop();
        // validate
        verify(scanner).stop();
    }

    @Test
    public void testUpdateShouldUpdateScanner() {
        // setup
        ScannerService scanner = MainContextHelper.INSTANCE.getScannerService();
        // execute
        fixture.update();
        // validate
        verify(scanner).update();
    }

    @Test
    public void testOnSharedPreferenceChangedShouldUpdateScanner() {
        // setup
        ScannerService scanner = MainContextHelper.INSTANCE.getScannerService();
        // execute
        fixture.onSharedPreferenceChanged(null, null);
        // validate
        verify(scanner).update();
    }

    @Test
    public void testOptionMenu() {
        // setup
        OptionMenu optionMenu = fixture.getOptionMenu();
        // execute
        Menu actual = optionMenu.getMenu();
        // validate
        assertNotNull(actual.findItem(R.id.action_filter));
        assertNotNull(actual.findItem(R.id.action_scanner));
    }

    @Test
    public void testGetCurrentMenuItem() {
        // setup
        MenuItem menuItem = mock(MenuItem.class);
        NavigationMenuController navigationMenuController = mock(NavigationMenuController.class);
        when(navigationMenuController.getCurrentMenuItem()).thenReturn(menuItem);
        fixture.setNavigationMenuController(navigationMenuController);
        // execute
        MenuItem actual = fixture.getCurrentMenuItem();
        // validate
        assertEquals(menuItem, actual);
        verify(navigationMenuController).getCurrentMenuItem();
    }

    @Test
    public void testGetCurrentNavigationMenu() {
        // setup
        NavigationMenu navigationMenu = NavigationMenu.CHANNEL_GRAPH;
        NavigationMenuController navigationMenuController = mock(NavigationMenuController.class);
        when(navigationMenuController.getCurrentNavigationMenu()).thenReturn(navigationMenu);
        fixture.setNavigationMenuController(navigationMenuController);
        // execute
        NavigationMenu actual = fixture.getCurrentNavigationMenu();
        // validate
        assertEquals(navigationMenu, actual);
        verify(navigationMenuController).getCurrentNavigationMenu();
    }

    @Test
    public void testSetCurrentNavigationMenu() {
        // setup
        NavigationMenu navigationMenu = NavigationMenu.CHANNEL_GRAPH;
        Settings settings = MainContextHelper.INSTANCE.getSettings();
        NavigationMenuController navigationMenuController = mock(NavigationMenuController.class);
        fixture.setNavigationMenuController(navigationMenuController);
        // execute
        fixture.setCurrentNavigationMenu(navigationMenu);
        // validate
        verify(navigationMenuController).setCurrentNavigationMenu(navigationMenu);
        verify(settings).saveSelectedMenu(navigationMenu);
    }

    @Test
    public void testGetNavigationView() {
        // setup
        NavigationMenuController navigationMenuController = mock(NavigationMenuController.class);
        NavigationView navigationView = mock(NavigationView.class);
        when(navigationMenuController.getNavigationView()).thenReturn(navigationView);
        fixture.setNavigationMenuController(navigationMenuController);
        // execute
        NavigationView actual = fixture.getNavigationView();
        // validate
        assertEquals(navigationView, actual);
        verify(navigationMenuController).getNavigationView();
    }

}