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

package com.vrem.util;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ConfigUtilsTest {

    private MainActivity mainActivity;
    private Locale defaultLocale;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();
        defaultLocale = ConfigUtils.getConfigLocale(mainActivity.getResources().getConfiguration());
    }

    @Test
    public void testGetDefaultCountryCode() throws Exception {
        // setup
        String expected = defaultLocale.getCountry();
        // execute
        String actual = ConfigUtils.getDefaultCountryCode(mainActivity);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultLanguageTag() throws Exception {
        // setup
        String expected = LocaleUtils.toLanguageTag(defaultLocale);
        // execute
        String actual = ConfigUtils.getDefaultLanguageTag(mainActivity);
        // validate
        assertEquals(expected, actual);
    }


}