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

package com.vrem.wifianalyzer.navigation;

import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class NavigationMenuControllerTest {
    private MainActivity mainActivity;
    private NavigationMenuController fixture;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = mainActivity.getNavigationMenuController();
        navigationView = fixture.getNavigationView();
        bottomNavigationView = fixture.getBottomNavigationView();
    }

    @After
    public void tearDown() {
        fixture.setCurrentNavigationMenu(NavigationMenu.ACCESS_POINTS);
    }

    @Test
    public void testNavigationMenuView() {
        // execute
        Menu menu = navigationView.getMenu();
        // validate
        assertEquals(NavigationMenu.values().length, menu.size());
        IterableUtils.forEach(EnumUtils.values(NavigationGroup.class), new NavigationGroupClosure(menu));
    }

    @Test
    public void testGetCurrentMenuItem() {
        // setup
        MenuItem expected = getNavigationViewMenuItem(NavigationMenu.ACCESS_POINTS);
        // execute
        MenuItem actual = fixture.getCurrentMenuItem();
        // validate
        assertEquals(expected, actual);
        assertTrue(actual.isCheckable());
        assertTrue(actual.isChecked());
    }

    @Test
    public void testGetCurrentNavigationMenu() {
        // execute
        NavigationMenu actual = fixture.getCurrentNavigationMenu();
        // validate
        assertEquals(NavigationMenu.ACCESS_POINTS, actual);
    }

    @Test
    public void testSetCurrentNavigationMenuWithNavigationView() {
        // setup
        NavigationMenu expected = NavigationMenu.CHANNEL_GRAPH;
        // execute
        fixture.setCurrentNavigationMenu(expected);
        // validate
        assertEquals(expected, fixture.getCurrentNavigationMenu());
        assertTrue(getNavigationViewMenuItem(NavigationMenu.CHANNEL_GRAPH).isCheckable());
        assertTrue(getNavigationViewMenuItem(NavigationMenu.CHANNEL_GRAPH).isChecked());
        assertFalse(getNavigationViewMenuItem(NavigationMenu.ACCESS_POINTS).isCheckable());
        assertFalse(getNavigationViewMenuItem(NavigationMenu.ACCESS_POINTS).isChecked());
    }

    @Test
    public void testSetCurrentNavigationMenuWithBottomNavigationView() {
        // setup
        NavigationMenu expected = NavigationMenu.CHANNEL_GRAPH;
        // execute
        fixture.setCurrentNavigationMenu(expected);
        // validate
        assertEquals(expected, fixture.getCurrentNavigationMenu());
        MenuItem menuItem = getBottomNavigationMenuItem(NavigationMenu.CHANNEL_GRAPH);
        assertTrue(menuItem.isCheckable());
        assertTrue(menuItem.isChecked());
        assertFalse(getBottomNavigationMenuItem(NavigationMenu.ACCESS_POINTS).isCheckable());
    }

    private MenuItem getNavigationViewMenuItem(NavigationMenu navigationMenu) {
        return navigationView.getMenu().findItem(navigationMenu.ordinal());
    }

    private MenuItem getBottomNavigationMenuItem(NavigationMenu navigationMenu) {
        return bottomNavigationView.getMenu().findItem(navigationMenu.ordinal());
    }

    private class NavigationGroupClosure implements Closure<NavigationGroup> {
        private final Menu menu;

        private NavigationGroupClosure(@NonNull Menu menu) {
            this.menu = menu;
        }

        @Override
        public void execute(NavigationGroup navigationGroup) {
            IterableUtils.forEach(navigationGroup.getNavigationMenus(), new NavigationMenuClosure(menu, navigationGroup));
        }
    }

    private class NavigationMenuClosure implements Closure<NavigationMenu> {
        private final Menu menu;
        private final NavigationGroup navigationGroup;

        private NavigationMenuClosure(@NonNull Menu menu, @NonNull NavigationGroup navigationGroup) {
            this.menu = menu;
            this.navigationGroup = navigationGroup;
        }

        @Override
        public void execute(NavigationMenu navigationMenu) {
            MenuItem actual = menu.getItem(navigationMenu.ordinal());
            assertEquals(navigationGroup.ordinal(), actual.getGroupId());
            assertEquals(mainActivity.getResources().getString(navigationMenu.getTitle()), actual.getTitle());
            assertEquals(navigationMenu.ordinal(), actual.getItemId());
            assertEquals(navigationMenu.ordinal(), actual.getOrder());
        }
    }
}