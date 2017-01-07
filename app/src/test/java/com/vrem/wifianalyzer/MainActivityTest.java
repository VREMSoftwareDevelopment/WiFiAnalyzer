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

package com.vrem.wifianalyzer;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.menu.OptionMenu;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.navigation.NavigationMenuView;
import com.vrem.wifianalyzer.navigation.Options;
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
        // setup
        WiFiBand currentWiFiBand = WiFiBand.GHZ2;
        String expected = makeSubtitle(currentWiFiBand);
        // execute
        CharSequence actual = fixture.getSupportActionBar().getSubtitle();
        // validate
        assertEquals(expected, actual.toString());
        assertTrue(MainContext.INSTANCE.getScanner().isRunning());
    }

    @Test
    public void testClickingOnToolbarTogglesWiFiBand() throws Exception {
        // setup
        String expectedGHZ2 = makeSubtitle(WiFiBand.GHZ2);
        String expectedGHZ5 = makeSubtitle(WiFiBand.GHZ5);
        // execute & validate
        assertEquals(NavigationMenu.ACCESS_POINTS, fixture.getNavigationMenuView().getCurrentNavigationMenu());

        assertEquals(expectedGHZ2, fixture.getSupportActionBar().getSubtitle().toString());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(expectedGHZ5, fixture.getSupportActionBar().getSubtitle().toString());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(expectedGHZ2, fixture.getSupportActionBar().getSubtitle().toString());
    }

    @Test
    public void testClickingOnToolbarDoesNotTogglesWiFiBand() throws Exception {
        // setup
        NavigationMenuView navigationMenuView = fixture.getNavigationMenuView();
        navigationMenuView.setCurrentNavigationMenu(NavigationMenu.VENDOR_LIST);
        String expectedGHZ2 = makeSubtitle(WiFiBand.GHZ2);
        // execute and validate
        assertEquals(expectedGHZ2, fixture.getSupportActionBar().getSubtitle().toString());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(expectedGHZ2, fixture.getSupportActionBar().getSubtitle().toString());
    }

    @Test
    public void testUpdateActionBarCallsOptionMenuUpdate() throws Exception {
        // setup
        boolean isScannerSwitch = fixture.getNavigationMenuView().getCurrentNavigationMenu().getOptions().contains(Options.ScannerSwitch);
        OptionMenu optionMenu = mock(OptionMenu.class);
        fixture.setOptionMenu(optionMenu);
        // execute
        fixture.updateActionBar();
        // validate
        verify(optionMenu).update(isScannerSwitch);
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
    public void testOnDestroy() throws Exception {
        // setup
        Scanner scanner = MainContextHelper.INSTANCE.getScanner();
        // execute
        fixture.onDestroy();
        // validate
        verify(scanner).unregister(fixture.getConnectionView());
    }

    private String makeSubtitle(@NonNull WiFiBand currentWiFiBand) {
        int color = ContextCompat.getColor(fixture, R.color.connected);
        String subtitleText = makeSubtitleText("<font color='" + color + "'><strong>", "</strong></font>", "<small>", "</small>");
        if (WiFiBand.GHZ5.equals(currentWiFiBand)) {
            subtitleText = makeSubtitleText("<small>", "</small>", "<font color='" + color + "'><strong>", "</strong></font>");
        }
        return fixture.fromHtml(subtitleText).toString();
    }

    private String makeSubtitleText(@NonNull String tag1, @NonNull String tag2, @NonNull String tag3, @NonNull String tag4) {
        return tag1 + WiFiBand.GHZ2.getBand() + tag2 + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + tag3 + WiFiBand.GHZ5.getBand() + tag4;
    }

}