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

package com.vrem.wifianalyzer.navigation.items;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.about.AboutActivity;
import com.vrem.wifianalyzer.navigation.NavigationMenu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ActivityItemTest {

    @Mock
    private MainActivity mockMainActivity;
    @Mock
    private MenuItem mockMenuItem;
    @Mock
    private Intent mockIntent;

    @Test
    public void testActivate() throws Exception {
        // setup
        ActivityItem fixture = new TestActivityItem();
        // execute
        fixture.activate(mockMainActivity, mockMenuItem, NavigationMenu.ABOUT);
        // validate
        verify(mockMainActivity).startActivity(mockIntent);
    }

    @Test
    public void testIsRegistered() throws Exception {
        // setup
        ActivityItem fixture = new ActivityItem(AboutActivity.class);
        // execute & validate
        assertFalse(fixture.isRegistered());
    }

    private class TestActivityItem extends ActivityItem {
        private TestActivityItem() {
            super(AboutActivity.class);
        }

        @Override
        Intent createIntent(@NonNull MainActivity mainActivity) {
            assertEquals(mockMainActivity, mainActivity);
            return mockIntent;
        }
    }
}