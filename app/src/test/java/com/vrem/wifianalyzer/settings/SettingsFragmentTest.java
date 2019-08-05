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

import android.os.Build;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import androidx.preference.Preference;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
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
        String key = fixture.getString(R.string.experimental_key);
        // execute
        Preference actual = fixture.findPreference(key);
        // validate
        assertTrue(actual.isVisible());
    }

    @Test
    public void testWiFiOnExitIsVisible() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture);
        String key = fixture.getString(R.string.wifi_off_on_exit_key);
        // execute
        Preference actual = fixture.findPreference(key);
        // validate
        assertTrue(actual.isVisible());
    }

/*
    @Test
    @Config(sdk = Build.VERSION_CODES.Q)
    public void testExperimentalIsNotVisible() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture);
        String key = fixture.getString(R.string.experimental_key);
        // execute
        Preference actual = fixture.findPreference(key);
        // validate
        assertFalse(actual.isVisible());
    }

    @Test
    @Config(sdk = Build.VERSION_CODES.Q)
    public void testWiFiOnExitIsNotVisible() {
        // setup
        RobolectricUtil.INSTANCE.startFragment(fixture);
        String key = fixture.getString(R.string.wifi_off_on_exit_key);
        // execute
        Preference actual = fixture.findPreference(key);
        // validate
        assertFalse(actual.isVisible());
    }
*/

}