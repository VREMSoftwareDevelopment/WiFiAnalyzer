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

    private MainActivity mainActivity;
    private CountryPreference fixture;

    @Before
    public void setUp() throws Exception {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();
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