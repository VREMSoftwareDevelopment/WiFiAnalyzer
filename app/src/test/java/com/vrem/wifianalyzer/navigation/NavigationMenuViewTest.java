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

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NavigationMenuViewTest {
    @Mock
    private MainActivity activity;
    @Mock
    private NavigationView navigationView;
    @Mock
    private Menu menu;
    @Mock
    private MenuItem menuItem;
    @Mock
    private MenuItem selectedMenuItem;

    private NavigationMenuView fixture;

    @Before
    public void setUp() throws Exception {
        withNavigationView();

        fixture = new NavigationMenuView(activity);
    }

    @After
    public void tearDown() throws Exception {
        verifyNavigationView();
    }

    private void verifyNavigationView() {
        verify(activity).findViewById(R.id.nav_view);

        verify(navigationView, atLeast(1)).getMenu();
        verify(navigationView).setNavigationItemSelectedListener(activity);

        for (NavigationGroup navigationGroup : NavigationGroup.values()) {
            for (NavigationMenu navigationMenu : navigationGroup.navigationMenu()) {
                verify(menu).add(navigationGroup.ordinal(), navigationMenu.ordinal(), navigationMenu.ordinal(), navigationMenu.getTitle());
                verify(menuItem).setIcon(navigationMenu.getIcon());
            }
        }
    }

    private void withNavigationView() {
        when(activity.findViewById(R.id.nav_view)).thenReturn(navigationView);
        when(navigationView.getMenu()).thenReturn(menu);

        for (NavigationGroup navigationGroup : NavigationGroup.values()) {
            for (NavigationMenu navigationMenu : navigationGroup.navigationMenu()) {
                when(menu.add(navigationGroup.ordinal(), navigationMenu.ordinal(), navigationMenu.ordinal(), navigationMenu.getTitle())).thenReturn(menuItem);
            }
        }
    }

    @Test
    public void testDefaultMenuItem() throws Exception {
        // setup
        when(menu.getItem(NavigationMenu.WIFI_LIST.ordinal())).thenReturn(selectedMenuItem);
        // execute
        MenuItem actual = fixture.defaultMenuItem();
        // validate
        assertEquals(selectedMenuItem, actual);
        verify(menu).getItem(NavigationMenu.WIFI_LIST.ordinal());
    }

    @Test
    public void testSelectedMenuItemWithActivity() throws Exception {
        // setup
        NavigationMenu expected = NavigationMenu.SETTINGS;
        // execute
        NavigationMenu actual = fixture.selectedMenuItem(expected.ordinal());
        // validate
        assertEquals(expected, actual);
        verify(menu, never()).size();
        verify(menuItem, never()).setCheckable(anyBoolean());
        verify(menuItem, never()).setChecked(anyBoolean());
    }

    @Test
    public void testGetSelectedMenuItemWithFragment() throws Exception {
        // setup
        int size = 3;
        NavigationMenu expected = NavigationMenu.CHANNEL_ANALYZER;
        withSelectedMenuItem(expected, size);
        // execute
        NavigationMenu actual = fixture.selectedMenuItem(expected.ordinal());
        // validate
        assertEquals(expected, actual);
        verifySelectedMenuItem(expected, size);
    }

    private void withSelectedMenuItem(NavigationMenu expected, int size) {
        when(menu.size()).thenReturn(size);
        for (int i = 0; i < size; i++) {
            when(menu.getItem(i)).thenReturn(menuItem);
        }
    }

    private void verifySelectedMenuItem(NavigationMenu expected, int size) {
        verify(menu, atLeast(1)).size();
        for (int i = 0; i < size; i++) {
            verify(menu).getItem(i);
        }
        verify(menuItem).setCheckable(true);
        verify(menuItem).setChecked(true);
        verify(menuItem, times(2)).setCheckable(false);
        verify(menuItem, times(2)).setChecked(false);
    }

}