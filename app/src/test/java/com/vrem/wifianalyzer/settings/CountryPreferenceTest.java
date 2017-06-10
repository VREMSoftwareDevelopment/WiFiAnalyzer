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

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CountryPreferenceTest {

    private List<WiFiChannelCountry> countries;
    private CountryPreference fixture;

    @Before
    public void setUp() {
        MainActivity mainActivity = RobolectricUtil.INSTANCE.getActivity();
        fixture = new CountryPreference(mainActivity, Robolectric.buildAttributeSet().build());

        countries = WiFiChannelCountry.getAll();
        Collections.sort(countries, new WiFiChannelCountryComparator());
    }

    @Test
    public void testGetEntries() throws Exception {
        // execute
        CharSequence[] actual = fixture.getEntries();
        // validate
        int expectedSize = countries.size();
        assertEquals(expectedSize, actual.length);
        assertEquals(countries.get(0).getCountryName(), actual[0]);
        assertEquals(countries.get(expectedSize - 1).getCountryName(), actual[expectedSize - 1]);
    }

    @Test
    public void testGetEntryValues() throws Exception {
        // execute
        CharSequence[] actual = fixture.getEntryValues();
        // validate
        int expectedSize = countries.size();
        assertEquals(expectedSize, actual.length);
        assertEquals(countries.get(0).getCountryCode(), actual[0]);
        assertEquals(countries.get(expectedSize - 1).getCountryCode(), actual[expectedSize - 1]);
    }

    private static class WiFiChannelCountryComparator implements Comparator<WiFiChannelCountry> {
        @Override
        public int compare(WiFiChannelCountry lhs, WiFiChannelCountry rhs) {
            return new CompareToBuilder()
                .append(lhs.getCountryName(), rhs.getCountryName())
                .append(lhs.getCountryCode(), rhs.getCountryCode())
                .toComparison();
        }
    }
}