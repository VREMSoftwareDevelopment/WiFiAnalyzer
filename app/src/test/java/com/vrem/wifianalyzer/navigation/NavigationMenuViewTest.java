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

import com.vrem.wifianalyzer.RobolectricBaseTest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NavigationMenuViewTest extends RobolectricBaseTest {
    private NavigationMenuView fixture;
    private NavigationView navigationView;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        fixture = activity.getNavigationMenuView();
        navigationView = fixture.getNavigationView();
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
                assertEquals(resources.getString(navigationMenu.getTitle()), actual.getTitle());
                assertEquals(navigationMenu.ordinal(), actual.getItemId());
                assertEquals(navigationMenu.ordinal(), actual.getOrder());
            }
        }
    }

    @Test
    public void testDefaultMenuItem() throws Exception {
        // setup
        MenuItem expected = navigationView.getMenu().getItem(NavigationMenu.ACCESS_POINTS.ordinal());
        // execute
        MenuItem actual = fixture.defaultMenuItem();
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testSelectedMenuItem() throws Exception {
        // setup
        NavigationMenu expected = NavigationMenu.CHANNEL_GRAPH;
        // execute
        NavigationMenu actual = fixture.selectedMenuItem(expected.ordinal());
        // validate
        assertEquals(expected, actual);
    }

}