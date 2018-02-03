/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.settings;

import android.view.MenuItem;

import com.vrem.wifianalyzer.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SettingActivityTest {
    private MenuItem menuItem;
    private SettingActivity fixture;

    @Before
    public void setUp() {
        menuItem = mock(MenuItem.class);
        fixture = Robolectric.setupActivity(SettingActivity.class);
    }

    @Test
    public void testOnOptionsItemSelectedWithHome() throws Exception {
        // setup
        when(menuItem.getItemId()).thenReturn(android.R.id.home);
        // execute
        boolean actual = fixture.onOptionsItemSelected(menuItem);
        // validate
        assertTrue(actual);
        verify(menuItem).getItemId();
    }

    @Test
    public void testOnOptionsItemSelected() throws Exception {
        // setup
        when(menuItem.getItemId()).thenReturn(-android.R.id.home);
        // execute
        boolean actual = fixture.onOptionsItemSelected(menuItem);
        // validate
        assertFalse(actual);
        verify(menuItem).getItemId();
    }
}