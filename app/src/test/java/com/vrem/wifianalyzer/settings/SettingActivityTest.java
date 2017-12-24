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

package com.vrem.wifianalyzer.settings;

import android.app.ActionBar;
import android.view.MenuItem;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SettingActivityTest {

    private SettingActivity fixture;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();
        fixture = Robolectric.setupActivity(SettingActivity.class);
    }

    @Test
    public void testTitle() throws Exception {
        // setup
        String expected = fixture.getResources().getString(R.string.action_settings);
        // execute
        ActionBar actual = fixture.getActionBar();
        // validate
        assertNotNull(actual);
        assertEquals(expected, actual.getTitle());
    }

    @Test
    public void testOnOptionsItemSelectedWithHome() throws Exception {
        // setup
        MenuItem menuItem = mock(MenuItem.class);
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
        MenuItem menuItem = mock(MenuItem.class);
        // execute
        boolean actual = fixture.onOptionsItemSelected(menuItem);
        // validate
        assertFalse(actual);
    }
}