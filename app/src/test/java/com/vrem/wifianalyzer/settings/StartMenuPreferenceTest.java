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

package com.vrem.wifianalyzer.settings;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.navigation.NavigationGroup;
import com.vrem.wifianalyzer.navigation.NavigationMenu;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class StartMenuPreferenceTest {

    private MainActivity mainActivity;
    private StartMenuPreference fixture;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new StartMenuPreference(mainActivity, Robolectric.buildAttributeSet().build());
    }

    @Test
    public void testGetEntries() {
        // setup
        List<NavigationMenu> expected = NavigationGroup.GROUP_FEATURE.getNavigationMenus();
        // execute
        CharSequence[] actual = fixture.getEntries();
        // validate
        assertEquals(expected.size(), actual.length);
        assertEquals(mainActivity.getResources().getString(expected.get(0).getTitle()), actual[0]);
        int index = expected.size() - 1;
        assertEquals(mainActivity.getResources().getString(expected.get(index).getTitle()), actual[index]);
    }

    @Test
    public void testGetEntryValues() {
        // setup
        List<NavigationMenu> expected = NavigationGroup.GROUP_FEATURE.getNavigationMenus();
        // execute
        CharSequence[] actual = fixture.getEntryValues();
        // validate
        assertEquals(expected.size(), actual.length);
        assertEquals("" + expected.get(0).ordinal(), actual[0]);
        int index = expected.size() - 1;
        assertEquals("" + expected.get(index).ordinal(), actual[index]);
    }
}