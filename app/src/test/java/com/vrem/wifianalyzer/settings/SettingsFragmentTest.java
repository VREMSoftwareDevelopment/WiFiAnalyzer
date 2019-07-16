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

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SettingsFragmentTest {
    private SettingsFragment fixture;

    @Before
    public void setUp() {
        fixture = new SettingsFragment();
    }


    @Test
    public void testOnCreate() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture);
        // validate
        assertNotNull(fixture.getView());
    }

    @Test
    public void testExperimentalIsVisible() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture);
        String experimental = fixture.getString(R.string.experimental_key);
        // validate
        assertTrue(fixture.findPreference(experimental).isVisible());
    }

}