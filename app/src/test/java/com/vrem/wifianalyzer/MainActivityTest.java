/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.navigation.NavigationMenuView;
import com.vrem.wifianalyzer.settings.ThemeStyle;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    private MainActivity fixture;

    @Before
    public void setUp() {
        fixture = RobolectricUtil.INSTANCE.getMainActivity();
    }

    @After
    public void tearDown() {
        fixture.getNavigationMenuView().setCurrentNavigationMenu(NavigationMenu.ACCESS_POINTS);
    }

    @Test
    public void testMainActivity() throws Exception {
        assertEquals(WiFiBand.GHZ2.getBand(), fixture.getSupportActionBar().getSubtitle());
        assertTrue(MainContext.INSTANCE.getScanner().isRunning());
    }

    @Test
    public void testClickingOnToolbarTogglesWiFiBand() throws Exception {
        assertEquals(NavigationMenu.ACCESS_POINTS, fixture.getNavigationMenuView().getCurrentNavigationMenu());

        assertEquals(WiFiBand.GHZ2.getBand(), fixture.getSupportActionBar().getSubtitle());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(WiFiBand.GHZ5.getBand(), fixture.getSupportActionBar().getSubtitle());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(WiFiBand.GHZ2.getBand(), fixture.getSupportActionBar().getSubtitle());
    }

    @Test
    public void testClickingOnToolbarDoesNotTogglesWiFiBand() throws Exception {
        NavigationMenuView navigationMenuView = fixture.getNavigationMenuView();
        navigationMenuView.setCurrentNavigationMenu(NavigationMenu.VENDOR_LIST);

        assertEquals(WiFiBand.GHZ2.getBand(), fixture.getSupportActionBar().getSubtitle());
        fixture.findViewById(R.id.toolbar).performClick();
        assertEquals(WiFiBand.GHZ2.getBand(), fixture.getSupportActionBar().getSubtitle());
    }

    @Test
    public void testOnPause() throws Exception {
        fixture.onPause();
        assertFalse(MainContext.INSTANCE.getScanner().isRunning());
        fixture.onResume();
        assertTrue(MainContext.INSTANCE.getScanner().isRunning());
    }

    @Test
    public void testOnBackPressed() throws Exception {
        // setup
        // execute
        fixture.onBackPressed();
        // validate
    }

    @Test
    public void testShouldNotReloadWithNoChanges() throws Exception {
        // setup
        ThemeStyle currentThemeStyle = fixture.getCurrentThemeStyle();
        // execute && validate
        assertFalse(fixture.shouldReload());
        assertEquals(currentThemeStyle, fixture.getCurrentThemeStyle());
    }

    @Test
    public void testShouldReloadWithThemeChange() throws Exception {
        // setup
        ThemeStyle expected = fixture.getCurrentThemeStyle();
        fixture.setCurrentThemeStyle(ThemeStyle.LIGHT.equals(expected) ? ThemeStyle.DARK : ThemeStyle.LIGHT);
        // execute && validate
        assertTrue(fixture.shouldReload());
        assertEquals(expected, fixture.getCurrentThemeStyle());
    }

}