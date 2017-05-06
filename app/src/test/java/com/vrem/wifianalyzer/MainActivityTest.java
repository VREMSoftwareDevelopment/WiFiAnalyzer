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

package com.vrem.wifianalyzer;

import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.menu.OptionMenu;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.navigation.NavigationMenuView;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
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
    public void testMainActivity() throws Exception {
        assertTrue(MainContext.INSTANCE.getScanner().isRunning());
    }

    @Test
    public void testClickingOnToolbarTogglesWiFiBand() throws Exception {
        // setup
        NavigationMenuView navigationMenuView = fixture.getNavigationMenuView();
        navigationMenuView.setCurrentNavigationMenu(NavigationMenu.CHANNEL_RATING);
        // execute and validate
        assertEquals(WiFiBand.GHZ2, MainContext.INSTANCE.getSettings().getWiFiBand());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(WiFiBand.GHZ5, MainContext.INSTANCE.getSettings().getWiFiBand());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(WiFiBand.GHZ2, MainContext.INSTANCE.getSettings().getWiFiBand());
    }

    @Test
    public void testClickingOnToolbarDoesNotTogglesWiFiBand() throws Exception {
        // setup
        NavigationMenuView navigationMenuView = fixture.getNavigationMenuView();
        navigationMenuView.setCurrentNavigationMenu(NavigationMenu.VENDOR_LIST);
        // execute and validate
        assertEquals(WiFiBand.GHZ2, MainContext.INSTANCE.getSettings().getWiFiBand());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(WiFiBand.GHZ2, MainContext.INSTANCE.getSettings().getWiFiBand());
    }

    @Test
    public void testOnPauseCallsOptionMenuPause() throws Exception {
        // setup
        OptionMenu optionMenu = mock(OptionMenu.class);
        fixture.setOptionMenu(optionMenu);
        // execute
        fixture.onPause();
        // validate
        verify(optionMenu).pause();
    }

    @Test
    public void testOnResumeCallsOptionMenuResume() throws Exception {
        // setup
        OptionMenu optionMenu = mock(OptionMenu.class);
        fixture.setOptionMenu(optionMenu);
        // execute
        fixture.onResume();
        // validate
        verify(optionMenu).resume();
    }

    @Test
    public void testOnCreateOptionsMenu() throws Exception {
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
    public void testOnOptionsItemSelected() throws Exception {
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
    public void testOnStop() throws Exception {
        // setup
        Scanner scanner = MainContextHelper.INSTANCE.getScanner();
        // execute
        fixture.onStop();
        // validate
        verify(scanner).setWiFiOnExit();
    }

    @Test
    public void testOptionMenu() throws Exception {
        // setup
        OptionMenu optionMenu = fixture.getOptionMenu();
        // execute
        Menu actual = optionMenu.getMenu();
        // validate
        assertNotNull(actual.findItem(R.id.action_filter));
        assertNotNull(actual.findItem(R.id.action_scanner));
    }

}