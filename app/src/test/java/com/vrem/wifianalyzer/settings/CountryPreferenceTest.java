/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CountryPreferenceTest {

    private CountryPreference fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getMainActivity();
        fixture = new CountryPreference(mainActivity, Robolectric.buildAttributeSet().build());
    }

    @Test
    public void testGetEntries() throws Exception {
        // setup
        List<WiFiChannelCountry> expected = WiFiChannelCountry.getAll();
        // execute
        CharSequence[] actual = fixture.getEntries();
        // validate
        int expectedSize = expected.size();
        assertEquals(expectedSize, actual.length);
        assertEquals(expected.get(2).getCountryName(), actual[0]);
        assertEquals(expected.get(expectedSize - 1).getCountryName(), actual[expectedSize - 1]);
    }

    @Test
    public void testGetEntryValues() throws Exception {
        // setup
        List<WiFiChannelCountry> expected = WiFiChannelCountry.getAll();
        // execute
        CharSequence[] actual = fixture.getEntryValues();
        // validate
        int expectedSize = expected.size();
        assertEquals(expectedSize, actual.length);
        assertEquals(expected.get(2).getCountryCode(), actual[0]);
        assertEquals(expected.get(expectedSize - 1).getCountryCode(), actual[expectedSize - 1]);
    }
}