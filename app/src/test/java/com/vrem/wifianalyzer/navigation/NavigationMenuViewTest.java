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

package com.vrem.wifianalyzer.navigation;

import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class NavigationMenuViewTest {
    private MainActivity mainActivity;
    private NavigationMenuView fixture;
    private NavigationView navigationView;

    @Before
    public void setUp() throws Exception {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();
        fixture = mainActivity.getNavigationMenuView();
        navigationView = fixture.getNavigationView();
    }

    @After
    public void tearDown() throws Exception {
        fixture.setCurrentNavigationMenu(NavigationMenu.ACCESS_POINTS);
    }

    @Test
    public void testNavigationMenuView() throws Exception {
        // execute
        Menu menu = navigationView.getMenu();
        // validate
        assertEquals(NavigationMenu.values().length, menu.size());

        for (NavigationGroup navigationGroup : NavigationGroup.values()) {
            for (NavigationMenu navigationMenu : navigationGroup.navigationMenu()) {
                MenuItem actual = menu.getItem(navigationMenu.ordinal());
                assertEquals(navigationGroup.ordinal(), actual.getGroupId());
                assertEquals(mainActivity.getResources().getString(navigationMenu.getTitle()), actual.getTitle());
                assertEquals(navigationMenu.ordinal(), actual.getItemId());
                assertEquals(navigationMenu.ordinal(), actual.getOrder());
            }
        }
    }

    @Test
    public void testGetCurrentMenuItem() throws Exception {
        // setup
        MenuItem expected = navigationView.getMenu().getItem(NavigationMenu.ACCESS_POINTS.ordinal());
        // execute
        MenuItem actual = fixture.getCurrentMenuItem();
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCurrentNavigationMenu() throws Exception {
        // execute
        NavigationMenu actual = fixture.getCurrentNavigationMenu();
        // validate
        assertEquals(NavigationMenu.ACCESS_POINTS, actual);
    }

    @Test
    public void testSetCurrentNavigationMenu() throws Exception {
        // setup
        NavigationMenu expected = NavigationMenu.CHANNEL_GRAPH;
        // execute
        fixture.setCurrentNavigationMenu(expected);
        // validate
        assertEquals(expected, fixture.getCurrentNavigationMenu());
    }

    @Test
    public void testFindCurrentNavigationMenu() throws Exception {
        // setup
        NavigationMenu expected = NavigationMenu.CHANNEL_RATING;
        // execute
        NavigationMenu actual = fixture.findNavigationMenu(expected.ordinal());
        // validate
        assertEquals(expected, actual);
    }

}