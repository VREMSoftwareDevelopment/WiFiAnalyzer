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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DrawerNavigationTest {
    @Mock
    private MainActivity mainActivity;
    @Mock
    private Toolbar toolbar;
    @Mock
    private Configuration configuration;
    @Mock
    private DrawerLayout drawerLayout;
    @Mock
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private DrawerNavigation fixture;

    @Before
    public void setUp() {
        when(mainActivity.findViewById(R.id.drawer_layout)).thenReturn(drawerLayout);

        fixture = new DrawerNavigation(mainActivity, toolbar) {
            @Override
            ActionBarDrawerToggle create(
                @NonNull MainActivity mainActivityInput,
                @NonNull Toolbar toolbarInput,
                @NonNull DrawerLayout drawerLayoutInput,
                int openDrawerContentDescRes,
                int closeDrawerContentDescRes) {

                Assert.assertEquals(mainActivity, mainActivityInput);
                Assert.assertEquals(toolbar, toolbarInput);
                Assert.assertEquals(drawerLayout, drawerLayoutInput);
                Assert.assertEquals(R.string.navigation_drawer_open, openDrawerContentDescRes);
                Assert.assertEquals(R.string.navigation_drawer_close, closeDrawerContentDescRes);

                return actionBarDrawerToggle;
            }
        };
    }

    @Test
    public void testConstructor() {
        // validate
        verify(mainActivity).findViewById(R.id.drawer_layout);
        verify(drawerLayout).addDrawerListener(actionBarDrawerToggle);
        verify(actionBarDrawerToggle).syncState();
    }

    @Test
    public void testSyncState() {
        // execute
        fixture.syncState();
        // validate
        verify(actionBarDrawerToggle, times(2)).syncState();
    }

    @Test
    public void testOnConfigurationChanged() {
        // execute
        fixture.onConfigurationChanged(configuration);
        // validate
        verify(actionBarDrawerToggle).onConfigurationChanged(configuration);
    }

}